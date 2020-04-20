package zup.webservices.adm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import filters.MessageModelFilterBean;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.business.LogBusiness;
import zup.business.MessageModelBusiness;
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
import zup.validate.MessageModelValidate;

@Path("/messagemodel2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageModelService2 {
	private static Logger logger = Logger.getLogger(MessageModelService2.class);
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
			MessageModelBusiness mb = new MessageModelBusiness();
			List<MessageModel> tList = mb.findAll();
			for (MessageModel messageModel : tList) {
				messageModel.addLinks("self", sub.selfMessageModel(messageModel.getMessageModelId(), uriInfo));
				messageModel.addLinks("ed", sub.edMessageModel(messageModel.getMessageModelId(), uriInfo));
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

	@PUT
	@Path("/ed/{id}")
	public Response mergeED(@PathParam("id") int id, @Context HttpServletRequest request) {
		MessageModelBusiness mb = new MessageModelBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			MessageModel t = mb.getById(id);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			t.changeEnabled();
			mb.merge(t);
			Log log = new Log(InformationType.MESSAGEMODEL, t.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			String message = "";
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.MESSAGE_MODEL_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.MESSAGE_MODEL_INACTIVATE_SUCCESS);
				log.setOperationType(OperationType.INATIVACAO);
			}
			lb.save(log);
			System.out.println(request.getRemoteAddr());
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
	public Response mergeE(@Valid MessageModel messageModel)
			throws HibernateException, DataAccessLayerException, SQLException, ModelException {
		MessageModelBusiness mb = new MessageModelBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			MessageModel t1 = mb.findByName(messageModel.getName());
			
			if(t1 != null){
				int messageModelId = messageModel.getMessageModelId();
				int t1Id = t1.getMessageModelId();				
				if (messageModelId != t1Id){
					throw new ZEMException(
							ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.MESSAGE_MODEL_DUPLICATED_NAME_ERROR,
									Messages.getInstance().getMessage(IMessages.MESSAGE_MODEL_DUPLICATED_NAME_ERROR)));
				}
			}
			
			MessageModel t = (MessageModel) mb.getById(messageModel.getMessageModelId());
			t.setName(messageModel.getName());
			t.setMessageBody(messageModel.getMessageBody());
			t.setSubject(messageModel.getSubject());
			mb.merge(t);
			Log log = new Log(InformationType.MESSAGEMODEL, t.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.MESSAGE_MODEL_UPDATE_SUCCESS)))
					.build();
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
		}
	}

	@POST
	public Response saveEAT(@Valid MessageModel messageModel)
			throws HibernateException, ModelException, SQLException {
		MessageModelBusiness mb = new MessageModelBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			if (mb.findByName(messageModel.getName()) != null)
				throw new ZEMException(
						ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.MESSAGE_MODEL_DUPLICATED_NAME_ERROR,
								Messages.getInstance().getMessage(IMessages.MESSAGE_MODEL_DUPLICATED_NAME_ERROR)));
			mb.save(messageModel);
			Log log = new Log(InformationType.MESSAGEMODEL, messageModel.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.MESSAGE_MODEL_SAVE_SUCCESS)))
					.build();
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
		} catch (ConstraintViolationException me) {
			me.printStackTrace();
			String message="";
			for(ConstraintViolation cv:me.getConstraintViolations())
				message+="\n"+cv.getMessage();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ConstraintViolationException", message));
		}
	}

	@GET
	@Path("/search")
	public Response searchPag2(@BeanParam MessageModelFilterBean filter) {
		MessageModelBusiness mb = new MessageModelBusiness();
		try {
			Result<MessageModel> result = mb.searchPag2(filter.getPage(), filter.getName(), filter.getSubject(),
					filter.getEnabled());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (MessageModel messageModel : result.getList()) {
				messageModel.addLinks("self", sub.selfMessageModel(messageModel.getMessageModelId(), uriInfo));
				messageModel.addLinks("ed", sub.edMessageModel(messageModel.getMessageModelId(), uriInfo));
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
		MessageModelBusiness mb = new MessageModelBusiness();
		try {
			List<MessageModel> tList = new ArrayList<MessageModel>();
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
		MessageModelBusiness mb = new MessageModelBusiness();
		List<MessageModel> tList = mb.getCombos();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@GET
	@Path("/enabled")
	public Response getEnabled() throws HibernateException, SQLException, ValidationException, ModelException {
		MessageModelBusiness mb = new MessageModelBusiness();
		List<MessageModel> tList = mb.findEnabled();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}
	
	
	@GET
	@Path("/{id}")
	public Response getById22(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		MessageModelBusiness mb = new MessageModelBusiness();
		MessageModel t = mb.getById(id);
		if (t == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		} else {
			String link = sub.selfMessageModel(t.getMessageModelId(), uriInfo);
			t.addLinks("ed", link);
			return Response.ok(t).build();
		}
	}
}
