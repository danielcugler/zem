package zup.webservices.adm;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import JsonTransformers.BMCTransformer;
import flexjson.JSONSerializer;
import zup.action.Response;
import zup.bean.BroadcastMessageCategory;
import zup.business.BroadcastMessageCategoryBusiness;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.validate.BroadcastMessageCategoryValidate;

@Path("/broadcastmessagecategory")
public class BroadcastMessageCategoryService extends AbstractService<BroadcastMessageCategory, Integer> {
	private static Logger logger = Logger.getLogger(BroadcastMessageCategory.class);

	BroadcastMessageCategoryBusiness mb = new BroadcastMessageCategoryBusiness();
	BroadcastMessageCategoryValidate mv = new BroadcastMessageCategoryValidate();

	public BroadcastMessageCategoryService() {
		super(BroadcastMessageCategory.class);
		super.setBusiness(mb);
		super.setValidate(mv);
		super.setCampoOrdenacao("name");
	}

	@GET
	@Path("/combo")
	public Response getForCombo() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<BroadcastMessageCategory> tList = mb.findAll();
			String citizenJson = new JSONSerializer().transform(new BMCTransformer(), "name")
					.exclude("*.class", "broadcastMessageCollection").serialize(tList);
			response.setJsonList(citizenJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	
	@GET
	@Path("/combos")
	public javax.ws.rs.core.Response getForCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<BroadcastMessageCategory> tList = mb.findAll();
			return javax.ws.rs.core.Response.ok(tList).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}
	
	
	@Override
	@GET
	public Response getAll() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<BroadcastMessageCategory> tList = (List<BroadcastMessageCategory>) (((BroadcastMessageCategoryBusiness) super.getBusiness())
					.findAll());
			String citizenJson = new JSONSerializer().transform(new BMCTransformer(), "name").exclude("*.class")
					.serialize(tList);
			response.setJsonList(citizenJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@GET
	@Path("/name/{name}")
	public Response findByName(@PathParam("name") String name)
			throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<BroadcastMessageCategory> tList = (List<BroadcastMessageCategory>) (((BroadcastMessageCategoryBusiness) super.getBusiness())
					.findByName(name));
			String citizenJson = new JSONSerializer().exclude("*.class").serialize(tList);
			response.setJsonList(citizenJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

}
