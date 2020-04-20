package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.hateoas.Resources;

import filters.EntityCategoryFilterBean;
import filters.MessageModelFilterBean;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.LogBusiness;
import zup.business.SystemUserBusiness;
import zup.enums.Enabled;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.utils.ServiceUriBuilder;
import zup.validate.EntityCategoryValidate;

@Path("/eecm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntityEntityCategoryMapsService2 {
	private static Logger logger = Logger.getLogger(EntityCategoryService2.class);
	private ServiceUriBuilder sub = new ServiceUriBuilder();
	private @Context UriInfo uriInfo;

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}

	@GET
	public Response findAll() {
		try {
			EntityEntityCategoryMapsBusiness mb = new EntityEntityCategoryMapsBusiness();
			List<EntityEntityCategoryMaps> tList = mb.findAll();
			return Response.ok(tList).build();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}


	
	@GET
	@Path("/en/{id}")
	public Response getById22(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
/*		EntityEntityCategoryMaps mb = new EntityEntityCategoryMaps();
		EntityCategory t = mb.getById(id);
		if (t == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		String link = sub.selfEntityCategory(t.getEntityCategoryId(), uriInfo);
		t.addLinks("ed", link);
	*/
		return Response.ok().build();

	}

}
