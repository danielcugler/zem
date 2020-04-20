package zup.webservices.adm;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.xmlpull.v1.XmlPullParserException;

import filters.CallFollowFilterBean;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import zup.bean.AdditionalInformation;
import zup.bean.AttendanceTime;
import zup.bean.CallClassification;
import zup.bean.Entity;
import zup.bean.SolvedCall;
import zup.bean.SystemUser;
import zup.bean.UnreadCall;
import zup.bean.UnsolvedCall;
import zup.bean.WebUser;
import zup.business.AdditionalInformationBusiness;
import zup.business.CallClassificationBusiness;
import zup.business.EntityBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.SolvedCallBusiness;
import zup.business.SystemUserBusiness;
import zup.business.UnreadCallBusiness;
import zup.business.UnsolvedCallBusiness;
import zup.business.UnsolvedCallBusiness2;
import zup.business.WebUserBusiness;
import zup.enums.CallProgress;
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

@Path("/callfollow2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CallFollowService2 {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static Logger loggersc = Logger.getLogger(UnsolvedCall.class);

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
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
	@Path("/{id}")
	public Response getById(@PathParam("id") Long id) {
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();
		UnsolvedCall t;
		try {
			t = ucb.getById(id);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(t).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/combos")
	public Response getCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		try {
			List<CallClassification> tList = ccb.findAll();
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(tList).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	// ---------------------

	@GET
	@Path("/search")
	public Response searchPaginaCF2(@BeanParam CallFollowFilterBean filter)
			throws ParseException, HibernateException, SQLException, ModelException, InvalidEndpointException,
			InvalidPortException, XmlPullParserException, InvalidKeyException, NoSuchAlgorithmException, IOException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		MinioServer minioServer = new MinioServer();
		List<UnsolvedCall> red = new ArrayList<UnsolvedCall>();
		List<UnsolvedCall> green = new ArrayList<UnsolvedCall>();
		try {
			String iniDateS = filter.getIniDate();
			String endDateS = filter.getEndDate();
			Date iniDate;
			Date endDate;
			if (!iniDateS.isEmpty() && !endDateS.isEmpty()) {
				iniDate = new SimpleDateFormat("dd/MM/yyyy").parse(iniDateS);
				endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(endDateS+" 23:59:59");
				if (iniDate.after(endDate))
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DATE_ERROR,
							Messages.getInstance().getMessage(IMessages.DATE_ERROR)));
			} else {
				GregorianCalendar gcInicial = new GregorianCalendar();
				gcInicial.add(Calendar.DAY_OF_MONTH, -30); // 30 dias atrás.
				GregorianCalendar gcFinal = new GregorianCalendar();
				gcFinal.add(Calendar.DAY_OF_MONTH, +1); // hoje 00:00.
				iniDate = gcInicial.getTime(); // pega a data de 30 dias atrás.
				endDate = gcFinal.getTime();
			}
			List<UnsolvedCall> tList = scb.searchPaginaCF2(filter.getPage(), iniDate, endDate, filter.getCallSource(),
					filter.getCallClassificationId(), filter.getPriority(), filter.getUnsolvedCallId(),
					filter.getCitizenCpf());
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (UnsolvedCall uc : tList) {
				if (!uc.isNoMidia()) {
					Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
							"/" + uc.getParentCallId().getUnsolvedCallId() + "/1");
					if (myObjects.iterator().hasNext())
						uc.setFirstPhoto(minioServer.getMinioClient().presignedGetObject("call",
								myObjects.iterator().next().get().objectName()));

				}
				AttendanceTime at = null;
				at = uc.getEntityEntityCategoryMaps().getEntity().getAttendanceTime();
				int time = 0;
				if (uc.getPriority() == Priority.ALTA)
					time = at.getHighPriorityTime();
				else if (uc.getPriority() == Priority.MEDIA)
					time = at.getMediumPriorityTime();
				else if (uc.getPriority() == Priority.BAIXA)
					time = at.getLowPriorityTime();
				uc.setExpirationDate(adicionarDiasUteis(uc.getParentCallId().getCreationOrUpdateDate(), time));
				Date currentDate = new Date(System.currentTimeMillis());
				if ((new Date().after(new Date(uc.getExpirationDate().getTime() - (long) (1) * 1000 * 60 * 60 * 24)))
						&& (new Date().before(uc.getExpirationDate()))) {
					uc.setDelay("Laranja");
					green.add(uc);
				} else if ((currentDate.after(uc.getExpirationDate()))) {
					uc.setDelay("Vermelho");
					red.add(uc);
				} else if (uc.getCallProgress() == CallProgress.ENCAMINHADO) {
					uc.setDelay("Branco");
					green.add(uc);
				} else if (uc.getCallProgress() == CallProgress.VISUALIZADO) {
					uc.setDelay("Azul");
					green.add(uc);
				} else if (uc.getCallProgress() == CallProgress.ANDAMENTO) {
					if (((currentDate.before(uc.getExpirationDate()))))
						uc.setDelay("Verde");
					green.add(uc);
				}
				// }

			}
			int from = (filter.getPage() - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE));
			int to = from + Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
					- 1;
			System.out.println(from + ", " + to);
			if (filter.getCallDelay().isEmpty())
				return Response.ok(tList.subList(from, to < tList.size() ? to : tList.size()))
						.header("X-Total-Count", tList.size()).header("X-Per-Page", SystemConfiguration.getInstance()
								.getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
						.build();

			if (filter.getCallDelay().equals("Atrasado"))
				return Response.ok(red.subList(from, to < red.size() ? to : red.size()))
						.header("X-Total-Count", red.size()).header("X-Per-Page", SystemConfiguration.getInstance()
								.getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
						.build();
			else
				return Response.ok(green.subList(from, to < green.size() ? to : green.size()))
						.header("X-Total-Count", green.size()).header("X-Per-Page", SystemConfiguration.getInstance()
								.getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
						.build();

		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InsufficientDataException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ErrorResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InternalException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}

	}

	
	//search com entity
	@GET
	@Path("/search2")
	public Response searchPaginaCFEN(@BeanParam CallFollowFilterBean filter)
			throws ParseException, HibernateException, SQLException, ModelException, InvalidEndpointException,
			InvalidPortException, XmlPullParserException, InvalidKeyException, NoSuchAlgorithmException, IOException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		EntityBusiness eb = new EntityBusiness();
		MinioServer minioServer = new MinioServer();
		List<UnsolvedCall> red = new ArrayList<UnsolvedCall>();
		List<UnsolvedCall> green = new ArrayList<UnsolvedCall>();
		try {
			String iniDateS = filter.getIniDate();
			String endDateS = filter.getEndDate();
			Date iniDate;
			Date endDate;
			if (!iniDateS.isEmpty() && !endDateS.isEmpty()) {
				iniDate = new SimpleDateFormat("dd/MM/yyyy").parse(iniDateS);
				endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(endDateS+" 23:59:59");
				if (iniDate.after(endDate))
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DATE_ERROR,
							Messages.getInstance().getMessage(IMessages.DATE_ERROR)));
			} else {
				GregorianCalendar gcInicial = new GregorianCalendar();
				gcInicial.add(Calendar.DAY_OF_MONTH, -30); // 30 dias atrás.
				GregorianCalendar gcFinal = new GregorianCalendar();
				gcFinal.add(Calendar.DAY_OF_MONTH, +1); // hoje 00:00.
				iniDate = gcInicial.getTime(); // pega a data de 30 dias atrás.
				endDate = gcFinal.getTime();
			}
			List<UnsolvedCall> tList = scb.searchPaginaCFEN(filter.getPage(), iniDate, endDate, filter.getCallSource(),
					filter.getCallClassificationId(), filter.getPriority(), filter.getUnsolvedCallId(),
					filter.getCitizenCpf(),filter.getEntityId());
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (UnsolvedCall uc : tList) {
				if (!uc.isNoMidia()) {
					Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
							"/" + uc.getParentCallId().getUnsolvedCallId() + "/1");
					if (myObjects.iterator().hasNext())
						uc.setFirstPhoto(minioServer.getMinioClient().presignedGetObject("call",
								myObjects.iterator().next().get().objectName()));

				}
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
				Date currentDate = new Date(System.currentTimeMillis());
				if ((new Date().after(new Date(uc.getExpirationDate().getTime() - (long) (1) * 1000 * 60 * 60 * 24)))
						&& (new Date().before(uc.getExpirationDate()))) {
					uc.setDelay("Laranja");
					green.add(uc);
				} else if ((currentDate.after(uc.getExpirationDate()))) {
					uc.setDelay("Vermelho");
					red.add(uc);
				} else if (uc.getCallProgress() == CallProgress.ENCAMINHADO) {
					uc.setDelay("Branco");
					green.add(uc);
				} else if (uc.getCallProgress() == CallProgress.VISUALIZADO) {
					uc.setDelay("Azul");
					green.add(uc);
				} else if (uc.getCallProgress() == CallProgress.ANDAMENTO) {
					if (((currentDate.before(uc.getExpirationDate()))))
						uc.setDelay("Verde");
					green.add(uc);
				}
				// }

			}
			int from = (filter.getPage() - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE));
			int to = from + Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
					- 1;
			System.out.println(from + ", " + to);
			if (filter.getCallDelay().isEmpty())
				return Response.ok(tList.subList(from, to < tList.size() ? to : tList.size()))
						.header("X-Total-Count", tList.size()).header("X-Per-Page", SystemConfiguration.getInstance()
								.getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
						.build();

			if (filter.getCallDelay().equals("Atrasado"))
				return Response.ok(red.subList(from, to < red.size() ? to : red.size()))
						.header("X-Total-Count", red.size()).header("X-Per-Page", SystemConfiguration.getInstance()
								.getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
						.build();
			else
				return Response.ok(green.subList(from, to < green.size() ? to : green.size()))
						.header("X-Total-Count", green.size()).header("X-Per-Page", SystemConfiguration.getInstance()
								.getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
						.build();

		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InsufficientDataException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ErrorResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InternalException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}

	}
	
	
	@PUT
	@Path("/mergeec")
	public Response mergeec(UnsolvedCall sc) throws HibernateException, SQLException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
		UnsolvedCall sc1 = scb.getById(sc.getUnsolvedCallId());
		CallClassification c = (CallClassification) ccb.getById(sc.getCallClassificationId().getCallClassificationId());
		sc1.setDescription(sc.getDescription());
		sc1.setObservation(sc.getObservation());
		sc1.setPriority(sc.getPriority());
		sc1.setEntityEntityCategoryMaps(
				eecb.getByEEC(sc.getEntityEntityCategoryMaps().getEntityCategory().getEntityCategoryId(),
						sc.getEntityEntityCategoryMaps().getEntity().getEntityId()));
		sc1.setCallClassificationId(c);
		scb.merge(sc1);
		return Response.status(Status.CREATED)
				.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
	}

	@GET
	@Path("/media/{id}")
	public Response getMedias(@PathParam("id") Long id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		MinioServer minioServer = new MinioServer();
		ArrayList<String> list = new ArrayList<String>();
		MinioClient minioClient;
		try {
			minioClient = minioServer.getMinioClient();
			Iterable<io.minio.Result<Item>> myObjects = minioClient.listObjects("call", "/" + id + "/");
			for (io.minio.Result<Item> result : myObjects) {
				Item item = result.get();
				String url = minioClient.presignedGetObject("call", item.objectName(), 60 * 60 * 24);
				list.add(url);
			}
			return Response.ok(list).build();
		} catch (InvalidEndpointException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (InvalidPortException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (InsufficientDataException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (NoResponseException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (ErrorResponseException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (InternalException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		}
	}

	@GET
	@Path("/getcall/{id}")
	public Response getCall(@PathParam("id") Long id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		MinioServer minioServer = new MinioServer();
		try {
			UnsolvedCall ucall = scb.getById(id);
			List<String> medias = new ArrayList<String>();
			Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
					"/" + ucall.getParentCallId().getUnsolvedCallId() + "/");
			for (io.minio.Result<Item> result : myObjects) {
				Item item = result.get();
				String callImage = minioServer.getMinioClient().presignedGetObject("call", item.objectName(),
						60 * 60 * 24);
				medias.add(callImage);
			}
			if (medias.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			ucall.setImages(medias);
			return Response.ok(ucall).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InsufficientDataException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ErrorResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InternalException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidEndpointException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidPortException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	private static void copyFile(String source, String dest) throws IOException {

		File source1 = new File(source);
		File dest1 = new File(dest);
		dest1.mkdir();
		try {
			FileUtils.copyDirectory(source1, dest1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@POST
	public Response add(UnsolvedCall unsolvedCall)
			throws HibernateException, SQLException, IOException, DataAccessLayerException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		SystemUserBusiness sub = new SystemUserBusiness();
		try {
			UnsolvedCall t = scb.getById(unsolvedCall.getUnsolvedCallId());
			Long idp = t.getParentCallId().getUnsolvedCallId();
			UnsolvedCall parent = scb.getById(idp);
			t.setParentCallId(parent);
			t.setUnsolvedCallId(null);
			t.setEntityEntityCategoryMaps(
					eecb.getByEEC(unsolvedCall.getEntityEntityCategoryMaps().getEntityCategory().getEntityCategoryId(),
							unsolvedCall.getEntityEntityCategoryMaps().getEntity().getEntityId()));
			t.setCallClassificationId(ccb.getById(unsolvedCall.getCallClassificationId().getCallClassificationId()));
			t.setUpdatedOrModeratedBy(sub.getById(unsolvedCall.getUpdatedOrModeratedBy().getSystemUserUsername()));
			t.setPriority(unsolvedCall.getPriority());
			t.setDescription(unsolvedCall.getDescription());
			t.setObservation(unsolvedCall.getObservation());
			t.setCreationOrUpdateDate(new Date());
			scb.save(t);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/hist/{id}")
	public Response history(@PathParam("id") Long id) throws SQLException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		List<UnsolvedCall> tList = scb.history(id);
		if (tList.isEmpty())
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@PUT
	@Path("/ed/{scid}")
	public Response mergeED(@PathParam("scid") Long scid) throws HibernateException, SQLException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		UnsolvedCall t = (UnsolvedCall) scb.getById(scid);
		t.changeCallStatus();
		if (scb.merge(t))
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
		else
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR,
					Messages.getInstance().getMessage(IMessages.UPDATE_ERROR)));
	}

	@PUT
	@Path("/send/{scid}")
	public Response sendUC(@PathParam("scid") Long scid) throws HibernateException, SQLException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		UnsolvedCall t = (UnsolvedCall) scb.getById(scid);
		t.setCallProgress(CallProgress.ENCAMINHADO);
		if (scb.merge(t))
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
		else
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR,
					Messages.getInstance().getMessage(IMessages.UPDATE_ERROR)));
	}

	// Está funcionando corretamente (Validação: 22/09/2016)
	@POST
	@Path("/upcall")
	public Response updateCall(UnsolvedCall unsolvedCall) throws HibernateException, SQLException, ModelException {
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb = new UnreadCallBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		UnsolvedCall child = (UnsolvedCall) ucb.getById(unsolvedCall.getUnsolvedCallId());
		Long parentId = child.getParentCallId().getUnsolvedCallId();
		UnsolvedCall parent = (UnsolvedCall) ucb.getById(parentId);
		child.setWebUser(null);
		child.setCallProgress(unsolvedCall.getCallProgress());
		if(unsolvedCall.getCallProgress() == CallProgress.REJEITADO)
			child.setCallStatus(CallStatus.REJEITADO);
		child.setObservation(unsolvedCall.getObservation());
		if (unsolvedCall.getCallStatus() != null)
			child.setCallStatus(unsolvedCall.getCallStatus());
		child.setParentCallId(null);
		child.setUnsolvedCallId(null);
		child.setCreationOrUpdateDate(new Date());
		if(unsolvedCall.getObservation() != null){
			AdditionalInformation ai = new AdditionalInformation();
			ai.setInformation(child.getObservation().getInformation());
			aib.save(ai);
			child.setObservation(ai);
		}
		if(unsolvedCall.getDescription() != null){
			AdditionalInformation ai = new AdditionalInformation();
			ai.setInformation(child.getDescription().getInformation());
			aib.save(ai);
			child.setDescription(ai);
		}

		if (ucb.save(child)) {
			child.setParentCallId(parent);
			ucb.merge(child);
			UnreadCall unreadCall = urb.findByUnsolvedcallId(parentId);
			if(unreadCall ==null){
			unreadCall = new UnreadCall();
			unreadCall.setUnreadCallId(parentId);
			unreadCall.setUnsolvedCallId(parent);
			}
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
		} else {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR,
					Messages.getInstance().getMessage(IMessages.UPDATE_ERROR)));
		}
	}

	// Está funcionando corretamente (Validação: 22/09/2016)
	@POST
	@Path("/finishcall")
	public Response finishcall(UnsolvedCall unsolvedCall) throws HibernateException, SQLException, ModelException {
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb = new UnreadCallBusiness();
		SolvedCallBusiness scb = new SolvedCallBusiness();
		WebUserBusiness wub = new WebUserBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		SystemUserBusiness sub = new SystemUserBusiness();
		AdditionalInformation ai = new AdditionalInformation();
		EntityBusiness eb = new EntityBusiness();
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		UnsolvedCall t = (UnsolvedCall) ucb.getById(unsolvedCall.getUnsolvedCallId());
		Long parentId = t.getParentCallId().getUnsolvedCallId();
		UnsolvedCall parent = (UnsolvedCall) ucb.getById(parentId);
		WebUser wu = null;
		boolean existWu = false;
		if (parent.getWebUser() != null) {
			existWu = true;
			wu = wub.getById(parent.getWebUser().getPublicIdentificationKey());
			parent.setWebUser(null);
			wub.merge(wu);
			t.setWebUser(null);
		}
		t.setCallProgress(unsolvedCall.getCallProgress());
		t.setParentCallId(null);
		t.setUnsolvedCallId(null);
		t.setCreationOrUpdateDate(new Date());
		
		int entityCategoryId = t.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityCategoryId();
		int entityId = t.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId();			
		t.addEntityEntityCategoryMaps(ecb.getById(entityCategoryId), eb.getById(entityId));
		if (t.getEntityEntityCategoryMaps().getEntityCategory().getSend_message().getValue() == 0)
			t.setCallProgress(CallProgress.FINALIZADO);
		else 
			t.setCallProgress(CallProgress.PROCESSADO);
		
		if (unsolvedCall.getObservation() != null) {			
			ai.setInformation(unsolvedCall.getObservation().getInformation());
			aib.save(ai);
			t.setObservation(ai);			
		}
		SystemUser su = sub.getById(unsolvedCall.getUpdatedOrModeratedBy().getSystemUserUsername());
		t.setUpdatedOrModeratedBy(su);
		if (ucb.save(t)) {
			t.setParentCallId(parent);
			if (ucb.merge(t)) {				
				if (t.getEntityEntityCategoryMaps().getEntityCategory().getSend_message().getValue() == 0) {
					List<UnsolvedCall> uclist = ucb.history(t.getParentCallId().getUnsolvedCallId());
					List<SolvedCall> sc2list = new ArrayList<SolvedCall>();
					SolvedCall sc = uclist.get(0).toSolvedCall();
					scb.save(sc);
					sc2list.add(sc);

					for (int ii = 1; ii < uclist.size(); ii++) {
						UnsolvedCall sc0 = uclist.get(ii);
						SolvedCall sc2 = sc0.toSolvedCall();
						scb.save2(sc2);
						sc2list.add(sc2);
					}
					UnreadCall unreadCall = urb.findByUnsolvedcallId(parentId);
					unreadCall.setUnsolvedCallId(null);
					urb.saveOrUpdate(unreadCall);
					ucb.delete(uclist.get(0));
					for (SolvedCall solved : sc2list) {
						solved.setParentCallId(sc);
						scb.merge(solved);
					}
				}
				UnreadCall unreadCall = urb.findByUnsolvedcallId(parentId);
				if(unreadCall ==null){
				unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(parentId);
				unreadCall.setSolvedCallId(scb.getById(parentId));
				}
				unreadCall.setRead(false);
				urb.saveOrUpdate(unreadCall);
				return Response.status(Status.CREATED)
						.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
			} else {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR,
						Messages.getInstance().getMessage(IMessages.UPDATE_ERROR)));
			}
		} else {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR,
					Messages.getInstance().getMessage(IMessages.UPDATE_ERROR)));
		}
	}
	
	@PUT
	@Path("/seen/{scid}")
	public Response seenC(@PathParam("scid") Long scid) throws HibernateException, SQLException, ModelException {
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb= new UnreadCallBusiness();
		AdditionalInformationBusiness aib=new AdditionalInformationBusiness();
		UnsolvedCall t = (UnsolvedCall) ucb.getById(scid);
		Long parentId=t.getParentCallId().getUnsolvedCallId();
		if (t.getCallProgress() == CallProgress.VISUALIZADO) {
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.ALREADYVIEWED_ERROR))).build();
		}
		UnsolvedCall parent = (UnsolvedCall) ucb.getById(parentId);
		t.setUnsolvedCallId(null);
		t.setParentCallId(parent.getParentCallId());
		t.setCallProgress(CallProgress.VISUALIZADO);
		AdditionalInformation ai=new AdditionalInformation();
		ai.setInformation(Messages.getInstance().getMessage(IMessages.CALL_VIZUALIZED_SUCCESS));
		aib.save(ai);
		t.setObservation(ai);
		t.setCreationOrUpdateDate(new Date());
		if (ucb.merge(t)) {
			UnreadCall unreadCall = urb.findByUnsolvedcallId(parentId);
			if(unreadCall ==null){
			unreadCall = new UnreadCall();
			unreadCall.setUnreadCallId(parentId);
			unreadCall.setUnsolvedCallId(parent);
			}
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
		} else {
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.VIEW_ERROR))).build();
		}
	}

	/*
	 * @POST
	 * 
	 * @Path("/updateCall") public Response updateCall(UnsolvedCall
	 * unsolvedCall) throws HibernateException, SQLException, ModelException {
	 * Response response = new Response(); UnsolvedCall t = (UnsolvedCall)
	 * scb.getById(unsolvedCall.getUnsolvedCallId()); UnsolvedCall parent =
	 * (UnsolvedCall) scb.getById(t.getParentCallId().getUnsolvedCallId());
	 * WebUser wu = null; boolean existWu = false; if (parent.getWebUser() !=
	 * null) { existWu = true; wu =
	 * wub.getById(parent.getWebUser().getPublicIdentificationKey());
	 * wu.setUnsolvedCallId(null); parent.setWebUser(null); wub.merge(wu);
	 * t.setWebUser(null); } if (unsolvedCall.getCallProgress() != null)
	 * t.setCallProgress(unsolvedCall.getCallProgress()); if
	 * (unsolvedCall.getObservation() != null)
	 * t.setObservation(unsolvedCall.getObservation()); if
	 * (unsolvedCall.getDescription() != null)
	 * t.setDescription(unsolvedCall.getDescription()); if
	 * (unsolvedCall.getCallProgress() != null)
	 * t.setCallProgress(unsolvedCall.getCallProgress()); if
	 * (unsolvedCall.getCallStatus() != null)
	 * t.setCallStatus(unsolvedCall.getCallStatus()); if
	 * (unsolvedCall.getUpdatedOrModeratedBy() != null)
	 * t.setUpdatedOrModeratedBy(unsolvedCall.getUpdatedOrModeratedBy());
	 * t.setParentCallId(null); t.setUnsolvedCallId(null);
	 * t.setCreationOrUpdateDate(new Date()); if (scb.save(t)) {
	 * t.setParentCallId(parent); scb.merge(t); if
	 * (t.getCallProgress().equals(CallProgress.FINALIZADO)) {
	 * List<UnsolvedCall> uclist = new ArrayList<UnsolvedCall>(
	 * scb.history(t.getParentCallId().getUnsolvedCallId())); List<SolvedCall>
	 * sclist = new ArrayList<SolvedCall>(); List<SolvedCall> sc2list = new
	 * ArrayList<SolvedCall>(); SolvedCall sc = uclist.get(0).toSolvedCall();
	 * sb2.saveUN(sc); if (existWu) { sc.setWebUser(wu); wu.setSolvedCallId(sc);
	 * } sc2list.add(sc);
	 * 
	 * for (int ii = 1; ii < uclist.size(); ii++) { UnsolvedCall sc0 =
	 * uclist.get(ii); SolvedCall sc2 = sc0.toSolvedCall(); sb2.save2(sc2);
	 * sc2list.add(sc2); }
	 * 
	 * UnsolvedCall usc = scb.getById(uclist.get(0).getUnsolvedCallId()); uclist
	 * = null; scb.mergeUN(usc); scb.deleteUN(usc);
	 * 
	 * for (SolvedCall solved : sc2list) { solved.setParentCallId(sc);
	 * sb2.merge(solved); } if (existWu) { wub.update(wu); } }
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); response.setSuccess(true); } else {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * UPDATE_ERROR)); response.setSuccess(false); } return response; }
	 * 
	 * @POST
	 * 
	 * @Path("/rc/{uid}/{reply}") public Response replyCall(@PathParam("uid")
	 * Long uid, @PathParam("reply") String reply) throws HibernateException,
	 * SQLException, ModelException, IOException { Response response = new
	 * Response(); UnsolvedCall t = scb.getByIdRC(uid); UnsolvedCall parent =
	 * scb.getById(t.getParentCallId().getUnsolvedCallId());
	 * t.setParentCallId(parent); t.setUnsolvedCallId(null);
	 * 
	 * t.setCallProgress(CallProgress.FINALIZADO); t.setCreationOrUpdateDate(new
	 * Date()); Long id; scb.save(t); if ((id = scb.saveUN(t)) != (long) -1) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * REPLY_SUCCESS)); response.setSuccess(true); } else {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SAVE_ERROR)); response.setSuccess(false); } return response; }
	 * 
	 * @Override
	 * 
	 * @GET
	 * 
	 * @Path("/{id}") public Response getById(@PathParam("id") Long id) throws
	 * HibernateException, SQLException, ValidationException,
	 * DataAccessLayerException, ModelException { Response response = new
	 * Response(); UnsolvedCall t = scb.getById(id); if (t == null) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * RESULT_NOT_FOUND)); } else { String jsonList = new JSONSerializer()
	 * .include("photo", "parentCallId",
	 * "entityEntityCategoryMaps.entityCategory.send_message")
	 * .exclude("*.class", "unsolvedCallCollection", "solvedCallCollection",
	 * "broadcastMessageCollection", "citizenCpf") .serialize(t);
	 * response.setJsonList(jsonList);
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); } return response; }
	 * 
	 * @PUT
	 * 
	 * @Path("/seen/{scid}") public Response seenC(@PathParam("scid") Long scid)
	 * throws HibernateException, SQLException, ModelException { Response
	 * response = new Response(); UnsolvedCall t = (UnsolvedCall)
	 * scb.getById(scid); if (t.getCallProgress() == CallProgress.VISUALIZADO) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * ALREADYVIEWED_ERROR)); response.setSuccess(true); return response; }
	 * UnsolvedCall parent = (UnsolvedCall)
	 * scb.getById(t.getParentCallId().getUnsolvedCallId());
	 * t.setUnsolvedCallId(null); t.setParentCallId(parent.getParentCallId());
	 * t.setCallProgress(CallProgress.VISUALIZADO);
	 * t.setCreationOrUpdateDate(new Date()); if (scb.merge(t)) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); response.setSuccess(true); String jsonList = new
	 * JSONSerializer() .exclude("*.class", "entityEntityCategoryMaps",
	 * "callClassificationId", "updatedOrModeratedBy", "priority",
	 * "observation") .transform(new DateTransformer("dd/MM/yyyy hh:mm"),
	 * "creationOrUpdateDate").serialize(t); } else {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * VIEW_ERROR)); response.setSuccess(false); }
	 * 
	 * return response; }
	 * 
	 * public static Date adicionarDiasUteis(Date data, Integer
	 * qtdeDiasAcrescentados) { Calendar dataInicial = Calendar.getInstance();
	 * dataInicial.setTime(data); while (qtdeDiasAcrescentados > 0) {
	 * dataInicial.add(Calendar.DAY_OF_MONTH, 1); int diaDaSemana =
	 * dataInicial.get(Calendar.DAY_OF_WEEK); if (diaDaSemana !=
	 * Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY) {
	 * --qtdeDiasAcrescentados; } } return dataInicial.getTime(); }
	 * 
	 */

}
