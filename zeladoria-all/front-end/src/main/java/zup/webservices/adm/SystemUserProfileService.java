package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import JsonTransformers.EnabledTransformer;
import JsonTransformers.FieldNameTransformer;
import JsonTransformers.FieldValueTransformer;
import flexjson.JSONSerializer;
import zup.action.Response;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.SystemUser;
import zup.bean.SystemUserProfile;
import zup.bean.SystemUserProfilePermission;
import zup.business.SystemUserBusiness;
import zup.business.SystemUserProfileBusiness;
import zup.business.SystemUserProfilePermissionBusiness;
import zup.enums.Enabled;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.Messages;

@Path("/systemuserprofile")
public class SystemUserProfileService extends AbstractService<SystemUserProfile, Integer> {
	private static Logger logger = Logger.getLogger(SystemUserProfileService.class);

	SystemUserProfileBusiness mb = new SystemUserProfileBusiness();
	SystemUserProfilePermissionBusiness supb = new SystemUserProfilePermissionBusiness();
	SystemUserBusiness sb = new SystemUserBusiness();

	public SystemUserProfileService() {
		super(SystemUserProfile.class);
		super.setBusiness(mb);
		super.setCampoOrdenacao("name");
		JSONSerializer serializer = new JSONSerializer();
		serializer.include("systemUserProfileId", "name", "enabled", "systemUserProfilePermissionCollection");
		serializer.exclude("*");
		serializer.transform(new FieldValueTransformer("Habilitado"), "enabled");
		Map<String, String> propertyMapRefObj = new HashMap<String, String>();
		Map<String, String> propertyMap = new HashMap<String, String>();
		// Perfil de usuário
		propertyMap.put("name", "Nome");
		propertyMap.put("systemUserProfileId", "ID de Perfil de usuário");
		for (Map.Entry<String, String> entry : propertyMap.entrySet())
			serializer.transform(new FieldNameTransformer(entry.getValue()), entry.getKey());
		super.setSerializer(serializer);
	}

	@Override
	public String jsonRename(String json) {
		JSONObject my_obj = new JSONObject(json);

		JSONArray ja = (JSONArray) my_obj.get("systemUserProfilePermissionCollection");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = (JSONObject) ja.get(i);
			jo.put("Nome", jo.remove("name"));
			jo.put("", jo.remove("Nome"));
		}

		return my_obj.toString();
	}

	@Override
	public boolean mergeEDLog(SystemUserProfile t, Enabled en, String username)
			throws HibernateException, DataAccessLayerException, SQLException {
		if (mb.update(t)) {
			List<SystemUserProfilePermission> listec = new ArrayList<SystemUserProfilePermission>(
					t.getSystemUserProfilePermissionCollection());
			t.setSystemUserProfilePermissionCollection(listec);
			String json = jsonRename(super.getSerializer().serialize(t));

			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(InformationType.SYSTEMUSERPROFILE);
			if (en.equals(Enabled.ENABLED))
				log.setOperationType(OperationType.ATIVACAO);
			else
				log.setOperationType(OperationType.INATIVACAO);
			try {
				log.setSystemUserUsername(sb.getById(username));
			} catch (ModelException e) {
				e.printStackTrace();
			}
		//	log.setContent(json);
			lb.save(log);
			return true;
		}
		return false;
	}

	public boolean saveLog(SystemUserProfile t, String username)
			throws HibernateException, DataAccessLayerException, SQLException {
		if (mb.save(t)) {
			List<SystemUserProfilePermission> list = new ArrayList<SystemUserProfilePermission>(
					t.getSystemUserProfilePermissionCollection());
			t.setSystemUserProfilePermissionCollection(list);
			String json = jsonRename(super.getSerializer().serialize(t));
			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(getByClass(t));
			log.setOperationType(OperationType.INCLUSAO);
			try {
				log.setSystemUserUsername(sb.getById(username));
			} catch (ModelException e) {
				e.printStackTrace();
			}
		//	log.setContent(json);
			lb.save(log);
			return true;
		}
		return false;
	}

	@Override
	public boolean mergeLog(SystemUserProfile t, String username)
			throws HibernateException, DataAccessLayerException, SQLException {
		if (mb.update(t)) {
			List<SystemUserProfilePermission> list = new ArrayList<SystemUserProfilePermission>(
					t.getSystemUserProfilePermissionCollection());
			t.setSystemUserProfilePermissionCollection(list);
			String json = jsonRename(super.getSerializer().serialize(t));

			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(InformationType.SYSTEMUSERPROFILE);
			log.setOperationType(OperationType.EDICAO);
			try {
				log.setSystemUserUsername(sb.getById(username));
			} catch (ModelException e) {
				e.printStackTrace();
			}
		//	log.setContent(json);
			lb.save(log);
			return true;
		}
		return false;
	}

	@PUT
	@Path("/ed/{id}")
	public Response mergeED(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ModelException {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		Response response = new Response();
		SystemUserProfile t = mb.getById(id);
		List<SystemUser> us = sb.getByProfile(id);
		if (!us.isEmpty()) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.DISABLE_PROFILE_ERROR));
			response.setSuccess(false);

			return response;
		} else {
			t.changeEnabled();
			if (mergeEDLog(t, t.getEnabled(), username)) {
				response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
				response.setSuccess(true);
			} else {
				response.setMessage(Messages.getInstance().getMessage(IMessages.ACTIVATION_ERROR));
				response.setSuccess(false);
			}
			return response;
		}
	}

	@GET
	@Path("/combos")
	public Response getCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SystemUserProfile> tList = new ArrayList<SystemUserProfile>();
			tList = mb.findAll();
			String messageModelJson = new JSONSerializer()
					.exclude("*.class", "systemUserProfilePermissionCollection", "enabled").serialize(tList);
			response.setJsonList(messageModelJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		return response;
	}

	@GET
	@Path("/combosativos")
	public Response getCombosAtivos() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SystemUserProfile> tList = new ArrayList<SystemUserProfile>();
			tList = mb.findActives();
			String messageModelJson = new JSONSerializer()
					.exclude("*.class", "systemUserProfilePermissionCollection", "enabled").serialize(tList);
			response.setJsonList(messageModelJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		return response;
	}

	@GET
	@Path("/searchAll/{name}/{supPerm}/{enabled}")
	public Response searchAll(@PathParam("name") String name, @PathParam("supPerm") String supPerm,
			@PathParam("enabled") String enabled)
					throws HibernateException, SQLException, ModelException, ParseException {
		Response response = new Response();
		List<SystemUserProfile> tList = mb.searchAll(name, supPerm, enabled);
		String jsonList = new JSONSerializer().exclude("*.class").serialize(tList);
		response.setJsonList(jsonList);
		if (tList.isEmpty()) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		return response;
	}

	@Override
	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		Response response = new Response();
		SystemUserProfile t = mb.getById(id);

		if (t == null) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else {
			String jsonList = new JSONSerializer()
					.include("systemUserProfileId", "name", "enabled", "systemUserProfilePermissionCollection")
					.exclude("*.class").serialize(t);
			response.setJsonList(jsonList);
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		}
		return response;
	}

	@GET
	@Path("/search/{page}/{name}/{enabled}")
	public Response searchPag(@PathParam("page") int page, @PathParam("name") String name,
			@PathParam("enabled") String enabled)
					throws ParseException, HibernateException, SQLException, ModelException {
		Response response = new Response();
		try {
			Map<Integer, List<SystemUserProfile>> tMap = mb.searchPag(page, name, enabled);
			Integer count = 0;
			List<SystemUserProfile> tList = new ArrayList<SystemUserProfile>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			if (tList.isEmpty())
				response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			else {
				String strJson = new JSONSerializer().transform(new EnabledTransformer(), "enabled").exclude("*.class")
						.serialize(tList);
				response.setJsonList(strJson);
				response.setCount(count);
				response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
			}
		} catch (ModelException me) {
			response.setMessage(me.getMessage());
		}
		return response;
	}

	@GET
	@Path("/combo")
	public Response getForCombo() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SystemUserProfile> tList = mb.findAll();
			String citizenJson = new JSONSerializer()
					.exclude("*.class", "enabled", "systemUserProfilePermissionCollection", "systemUserCollection")
					.serialize(tList);
			response.setJsonList(citizenJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@GET
	@Path("/combosup")
	public Response getForComboSUP() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SystemUserProfilePermission> tList = supb.findAll();
			String citizenJson = new JSONSerializer().exclude("*.class", "systemUserProfileCollection")
					.serialize(tList);
			response.setJsonList(citizenJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@Override
	@GET
	public Response getAll() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<SystemUserProfile> tList = mb.findAll();
			Hibernate.initialize(tList);

			String entityJson = new JSONSerializer().include("systemUserProfilePermissionCollection", "name")
					.exclude("*.class", "*").deepSerialize(tList);
			response.setJsonList(entityJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}

		return response;
	}

	@POST
	@Path("/")
	public Response saveE(SystemUserProfile sup)
			throws HibernateException, SQLException, ModelException {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		Response response = new Response();
		SystemUserProfile t = new SystemUserProfile();
		try {
			if (mb.findByName(sup.getName()) != null) {
				response.setSuccess(false);
				response.setMessage(Messages.getInstance().getMessage(IMessages.DUPLICATED_PROFILE));
				return response;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		t.setName(sup.getName());
		t.setEnabled(sup.getEnabled());
		Collection<SystemUserProfilePermission> ecc = new ArrayList<SystemUserProfilePermission>();
		t.setSystemUserProfilePermissionCollection(ecc);
		for (SystemUserProfilePermission ii : sup.getSystemUserProfilePermissionCollection()) {
			t.addSystemUserProfilePermission(supb.getById(ii.getSystemUserProfilePermissionId()));
		}
		if (saveLog(t, username)) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
			response.setSuccess(true);
		} else {
			response.setMessage(Messages.getInstance().getMessage(IMessages.ACTIVATION_ERROR));
			response.setSuccess(false);
		}
		return response;
	}

	@PUT
	@Path("/")
	public Response mergeE(SystemUserProfile sup)
			throws HibernateException, SQLException, ModelException {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		Response response = new Response();
		SystemUserProfile t = (SystemUserProfile) mb.getById(sup.getSystemUserProfileId());
		t.setName(sup.getName());
		Collection<SystemUserProfilePermission> ecc = new ArrayList<SystemUserProfilePermission>(
				t.getSystemUserProfilePermissionCollection());
		Collection<SystemUserProfilePermission> ecc2 = sup.getSystemUserProfilePermissionCollection();
		Collection<SystemUserProfilePermission> remove = new ArrayList<SystemUserProfilePermission>();
		for (SystemUserProfilePermission ii : ecc2) {
			if (!ecc.contains(ii))
				t.addSystemUserProfilePermission(supb.getById(ii.getSystemUserProfilePermissionId()));
		}
		for (SystemUserProfilePermission ii : ecc) {
			if (!ecc2.contains(ii))
				t.removeSystemUserProfilePermission(supb.getById(ii.getSystemUserProfilePermissionId()));
		}
		if (mergeLog(t, username)) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
			response.setSuccess(true);
		} else {
			response.setMessage(Messages.getInstance().getMessage(IMessages.ACTIVATION_ERROR));
			response.setSuccess(false);
		}
		return response;
	}

	@GET
	@Path("/ec/{id}")
	public Response getById(@PathParam("id") int entityId)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Response response = new Response();
		SystemUserProfile t = mb.getById(entityId);
		Collection<SystemUserProfilePermission> eclist = new ArrayList<SystemUserProfilePermission>(
				t.getSystemUserProfilePermissionCollection());
		t.setSystemUserProfilePermissionCollection(eclist);
		String entityJson = new JSONSerializer().include("systemUserProfilePermissionCollection").exclude("*.class")
				.serialize(t);
		response.setJsonList(entityJson);
		if (t == null) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		return response;
	}
}
