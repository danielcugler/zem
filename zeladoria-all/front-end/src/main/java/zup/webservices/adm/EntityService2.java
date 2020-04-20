package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
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
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.hateoas.Resources;

import filters.EntityFilterBean;
import filters.MessageModelFilterBean;
import flexjson.JSONSerializer;
import zup.bean.AttendanceTime;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.EntityEntityCategoryMapsPK;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.bean.SystemUser;
import zup.bean.UnsolvedCall;
import zup.business.AttendanceTimeBusiness;
import zup.business.EntityBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.LogBusiness;
import zup.business.SystemUserBusiness;
import zup.business.UnsolvedCallBusiness2;
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
import zup.validate.EntityValidate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

@Path("/entity2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntityService2 {
	private static Logger logger = Logger.getLogger(EntityService2.class);
	private ServiceUriBuilder sub = new ServiceUriBuilder();
	private @Context UriInfo uriInfo;

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}
	
	public ZEMException exceptionHandle(Exception ex){
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));
		if (ex instanceof DataAccessLayerException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", ex.getMessage()));
		if (ex instanceof ZEMException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ZEMException", ex.getMessage()));
		if (ex instanceof ModelException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", ex.getMessage()));
		if (ex instanceof SQLException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", ex.getMessage()));
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));

     	return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", ex.getMessage()));
	}

	@GET
	public Response findAll() {
		EntityBusiness mb = new EntityBusiness();
		try {
			List<Entity> tList = mb.findAll();
			for (Entity Entity : tList) {
				Entity.addLinks("self", sub.selfEntity(Entity.getEntityId(), uriInfo));
				Entity.addLinks("ed", sub.edEntity(Entity.getEntityId(), uriInfo));
			}
			return Response.ok(tList).build();
//			catch (HibernateException | DataAccessLayerException | SQLException | ModelException e)
		} catch (Exception e) {
			e.printStackTrace();
			throw ZEMException.exceptionHandle(e);
		}
	}

	
	@POST
	@Path("/teste")
	public Response testepost() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", "Funcionou");
	return Response.ok(map).build();
	}

	@PUT
	@Path("/teste")
	public Response testeput() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", "Funcionou");
	return Response.ok(map).build();
	}
	
	
	@GET
	@Path("/teste/{id}")
	public Response testeget(@PathParam("id") int id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", "Funcionou "+ id);
	return Response.ok(map).build();
	}
	
	
	
	@GET
	@Path("/combosativos")
	public Response getCombosAtivos() throws HibernateException, SQLException, ValidationException, ModelException {
		EntityBusiness mb = new EntityBusiness();
		try {
			List<Entity> tList = new ArrayList<Entity>();
			tList = mb.findActives();
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(tList).build();
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

	/*@PUT
	@Path("/ed/{id}")
	public Response mergeED(@PathParam("id") int id) {
		EntityBusiness mb = new EntityBusiness();
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
	    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username2 = user.getUsername(); //get logged in username
		UnsolvedCallBusiness2 ub = new UnsolvedCallBusiness2();
		SystemUserBusiness sub = new SystemUserBusiness();
		try {
			Entity t = mb.getById(id);
			List<UnsolvedCall> uc = ub.getByEntity(id);
			List<SystemUser> su = sub.getByEnity(id);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			if (!su.isEmpty() && t.getEnabled().equals(Enabled.ENABLED))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.ENTITY_USER_ERROR,
						Messages.getInstance().getMessage(IMessages.ENTITY_USER_ERROR)));
			if (!uc.isEmpty() && t.getEnabled().equals(Enabled.ENABLED))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.ENTITY_CALL_ERROR,
						Messages.getInstance().getMessage(IMessages.ENTITY_CALL_ERROR)));
			t.changeEnabled();
			mb.merge(t);
			Log log = new Log(InformationType.ENTITY, t.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			String message = "";
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.ENTITY_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.ENTITY_INACTIVATE_SUCCESS);
				log.setOperationType(OperationType.INATIVACAO);
			}
			lb.save(log);
			return Response.status(Status.OK).entity(makeMessage(message)).build();
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
	}*/

	@PUT
	@Path("/ed/{id}")
	public Response mergeED(@PathParam("id") int id) {
		EntityBusiness mb = new EntityBusiness();
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
	    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username = user.getUsername(); //get logged in username
		UnsolvedCallBusiness2 ub = new UnsolvedCallBusiness2();
		SystemUserBusiness sub = new SystemUserBusiness();
		try {
			Entity t = mb.getById(id);
			List<UnsolvedCall> uc = ub.getByEntity(id);
			List<SystemUser> su = sub.getByEnity(id);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			if (!su.isEmpty() && t.getEnabled().equals(Enabled.ENABLED))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.ENTITY_USER_ERROR,
						Messages.getInstance().getMessage(IMessages.ENTITY_USER_ERROR)));
			if (!uc.isEmpty() && t.getEnabled().equals(Enabled.ENABLED))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.ENTITY_CALL_ERROR,
						Messages.getInstance().getMessage(IMessages.ENTITY_CALL_ERROR)));
			t.changeEnabled();
			mb.merge(t);
			Log log = new Log(InformationType.ENTITY, t.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			String message = "";
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.ENTITY_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.ENTITY_INACTIVATE_SUCCESS);
				log.setOperationType(OperationType.INATIVACAO);
			}
			lb.save(log);
			return Response.status(Status.OK).entity(makeMessage(message)).build();
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

	
	public boolean containsElem(Integer id, Collection<EntityCategory> list) {
		for (EntityCategory ec2 : list)
			if (id == ec2.getEntityCategoryId())
				return false;
		return true;
	}

	public Map<String, List<Integer>> getEqualities(Collection<EntityCategory> map1, Collection<EntityCategory> map2) {
		Map<String, List<Integer>> equalList = new HashMap<String, List<Integer>>();
		List<Integer> equals = new ArrayList<Integer>();
		List<Integer> remove = new ArrayList<Integer>();
		List<Integer> add = new ArrayList<Integer>();
		for (EntityCategory ec : map1)
			remove.add(ec.getEntityCategoryId());
		for (EntityCategory ec : map2)
			add.add(ec.getEntityCategoryId());
		for (EntityCategory eecm1 : map1)
			for (EntityCategory eecm2 : map2)
				if (eecm2.getEntityCategoryId() == eecm1.getEntityCategoryId()) {
					System.out.println(eecm2.getEntityCategoryId());
					remove.remove(eecm1.getEntityCategoryId());
					add.remove(eecm1.getEntityCategoryId());
					break;
				}

		equalList.put("add", add);
		equalList.put("remove", remove);
		return equalList;
	}

	@PUT
	public Response mergeE(Entity entity)
			throws HibernateException, DataAccessLayerException, SQLException, ModelException, ParseException {
		EntityBusiness mb = new EntityBusiness();
		EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
	    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username = user.getUsername(); //get logged in username
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		try {
			Entity t1 = mb.findByName(entity.getName());

			if (t1 != null) {
				int entityId = entity.getEntityId();
				int t1Id = t1.getEntityId();
				if (entityId != t1Id) {
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_ENTITY,
							Messages.getInstance().getMessage(IMessages.DUPLICATED_ENTITY)));
				}
			}

			Entity t = (Entity) mb.getById(entity.getEntityId());
			t.setName(entity.getName());
			t.setIcon(entity.getIcon());
			AttendanceTime at = t.getAttendanceTime();
			at.setHighPriorityTime(entity.getAttendanceTime().getHighPriorityTime());
			at.setMediumPriorityTime(entity.getAttendanceTime().getMediumPriorityTime());
			at.setLowPriorityTime(entity.getAttendanceTime().getLowPriorityTime());
			mb.merge(t);
			Collection<EntityCategory> eccol = entity.getEntityCategoryCollection();
			// t.setEntityCategoryCollection(new ArrayList<EntityCategory>());
			Collection<EntityCategory> ecList = new ArrayList<EntityCategory>(t.getEntityCategoryCollection());
			Map<String, List<Integer>> map = getEqualities(ecList, entity.getEntityCategoryCollection());
			for (Integer entityCategoryId : map.get("add")) {
				EntityEntityCategoryMaps eecm = new EntityEntityCategoryMaps(entityCategoryId, t.getEntityId());
				eecb.save(eecm);
			}
			for (Integer entityCategoryId : map.get("remove")) {
				EntityEntityCategoryMapsPK eecm = new EntityEntityCategoryMapsPK(entityCategoryId, t.getEntityId());				
				EntityEntityCategoryMaps emaps = eecb.getById(eecm);
				eecb.delete(emaps);
			}
			t = mb.getById(t.getEntityId());
			Log log = new Log(InformationType.ENTITY, t.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.ENTITY_UPDATE_SUCCESS))).build();
		} catch (ConstraintViolationException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ConstraintViolationException",
					"Categoria já usada em um chamado, logo não pode ser modificada!"));
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
	
	@POST
	public Response save(Entity entity)
			throws ModelException, ParseException {
		EntityBusiness mb = new EntityBusiness();
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
	    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    String username = user.getUsername(); //get logged in username
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		try {
			if (mb.findByName(entity.getName()) != null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_ENTITY,
						Messages.getInstance().getMessage(IMessages.DUPLICATED_ENTITY)));
			Collection<EntityCategory> eccol = entity.getEntityCategoryCollection();
			entity.setEntityCategoryCollection(new ArrayList<EntityCategory>());
			for (EntityCategory ec : eccol)
				entity.addEntityCategoryCollection(ecb.getById(ec.getEntityCategoryId()));
			mb.saveEnt(entity);
			Log log = new Log(InformationType.ENTITY, entity.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.ENTITY_SAVE_SUCCESS))).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		}
	}

	@GET
	@Path("/search")
	@PreAuthorize("hasRole('ROLE_ENTITY_READ')")
	public Response searchPag2(@BeanParam MessageModelFilterBean filter) {
	      User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      String name = user.getUsername(); //get logged in username
	      System.out.println("Username: "+ name);
		try {
			EntityBusiness mb = new EntityBusiness();
			Result<Entity> result = mb.searchPag2(filter.getPage(), filter.getName(), filter.getEnabled());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (Entity entity : result.getList()) {
				entity.addLinks("self", sub.selfEntity(entity.getEntityId(), uriInfo));
				entity.addLinks("ed", sub.edEntity(entity.getEntityId(), uriInfo));
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
	@Path("/search2")
	public Response searchPag2(@BeanParam EntityFilterBean filter) {
		EntityBusiness mb = new EntityBusiness();
		Map<Integer, List<Entity>> tMap = null;
		try {
			try {
				tMap = mb.searchPag(filter.getPage(), filter.getName(), filter.getEnabled());
			} catch (HibernateException he) {
				// TODO Auto-generated catch block
				he.printStackTrace();
				throw new ZEMException(
						ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
			} catch (SQLException se) {
				// TODO Auto-generated catch block
				se.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
			}
			Integer count = 0;
			List<Entity> tList = new ArrayList<Entity>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			else {
				for (Entity Entity : tList) {
					Entity.addLinks("self", sub.selfEntity(Entity.getEntityId(), uriInfo));
					Entity.addLinks("ed", sub.edEntity(Entity.getEntityId(), uriInfo));
				}
				return Response.ok(tList).header("X-Total-Count", count).header("X-Per-Page", SystemConfiguration
						.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)).build();
			}
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		}
	}

	@GET
	@Path("/search/{page}/{name}/{enabled}")
	public Response searchPag(@PathParam("page") int page, @PathParam("name") String name,
			@PathParam("enabled") String enabled) {
		EntityBusiness mb = new EntityBusiness();
		Map<Integer, List<Entity>> tMap = null;
		try {
			try {
				tMap = mb.searchPag(page, name, enabled);
			} catch (HibernateException he) {
				he.printStackTrace();
				throw new ZEMException(
						ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
			} catch (SQLException se) {
				se.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
			}
			Integer count = 0;
			List<Entity> tList = new ArrayList<Entity>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			else {
				for (Entity Entity : tList) {
					Entity.addLinks("self", sub.selfEntity(Entity.getEntityId(), uriInfo));
					Entity.addLinks("ed", sub.edEntity(Entity.getEntityId(), uriInfo));
				}
				return Response.ok(tList).header("X-Total-Count", count).header("X-Per-Page", SystemConfiguration
						.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)).build();
			}
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		}
	}

	@GET
	@Path("/searchall2")
	public Response getAll2() throws HibernateException, SQLException, ValidationException, ModelException {
		EntityBusiness mb = new EntityBusiness();
		try {
			List<Entity> tList = new ArrayList<Entity>();
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
	@Path("/lock/{entityId}")
	public Response getByIdECLock(@PathParam("entityId") Integer entityId)
			throws HibernateException, SQLException, ValidationException, ModelException {
		EntityBusiness mb = new EntityBusiness();
		try {
			List<EntityCategory> tList = mb.getByIdECLock(entityId);
			return Response.ok(tList).build();
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ME", me.getMessage()));
		}
	}

	@GET
	@Path("/combos")
	public Response getCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		EntityBusiness mb = new EntityBusiness();
		List<Entity> tList = mb.findAll();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@GET
	@Path("/{id}")
	public Response getById22(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		EntityBusiness mb = new EntityBusiness();
		try {
			Entity t = mb.getById(id);
			if (t == null) {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			} else {
				String link = sub.selfEntity(t.getEntityId(), uriInfo);
				t.addLinks("ed", link);
				return Response.ok(t).build();
			}
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

}
