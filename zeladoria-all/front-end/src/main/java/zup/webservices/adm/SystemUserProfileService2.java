package zup.webservices.adm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import filters.SystemUserProfileFilterBean;
import flexjson.JSONSerializer;
import zup.bean.Log;
import zup.bean.SystemUserProfile;
import zup.bean.SystemUserProfilePermission;
import zup.bean.Result;
import zup.bean.SystemUser;
import zup.business.LogBusiness;
import zup.business.SystemUserProfileBusiness;
import zup.business.SystemUserProfilePermissionBusiness;
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
import zup.validate.SystemUserProfileValidate;

@Path("/systemuserprofile2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemUserProfileService2 {
	private static Logger logger = Logger.getLogger(SystemUserProfileService2.class);
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
			SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
			List<SystemUserProfile> tList = mb.findAll();
			for (SystemUserProfile systemUserProfile : tList) {
				systemUserProfile.addLinks("self",
						sub.selfSystemUserProfile(systemUserProfile.getSystemUserProfileId(), uriInfo));
				systemUserProfile.addLinks("ed",
						sub.edSystemUserProfile(systemUserProfile.getSystemUserProfileId(), uriInfo));
			}
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
	@Path("/combosativos")
	public Response getCombosAtivos() throws HibernateException, SQLException, ValidationException, ModelException {
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		try {
			List<SystemUserProfile> tList = new ArrayList<SystemUserProfile>();
			tList = mb.findActives();
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(tList).build();
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

	@PUT
	@Path("/ed/{id}")
	public Response mergeED(@PathParam("id") int id) {
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			SystemUserProfile t = mb.getById(id);
			List<SystemUser> us = sb.getByProfile(id);
			if (!us.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DISABLE_PROFILE_ERROR,
						Messages.getInstance().getMessage(IMessages.DISABLE_PROFILE_ERROR)));
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			t.changeEnabled();
			mb.merge(t);
			Log log = new Log(InformationType.SYSTEMUSERPROFILE, t.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			String message = "";
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.PROFILE_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.PROFILE_INACTIVATE_SUCCESS);
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
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@PUT
	public Response mergeE(SystemUserProfile systemUserProfile)
			throws HibernateException, DataAccessLayerException, SQLException, ModelException {
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		SystemUserProfilePermissionBusiness supb = new	SystemUserProfilePermissionBusiness();
		LogBusiness lb = new LogBusiness();
		try {
			SystemUserProfile t = (SystemUserProfile) mb.getById(systemUserProfile.getSystemUserProfileId());
			t.setName(systemUserProfile.getName());
			t.setSystemUserProfilePermissionCollection(new ArrayList<SystemUserProfilePermission>());
			for (SystemUserProfilePermission ii : systemUserProfile.getSystemUserProfilePermissionCollection())
				t.addSystemUserProfilePermission(supb.getById(ii.getSystemUserProfilePermissionId()));
			mb.merge(t);
			Log log = new Log(InformationType.SYSTEMUSERPROFILE, t.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.PROFILE_UPDATE_SUCCESS))).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@POST
	public Response saveEAT(SystemUserProfile systemUserProfile)
			throws HibernateException, ModelException, SQLException {
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		SystemUserProfilePermissionBusiness supb = new	SystemUserProfilePermissionBusiness();
		LogBusiness lb = new LogBusiness();
		try {
			Collection<SystemUserProfilePermission> suppcol = systemUserProfile
					.getSystemUserProfilePermissionCollection();
			systemUserProfile.setSystemUserProfilePermissionCollection(new ArrayList<SystemUserProfilePermission>());
			for (SystemUserProfilePermission ii : suppcol)
				systemUserProfile.addSystemUserProfilePermission(supb.getById(ii.getSystemUserProfilePermissionId()));
			mb.save(systemUserProfile);
			Log log = new Log(InformationType.SYSTEMUSERPROFILE, systemUserProfile.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.PROFILE_SAVE_SUCCESS))).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@GET
	@Path("/search")
	public Response searchPag2(@BeanParam SystemUserProfileFilterBean filter) {
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		try {
			Result<SystemUserProfile> result = mb.searchPag2(filter.getPage(), filter.getName(), filter.getEnabled());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (SystemUserProfile systemUserProfile : result.getList()) {
				systemUserProfile.addLinks("self",
						sub.selfSystemUserProfile(systemUserProfile.getSystemUserProfileId(), uriInfo));
				systemUserProfile.addLinks("ed",
						sub.edSystemUserProfile(systemUserProfile.getSystemUserProfileId(), uriInfo));
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
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		try {
			List<SystemUserProfile> tList = new ArrayList<SystemUserProfile>();
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
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		List<SystemUserProfile> tList = mb.findAll();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@GET
	@Path("/{id}")
	public Response getById22(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
		SystemUserProfile t = mb.getById(id);
		if (t == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		} else {
			String link = sub.selfSystemUserProfile(t.getSystemUserProfileId(), uriInfo);
			t.addLinks("ed", link);
			return Response.ok(t).build();
		}
	}
}
