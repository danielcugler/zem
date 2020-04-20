package zup.webservices.adm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;

import flexjson.JSONSerializer;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.ISystemConfiguration;
import zup.messages.SystemConfiguration;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/systemconfiguration")
public class SystemConfigurationService {
	
	@GET
	@Path("/ipserver")
	public Response findIpServer(){
		try{
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.IP_SERVER));
			System.out.println(message.toString());
			return Response.ok(message).build();
		}catch (Exception e){
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}
}
