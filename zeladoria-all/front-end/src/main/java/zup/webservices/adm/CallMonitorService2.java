package zup.webservices.adm;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.xmlpull.v1.XmlPullParserException;

import JsonTransformers.AnonymityTransformer;
import JsonTransformers.CallProgressTransformer;
import JsonTransformers.CallSourceTransformer;
import JsonTransformers.CallStatusTransformer;
import JsonTransformers.PriorityTransformer;
import JsonTransformers.SecretTransformer;
import filters.CallMonitorFilterBean;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import zup.action.ResultsMon;
import zup.bean.AdditionalInformation;
import zup.bean.AttendanceTime;
import zup.bean.CallClassification;
import zup.bean.Entity;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.Result;
import zup.bean.SolvedCall;
import zup.bean.UnsolvedCall;
import zup.bean.WebUser;
import zup.business.AdditionalInformationBusiness;
import zup.business.AttendanceTimeBusiness;
import zup.business.CallClassificationBusiness;
import zup.business.EntityBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.SolvedCallBusiness;
import zup.business.SystemUserBusiness;
import zup.business.UnsolvedCallBusiness;
import zup.business.UnsolvedCallBusiness2;
import zup.business.WebUserBusiness;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.CallStatus;
import zup.enums.Priority;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.utils.MinioServer;
import zup.validate.UnsolvedCallValidate;

@Path("/callmonitor2")
public class CallMonitorService2 {

	UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
	SolvedCallBusiness sb2 = new SolvedCallBusiness();
	EntityCategoryBusiness ecb = new EntityCategoryBusiness();
	CallClassificationBusiness ccb = new CallClassificationBusiness();
	SystemUserBusiness sub = new SystemUserBusiness();
	AdditionalInformationBusiness aib = new AdditionalInformationBusiness();

	AttendanceTimeBusiness ab = new AttendanceTimeBusiness();
	UnsolvedCallValidate scv = new UnsolvedCallValidate();
	WebUserBusiness wub = new WebUserBusiness();
	EntityBusiness eb = new EntityBusiness();
	EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static Logger loggersc = Logger.getLogger(UnsolvedCall.class);

	static Map<String, Integer> sortByValue(Map<String, Integer> map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o2, Object o1) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}
	
	
	@PUT
	@Path("/pr/{scid}/{priority}")
	public Response mergePR(@PathParam("scid") Long scid, @PathParam("priority") int priority)  {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		try{
		UnsolvedCall t = (UnsolvedCall) scb.getById(scid);
		Long idParent=t.getParentCallId().getUnsolvedCallId();
		t.setUnsolvedCallId(null);
		scb.save(t);
		UnsolvedCall parent = (UnsolvedCall) scb.getById(idParent);
		t.setParentCallId(parent);
		AdditionalInformation ai = new AdditionalInformation();
		ai.setInformation(Messages.getInstance().getMessage(IMessages.CALL_PRIORITY_SUCCESS));
		t.setParentCallId(null);
		t.setUnsolvedCallId(null);
		t.setPriority(Priority.fromValue(priority));
		t.setUpdatedOrModeratedBy(sub.getById(username));
		t.setCreationOrUpdateDate(new Date());
		aib.save(ai);
		t.setObservation(ai);
			t.setParentCallId(parent);
			scb.mergeUN(t);
		return Response.ok(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_PRIORITY_SUCCESS))).build();
		}catch(ModelException e){
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}

	public static Date adicionarDiasUteis(Date data, Integer qtdeDiasAcrescentados) {
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.setTime(data);
		while (qtdeDiasAcrescentados > 0) {
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
			int diaDaSemana = dataInicial.get(Calendar.DAY_OF_WEEK);
			if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY) {
				--qtdeDiasAcrescentados;
			}
		}
		return dataInicial.getTime();
	}

	@GET
	@Path("/cs")
	public Response enumList(@QueryParam("callSource") List<CallSource> callSource) {
		return Response.ok(callSource).build();
	}

	// SEARCH MONITOR DE CHAMADOS
	@GET
	@Path("/search")
	public Response searchMO() throws ParseException, HibernateException, SQLException, ModelException,
			InvalidEndpointException, InvalidPortException, XmlPullParserException, InvalidKeyException,
			InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException,
			ErrorResponseException, InternalException, IOException, InvalidExpiresRangeException {
		MinioServer minioServer = new MinioServer();
		EntityBusiness eb = new EntityBusiness();
		int count = 0;
		
		//Mostrar resultados dos últimos 30 dias.
		GregorianCalendar gcInicial = new GregorianCalendar();
		gcInicial.add(Calendar.DAY_OF_MONTH, -30); // 30 dias atrás.
		GregorianCalendar gcFinal = new GregorianCalendar();
		gcFinal.add(Calendar.DAY_OF_MONTH, +1); // hoje 00:00.
		Date initDate = gcInicial.getTime(); // pega a data de 30 dias atrás.
		Date endDate = gcFinal.getTime();
		
		Result<UnsolvedCall> result = scb.searchCallMonitorLast30Days(initDate, endDate);
		List<UnsolvedCall> tList = result.getList();
		if (tList.isEmpty() || tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		count = result.getCount();
		/*
		 * for (UnsolvedCall call : tList) { if (!call.isNoMidia()) {
		 * Iterable<io.minio.Result<Item>> myObjects =
		 * minioServer.getMinioClient().listObjects("call", "/" +
		 * call.getParentCallId().getUnsolvedCallId() + "/1.png"); if
		 * (myObjects.iterator().hasNext())
		 * call.setFirstPhoto(minioServer.getMinioClient().presignedGetObject(
		 * "call", myObjects.iterator().next().get().objectName())); } }
		 */
		LinkedHashMap<String, Integer> delayCounter = new LinkedHashMap<String, Integer>();
		int atrasado = 0;
		int emDia = 0;
		int emAlerta= 0;
		HashMap<String, int[]> callDelayCounter = new HashMap<String, int[]>();
		Date currentDate = new Date(System.currentTimeMillis());
		for (UnsolvedCall uc : tList) {
			if (!uc.isNoMidia()) {
				Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
						"/" + uc.getParentCallId().getUnsolvedCallId() + "/1");
				if (myObjects.iterator().hasNext()) 
					uc.setFirstPhoto( minioServer.getMinioClient().presignedGetObject("call",
							myObjects.iterator().next().get().objectName()));			
			}
			
			/*
			if (!uc.isNoMidia()) {
				Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
						"/" + uc.getParentCallId().getUnsolvedCallId() + "/1");
				if (myObjects.iterator().hasNext()) 
					uc.setFirstPhoto( minioServer.getMinioClient().presignedGetObject("call",
							myObjects.iterator().next().get().objectName()));
			}
			*/
			Entity en = eb.getById(uc.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId());
			AttendanceTime at = en.getAttendanceTime();
			int time = 0;
			if (uc.getPriority() == Priority.ALTA)
				time = at.getHighPriorityTime();
			else if (uc.getPriority() == Priority.MEDIA)
				time = at.getMediumPriorityTime();
			else if (uc.getPriority() == Priority.BAIXA)
				time = at.getLowPriorityTime();
			uc.setExpirationDate(adicionarDiasUteis(uc.getParentCallId().getCreationOrUpdateDate(), time));
			String delay = "";
			if (at != null) {
				if ((new Date().after(new Date(uc.getExpirationDate().getTime() - (long) (1) * 1000 * 60 * 60 * 24)))
						&& (new Date().before(uc.getExpirationDate()))) {
					uc.setDelay("Laranja");
					emAlerta++;
				} else if ((currentDate.after(uc.getExpirationDate()))) {
					uc.setDelay("Vermelho");
					atrasado++;
				} else if (uc.getCallProgress() == CallProgress.ENCAMINHADO) {
					uc.setDelay("Branco");
					emDia++;
				} else if (uc.getCallProgress() == CallProgress.VISUALIZADO) {
					uc.setDelay("Azul");
					emDia++;
				} else if (uc.getCallProgress() == CallProgress.ANDAMENTO || uc.getCallProgress() == CallProgress.NOVO
						|| uc.getCallProgress() == CallProgress.ENCAMINHADO
						|| uc.getCallProgress() == CallProgress.PROCESSADO) {
					if (((currentDate.before(uc.getExpirationDate()))))
						uc.setDelay("Verde");
					emDia++;
				}
			}
		}
		delayCounter.put("Atrasado", atrasado);
		delayCounter.put("Em alerta", emAlerta);
		delayCounter.put("Em dia", emDia);
		Charts charts = new Charts();
		charts.setCallClassificationCounter(scb.countCallClassification());
		charts.setEntityCounter(scb.countEntity());
		charts.setEntityCategoryCounter(scb.countEntityCategory());
		charts.setCallProgressCounter(scb.countCallProgress());
		charts.setCallDelayCounter(delayCounter);
		ResultsMon resp = new ResultsMon();
		resp.setCharts(charts);
		List<UnsolvedCall> list = new ArrayList<UnsolvedCall>(result.getList());
		for(UnsolvedCall un:list){
			EntityEntityCategoryMaps emaps = un.getEntityEntityCategoryMaps();
			emaps.setEntity(eb.getById(emaps.getEntityEntityCategoryMapsPK().getEntityId()));
				un.setEntityEntityCategoryMaps(emaps);
			}
		resp.setList(tList);
		return Response.ok(resp).header("X-Total-Count", result.getCount()).header("X-Per-Page",
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
				.build();
	}

	@GET
	@Path("/search1111")
	public Response searchMO(@BeanParam CallMonitorFilterBean filter) throws ParseException, HibernateException,
			SQLException, ModelException, InvalidEndpointException, InvalidPortException, XmlPullParserException,
			InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException,
			NoResponseException, ErrorResponseException, InternalException, IOException, InvalidExpiresRangeException {
		MinioServer minioServer = new MinioServer();
		int count = 0;
		Result<UnsolvedCall> result = scb.searchMO3(filter.getPage(), filter.getEntity(),
				filter.getCallClassification(), filter.getCallProgressEnum(), filter.getCallSourceEnum(),
				filter.getPriorityEnum(), filter.getOrderParam(), filter.getOrder());
		// Result<UnsolvedCall> result =new Result<UnsolvedCall>(0,new
		// ArrayList<UnsolvedCall>());
		List<UnsolvedCall> tList = result.getList();
		count = result.getCount();
		for (UnsolvedCall call : tList) {
			if (!call.isNoMidia()) {
				Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
						"/" + call.getParentCallId().getUnsolvedCallId() + "/1.png");
				if (myObjects.iterator().hasNext())
					call.setFirstPhoto(minioServer.getMinioClient().presignedGetObject("call",
							myObjects.iterator().next().get().objectName()));
			}
		}

		HashMap<String, int[]> entityCounter = new HashMap<String, int[]>();
		HashMap<String, int[]> entityCategoryCounter = new HashMap<String, int[]>();
		HashMap<String, int[]> callProgressCounter = new HashMap<String, int[]>();
		HashMap<String, int[]> callClassificationCounter = new HashMap<String, int[]>();
		HashMap<String, int[]> callDelayCounter = new HashMap<String, int[]>();
		Date currentDate = new Date(System.currentTimeMillis());
		for (UnsolvedCall uc : tList) {

			// contagem entidades
			int[] entityvalueWrapper = entityCounter.get(uc.getEntityEntityCategoryMaps().getEntity().getName());
			if (entityvalueWrapper == null) {
				entityCounter.put(uc.getEntityEntityCategoryMaps().getEntity().getName(), new int[] { 1 });
			} else {
				entityvalueWrapper[0]++;
			}
			// contagem categoria de entidades
			int[] entityCategoryvalueWrapper = entityCategoryCounter
					.get(uc.getEntityEntityCategoryMaps().getEntityCategory().getName());
			if (entityCategoryvalueWrapper == null) {
				entityCategoryCounter.put(uc.getEntityEntityCategoryMaps().getEntityCategory().getName(),
						new int[] { 1 });
			} else {
				entityCategoryvalueWrapper[0]++;
			}
			// contagem call progress
			int[] callProgressvalueWrapper = callProgressCounter.get(uc.getCallProgress().getStr());
			if (callProgressvalueWrapper == null) {
				callProgressCounter.put(uc.getCallProgress().getStr(), new int[] { 1 });
			} else {
				callProgressvalueWrapper[0]++;
			}
			// contagem call classification
			int[] callClassificationvalueWrapper = callClassificationCounter
					.get(uc.getCallClassificationId().getName());
			if (callClassificationvalueWrapper == null) {
				callClassificationCounter.put(uc.getCallClassificationId().getName(), new int[] { 1 });
			} else {
				callClassificationvalueWrapper[0]++;
			}
			// contagem call delay

			AttendanceTime at = null;
			at = uc.getEntityEntityCategoryMaps().getEntity().getAttendanceTime();
			int time = 0;
			if (uc.getPriority() == Priority.ALTA) {
				time = at.getHighPriorityTime();
			} else if (uc.getPriority() == Priority.MEDIA) {
				time = at.getMediumPriorityTime();
			} else if (uc.getPriority() == Priority.BAIXA) {
				time = at.getLowPriorityTime();
			}
			uc.setExpirationDate(adicionarDiasUteis(uc.getParentCallId().getCreationOrUpdateDate(), time));
			String delay = "";
			if (at != null) {

				if ((new Date().after(new Date(uc.getExpirationDate().getTime() - (long) (1) * 1000 * 60 * 60 * 24)))
						&& (new Date().before(uc.getExpirationDate()))) {
					uc.setDelay("Laranja");
					delay = "Em alerta";
				} else if ((currentDate.after(uc.getExpirationDate()))) { // Atrasada
					uc.setDelay("Vermelho");
					delay = "Atrasado";
				} else if (uc.getCallProgress() == CallProgress.ENCAMINHADO) {
					uc.setDelay("Branco");
				} else if (uc.getCallProgress() == CallProgress.VISUALIZADO) {
					uc.setDelay("Azul");
				} else if (uc.getCallProgress() == CallProgress.ANDAMENTO) {
					if (((currentDate.before(uc.getExpirationDate())))) // Em
																		// dia
						uc.setDelay("Verde");
					delay = "Em dia";
				}

				int[] callDelayvalueWrapper = callDelayCounter.get(delay);
				if (callDelayvalueWrapper == null) {
					callDelayCounter.put(delay, new int[] { 1 });
				} else {
					callDelayvalueWrapper[0]++;
				}

			}

		}
		System.out.println("------------Entidade----------------");
		Map<String, Integer> entityCounterMap = new HashMap<String, Integer>();
		for (Entry<String, int[]> entry : entityCounter.entrySet()) {
			// System.out.println(entry.getKey());
			// System.out.println(entry.getValue()[0]);
			entityCounterMap.put(entry.getKey(), entry.getValue()[0]);
		}

		for (Entry<String, Integer> entry : entityCounterMap.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}

		System.out.println("------------Entidade Categoria----------------");
		Map<String, Integer> entityCategoryCounterMap = new HashMap<String, Integer>();
		for (Entry<String, int[]> entry : entityCategoryCounter.entrySet()) {
			// System.out.println(entry.getKey());
			// System.out.println(entry.getValue()[0]);
			entityCategoryCounterMap.put(entry.getKey(), entry.getValue()[0]);
		}
		for (Entry<String, Integer> entry : entityCategoryCounterMap.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}

		System.out.println("------------Call progres----------------");
		Map<String, Integer> callProgressCounterMap = new HashMap<String, Integer>();
		for (Entry<String, int[]> entry : callProgressCounter.entrySet()) {
			// System.out.println(entry.getKey());
			// System.out.println(entry.getValue()[0]);
			callProgressCounterMap.put(entry.getKey(), entry.getValue()[0]);
		}
		System.out.println("------------Call Classification----------------");
		Map<String, Integer> callClassificationCounterMap = new HashMap<String, Integer>();
		for (Entry<String, int[]> entry : callClassificationCounter.entrySet()) {
			// System.out.println(entry.getKey());
			// System.out.println(entry.getValue()[0]);
			callClassificationCounterMap.put(entry.getKey(), entry.getValue()[0]);

		}
		System.out.println("------------Delay----------------");
		Map<String, Integer> callDelayCounterMap = new HashMap<String, Integer>();
		for (Entry<String, int[]> entry : callDelayCounter.entrySet()) {
			// System.out.println(entry.getKey());
			// System.out.println(entry.getValue()[0]);
			callDelayCounterMap.put(entry.getKey(), entry.getValue()[0]);
		}
		Map<String, Integer> callDelayCounterMapSort = sortByValue(callDelayCounterMap);
		Map<String, Integer> entityCounterMapSort = sortByValue(entityCounterMap);
		Map<String, Integer> entityCategoryCounterMapSort = sortByValue(entityCategoryCounterMap);
		/*
		 * Charts charts = new Charts();
		 * charts.setCallClassificationCounter(callClassificationCounterMap);
		 * charts.setEntityCounter(entityCounterMapSort);
		 * charts.setEntityCategoryCounter(entityCategoryCounterMapSort);
		 * charts.setCallProgressCounter(callProgressCounterMap);
		 * charts.setCallDelayCounter(callDelayCounterMapSort);
		 */
		if (tList.isEmpty() || tList == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		}
		List<UnsolvedCall> ucList = new ArrayList<UnsolvedCall>(tList);

		/*
		 * if (callDelay.equals("Atrasado")) for (UnsolvedCall uc2 : ucList) if
		 * (uc2.getDelay() != "Vermelho") tList.remove(uc2); if
		 * (callDelay.equals("Emdia")) for (UnsolvedCall uc2 : ucList) if
		 * (uc2.getDelay() == "Vermelho") tList.remove(uc2);
		 */

		return Response.ok(tList).build();
	}

	/*
	 * 
	 * @GET
	 * 
	 * @Path(
	 * "/search/{periodod}/{periodoa}/{callSource}/{entity}/{callClassification}/{priority}/{callProgress}/{sortColumn}/{order}/{callDelay}")
	 * public Response searchMO( @PathParam("periodod") String periodod,
	 * 
	 * @PathParam("periodoa") String periodoa, @PathParam("callSource") String
	 * callSource,
	 * 
	 * @PathParam("entity") String entity, @PathParam("callClassification")
	 * String callClassification,
	 * 
	 * @PathParam("priority") String priority, @PathParam("callProgress") String
	 * callProgress,@PathParam("sortColumn") int sortColumn, @PathParam("order")
	 * int order,
	 * 
	 * @PathParam("callDelay") String callDelay) throws ParseException,
	 * HibernateException, SQLException, ModelException {
	 * 
	 * Response response = new Response(); if (!periodod.equals("z") &&
	 * !periodoa.equals("z")) { Date periodod2 = new
	 * SimpleDateFormat("ddMMyyyy").parse(periodod); Date periodoa2 = new
	 * SimpleDateFormat("ddMMyyyy").parse(periodoa); if
	 * (periodod2.after(periodoa2)) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * DATE_ERROR)); return response; } } List<UnsolvedCall> tList = new
	 * ArrayList<UnsolvedCall>(); Integer count = 0; try { //List<UnsolvedCall>
	 * llist = scb.findAll(); Map<Integer, List<UnsolvedCall>> tMap =
	 * scb.searchMO(periodod, periodoa, callSource, entity, callClassification,
	 * priority, callProgress,sortColumn,order);
	 * 
	 * for (Integer key : tMap.keySet()) { count = key; tList = tMap.get(key);
	 * break; } for (UnsolvedCall call : tList) { if (!call.isNoMidia()) {
	 * 
	 * File file = new File(SystemConfiguration.getInstance()
	 * .getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY) +
	 * call.getParentCallId().getUnsolvedCallId() + "/1.png");
	 * 
	 * if (file.exists()) { byte[] data = null; try { data =
	 * Files.readAllBytes(file.toPath()); } catch (IOException e) {
	 * e.printStackTrace(); } String
	 * media=Base64.encodeBase64URLSafeString(data); Collection<String> cb64=
	 * new ArrayList<String>(); cb64.add(media); call.setImages(cb64);
	 * Collection<byte[]> cbyte = new ArrayList<byte[]>(); cbyte.add(data);
	 * call.setMedias(cbyte); } else
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * MEDIA_ERROR)); } } } catch (ModelException me) {
	 * response.setMessage(me.getMessage()); } HashMap<String, int[]>
	 * entityCounter = new HashMap<String, int[]>(); HashMap<String, int[]>
	 * entityCategoryCounter = new HashMap<String, int[]>(); HashMap<String,
	 * int[]> callProgressCounter = new HashMap<String, int[]>();
	 * HashMap<String, int[]> callClassificationCounter = new HashMap<String,
	 * int[]>(); HashMap<String, int[]> callDelayCounter = new HashMap<String,
	 * int[]>(); Date currentDate = new Date(System.currentTimeMillis()); for
	 * (UnsolvedCall uc : tList) {
	 * 
	 * //contagem entidades int[] entityvalueWrapper =
	 * entityCounter.get(uc.getEntityEntityCategoryMaps().getEntity().getName())
	 * ; if (entityvalueWrapper == null) {
	 * entityCounter.put(uc.getEntityEntityCategoryMaps().getEntity().getName(),
	 * new int[] { 1 }); } else { entityvalueWrapper[0]++; } //contagem
	 * categoria de entidades int[] entityCategoryvalueWrapper =
	 * entityCategoryCounter.get(uc.getEntityEntityCategoryMaps().
	 * getEntityCategory().getName()); if (entityCategoryvalueWrapper == null) {
	 * entityCategoryCounter.put(uc.getEntityEntityCategoryMaps().
	 * getEntityCategory().getName(), new int[] { 1 }); } else {
	 * entityCategoryvalueWrapper[0]++; } //contagem call progress int[]
	 * callProgressvalueWrapper =
	 * callProgressCounter.get(uc.getCallProgress().getStr()); if
	 * (callProgressvalueWrapper == null) {
	 * callProgressCounter.put(uc.getCallProgress().getStr(), new int[] { 1 });
	 * } else { callProgressvalueWrapper[0]++; } //contagem call classification
	 * int[] callClassificationvalueWrapper =
	 * callClassificationCounter.get(uc.getCallClassificationId().getName()); if
	 * (callClassificationvalueWrapper == null) {
	 * callClassificationCounter.put(uc.getCallClassificationId().getName(), new
	 * int[] { 1 }); } else { callClassificationvalueWrapper[0]++; } //contagem
	 * call delay
	 * 
	 * 
	 * 
	 * 
	 * AttendanceTime at = null; at =
	 * uc.getEntityEntityCategoryMaps().getEntity().getAttendanceTime(); int
	 * time = 0; if (uc.getPriority() == Priority.ALTA) { time =
	 * at.getHighPriorityTime(); } else if (uc.getPriority() == Priority.MEDIA)
	 * { time = at.getMediumPriorityTime(); } else if (uc.getPriority() ==
	 * Priority.BAIXA) { time = at.getLowPriorityTime(); }
	 * uc.setExpirationDate(adicionarDiasUteis(uc.getParentCallId().
	 * getCreationOrUpdateDate(), time)); String delay=""; if (at != null) {
	 * 
	 * if ((new Date().after(new Date(uc.getExpirationDate().getTime() - (long)
	 * (1) * 1000 * 60 * 60 * 24))) && (new
	 * Date().before(uc.getExpirationDate()))) { uc.setDelay("Laranja");
	 * delay="Em alerta"; }
	 * 
	 * else if ((currentDate.after(uc.getExpirationDate()))) { // Atrasada
	 * uc.setDelay("Vermelho"); delay="Atrasado"; } else if
	 * (uc.getCallProgress() == CallProgress.ENCAMINHADO) {
	 * uc.setDelay("Branco"); } else if (uc.getCallProgress() ==
	 * CallProgress.VISUALIZADO) { uc.setDelay("Azul"); } else if
	 * (uc.getCallProgress() == CallProgress.ANDAMENTO) { if
	 * (((currentDate.before(uc.getExpirationDate())))) // Em // dia
	 * uc.setDelay("Verde"); delay="Em dia"; }
	 * 
	 * int[] callDelayvalueWrapper = callDelayCounter.get(delay); if
	 * (callDelayvalueWrapper == null) { callDelayCounter.put(delay, new int[] {
	 * 1 }); } else { callDelayvalueWrapper[0]++; }
	 * 
	 * }
	 * 
	 * } System.out.println("------------Entidade----------------"); Map<String,
	 * Integer> entityCounterMap=new HashMap<String, Integer>(); for
	 * (Entry<String, int[]> entry : entityCounter.entrySet()) { //
	 * System.out.println(entry.getKey()); //
	 * System.out.println(entry.getValue()[0]);
	 * entityCounterMap.put(entry.getKey(), entry.getValue()[0]); }
	 * 
	 * for (Entry<String, Integer> entry : entityCounterMap.entrySet()) {
	 * System.out.println(entry.getKey()); System.out.println(entry.getValue());
	 * }
	 * 
	 * 
	 * System.out.println("------------Entidade Categoria----------------");
	 * Map<String, Integer> entityCategoryCounterMap=new HashMap<String,
	 * Integer>(); for (Entry<String, int[]> entry :
	 * entityCategoryCounter.entrySet()) { //
	 * System.out.println(entry.getKey()); //
	 * System.out.println(entry.getValue()[0]);
	 * entityCategoryCounterMap.put(entry.getKey(), entry.getValue()[0]); } for
	 * (Entry<String, Integer> entry : entityCategoryCounterMap.entrySet()) {
	 * System.out.println(entry.getKey()); System.out.println(entry.getValue());
	 * }
	 * 
	 * 
	 * System.out.println("------------Call progres----------------");
	 * Map<String, Integer> callProgressCounterMap=new HashMap<String,
	 * Integer>(); for (Entry<String, int[]> entry :
	 * callProgressCounter.entrySet()) { // System.out.println(entry.getKey());
	 * // System.out.println(entry.getValue()[0]);
	 * callProgressCounterMap.put(entry.getKey(), entry.getValue()[0]); }
	 * System.out.println("------------Call Classification----------------");
	 * Map<String, Integer> callClassificationCounterMap=new HashMap<String,
	 * Integer>(); for (Entry<String, int[]> entry :
	 * callClassificationCounter.entrySet()) { //
	 * System.out.println(entry.getKey()); //
	 * System.out.println(entry.getValue()[0]);
	 * callClassificationCounterMap.put(entry.getKey(), entry.getValue()[0]);
	 * 
	 * } System.out.println("------------Delay----------------"); Map<String,
	 * Integer> callDelayCounterMap=new HashMap<String, Integer>(); for
	 * (Entry<String, int[]> entry : callDelayCounter.entrySet()) { //
	 * System.out.println(entry.getKey()); //
	 * System.out.println(entry.getValue()[0]);
	 * callDelayCounterMap.put(entry.getKey(), entry.getValue()[0]); }
	 * Map<String, Integer>
	 * callDelayCounterMapSort=sortByValue(callDelayCounterMap); Map<String,
	 * Integer> entityCounterMapSort=sortByValue(entityCounterMap); Map<String,
	 * Integer>
	 * entityCategoryCounterMapSort=sortByValue(entityCategoryCounterMap);
	 * Charts charts=new Charts();
	 * charts.setCallClassificationCounter(callClassificationCounterMap);
	 * charts.setEntityCounter(entityCounterMapSort);
	 * charts.setEntityCategoryCounter(entityCategoryCounterMapSort);
	 * charts.setCallProgressCounter(callProgressCounterMap);
	 * charts.setCallDelayCounter(callDelayCounterMapSort); if (tList.isEmpty()
	 * || tList == null) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * RESULT_NOT_FOUND)); response.setSuccess(false); return response; }
	 * List<UnsolvedCall> ucList = new ArrayList<UnsolvedCall>(tList);
	 * 
	 * if (callDelay.equals("Atrasado")) for (UnsolvedCall uc2 : ucList) if
	 * (uc2.getDelay() != "Vermelho") tList.remove(uc2); if
	 * (callDelay.equals("Emdia")) for (UnsolvedCall uc2 : ucList) if
	 * (uc2.getDelay() == "Vermelho") tList.remove(uc2);
	 * 
	 * 
	 * String chartJson = new JSONSerializer()
	 * .exclude("*.class").serialize(charts); response.setGraph(chartJson);
	 * String strJson = new JSONSerializer()
	 * .include("callClassificationId.name",
	 * "parentCallId.unsolvedCallId","entityEntityCategoryMaps.entity.name",
	 * "entityEntityCategoryMaps.entityCategory.name",
	 * "updatedOrModeratedBy.systemUserUsername", "images","medias", "priority",
	 * "unsolvedCallId", "callStatus", "callProgress", "callSource",
	 * "parentCallId.creationOrUpdateDate", "description.information", "delay",
	 * "expirationDate", "noMidia") .exclude("*.class", "*").transform(new
	 * CallStatusTransformer(), "callStatus") .transform(new
	 * CallProgressTransformer(), "callProgress") .transform(new
	 * AnonymityTransformer(), "anonymity").transform(new SecretTransformer(),
	 * "secret") .transform(new CallSourceTransformer(),
	 * "callSource").transform(new PriorityTransformer(), "priority")
	 * .transform(new DateTransformer("dd/MM/yyyy HH:mm"), "expirationDate")
	 * .transform(new DateTransformer("dd/MM/yyyy HH:mm"),
	 * Date.class).serialize(tList); response.setJsonList(strJson);
	 * response.setCount(count);
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); return response; }
	 */

}