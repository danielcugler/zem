package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import filters.CallClassificationFilterBean;
import filters.MessageModelFilterBean;
import zup.bean.CallClassification;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.business.CallClassificationBusiness;
import zup.business.LogBusiness;
import zup.business.MessageModelBusiness;
import zup.business.SystemUserBusiness;
import zup.enums.Enabled;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;

@Path("/callclassification2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CallClassificationService2 {
	private static Logger logger = Logger.getLogger(CallClassification.class);

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}
	
	@POST
	public Response save(CallClassification callClassification) {
		CallClassificationBusiness mb = new CallClassificationBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			mb.save(callClassification);
			Log log = new Log(InformationType.CALLCLASSIFICATION, callClassification.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			log.setOperationType(OperationType.INCLUSAO);
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}
	
	@PUT
	public Response merge(CallClassification callClassification) {
		CallClassificationBusiness mb = new CallClassificationBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			mb.merge(callClassification);
			Log log = new Log(InformationType.CALLCLASSIFICATION, callClassification.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			log.setOperationType(OperationType.INCLUSAO);
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION))).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@GET
	@Path("/combos")
	public Response getCombos() {
		CallClassificationBusiness mb = new CallClassificationBusiness();
		try {
			List<CallClassification> tList = new ArrayList<CallClassification>();
			try {
				tList = mb.findAll();
				return Response.ok(tList).build();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
			} catch (DataAccessLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
			}

		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", me.getMessage()));
		}
	}

	@GET
	@Path("/search")
	public Response search(@BeanParam CallClassificationFilterBean filter) {
		CallClassificationBusiness mb = new CallClassificationBusiness();
		Result<CallClassification> result;
		try {
			result = mb.search(filter.getPage(), filter.getName());
		if (result.getList().isEmpty())
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(result.getList()).header("X-Total-Count", result.getCount()).header("X-Per-Page",
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
				.build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ZEMException", e.getMessage()));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ParseException", e.getMessage()));
		}
	}

	@GET
	@Path("/combo")
	public Response getForCombo() {
		CallClassificationBusiness mb = new CallClassificationBusiness();
		try {
			List<CallClassification> tList;
			try {
				tList = mb.findAll();
				if (tList.isEmpty())
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
							Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
				return Response.ok(tList).build();
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
			} catch (DataAccessLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
			}

		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", me.getMessage()));
		}
	}

}
