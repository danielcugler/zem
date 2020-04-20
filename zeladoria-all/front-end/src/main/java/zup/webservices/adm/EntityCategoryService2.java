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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import filters.EntityCategoryFilterBean;
import filters.MessageModelFilterBean;
import zup.bean.EntityCategory;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.business.EntityCategoryBusiness;
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

@Path("/entitycategory2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntityCategoryService2 {
	private static Logger logger = Logger.getLogger(EntityCategoryService2.class);
//	private EntityCategoryBusiness mb = new EntityCategoryBusiness();
//	private EntityCategoryValidate mv = new EntityCategoryValidate();
//	private LogBusiness lb = new LogBusiness();
//	private SystemUserBusiness sb = new SystemUserBusiness();
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
			EntityCategoryBusiness mb = new EntityCategoryBusiness();
			List<EntityCategory> tList = mb.findAll();
			for (EntityCategory entityCategory : tList) {
				entityCategory.addLinks("self", sub.selfEntityCategory(entityCategory.getEntityCategoryId(), uriInfo));
				entityCategory.addLinks("ed", sub.edEntityCategory(entityCategory.getEntityCategoryId(), uriInfo));
			}
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

	@PUT
	@Path("/ed/{id}")
	public Response mergeED(@PathParam("id") int id) {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {			
			EntityCategory t = mb.getById(id);
			
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
	//		if(!t.getEntityCollection().isEmpty()){
	//			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.ENTITY_CATEGORY_ERROR,
	//					Messages.getInstance().getMessage(IMessages.ENTITY_CATEGORY_ERROR)));
	//		}
			t.changeEnabled();
			mb.merge(t);
			Log log = new Log(InformationType.ENTITYCATEGORY, t.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			String message = "";
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.ENTITY_CATEGORY_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.ENTITY_CATEGORY_INACTIVATE_SUCCESS);
				log.setOperationType(OperationType.INATIVACAO);
			}
			lb.save(log);
			return Response.status(Status.OK).entity(makeMessage(message)).build();
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR, se.getMessage()));
		}
	}

	@PUT
	public Response mergeE(EntityCategory entityCategory)
			throws HibernateException, DataAccessLayerException, SQLException, ModelException, ParseException {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {
			EntityCategory t1 = mb.findByName(entityCategory.getName());		
			
			if(t1 != null){
				int entityCategoryId = entityCategory.getEntityCategoryId();
				int t1Id = t1.getEntityCategoryId();				
				if (entityCategoryId != t1Id){
					throw new ZEMException(
							ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_ENTITY_CATEGORY,
									Messages.getInstance().getMessage(IMessages.DUPLICATED_ENTITY_CATEGORY)));
				}			
			}
			
			EntityCategory t = (EntityCategory) mb.getById(entityCategory.getEntityCategoryId());
			t.setName(entityCategory.getName());
			t.setSend_message(entityCategory.getSend_message());
			mb.merge(t);
			Log log = new Log(InformationType.ENTITYCATEGORY, t.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.ENTITY_CATEGORY_UPDATE_SUCCESS)))
					.build();
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR, se.getMessage()));
		}
	}

	@POST
	public Response saveEAT(EntityCategory entityCategory)
		 {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {
			if (mb.findByName(entityCategory.getName()) != null)
				throw new ZEMException(
						ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_ENTITY_CATEGORY,
								Messages.getInstance().getMessage(IMessages.DUPLICATED_ENTITY_CATEGORY)));
			mb.save(entityCategory);
			Log log = new Log(InformationType.ENTITYCATEGORY, entityCategory.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.ENTITY_CATEGORY_SAVE_SUCCESS)))
					.build();
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.UPDATE_ERROR, se.getMessage()));
		} catch (ParseException e) {
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernaterException", e.getMessage()));
		}
	}

	@GET
	@Path("/search")
	public Response searchPag2(@BeanParam MessageModelFilterBean filter) {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		try {
			Result<EntityCategory> result = mb.searchPag2(filter.getPage(), filter.getName(), filter.getEnabled());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (EntityCategory entityCategory : result.getList()) {
				entityCategory.addLinks("self", sub.selfEntityCategory(entityCategory.getEntityCategoryId(), uriInfo));
				entityCategory.addLinks("ed", sub.edEntityCategory(entityCategory.getEntityCategoryId(), uriInfo));
			}
			return Response.ok(result.getList()).header("X-Total-Count", result.getCount()).header("X-Per-Page",
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
					.build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} 
	}

	@GET
	@Path("/searchall2")
	public Response getAll2() throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			EntityCategoryBusiness mb = new EntityCategoryBusiness();
			List<EntityCategory> tList = new ArrayList<EntityCategory>();
			tList = mb.findAll();
			if (tList == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			
			int count = tList.size();
			return Response.ok(tList).header("X-Total-Count", count).build();
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ME", me.getMessage()));
		}
	}

	@GET
	@Path("/combos")
	public Response getCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		List<EntityCategory> tList = mb.findAll();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@GET
	@Path("/combosativos")
	public Response getCombosAtivos() throws HibernateException, SQLException, ValidationException, ModelException {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		List<EntityCategory> tList = mb.findActives();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}
	
	@GET
	@Path("/en/{entityid}")
	public Response getByEntity(@PathParam("entityid") Integer entityid) throws HibernateException, SQLException, ValidationException, ModelException {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		List<EntityCategory> tList = mb.findByEntity(entityid);
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	
	
	@GET
	@Path("/{id}")
	public Response getById22(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		EntityCategoryBusiness mb = new EntityCategoryBusiness();
		EntityCategory t = mb.getById(id);
		if (t == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		String link = sub.selfEntityCategory(t.getEntityCategoryId(), uriInfo);
		t.addLinks("ed", link);
		return Response.ok(t).build();

	}

}
