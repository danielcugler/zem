package zup.webservices.adm;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import flexjson.JSONSerializer;
import zup.action.Response;
import zup.bean.SystemUserProfilePermission;
import zup.business.SystemUserProfilePermissionBusiness;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.Messages;

@Path("/systemuserprofilepermission")
public class SystemUserProfilePermissionService extends AbstractService<SystemUserProfilePermission, Integer> {
	private static Logger logger = Logger.getLogger(SystemUserProfilePermissionService.class);

	SystemUserProfilePermissionBusiness supb = new SystemUserProfilePermissionBusiness();

	public SystemUserProfilePermissionService() {
		super(SystemUserProfilePermission.class);
		super.setBusiness(supb);
		super.setCampoOrdenacao("name");
	}

	@GET
	@Path("/combo")
	public Response getForCombo() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SystemUserProfilePermission> tList = supb.findAll();
			String json = new JSONSerializer().exclude("*.class", "systemUserProfileCollection").serialize(tList);
			response.setJsonList(json);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@GET
	@Path("/ec/{id}")
	public Response getById(@PathParam("id") int entityId)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Response response = new Response();
		SystemUserProfilePermission t = supb.getById(entityId);
		String entityJson = new JSONSerializer().exclude("*.class", "SystemUserProfileCollection").serialize(t);
		response.setJsonList(entityJson);
		if (t == null) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		return response;
	}

}
