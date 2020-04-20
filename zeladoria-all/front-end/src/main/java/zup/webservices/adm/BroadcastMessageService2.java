package zup.webservices.adm;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.json.JSONObject;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import JsonTransformers.BMCTransformer;
import JsonTransformers.EnabledTransformer;
import JsonTransformers.FieldNameTransformer;
import JsonTransformers.FieldValueTransformer;
import filters.BroadcastMessageFilterBean;
import filters.MessageModelFilterBean;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import zup.bean.BroadcastMessage;
import zup.bean.BroadcastMessageCategory;
import zup.bean.Firebase;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.bean.SystemUser;
import zup.business.BroadcastMessageBusiness;
import zup.business.BroadcastMessageCategoryBusiness;
import zup.business.FirebaseBusiness;
import zup.business.LogBusiness;
import zup.business.SystemUserBusiness;
import zup.enums.BroadcastMessageCategoryName;
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
import zup.validate.BroadcastMessageValidate;

@Path("/broadcastmessage2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BroadcastMessageService2 {
	private static Logger logger = Logger.getLogger(BroadcastMessageService2.class);

	private SystemUserBusiness sb = new SystemUserBusiness();
	private LogBusiness lb = new LogBusiness();
	private ServiceUriBuilder sub = new ServiceUriBuilder();
	private @Context UriInfo uriInfo;

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}

	@GET
	@Path("/search")
	public Response searchPag2(@BeanParam BroadcastMessageFilterBean filter) {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		try {
			if (!filter.getCreationDate().isEmpty() && !filter.getPublicationDate().isEmpty()) {
				Date cdate = new SimpleDateFormat("ddMMyyyy").parse(filter.getCreationDate());
				Date pdate = new SimpleDateFormat("ddMMyyyy").parse(filter.getPublicationDate());
				if (cdate.after(pdate))
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DATE_ERROR,
							Messages.getInstance().getMessage(IMessages.DATE_ERROR)));
			}
			Result<BroadcastMessage> result = mb.searchPag2(filter.getPage(), filter.getSubject(), filter.getBmc(),
					filter.getEnabled(), filter.getCreationDate(), filter.getPublicationDate());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (BroadcastMessage broadcastMessage : result.getList()) {
				broadcastMessage.addLinks("self",
						sub.selfBroadcastMessage(broadcastMessage.getBroadcastMessageId(), uriInfo));
				broadcastMessage.addLinks("ed",
						sub.edBroadcastMessage(broadcastMessage.getBroadcastMessageId(), uriInfo));
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
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ParseException", e.getMessage()));

		}
	}

	@GET
	@Path("/search2")
	public Response search(@BeanParam BroadcastMessageFilterBean filter)
			throws ParseException, HibernateException, SQLException, ModelException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		if (!filter.getCreationDate().equals("z") && !filter.getPublicationDate().equals("z")) {
			Date cdate = new SimpleDateFormat("ddMMyyyy").parse(filter.getCreationDate());
			Date pdate = new SimpleDateFormat("ddMMyyyy").parse(filter.getPublicationDate());
			if (cdate.after(pdate))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DATE_ERROR,
						Messages.getInstance().getMessage(IMessages.DATE_ERROR)));
		}
		try {
			Map<Integer, List<BroadcastMessage>> tMap = mb.search(filter.getPage(), filter.getSubject(),
					filter.getBmc(), filter.getEnabled(), filter.getCreationDate(), filter.getPublicationDate());
			Integer count = 0;
			List<BroadcastMessage> tList = new ArrayList<BroadcastMessage>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			else {
				for (BroadcastMessage broadcastMessage : tList) {
					broadcastMessage.addLinks("self",
							sub.selfBroadcastMessage(broadcastMessage.getBroadcastMessageId(), uriInfo));
					broadcastMessage.addLinks("ed",
							sub.edBroadcastMessage(broadcastMessage.getBroadcastMessageId(), uriInfo));
				}
				return Response.ok(tList).header("X-Total-Count", count).header("X-Per-Page", SystemConfiguration
						.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)).build();
			}
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	public BroadcastMessageService2() {
	}

	
	
	
	
	
	//Notificação
	public void sendNotification(String title, String body) {
		HttpClient client = HttpClientBuilder.create().build();
		try {
			// Url do servidor firebase
			HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
			post.setHeader("Content-type", "application/json");
			// Chave do servidor firebase cloud message
			post.setHeader("Authorization",
					"key=AAAASxbAkYs:APA91bE5L8nRzN9y3zfRFtJcxyr5AHCz9WJfBUK69TNZtHolF7gTXrPudD3Bt8eeMAC6rKks4xhAVnwKZ46PkFEzPhabQpxs7VxooQOAxvu0sFVh1mHx4owhjgGxlkVCuLsuFNkJBvAw");
			FirebaseBusiness fb = new FirebaseBusiness();
			List<Firebase> citizens = fb.findAll();
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("body", body);
			JSONObject message = new JSONObject();
			//Prioridade
			message.put("priority", "high");
			//Alerta sonoro
			message.put("sound", "default");
			message.put("notification", notification);
			List<String> tokens = new ArrayList<String>();
			for(Firebase fr: citizens){
			tokens.add(fr.getToken());
			}
			//Chaves dos usuários
			message.put("registration_ids", tokens);
			post.setEntity(new StringEntity(message.toString(), "UTF-8"));
			HttpResponse response;
			response = client.execute(post);
			return;
		} catch (ClientProtocolException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ClientProtocolException", e.getMessage()));
		} catch (IOException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "IOException", e.getMessage()));
		} catch (HibernateException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}
	
	
	
	
	
	
	
	@PUT
	@Path("/publicar/{bmid}")
	public Response publicar(@PathParam("bmid") int bmid)
			throws HibernateException, SQLException, ModelException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		try {
			Date newDate = new Date();
			BroadcastMessage bmm = mb.getById(bmid);			
			bmm.setPublicationDate(new Date());
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(newDate);
			cal.add(Calendar.DATE, bmm.getDaysExpiration());
			bmm.setExpirationDate(cal.getTime());			
			mb.merge(bmm);
			Log log = new Log(InformationType.BROADCASTMESSAGE, bmm.makeLog());
			log.setOperationType(OperationType.PUBLICACAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			sendNotification("Novo Comunicado em Massa", bmm.getSubject());
			return Response.status(Status.OK)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.PUBLISH_SUCCESS))).build();
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

	@PUT
	@Path("/ed/{id}")
	public Response mergeED(@PathParam("id") int id) {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		try {
			BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
			BroadcastMessage t = mb.getById(id);
			SystemUser su = sb.getById(username);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			if (su == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			t.changeEnabled();
			mb.merge(t);
			String message = "";
			Log log = new Log(InformationType.BROADCASTMESSAGE, t.makeLog());
			log.setSystemUserUsername(su);
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.BROADCAST_MESSAGE_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.BROADCAST_MESSAGE_INACTIVATE_SUCCESS);
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
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@POST
	@Path("/svbm") // Salvar Comunicado em Massa
	public Response add(BroadcastMessage bm)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, IOException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		BroadcastMessageCategoryBusiness bmcb = new BroadcastMessageCategoryBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		try {
			Date newDate = new Date();
			if (bm.getPublicationDate() != null){
				bm.setPublicationDate(new Date());
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(newDate);
				cal.add(Calendar.DATE, bm.getExpirationDateVal());
				bm.setExpirationDate(cal.getTime());
			}				
			SystemUser sup = sb.getById(username);
			BroadcastMessageCategory bmc = bmcb
					.getById(bm.getBroadcastMessageCategoryId().getBroadcastMessageCategoryId());
			bm.setCreatedBy(sup);
			bm.setBroadcastMessageCategoryId(bmc);			
			bm.setCreationDate(newDate);
			bm.setRegistrationDate(newDate);			
			mb.save(bm);
			Log log = new Log(InformationType.BROADCASTMESSAGE, bm.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(sup);
			lb.save(log);
			return Response.status(Status.OK)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.BROADCAST_MESSAGE_SAVE_SUCCESS)))
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@POST
	@Path("/sp") // Salvar e publicar comunicado em massa.
	public Response addP(BroadcastMessage bm)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, IOException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		BroadcastMessageCategoryBusiness bmcb = new BroadcastMessageCategoryBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		try {
			// if (bm.getPublicationDate() != null)
			bm.setPublicationDate(new Date());
			// BroadcastMessage bm2= mb.getById(bm.getBroadcastMessageId());
			// SystemUser su =
			// sb.getById(bm.getCreatedBy().getSystemUserUsername());
			SystemUser sup = sb.getById(username);
			BroadcastMessageCategory bmc = bmcb
					.getById(bm.getBroadcastMessageCategoryId().getBroadcastMessageCategoryId());
			bm.setCreatedBy(sup);
			bm.setBroadcastMessageCategoryId(bmc);
			Date newDate = new Date();
			bm.setCreationDate(newDate);
			bm.setPublicationDate(newDate);
			bm.setRegistrationDate(newDate);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(newDate);
			cal.add(Calendar.DATE, bm.getExpirationDateVal());
			bm.setExpirationDate(cal.getTime());
			mb.save(bm);
			Log log = new Log(InformationType.BROADCASTMESSAGE, bm.makeLog());
			log.setOperationType(OperationType.PUBLICACAO);
			log.setSystemUserUsername(sup);
			lb.save(log);
			sendNotification("Novo Comunicado em Massa", bm.getSubject());
			return Response.status(Status.OK)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.PUBLISH_SUCCESS))).build();
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
	@Path("/upbm") // Editar comunicado em passa.
	public Response update(BroadcastMessage bm)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		BroadcastMessageCategoryBusiness bmcb = new BroadcastMessageCategoryBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		try {
			BroadcastMessage bm2 = mb.getById(bm.getBroadcastMessageId());
			Date newDate = new Date();
			if (bm2.getPublicationDate() != null){
				//bm2.setPublicationDate(new Date());
				if (bm.getExpirationDateVal() != -1) {
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(bm2.getPublicationDate());
					cal.add(Calendar.DATE, bm.getExpirationDateVal());
					bm2.setExpirationDate(cal.getTime());
				}
			}				
			SystemUser sup = sb.getById(username);
			BroadcastMessageCategory bmc = bmcb
					.getById(bm.getBroadcastMessageCategoryId().getBroadcastMessageCategoryId());
			bm2.setSubject(bm.getSubject());
			bm2.setMessageBody(bm.getMessageBody());
			bm2.setBroadcastMessageCategoryId(bmc);
			bm2.setDaysExpiration(bm.getDaysExpiration());			
			bm2.setRegistrationDate(newDate);			
			mb.merge(bm2);
			Log log = new Log(InformationType.BROADCASTMESSAGE, bm2.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sup);
			lb.save(log);
			return Response.status(Status.OK)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.BROADCAST_MESSAGE_UPDATE_SUCCESS)))
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@PUT
	@Path("/mp") // Salvar e publicar comunicado em massa - UPDATE
	public Response updateP(BroadcastMessage bm)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		BroadcastMessageCategoryBusiness bmcb = new BroadcastMessageCategoryBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
		try {
			BroadcastMessage bm2 = mb.getById(bm.getBroadcastMessageId());
			SystemUser sup = sb.getById(username);
			bm2.setPublicationDate(new Date());
			BroadcastMessageCategory bmc = bmcb
					.getById(bm.getBroadcastMessageCategoryId().getBroadcastMessageCategoryId());
			bm2.setSubject(bm.getSubject());
			bm2.setMessageBody(bm.getMessageBody());
			bm.setBroadcastMessageCategoryId(bmc);
			bm2.setBroadcastMessageCategoryId(bmc);	
			bm2.setDaysExpiration(bm.getDaysExpiration());
			Date newDate = new Date();
			bm2.setPublicationDate(new Date());
			bm2.setRegistrationDate(new Date());
			if (bm.getExpirationDateVal() != -1) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(newDate);
				cal.add(Calendar.DATE, bm.getExpirationDateVal());
				bm2.setExpirationDate(cal.getTime());
			}
			mb.merge(bm2);
			Log log = new Log(InformationType.BROADCASTMESSAGE, bm.makeLog());
			log.setOperationType(OperationType.PUBLICACAO);
			log.setSystemUserUsername(sup);
			lb.save(log);
			return Response.status(Status.OK)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.PUBLISH_SUCCESS))).build();
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
	public Response findAll() {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		try {
			List<BroadcastMessage> tList = mb.findAll();
			for (BroadcastMessage messageModel : tList) {
				messageModel.addLinks("self", sub.selfMessageModel(messageModel.getBroadcastMessageId(), uriInfo));
				messageModel.addLinks("ed", sub.edMessageModel(messageModel.getBroadcastMessageId(), uriInfo));
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

	@GET
	@Path("/searchall2")
	public Response getAll2() throws HibernateException, SQLException, ValidationException, ModelException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		try {
			List<BroadcastMessage> tList = new ArrayList<BroadcastMessage>();
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
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		List<BroadcastMessage> tList = mb.findAll();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		BroadcastMessageBusiness mb = new BroadcastMessageBusiness();
		try {
			BroadcastMessage t = mb.getById(id);
			return Response.ok(t).build();

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

}
