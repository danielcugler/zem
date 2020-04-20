package zup.facade;

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

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;
import org.xmlpull.v1.XmlPullParserException;

import filters.CallMonitoringFilterBean;
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
import zup.bean.CallClassification;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.Result;
import zup.bean.UnreadCall;
import zup.bean.UnsolvedCall;
import zup.business.AdditionalInformationBusiness;
import zup.business.CallClassificationBusiness;
import zup.business.EntityBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.SystemUserBusiness;
import zup.business.UnreadCallBusiness;
import zup.business.UnsolvedCallBusiness2;
import zup.enums.CallProgress;
import zup.enums.CallStatus;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.utils.MinioServer;

public class CallModerationFacade {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	// private static Logger loggersc = Logger.getLogger(UnsolvedCall.class);

	public Map<String, String> makeMessage(String code) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", Messages.getInstance().getMessage(code));
		message.put("code", code);
		return message;
	}

	/*
	 * @PUT
	 * 
	 * @Path("/mergeec") public Response mergeec(UnsolvedCall sc) throws
	 * HibernateException, SQLException, ModelException { Response response =
	 * new Response(); UnsolvedCall sc1 = scb.getById(sc.getUnsolvedCallId());
	 * CallClassification c = (CallClassification)
	 * ccb.getById(sc.getCallClassificationId().getCallClassificationId());
	 * sc1.setDescription(sc.getDescription());
	 * sc1.setObservation(sc.getObservation());
	 * sc1.setPriority(sc.getPriority()); sc1.setEntityEntityCategoryMaps(
	 * eecb.getByEEC(sc.getEntityEntityCategoryMaps().getEntityCategory().
	 * getEntityCategoryId(),
	 * sc.getEntityEntityCategoryMaps().getEntity().getEntityId()));
	 * sc1.setCallClassificationId(c); scb.merge(sc1);
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); return response; }
	 */

	public ArrayList<String> getMedias(Long id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException,
			InvalidEndpointException, InvalidPortException, XmlPullParserException, InvalidKeyException,
			InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException,
			ErrorResponseException, InternalException, IOException, InvalidExpiresRangeException {
		MinioServer minioServer = new MinioServer();
		ArrayList<String> list = new ArrayList<String>();
		MinioClient minioClient;
		minioClient = minioServer.getMinioClient();
		Iterable<io.minio.Result<Item>> myObjects = minioClient.listObjects("call", "/" + id + "/");
		for (io.minio.Result<Item> result : myObjects) {
			Item item = result.get();
			String url = minioClient.presignedGetObject("call", item.objectName());
			list.add(url);
		}
		return list;
	}

	public static String getHash(String txt, String hashType) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
			byte[] array = md.digest(txt.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

	public static String md5(String txt) {
		return getHash(txt, "MD5");
	}

	public static String sha1(String txt) {
		return getHash(txt, "SHA1");
	}

	public Result<UnsolvedCall> searchPag2(CallMonitoringFilterBean filter) throws ParseException, HibernateException,
			SQLException, ModelException, InvalidEndpointException, InvalidPortException, XmlPullParserException,
			InvalidKeyException, InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException,
			NoResponseException, ErrorResponseException, InternalException, IOException, InvalidExpiresRangeException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		MinioServer minioServer = new MinioServer();
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
			SimpleDateFormat date = new SimpleDateFormat("ddMMyyyy");
			GregorianCalendar gcInicial = new GregorianCalendar();
			gcInicial.add(Calendar.DAY_OF_MONTH, -30); // 30 dias atrás.
			GregorianCalendar gcFinal = new GregorianCalendar();
			gcFinal.add(Calendar.DAY_OF_MONTH, +1); // hoje 00:00.
			iniDate = gcInicial.getTime(); // pega a data de 30 dias atrás.
			endDate = gcFinal.getTime();
		}
		Result<UnsolvedCall> result = scb.searchPag(filter.getPage(), iniDate, endDate, filter.getCallSource(),
				filter.getCallClassificationId(), filter.getEntityCategoryId(), filter.getPriority(),
				filter.getUsername(), filter.getCallStatus(), filter.getUnsolvedCallId(), filter.getCitizenCpf());
		List<UnsolvedCall> tList = new ArrayList<UnsolvedCall>(result.getList());
		for (UnsolvedCall call : tList) {
			if (!call.isNoMidia()) {
				
				Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
						"/" + call.getParentCallId().getUnsolvedCallId() + "/1");
				if (myObjects.iterator().hasNext()) 
					call.setFirstPhoto( minioServer.getMinioClient().presignedGetObject("call",
							myObjects.iterator().next().get().objectName()));
				/*	
					for (io.minio.Result<Item> result2 : myObjects) {
					Item item = result2.get();
					call.setFirstPhoto(
							minioServer.getMinioClient().presignedGetObject("call", item.objectName(), 60 * 60 * 24));
					break;
				}
				*/
				
			}
		}
		result.setResult(tList);
		return result;

	}

	public UnsolvedCall getById(Long id) throws SQLException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		return scb.getById(id);
	}

	public List<UnsolvedCall> history(@PathParam("id") Long id) throws SQLException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		return scb.history(id);
	}

	public List<UnsolvedCall> getAll() throws HibernateException, SQLException, ValidationException, ModelException,
			InvalidEndpointException, InvalidPortException, XmlPullParserException, InvalidKeyException,
			InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException,
			ErrorResponseException, InternalException, IOException, InvalidExpiresRangeException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		MinioServer minioServer = new MinioServer();
		List<UnsolvedCall> tList = scb.findAll();
		for (UnsolvedCall call : tList) {
			if (!call.isNoMidia()) {
				Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
						"/" + call.getParentCallId().getUnsolvedCallId() + "/");
				for (io.minio.Result<Item> result2 : myObjects) {
					Item item = result2.get();
					call.setFirstPhoto(
							minioServer.getMinioClient().presignedGetObject("call", item.objectName()));
					break;
				}
			}
		}
		return tList;
	}

	public Map<String, List> getCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
//		EntityEntityCategoryMapsBusiness eecb=new EntityEntityCategoryMapsBusiness();
		List<CallClassification> ccList = ccb.findAll();
		List<EntityCategory> ecList = ecb.findActives();
		Map<String, List> map = new HashMap<String, List>();
		map.put("cc", ccList);
		map.put("ec", ecList);
		return map;
	}
	
	
	
	public Map<String, List> getCombos2() throws HibernateException, SQLException, ValidationException, ModelException {
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		EntityBusiness enb = new EntityBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		List<CallClassification> ccList = ccb.findAll();
		List<EntityCategory> ecList = ecb.findAll();
		List<Entity> enList = enb.findAll();
		Map<String, List> map = new HashMap<String, List>();
		map.put("cc", ccList);
		map.put("ec", ecList);
		map.put("en", enList);
		
		return map;
	}

	public Map<String, String> mergeED(Long scid, String username)
			throws HibernateException, SQLException, ModelException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb = new UnreadCallBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		SystemUserBusiness sub = new SystemUserBusiness();
		UnsolvedCall t = (UnsolvedCall) scb.getById(scid);
		Long parentId= t.getParentCallId().getUnsolvedCallId();
		t.setUnsolvedCallId(null);
		t.setParentCallId(null);
		scb.save(t);
		UnsolvedCall parent = scb.getById(parentId);
		t.setParentCallId(parent);
		AdditionalInformation ai = new AdditionalInformation();
		t.changeCallStatus();
		t.setUpdatedOrModeratedBy(sub.getById(username));
		t.setCreationOrUpdateDate(new Date());		
		String code;
		if (t.getCallStatus() == CallStatus.ATIVO) {
			ai.setInformation(Messages.getInstance().getMessage(IMessages.CALL_ACTIVATE_SUCCESS));
			code = IMessages.CALL_ACTIVATE_SUCCESS;
		} else {			
			//t.setCallProgress(CallProgress.REJEITADO);
			ai.setInformation(Messages.getInstance().getMessage(IMessages.CALL_INACTIVATE_SUCCESS));
			code = IMessages.CALL_INACTIVATE_SUCCESS;
		}
		aib.save(ai);
		t.setObservation(ai);
		scb.mergeUN(t);
		UnreadCall unreadCall = new UnreadCall();
		unreadCall.setUnsolvedCallId(parent);
		unreadCall.setRead(false);
		urb.save(unreadCall);
		return makeMessage(code);
	}

	public Map<String, String> sendUC(Long scid, String username) throws HibernateException, SQLException,
			ModelException, DataAccessLayerException, ValidationException, IOException {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		SystemUserBusiness sub = new SystemUserBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		UnsolvedCall t = (UnsolvedCall) scb.getById(scid);
		Long parentId= t.getParentCallId().getUnsolvedCallId();
		t.setUnsolvedCallId(null);
		t.setParentCallId(null);
		scb.save(t);
		UnsolvedCall parent = scb.getById(parentId);
		t.setParentCallId(parent);		
		t.setCallProgress(CallProgress.ENCAMINHADO);
		AdditionalInformation obs= new AdditionalInformation();
		obs.setInformation(Messages.getInstance().getMessage(IMessages.CALL_SEND_SUCCESS));
		aib.save(obs);
		t.setObservation(obs);
		t.setCreationOrUpdateDate(new Date());
		t.setUpdatedOrModeratedBy(sub.getById(username));
		scb.merge(t);
		return makeMessage(IMessages.CALL_SEND_SUCCESS);
	}
}
