package zup.webservices.adm;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.hibernate.HibernateException;

import zup.bean.SystemUserProfilePermission;
import zup.business.LogBusiness;
import zup.business.SystemUserProfilePermissionBusiness;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.Messages;

@Path("/supp2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemUserProfilePermissionService2 {
	private SystemUserProfilePermissionBusiness mb = new SystemUserProfilePermissionBusiness();
	LogBusiness lb = new LogBusiness();
	private @Context UriInfo uriInfo;

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}

	@GET
	public Response findAll() {
		try {
			List<SystemUserProfilePermission> tList = mb.findAll();
			return Response.ok(tList).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@GET
	@Path("/{id}")
	public Response getById22(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		SystemUserProfilePermission t = mb.getById(id);
		if (t == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(t).build();
	}
}
