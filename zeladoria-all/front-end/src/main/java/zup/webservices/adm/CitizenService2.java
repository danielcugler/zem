package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import filters.CitizenFilterBean;
import zup.bean.BroadcastMessage;
import zup.bean.Citizen;
import zup.bean.Log;
import zup.bean.Result;
import zup.business.CitizenBusiness;
import zup.business.LogBusiness;
import zup.business.SystemUserBusiness;
import zup.enums.Enabled;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.formatter.FormatterMd5;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.utils.ServiceUriBuilder;
import zup.validate.CitizenValidate;

@Path("/citizen2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitizenService2 {
	private static Logger logger = Logger.getLogger(Citizen.class);

	
	private ServiceUriBuilder sub = new ServiceUriBuilder();
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
			CitizenBusiness mb = new CitizenBusiness();
			List<Citizen> tList = mb.findAll();
			for (Citizen citizen : tList) {
				citizen.addLinks("self", sub.selfCitizen(citizen.getCitizen_cpf(), uriInfo));
				citizen.addLinks("ed", sub.edCitizen(citizen.getCitizen_cpf(), uriInfo));
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
	@Path("/ed/{cpf}")
	public Response mergeED(@PathParam("cpf") String cpf) {
		CitizenBusiness mb = new CitizenBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {
			Citizen t = mb.getById(cpf);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			t.changeEnabled();
					mb.merge(t);
			Log log = new Log(InformationType.CITZEN, t.makeLog());
			log.setSystemUserUsername(sb.getById(username));
			String message = "";
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.CITIZEN_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.CITIZEN_INACTIVATE_SUCCESS);
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
	public Response mergeE(Citizen citizen)
			throws HibernateException, DataAccessLayerException, SQLException, ModelException {
		CitizenBusiness mb = new CitizenBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {
			Citizen t = (Citizen) mb.getById(citizen.getCitizen_cpf());
			t.setName(citizen.getName());
			t.setEmail(citizen.getEmail());
			mb.merge(t);
			Log log = new Log(InformationType.MESSAGEMODEL, t.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.CITIZEN_UPDATE_SUCCESS))).build();
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

	public static String getHash(String txt, String hashType) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
			byte[] array = md.digest(txt.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			
		}
		return null;
	}

	public static String md5(String txt) {
		return getHash(txt, "MD5");
	}

	public static String sha1(String txt) {
		return getHash(txt, "SHA1");
	}
	
	@POST
	@Path("/webuser")
	public Response saveEATWeb(Citizen citizen) throws HibernateException, ModelException, SQLException {
		CitizenBusiness mb = new CitizenBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		try {
			Citizen cit=mb.getById(citizen.getCitizen_cpf());
			if(cit!=null)
				if(cit.getEmail().equals(citizen.getEmail()))
					return Response.status(Status.OK)
							.entity(cit).build();
					else
						return Response.status(Status.BAD_REQUEST)
								.entity(makeMessage("Usuário já cadastrado")).build();
			citizen.setPublicKey(FormatterMd5.md5(citizen.getCitizen_cpf() + citizen.getEmail()));
			mb.save(citizen);
			Log log = new Log(InformationType.CITZEN, citizen.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
//			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(citizen).build();
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}
	
	
	@POST
	public Response saveEAT(Citizen citizen) throws HibernateException, ModelException, SQLException {
		CitizenBusiness mb = new CitizenBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {
			mb.save(citizen);
			Log log = new Log(InformationType.CITZEN, citizen.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.CITIZEN_SAVE_SUCCESS))).build();
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
	public Response searchPag2(@BeanParam CitizenFilterBean filter) {
		try {
			CitizenBusiness mb = new CitizenBusiness();
			Result<Citizen> result = mb.searchPag2(filter.getPage(), filter.getName(), filter.getEmail(),
					filter.getEnabled());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (Citizen citizen : result.getList()) {
				citizen.addLinks("self", sub.selfCitizen(citizen.getCitizen_cpf(), uriInfo));
				citizen.addLinks("ed", sub.edCitizen(citizen.getCitizen_cpf(), uriInfo));
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
			CitizenBusiness mb = new CitizenBusiness();
			List<Citizen> tList = new ArrayList<Citizen>();
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
		CitizenBusiness mb = new CitizenBusiness();
		List<Citizen> tList = mb.findAll();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@GET
	@Path("/{id}")
	public Response getById22(@PathParam("id") String cpf)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		CitizenBusiness mb = new CitizenBusiness();
		Citizen t = mb.getById(cpf);
		if (t == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		} else {
			String link = sub.selfCitizen(t.getCitizen_cpf(), uriInfo);
			t.addLinks("ed", link);
			return Response.ok(t).build();
		}
	}

	@GET
	@Path("/read/{id}")
	public Response getByIdRead(@PathParam("id") String cpf)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		CitizenBusiness mb = new CitizenBusiness();
		List<Integer> t = mb.getRead(cpf);
		if (t == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		}
		return Response.ok(t).build();
		
	}
	
	@GET
	@Path("/login")
	public Response login(@QueryParam("email") String email, @QueryParam("password") String password)
			throws ParseException, HibernateException, SQLException, ModelException {
		CitizenBusiness mb = new CitizenBusiness();
		try {
			Citizen c = mb.login(email);
			if (c == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.USER_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
			/*if (c.getPassword().equals(FormatterMd5.md5(password))) {
				return Response.status(Status.OK)
						.entity(makeMessage(Messages.getInstance().getMessage(IMessages.LOGIN_SUCCESS))).build();
			} else {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.PASSWORD_ERROR,
						Messages.getInstance().getMessage(IMessages.PASSWORD_ERROR)));
			}*/
			if (!c.getPassword().equals(FormatterMd5.md5(password))){
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.PASSWORD_ERROR,
						Messages.getInstance().getMessage(IMessages.PASSWORD_ERROR)));
			} else if(c.getEnabled() == Enabled.DISABLED){
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.INACTIVE_USER,
						Messages.getInstance().getMessage(IMessages.INACTIVE_USER)));
			} else {
				return Response.status(Status.OK)
						.entity(c).build();
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
		}

	}
}
