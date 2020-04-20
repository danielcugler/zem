package zup.webservices.adm;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.HibernateException;

import flexjson.JSONSerializer;
import zup.action.Response;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/menu")
public class MenuService {
@GET
public Response getMenu() throws HibernateException, SQLException, ValidationException, ModelException {
	Response response = new Response();
    String menuJson=SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.MENU_JSON);	
		response.setJsonList(menuJson);
	response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
	return response;
}
}
