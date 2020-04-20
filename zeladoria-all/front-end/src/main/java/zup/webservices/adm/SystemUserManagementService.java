package zup.webservices.adm;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import flexjson.JSONSerializer;
import zup.action.Response;
import zup.bean.SystemUser;
import zup.business.SystemUserManagementBusiness;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.Messages;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/systemuserm")
public class SystemUserManagementService {
	private static Logger logger = Logger.getLogger(SystemUserProfileService.class);
	private SystemUserManagementBusiness subusiness;

	@GET
	@Path("/susup/{idu}/{idp}")
	public Response findByNameAndProfile(@PathParam("name") String name, @PathParam("idp") int idp)
			throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SystemUser> tList = subusiness.findByNameAndPerfil(name, idp);
			String jsonList = new JSONSerializer().exclude("*.class").serialize(tList);
			response.setJsonList(jsonList);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}
}