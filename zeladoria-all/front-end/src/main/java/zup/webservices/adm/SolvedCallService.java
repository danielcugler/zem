package zup.webservices.adm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import JsonTransformers.CallStatusTransformer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import zup.action.Response;
import zup.bean.CallClassification;
import zup.bean.EntityCategory;
import zup.bean.SolvedCall;
import zup.business.CallClassificationBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.SolvedCallBusiness;
import zup.business.SystemUserBusiness;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.validate.SolvedCallValidate;

@Path("/solvedcall")
public class SolvedCallService extends AbstractService<SolvedCall, Long> {

	SolvedCallBusiness scb = new SolvedCallBusiness();
	EntityCategoryBusiness ecb = new EntityCategoryBusiness();
	CallClassificationBusiness ccb = new CallClassificationBusiness();
	SystemUserBusiness sub = new SystemUserBusiness();
	SolvedCallValidate scv = new SolvedCallValidate();
	EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();

	private static Logger loggersc = Logger.getLogger(SolvedCall.class);
	private static final String SERVER_UPLOAD_LOCATION_FOLDER = "C:/uploadcall/";

	public SolvedCallService() {
		super(SolvedCall.class);
		super.setBusiness(scb);
		super.setValidate(scv);
		super.setCampoOrdenacao("creationDate");
	}

	@PUT
	@Path("/merge/{entityCategory}/{callClassification}")
	public Response merge(@PathParam("entityCategory") int entityCategory,
			@PathParam("callClassification") int callClassification, SolvedCall sc)
					throws HibernateException, SQLException, ModelException {
		Response response = new Response();
		SolvedCall sc1 = scb.getById(sc.getSolvedCallId());
		CallClassification c = (CallClassification) ccb.getById(callClassification);
		EntityCategory ec = (EntityCategory) ecb.getById(entityCategory);
		sc1.setDescription(sc.getDescription());
		sc1.setObservation(sc.getObservation());
		sc1.setPriority(sc.getPriority());
		sc1.setEntityEntityCategoryMaps(
				eecb.getByEEC(sc.getEntityEntityCategoryMaps().getEntityCategory().getEntityCategoryId(),
						sc.getEntityEntityCategoryMaps().getEntity().getEntityId()));

		sc1.setCallClassificationId(c);
		scb.merge(sc1);
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		return response;
	}

	@GET
	@Path("/media/{id}")
	public Response getMedias(@PathParam("id") Long id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		Response response = new Response();
		File folder = new File(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY) + id);
		File[] files = folder.listFiles();
		ArrayList<byte[]> media = new ArrayList<byte[]>();

		for (File file : files) {
			if (file.exists()) {

				byte[] data = null;
				try {
					data = Files.readAllBytes(file.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				media.add(data);
			}
		}
		if (media.isEmpty()) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else {
			String jsonList = new JSONSerializer().serialize(media);
			response.setJsonList(jsonList);
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		}
		return response;
	}

	@Override
	@POST
	public Response add(SolvedCall solvedCall) throws HibernateException, SQLException {
		Response response = new Response();
		SolvedCall t = null;
		try {
			t = (SolvedCall) scb.getById(solvedCall.getSolvedCallId());
		} catch (DataAccessLayerException e) {
			response.setMessage(e.getMessage());
		} catch (ModelException e) {
			response.setMessage(e.getMessage());
		}
		t.setSolvedCallId(null);

		try {
			t.setEntityEntityCategoryMaps(
					eecb.getByEEC(solvedCall.getEntityEntityCategoryMaps().getEntityCategory().getEntityCategoryId(),
							solvedCall.getEntityEntityCategoryMaps().getEntity().getEntityId()));
		} catch (DataAccessLayerException e) {
			response.setMessage(e.getMessage());
		} catch (ModelException e) {
			response.setMessage(e.getMessage());
		}
		try {
			t.setCallClassificationId(ccb.getById(solvedCall.getCallClassificationId().getCallClassificationId()));
		} catch (DataAccessLayerException e) {
			response.setMessage(e.getMessage());
		} catch (ModelException e) {
			response.setMessage(e.getMessage());
		}
		try {
			t.setUpdatedOrModeratedBy(sub.getById(solvedCall.getUpdatedOrModeratedBy().getSystemUserUsername()));
		} catch (DataAccessLayerException e) {
			response.setMessage(e.getMessage());
		} catch (ModelException e) {
			response.setMessage(e.getMessage());
		}
		t.setPriority(solvedCall.getPriority());
		t.setDescription(solvedCall.getDescription());
		t.setObservation(solvedCall.getObservation());
		if (scb.save(t)) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
			response.setSuccess(true);
		} else {
			response.setMessage(Messages.getInstance().getMessage(IMessages.SAVE_ERROR));
			response.setSuccess(false);
		}
		return response;
	}

	@GET
	@Path("/search/{page}/{periodod}/{periodoa}/{callSource}/{callclassificationid}/{entityid}/{priority}/{userid}/{callStatus}")
	public Response searchPag(@PathParam("page") int page, @PathParam("periodod") String periodod,
			@PathParam("periodoa") String periodoa, @PathParam("callSource") String callSource,
			@PathParam("callclassificationid") String callclassificationid, @PathParam("entityid") String entityid,
			@PathParam("priority") String priority, @PathParam("userid") String userid,
			@PathParam("callStatus") String callStatus)
					throws ParseException, HibernateException, SQLException, ModelException {

		Response response = new Response();
		if (!periodod.equals("z") && !periodoa.equals("z")) {
			Date periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod);
			Date periodoa2 = new SimpleDateFormat("ddMMyyyy").parse(periodoa);
			if (periodod2.after(periodoa2)) {
				response.setMessage(Messages.getInstance().getMessage(IMessages.DATE_ERROR));
				return response;

			}
		}
		Map<Integer, List<SolvedCall>> tMap = scb.searchPag(page, periodod, periodoa, callSource, callclassificationid,
				entityid, priority, userid, callStatus);
		Integer count = 0;
		List<SolvedCall> tList = new ArrayList<SolvedCall>();
		for (Integer key : tMap.keySet()) {
			count = key;
			tList = tMap.get(key);
			break;
		}
		for (SolvedCall call : tList) {
			File file = new File(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY)
							+ call.getSolvedCallId() + "/1.png");
			byte[] data = null;
			try {
				data = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
			Collection<byte[]> cbyte = new ArrayList<byte[]>();
			cbyte.add(data);
			call.setMedias(cbyte);
		}

		String strJson = new JSONSerializer()
				.include("callClassificationId.name", "parentCallId.solvedCallId", "entityCategoryTarget.name",
						"updatedOrModeratedBy.systemUserUsername", "medias", "priority", "solvedCallId", "callStatus",
						"callProgress", "callSource", "creationOrUpdateDate", "description")
				.exclude("*.class", "*").transform(new CallStatusTransformer(), "callStatus")
				.transform(new DateTransformer("dd/MM/yyyy hh:mm"), "creationOrUpdateDate").serialize(tList);
		if (!tList.isEmpty()) {
			response.setJsonList(strJson);
			response.setCount(count);
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		} else
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return response;
	}

	@Override
	@GET
	public Response getAll() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SolvedCall> tList = scb.findAll();
			for (SolvedCall call : tList) {
				File file = new File(
						SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY)
								+ call.getSolvedCallId() + "/1.png");
				byte[] data = null;
				try {
					data = Files.readAllBytes(file.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				Collection<byte[]> cbyte = new ArrayList<byte[]>();
				cbyte.add(data);
				call.setMedias(cbyte);
			}
			String jsonList = new JSONSerializer().include("medias").exclude("*.class")
					.transform(new DateTransformer("dd/MM/yyyy hh:mm"), "creationOrUpdateDate").serialize(tList);
			response.setJsonList(jsonList);
		} catch (ModelException me) {

			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@PUT
	@Path("/ed/{scid}")
	public Response mergeED(@PathParam("scid") Long scid) throws HibernateException, SQLException, ModelException {
		Response response = new Response();
		SolvedCall t = (SolvedCall) scb.getById(scid);
		t.changeCallStatus();
		if (scb.merge(t)) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
			response.setSuccess(true);
		} else {
			response.setMessage(Messages.getInstance().getMessage(IMessages.ACTIVATION_ERROR));
			response.setSuccess(false);
		}
		return response;
	}

	@Override
	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") Long id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		Response response = new Response();
		SolvedCall t = scb.getById(id);
		if (t == null) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else {
			String jsonList = new JSONSerializer().include("photo", "parentCallId.parentCallId").exclude("*.class",
					"solvedCallCollection", "unsolvedCallCollection", "broadcastMessageCollection", "citizenCpf")
					.serialize(t);
			response.setJsonList(jsonList);
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		}
		return response;
	}

}
