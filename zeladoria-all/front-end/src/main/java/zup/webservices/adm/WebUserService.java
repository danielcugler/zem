package zup.webservices.adm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import JsonTransformers.AnonymityTransformer;
import JsonTransformers.CallProgressTransformer;
import JsonTransformers.CallSourceTransformer;
import JsonTransformers.CallStatusTransformer;
import JsonTransformers.FieldNameTransformer;
import JsonTransformers.FieldValueTransformer;
import JsonTransformers.PriorityTransformer;
import JsonTransformers.SecretTransformer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import zup.action.Response;
import zup.bean.AdditionalInformation;
import zup.bean.Address;
import zup.bean.SolvedCall;
import zup.bean.UnsolvedCall;
import zup.bean.WebUser;
import zup.business.AdditionalInformationBusiness;
import zup.business.AddressBusiness;
import zup.business.CallClassificationBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.SolvedCallBusiness;
import zup.business.SystemUserBusiness;
import zup.business.UnsolvedCallBusiness;
import zup.business.WebUserBusiness;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;
import zup.enums.Secret;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.formatter.FormatterMd5;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.validate.WebUserValidate;

@Path("/webuser")
public class WebUserService extends AbstractService<WebUser, String> {
	private static Logger logger = Logger.getLogger(WebUser.class);
	AddressBusiness ab = new AddressBusiness();
	AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
	WebUserBusiness wb = new WebUserBusiness();
	WebUserValidate wv = new WebUserValidate();
	UnsolvedCallBusiness scb = new UnsolvedCallBusiness();
	SolvedCallBusiness solvedcb = new SolvedCallBusiness();
	SolvedCallBusiness sb2 = new SolvedCallBusiness();
	EntityCategoryBusiness ecb = new EntityCategoryBusiness();
	CallClassificationBusiness ccb = new CallClassificationBusiness();
	SystemUserBusiness sub = new SystemUserBusiness();
	EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();

	public WebUserService() {
		super(WebUser.class);
		super.setBusiness(wb);
		super.setValidate(wv);
		super.setCampoOrdenacao("name");
		JSONSerializer serializer = new JSONSerializer();
		serializer.include("publicIdentificationKey", "name", "enabled", "webUserCpf", "email", "solvedCallId",
				"unsolvedCallId");
		serializer.exclude("*");
		serializer.transform(new FieldValueTransformer("Habilitado"), "enabled");
		Map<String, String> propertyMapRefObj = new HashMap<String, String>();
		Map<String, String> propertyMap = new HashMap<String, String>();
		// Usuário
		propertyMap.put("name", "Nome");
		propertyMap.put("publicIdentificationKey", "Chave de identificação pública");
		propertyMap.put("webUserCpf", "CPF do usuário");
		propertyMap.put("email", "E-mail");
		for (Map.Entry<String, String> entry : propertyMap.entrySet())
			serializer.transform(new FieldNameTransformer(entry.getValue()), entry.getKey());
		super.setSerializer(serializer);
	}

	@GET
	@Path("/search/{page}/{name}/{email}/{webUserCpf}")
	public Response searchPag(@PathParam("page") int page, @PathParam("name") String name,
			@PathParam("email") String email, @PathParam("webUserCpf") String webUserCpf)
					throws ParseException, HibernateException, SQLException, ModelException {
		Response response = new Response();
		try {
			Map<Integer, List<WebUser>> tMap = wb.searchPag(page, name, email, webUserCpf);
			Integer count = 0;
			List<WebUser> tList = new ArrayList<WebUser>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			if (tList.isEmpty())
				response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			else {
				String strJson = new JSONSerializer().exclude("*.class").serialize(tList);
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
	@Path("/child/{id}")
	public Response getLastChild(@PathParam("id") String id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		Response response = new Response();
		WebUser wu = wb.getById(id);
		if (wu.getCallType()) {
			UnsolvedCall ucall = scb.getLastChild(wu.getSolvedCallId().getSolvedCallId());
			if (!ucall.isNoMidia()) {
				File folder = new File(
						SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY)
								+ ucall.getUnsolvedCallId());
				ArrayList<byte[]> media = new ArrayList<byte[]>();
				if (folder.isDirectory()) {
					File[] files = folder.listFiles();
					if (files.length != 0)
						for (File file : files) {
							if (file.exists()) {
								byte[] data = null;
								try {
									data = Files.readAllBytes(file.toPath());
								} catch (IOException e) {
									e.printStackTrace();
								}
								media.add(data);
							}
						}
				}

				if (media.isEmpty()) {
					response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
				} else {
					StringWriter out = new StringWriter();
					new JSONSerializer().serialize(media, out);
					response.setJsonList(out.toString());

					response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
				}
				ucall.setMedias(media);
			}
			UnsolvedCall parent = ucall.getParentCallId();
			ucall.setParentCallId(parent);

			String strJson = new JSONSerializer()
					.include("callClassificationId.name", "targetStreetName", "targetAddressNumber",
							"targetNeighborhoodId", "targetNeighborhoodId.neighborhood_name",
							"parentCallId.unsolvedCallId", "parentCallId", "parentCallId.description",
							"entityEntityCategoryMaps.entityCategory.name", "updatedOrModeratedBy.systemUserUsername",
							"medias", "nomidia", "priority", "unsolvedCallId", "callStatus", "callProgress",
							"callSource", "creationOrUpdateDate", "description", "delay", "observation")
					.exclude("*.class").transform(new CallStatusTransformer(), "callStatus")
					.transform(new CallProgressTransformer(), "callProgress")
					.transform(new AnonymityTransformer(), "anonymity").transform(new SecretTransformer(), "secret")
					.transform(new CallSourceTransformer(), "callSource")
					.transform(new PriorityTransformer(), "priority")
					.transform(new DateTransformer("dd/MM/yyyy hh:mm"), "creationOrUpdateDate").serialize(ucall);
			response.setJsonList(strJson);
			return response;
		} else {
			UnsolvedCall ucall = scb.getLastChild(wu.getUnsolvedCallId().getUnsolvedCallId());
			File folder = new File(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY)
							+ ucall.getUnsolvedCallId());
			ArrayList<byte[]> media = new ArrayList<byte[]>();
			if (folder.isDirectory()) {
				File[] files = folder.listFiles();
				if (files.length != 0)
					for (File file : files) {
						if (file.exists()) {
							byte[] data = null;
							try {
								data = Files.readAllBytes(file.toPath());
							} catch (IOException e) {
								e.printStackTrace();
							}
							media.add(data);
						}
					}
			}

			if (media.isEmpty()) {
				response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			} else {
				String jsonList = new JSONSerializer().serialize(media);
				response.setJsonList(jsonList);
				response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
			}
			ucall.setMedias(media);
			UnsolvedCall parent = ucall.getParentCallId();
			ucall.setParentCallId(parent);

			String strJson = new JSONSerializer()
					.include("callClassificationId.name", "targetStreetName", "targetAddressNumber",
							"targetNeighborhoodId", "targetNeighborhoodId.neighborhood_name",
							"parentCallId.unsolvedCallId", "parentCallId", "parentCallId.description",
							"entityEntityCategoryMaps.entityCategory.name", "updatedOrModeratedBy.systemUserUsername",
							"medias", "priority", "unsolvedCallId", "callStatus", "callProgress", "callSource",
							"creationOrUpdateDate", "description", "delay", "observation")
					.exclude("*.class").transform(new CallStatusTransformer(), "callStatus")
					.transform(new CallProgressTransformer(), "callProgress")
					.transform(new AnonymityTransformer(), "anonymity").transform(new SecretTransformer(), "secret")
					.transform(new CallSourceTransformer(), "callSource")
					.transform(new PriorityTransformer(), "priority")
					.transform(new DateTransformer("dd/MM/yyyy hh:mm"), "creationOrUpdateDate").serialize(ucall);
			response.setJsonList(strJson);
			return response;
		}
	}

	@GET
	@Path("/childsolved/{id}")
	public Response getLastChildS(@PathParam("id") String id)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		Response response = new Response();
		WebUser wu = wb.getById(id);
		SolvedCall ucall = solvedcb.getLastChild(wu.getSolvedCallId().getSolvedCallId());
		File folder = new File(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY)
						+ ucall.getSolvedCallId());
		ArrayList<byte[]> media = new ArrayList<byte[]>();
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			if (files.length != 0)
				for (File file : files) {
					if (file.exists()) {
						byte[] data = null;
						try {
							data = Files.readAllBytes(file.toPath());
						} catch (IOException e) {
							e.printStackTrace();
						}
						media.add(data);
					}
				}
		}

		if (media.isEmpty()) {
			response.setMessage(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else {
			String jsonList = new JSONSerializer().serialize(media);
			response.setJsonList(jsonList);
			response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		}
		ucall.setMedias(media);
		SolvedCall parent = ucall.getParentCallId();
		ucall.setParentCallId(parent);

		String strJson = new JSONSerializer()
				.include("callClassificationId.name", "targetStreetName", "targetAddressNumber", "targetNeighborhoodId",
						"targetNeighborhoodId.neighborhood_name", "parentCallId.solvedCallId", "parentCallId",
						"parentCallId.description", "entityEntityCategoryMaps.entityCategory.name",
						"updatedOrModeratedBy.systemUserUsername", "medias", "priority", "solvedCallId", "callStatus",
						"callProgress", "callSource", "creationOrUpdateDate", "description", "delay", "observation")
				.exclude("*.class").transform(new CallStatusTransformer(), "callStatus")
				.transform(new CallProgressTransformer(), "callProgress")
				.transform(new AnonymityTransformer(), "anonymity").transform(new SecretTransformer(), "secret")
				.transform(new CallSourceTransformer(), "callSource").transform(new PriorityTransformer(), "priority")
				.transform(new DateTransformer("dd/MM/yyyy hh:mm"), "creationOrUpdateDate").serialize(ucall);
		response.setJsonList(strJson);
		return response;
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
	@Path("/add")
	public Response addNewCall(UnsolvedCall unsolvedCall)
			throws HibernateException, SQLException, IOException, DataAccessLayerException {
		Response response = new Response();

		try {
			unsolvedCall.setEntityEntityCategoryMaps(
					eecb.getByEEC(unsolvedCall.getEntityEntityCategoryMaps().getEntityCategory().getEntityCategoryId(),
							unsolvedCall.getEntityEntityCategoryMaps().getEntity().getEntityId()));

		} catch (ModelException e) {
			response.setMessage(e.getMessage());
		}
		try {
			unsolvedCall.setCallClassificationId(
					ccb.getById(unsolvedCall.getCallClassificationId().getCallClassificationId()));
		} catch (ModelException e) {
			response.setMessage(e.getMessage());
		}

		unsolvedCall.setPriority(Priority.MEDIA);
		unsolvedCall.setCreationOrUpdateDate(new Date());
		unsolvedCall.setCallProgress(CallProgress.NOVO);
		unsolvedCall.setSecret(Secret.DISABLED);
		unsolvedCall.setCallSource(CallSource.WEB);
		unsolvedCall.setParentCallId(unsolvedCall);
		String cpf = unsolvedCall.getWebUser().getWebUserCpf();
		WebUser wu = new WebUser();

		wu.setName(unsolvedCall.getWebUser().getName());
		wu.setEmail(unsolvedCall.getWebUser().getEmail());
		wu.setWebUserCpf(cpf);
		wu.setUnsolvedCallId(unsolvedCall);

		unsolvedCall.setWebUser(null);
		Address address = unsolvedCall.getAddressId();
		ab.save(address);
		unsolvedCall.setAddressId(address);
		Long id;
		try {
			if ((id = scb.saveUN(unsolvedCall)) != (long) -1) {
				Collection<String> medias = unsolvedCall.getMediasPath();

				String publicKey = FormatterMd5.md5(cpf + id);
				wu.setPublicIdentificationKey(publicKey);
				if (wb.save(wu))
					sendEmail(wu.getName(), wu.getEmail(), wu.getPublicIdentificationKey());
				if (medias != null) {
					int count = 1;
					for (String s : medias) {
						byte[] encodedText = Base64.decodeBase64(s.replaceAll("data:image/.+;base64,", ""));
						try {

							File files = new File(SystemConfiguration.getInstance().getSystemConfiguration(
									ISystemConfiguration.CALL_DIRECTORY) + "/" + unsolvedCall.getUnsolvedCallId());
							files.mkdirs();

							OutputStream stream = new FileOutputStream(SystemConfiguration.getInstance()
									.getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY) + "/"
									+ unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".png");

							InputStream in = new ByteArrayInputStream(encodedText);
							BufferedImage bi = ImageIO.read(in);
							ImageIO.write(bi, "png", stream);
							in.close();
							stream.close();
						} catch (Exception e) {
							response.setMessage(Messages.getInstance().getMessage(IMessages.SAVE_MEDIA_ERROR));
						}
					}
				}
				response.setJsonList(wu.getPublicIdentificationKey());
				response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION) + "WebUser");
				response.setSuccess(true);
			} else {
				response.setMessage(Messages.getInstance().getMessage(IMessages.SAVE_ERROR));
				response.setSuccess(false);
			}
		} catch (ModelException e) {
			response.setMessage(e.getMessage());
		}
		return response;
	}

	@GET
	public Response getAll() throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			List<WebUser> tList = (List<WebUser>) (((WebUserBusiness) super.getBusiness()).findAll());
			String WebUserJson = new JSONSerializer().exclude("*.class").serialize(tList);
			response.setJsonList(WebUserJson);
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));

		return response;
	}

	public void sendEmail(String nome, String email, String url) {

		final String username = "zeladoriamunicipal@gmail.com";
		final String password = "L4b1t3c*";
		String link = "http://www.souzem.com.br/showcall.jsp/?code=" + url;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("ZEM - Acompamento de Chamado");
			
			MimeMultipart multipart = new MimeMultipart("related");
			
			//Primeira parte: texto
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "Olá, " + nome + "!</br/><br/>"
					+ "<p>Para acompanhar seu chamado registrado através do ZEM, acesse o link abaixo:<br/>"
					+ link + "<br/><br/>"
					+ "Obrigado!<br/><br/>"
					+ "Equipe ZEM.</p><br/><br/>";
			messageBodyPart.setContent(htmlText, "text/html");
			multipart.addBodyPart(messageBodyPart);		
			
			message.setContent(multipart);

			Transport.send(message);  

			System.out.println("Mail sent succesfully!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
