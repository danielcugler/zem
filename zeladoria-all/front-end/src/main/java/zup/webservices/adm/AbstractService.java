package zup.webservices.adm;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;

import flexjson.JSONSerializer;
import zup.action.Response;
import zup.bean.AttendanceTime;
import zup.bean.BroadcastMessage;
import zup.bean.CallClassification;
import zup.bean.Citizen;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.Log;
import zup.bean.MessageModel;
import zup.bean.SolvedCall;
import zup.bean.SystemUser;
import zup.bean.SystemUserProfile;
import zup.bean.UnsolvedCall;
import zup.bean.WebUser;
import zup.business.AbstractBusiness;
import zup.business.LogBusiness;
import zup.business.SystemUserBusiness;
import zup.enums.Enabled;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.validate.AbstractValidate;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public abstract class AbstractService<T, PK extends Serializable> {
	private static Logger logger = Logger.getLogger(AbstractService.class);
	LogBusiness lb = new LogBusiness();
	SystemUserBusiness sb = new SystemUserBusiness();
	private AbstractBusiness<T, PK> business;
	private AbstractValidate<T> validate;
	private String campoOrdenacao;
	private Class<T> clazz;
	private JSONSerializer serializer;
	private static final Map<String, String> propertyMap = new HashMap<String, String>() {
		{
			// Modelo de Mensagem
			put("name", "Nome");
			put("messageBody", "Corpo da mensagem");
			put("subject", "Asunto");
			put("email", "E-mail");
			put("messageModelId", "ID Modelo de Mensagem");

			// Usuário
			put("worksAtEntity", "Entidade");
			put("attendanceTime", "Tempo de Atendimento");
			put("lowPriorityTime", "Prioridade Baixa");
			put("attendanceTime.lowPriorityTime", "Prioridade Baixa");
			put("highPriorityTime", "Prioridade Alta");
			put("attendanceTime.highPriorityTime", "Prioridade Alta");
			put("mediumPriorityTime", "Prioridade Média");
			put("attendanceTime.mediumPriorityTime", "Prioridade Média");
			put("entityId", "ID Entidade");
			put("attendanceTime.entityId", "ID Entidade");
			put("password", "Senha");
			put("personalPhone", "Telefone Pessoal");
			put("photoPath", "Foto");
			put("commercialPhone", "Telefone Comercial");
			put("sector", "Setor");
			put("systemUserProfileId", "ID Usuário");
			put("email", "E-mail");
			put("systemUserUsername", "Usuário");
			put("jobPosition", "Cargo");

			// Categoria de Entidade
			put("systemUserUsername", "Usuário");
			put("entityCategoryId", "ID Categoria de Entidade");
			put("enabled", "Habilitado");
			put("attendanceTime.enabled", "Habilitado");
			put("entity", "Habilitado");
			put("attendanceTime.entity", "Habilitado");
			put("attendanceTime.name", "Nome");

			// Comunicado em Massa
			put("createdBy", "Criado por");
			put("registrationDate", "Data de registro");
			put("creationDate", "Data de criação");
			put("publicationDate", "Data de publicação");
			put("registrationDate", "Data de registro");
			put("broadcastMessageCategoryId", "Categoria do comunicadodo em massa");
			put("broadcastMessageId", "ID Comunicado em massa");

			// Cidadão
			put("city_name", "Cidade");
			put("home_address_geograpical_coordinates", "Endereço residencial");
			put("citizen_cpf", "CPF");
			put("phone_number", "Telefone");
			put("neighborhood", "Bairro");
			put("neighborhood_id", "ID Bairro");
			put("neighborhood_name", "Nome do bairro");
			put("street_name", "Rua");
			put("address_number", "Numero:");

		}
	};

	public AbstractService(Class<T> persistenceClass) {
		clazz = persistenceClass;
	}

	public <T> InformationType getByClass(T t) {
		if (t instanceof AttendanceTime)
			return InformationType.ATTENDANCETIME;
		if (t instanceof BroadcastMessage)
			return InformationType.BROADCASTMESSAGE;
		if (t instanceof CallClassification)
			return InformationType.CALLCLASSIFICATION;
		if (t instanceof Citizen)
			return InformationType.CITZEN;
		if (t instanceof Entity)
			return InformationType.ENTITY;
		if (t instanceof EntityCategory)
			return InformationType.ENTITYCATEGORY;
		if (t instanceof MessageModel)
			return InformationType.MESSAGEMODEL;
		if (t instanceof SolvedCall)
			return InformationType.SOLVEDCALL;
		if (t instanceof SystemUser)
			return InformationType.SYSTEMUSER;
		if (t instanceof SystemUserProfile)
			return InformationType.SYSTEMUSERPROFILE;
		if (t instanceof UnsolvedCall)
			return InformationType.UNSOLVEDCALL;
		if (t instanceof WebUser)
			return InformationType.WEBUSER;
		return null;
	}

	public String jsonRename(String json) {

		return null;

	}

	public boolean save(T t) throws HibernateException, DataAccessLayerException, SQLException {
		if (business.save(t)) {
			String json = jsonRename(this.serializer.serialize(t));

			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(getByClass(t));
			log.setOperationType(OperationType.INCLUSAO);
			try {
				log.setSystemUserUsername(sb.getById("login"));
			} catch (ModelException e) {
				e.printStackTrace();
			}
		//	log.setContent(json);
			lb.save(log);
			return true;
		}
		return false;
	}

	public boolean saveLog(T t, String username) throws HibernateException, DataAccessLayerException, SQLException {
		if (business.save(t)) {

			String json = jsonRename(this.serializer.serialize(t));
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

	public JSONSerializer getSerializer() {
		return serializer;
	}

	public void setSerializer(JSONSerializer serializer) {
		this.serializer = serializer;
	}

	public boolean mergeLog(T t, String username) throws HibernateException, DataAccessLayerException, SQLException {
		if (business.merge(t)) {
			collectionFix(t);
			String json = jsonRename(this.serializer.serialize(t));
			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(getByClass(t));
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

	public boolean changePassLog(T t, String username)
			throws HibernateException, DataAccessLayerException, SQLException {
		if (business.merge(t)) {
			collectionFix(t);
			String json = jsonRename(this.serializer.serialize(t));
			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(getByClass(t));
			log.setOperationType(OperationType.ALTERARSENHA);
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

	public boolean publishLog(T t, String username) throws HibernateException, DataAccessLayerException, SQLException {
		if (business.merge(t)) {
			String json = jsonRename(this.serializer.serialize(t));
			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(getByClass(t));
			log.setOperationType(OperationType.PUBLICACAO);

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

	public boolean updateLog(T t, String username) throws HibernateException, DataAccessLayerException, SQLException {
		if (business.update(t)) {
			String json = jsonRename(this.serializer.serialize(t));
			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(getByClass(t));
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

	public T collectionFix(T t) {
		return t;
	}

	public boolean mergeEDLog(T t, Enabled en, String username)
			throws HibernateException, DataAccessLayerException, SQLException {
		if (business.update(t)) {
			collectionFix(t);
			String json = jsonRename(this.serializer.serialize(t));

			Log log = new Log();
			log.setChangeDate(new Date());
			log.setInformationType(getByClass(t));
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

	public AbstractBusiness<T, PK> getBusiness() {
		return business;
	}

	public void setBusiness(AbstractBusiness<T, PK> business) {
		this.business = business;
	}

	public AbstractValidate<T> getValidate() {
		return validate;
	}

	public void setValidate(AbstractValidate<T> validate) {
		this.validate = validate;
	}

	public String getCampoOrdenacao() {
		return campoOrdenacao;
	}

	public void setCampoOrdenacao(String campoOrdenacao) {
		this.campoOrdenacao = campoOrdenacao;
	}

	@GET
	public Response getAll() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<T> tList = business.findAll();
			String jsonList = new JSONSerializer().exclude("*.class").serialize(tList);
			response.setJsonList(jsonList);

		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@GET
	@Path("/pag/{page}")
	public Response getAll3(@PathParam("page") int page)
			throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<T> tList = business.findAll2(page, 5);
			String jsonList = new JSONSerializer().exclude("*.class").serialize(tList);
			response.setJsonList(jsonList);
			int count = business.countAll();
			response.setCount(count);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@GET
	@Path("/count")
	public Response countAll() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		int count = 0;
		count = business.countAll();
		String jsonList = new JSONSerializer().serialize(count);
		response.setJsonList(jsonList);
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") PK id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		Response response = new Response();
		T t = business.getById(id);
		if (t == null) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else {
			String jsonList = new JSONSerializer().exclude("*.class").serialize(t);
			response.setJsonList(jsonList);
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		}
		return response;
	}

	@POST
	public Response add(T t)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, IOException {
		Response response = new Response();
		String erros = validate.errosStr(t);
		if (!erros.isEmpty()) {
			response.setMessage(erros);

		} else {
			try {
				save(t);
				response.setMessage(Messages.getInstance().getMessage(IMessages.MESSAGE_SAVE_SUCCESS));
			} catch (ConstraintViolationException se) {
				if (se.getSQLState().equals("23505")) {
					logger.info(Messages.getInstance().getMessage(IMessages.DUPLICATED_FIELD_NAME));
					response.setMessage(Messages.getInstance().getMessage(IMessages.DUPLICATED_FIELD_NAME));
				}

			} catch (DataAccessLayerException me) {
				logger.info(me.getMessage());
				response.setMessage(me.getMessage());
			}

		}
		return response;
	}

	@PUT
	@Path("/merge")
	public Response merge(T t) throws HibernateException, SQLException, ValidationException, DataAccessLayerException {
		Response response = new Response();
		String erros = validate.errosStr(t);
		if (!erros.isEmpty()) {
			response.setMessage(erros);
		} else {
			try {
				merge(t);
				response.setMessage(Messages.getInstance().getMessage(IMessages.MESSAGE_SAVE_SUCCESS));
			} catch (ConstraintViolationException se) {
				if (se.getSQLState().equals("23505")) {
					response.setMessage(Messages.getInstance().getMessage(IMessages.DUPLICATED_FIELD_NAME));
					logger.info(Messages.getInstance().getMessage(IMessages.DUPLICATED_FIELD_NAME));
				}
				se.printStackTrace();
			} catch (DataAccessLayerException me) {
				logger.info(me.getMessage());
				response.setMessage(me.getMessage());
			}
		}
		return response;
	}

	@PUT
	public Response update(T t) throws HibernateException, SQLException, ValidationException, DataAccessLayerException {
		Response response = new Response();
		try {
			update(t);
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		} catch (DataAccessLayerException me) {
			logger.info(me.getMessage());
			response.setMessage(me.getMessage());
			return response;
		}
		return response;
	}

	@DELETE
	public Response delete(T t) throws HibernateException, SQLException, ModelException, ValidationException {
		Response response = new Response();
		try {

			business.delete(t);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		return response;
	}

	@GET
	@Path("/allsort")
	public Response getAllSort() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		List<T> tList = business.findAllSort(campoOrdenacao);
		String jsonList = new JSONSerializer().exclude("*.class").serialize(tList);
		response.setJsonList(jsonList);
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

}
