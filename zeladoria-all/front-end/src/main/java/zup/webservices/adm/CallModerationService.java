package zup.webservices.adm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.apache.tika.Tika;
import org.hibernate.HibernateException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.xmlpull.v1.XmlPullParserException;

import filters.CallMonitoringFilterBean;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import zup.bean.AdditionalInformation;
import zup.bean.Address;
import zup.bean.CallClassification;
import zup.bean.Citizen;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.Neighborhood;
import zup.bean.Result;
import zup.bean.SolvedCall;
import zup.bean.SystemUser;
import zup.bean.UnreadCall;
import zup.bean.UnsolvedCall;
import zup.bean.WebUser;
import zup.business.AdditionalInformationBusiness;
import zup.business.AddressBusiness;
import zup.business.CallClassificationBusiness;
import zup.business.CitizenBusiness;
import zup.business.EntityBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.NeighborhoodBusiness;
import zup.business.SolvedCallBusiness;
import zup.business.SystemUserBusiness;
import zup.business.UnreadCallBusiness;
import zup.business.UnsolvedCallBusiness2;
import zup.business.WebUserBusiness;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.CallStatus;
import zup.enums.Priority;
import zup.enums.Secret;
import zup.enums.SendMessage;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.facade.CallModerationFacade;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.utils.MinioServer;

@Path("/unsolvedcall3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
// @RequestScoped
public class CallModerationService {
	// @Inject
	private CallModerationFacade callModerationFacade = new CallModerationFacade();

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	// private static Logger loggersc = Logger.getLogger(UnsolvedCall.class);

	public void sendEmail(String nome, String email, String publicKey, Long unsolvedCallId) {
		MinioServer minioServer = new MinioServer();
		final String username = SystemConfiguration.getInstance()
				.getSystemConfiguration(ISystemConfiguration.USERNAME_EMAIL);
		final String password = SystemConfiguration.getInstance()
				.getSystemConfiguration(ISystemConfiguration.PASSWORD_EMAIL);
		String link = "http://www.souzem.com.br/showcall.jsp/?code=" + publicKey + "&call=" + unsolvedCallId;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.zoho.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			String url = "";
			try {
				url = minioServer.getMinioClient().presignedGetObject("zem", "zem.png");
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidBucketNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InsufficientDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InternalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidExpiresRangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidEndpointException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("ZEM - Acompamento de Chamado");

			MimeMultipart multipart = new MimeMultipart("related");

			// Primeira parte: texto
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "<div><h1><img style=\"vertical-align:middle\" width=\"100\" height=\"100\" src=\"" + url
					+ "\"></img>" + "<span>Zeladoria Municipal</span></h1></div><br>" + "Olá, " + nome + "!</br/><br/>"
					+ "<p>Seu chamado foi registrado com successo!<br/>" + "<p>O identificador do seu chamado é <br/>"
					+ unsolvedCallId
					+ "<p>Para acompanhar seu chamado registrado através do ZEM, acesse o link abaixo:<br/>" + link
					+ "<br/><br/>" + "Obrigado!<br/><br/>" + "Equipe ZEM.</p><br/><br/>";
			messageBodyPart.setContent(htmlText, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);

			message.setContent(multipart);

			Transport.send(message);

			System.out.println("Mail sent succesfully!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
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

	public static String sha1(String txt) {
		return getHash(txt, "SHA1");
	}

	/*
	 * @PUT
	 * 
	 * @Path("/mergeec") public Response mergeec(UnsolvedCall sc) throws
	 * HibernateException, SQLException, ModelException { Response response =
	 * new Response(); UnsolvedCall sc1 = scb.getById(sc.getUnsolvedCallId());
	 * CallClassification c = (CallClassification)
	 * ccb.getById(sc.getCallClassificationId().getCallClassificationId());
	 * sc1.setDescription(sc.getDescription());
	 * sc1.setObservation(sc.getObservation());
	 * sc1.setPriority(sc.getPriority()); sc1.setEntityEntityCategoryMaps(
	 * eecb.getByEEC(sc.getEntityEntityCategoryMaps().getEntityCategory().
	 * getEntityCategoryId(),
	 * sc.getEntityEntityCategoryMaps().getEntity().getEntityId()));
	 * sc1.setCallClassificationId(c); scb.merge(sc1);
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); return response; }
	 */

	@GET
	@Path("/media/{id}")
	public Response getMedias(@PathParam("id") Long id) {
		try {
			ArrayList<String> list = callModerationFacade.getMedias(id);
			if (list.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(list).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/nc/{cityId}")
	public javax.ws.rs.core.Response getNc(@PathParam("cityId") Integer cityId)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		EntityEntityCategoryMapsBusiness eecm=new EntityEntityCategoryMapsBusiness();
		NeighborhoodBusiness nb = new NeighborhoodBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		
		try {
			Map<String, List> map = new HashMap<String, List>();
			List<Entity> enList = eecm.getByEntity();
			List<CallClassification> ccList = ccb.findAll();
			List<Neighborhood> nbList = nb.findByCity(cityId);
			map.put("en", enList);
			map.put("cc", ccList);
			map.put("nb", nbList);
			return javax.ws.rs.core.Response.ok(map).build();
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
	@Path("/child/{publicKey}/{callId}")
	public Response getLastChild(@PathParam("publicKey") String publicKey, @PathParam("callId") Long callId) {
		CitizenBusiness cb = new CitizenBusiness();
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		SolvedCallBusiness scb2 = new SolvedCallBusiness();
		EntityBusiness eb = new EntityBusiness();
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		MinioServer minioServer = new MinioServer();
		SolvedCall callSolved = new SolvedCall();
		ArrayList<String> urls = new ArrayList<String>();
		try {
			UnsolvedCall call = scb.searchCitizenPk(callId, publicKey);
			if (call == null) {
				callSolved = scb2.searchCitizenPk(callId, publicKey);
				if (callSolved != null) {
					int entityCategoryId = callSolved.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK()
							.getEntityCategoryId();
					int entityId = callSolved.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK()
							.getEntityId();
					callSolved.addEntityEntityCategoryMaps(ecb.getById(entityCategoryId), eb.getById(entityId));

					if (!callSolved.isNoMidia()) {
						Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
								"/" + callSolved.getParentCallId().getSolvedCallId() + "/");
						for (io.minio.Result<Item> result : myObjects) {
							Item item = result.get();
							String url = minioServer.getMinioClient().presignedGetObject("call", item.objectName(),
									60 * 60 * 24);
							urls.add(url);
						}
						callSolved.setMediasPath(urls);
					}
				}
				return Response.ok(callSolved).build();
			} else {
				int entityCategoryId = call.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK()
						.getEntityCategoryId();
				int entityId = call.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId();

				call.addEntityEntityCategoryMaps(ecb.getById(entityCategoryId), eb.getById(entityId));
				if (!call.isNoMidia()) {
					Iterable<io.minio.Result<Item>> myObjects = minioServer.getMinioClient().listObjects("call",
							"/" + call.getParentCallId().getUnsolvedCallId() + "/");
					for (io.minio.Result<Item> result : myObjects) {
						Item item = result.get();
						String url = minioServer.getMinioClient().presignedGetObject("call", item.objectName(),
								60 * 60 * 24);
						urls.add(url);
					}
					call.setMediasPath(urls);
				}
				return Response.ok(call).build();
			}
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", me.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidEndpointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InsufficientDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ErrorResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		}
	}

	@POST
	@Path("/ann")
	public Response saveAN(UnsolvedCall unsolvedCall) {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb = new UnreadCallBusiness();
		EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		SystemUserBusiness sub = new SystemUserBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		NeighborhoodBusiness nb = new NeighborhoodBusiness();
		AddressBusiness adb = new AddressBusiness();
		MinioServer minioServer = new MinioServer();
		Tika tika = new Tika();
		try {
			if (unsolvedCall.getUpdatedOrModeratedBy() != null)
				unsolvedCall.setUpdatedOrModeratedBy(
						sub.findByUsername(unsolvedCall.getUpdatedOrModeratedBy().getSystemUserUsername()));
			unsolvedCall.setEntityEntityCategoryMaps(eecb.getByEEC(
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityCategoryId(),
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId()));
			unsolvedCall.setCallClassificationId(
					ccb.getById(unsolvedCall.getCallClassificationId().getCallClassificationId()));
			if (unsolvedCall.getAddressId() != null) {
				Address address = unsolvedCall.getAddressId();
				address.setNeighborhoodId(
						nb.getById(unsolvedCall.getAddressId().getNeighborhoodId().getNeighborhoodId()));
				adb.save(address);
			}
			AdditionalInformation desc = unsolvedCall.getDescription();
			unsolvedCall.setCreationOrUpdateDate(new Date());
			unsolvedCall.setCallStatus(CallStatus.ATIVO);
			unsolvedCall.setPriority(Priority.MEDIA);
			unsolvedCall.setCallProgress(CallProgress.NOVO);
			unsolvedCall.setSecret(Secret.DISABLED);
			unsolvedCall.setCallSource(CallSource.WEB);
			unsolvedCall.setRemove(false);
			aib.save(desc);
			if (unsolvedCall.getAddressId() != null) {
				Address address = unsolvedCall.getAddressId();
				address.setNeighborhoodId(
						nb.getById(unsolvedCall.getAddressId().getNeighborhoodId().getNeighborhoodId()));
				adb.save(address);
				unsolvedCall.setAddressId(address);
			}
			// adb.save(address);
			scb.save(unsolvedCall);
			unsolvedCall.setParentCallId(unsolvedCall);
			scb.merge(unsolvedCall);
			UnreadCall unreadCall = new UnreadCall();
			unreadCall.setUnreadCallId(unsolvedCall.getUnsolvedCallId());
			unreadCall.setUnsolvedCallId(unsolvedCall);
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			Collection<String> medias = unsolvedCall.getMediasPath();
			if (!unsolvedCall.isNoMidia()) {
				if (medias != null) {
					int count = 1;

					for (String s : medias) {
						byte[] encodedText = Base64.decodeBase64(s);
						try {
							MinioClient minioClient = minioServer.getMinioClient();
							InputStream fi = new ByteArrayInputStream(encodedText);
							String contentType = tika.detect(fi);
							if (contentType.equals("image/png"))
								minioClient.putObject("call",
										"/" + unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".png", fi,
										encodedText.length, contentType);
							if (contentType.equals("image/jpg"))
								minioClient.putObject("call",
										"/" + unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".jpg", fi,
										encodedText.length, contentType);
							if (contentType.equals("image/jpeg"))
								minioClient.putObject("call",
										"/" + unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".jpeg", fi,
										encodedText.length, contentType);
						} catch (MinioException e) {
							throw new ZEMException(
									ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new ZEMException(
									ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new ZEMException(
									ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new ZEMException(
									ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new ZEMException(
									ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
						}
					}

				}
			}
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_SAVE_SUCCESS))).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@POST
	@Path("/wu")
	public Response saveWU(UnsolvedCall unsolvedCall) {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb = new UnreadCallBusiness();
		EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		SystemUserBusiness sub = new SystemUserBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		NeighborhoodBusiness nb = new NeighborhoodBusiness();
		AddressBusiness adb = new AddressBusiness();
		CitizenBusiness ctb = new CitizenBusiness();
		MinioServer minioServer = new MinioServer();
		Tika tika = new Tika();
		String citizenName = unsolvedCall.getCitizenCpf().getName();
		try {
			long startTime = System.currentTimeMillis();
			if (unsolvedCall.getUpdatedOrModeratedBy() != null)
				unsolvedCall.setUpdatedOrModeratedBy(
						sub.findByUsername(unsolvedCall.getUpdatedOrModeratedBy().getSystemUserUsername()));
			Citizen citizen = ctb.getById(unsolvedCall.getCitizenCpf().getCitizen_cpf());

			if (!(citizen.getCitizen_cpf().equals(unsolvedCall.getCitizenCpf().getCitizen_cpf())
					&& citizen.getEmail().equals(unsolvedCall.getCitizenCpf().getEmail())))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));

			unsolvedCall.setEntityEntityCategoryMaps(eecb.getByEEC(
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityCategoryId(),
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId()));

			unsolvedCall.setCallClassificationId(
					ccb.getById(unsolvedCall.getCallClassificationId().getCallClassificationId()));
			if (unsolvedCall.getAddressId() != null) {
				Address address = unsolvedCall.getAddressId();
				address.setNeighborhoodId(
						nb.getById(unsolvedCall.getAddressId().getNeighborhoodId().getNeighborhoodId()));
				adb.save(address);
				unsolvedCall.setAddressId(address);
			}
			unsolvedCall.setCitizenCpf(citizen);
			AdditionalInformation desc = unsolvedCall.getDescription();
			unsolvedCall.setCreationOrUpdateDate(new Date());
			unsolvedCall.setCallStatus(CallStatus.ATIVO);
			unsolvedCall.setPriority(Priority.MEDIA);
			unsolvedCall.setCallProgress(CallProgress.NOVO);
			unsolvedCall.setSecret(Secret.DISABLED);
			unsolvedCall.setCallSource(CallSource.WEB);
			unsolvedCall.setRemove(false);
			aib.save(desc);
			// adb.save(address);
			scb.save(unsolvedCall);
			unsolvedCall.setParentCallId(unsolvedCall);
			scb.merge(unsolvedCall);
			UnreadCall unreadCall = urb.findByUnsolvedcallId(unsolvedCall.getUnsolvedCallId());
			if (unreadCall == null) {
				unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(unsolvedCall.getUnsolvedCallId());
				unreadCall.setUnsolvedCallId(unsolvedCall);
			}
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			Collection<String> medias = unsolvedCall.getMediasPath();
			long stopTime = System.currentTimeMillis();
			System.out.println((stopTime - startTime) / 1000);
			long startTime2 = System.currentTimeMillis();
			if (!unsolvedCall.isNoMidia()) {
				if (medias != null) {
					int count = 1;

					for (String s : medias) {
						byte[] encodedText = Base64.decodeBase64(s);
						try {
							MinioClient minioClient = minioServer.getMinioClient();
							InputStream fi = new ByteArrayInputStream(encodedText);
							String contentType = tika.detect(fi);
							if (contentType.equals("image/png"))
								minioClient.putObject("call",
										"/" + unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".png", fi,
										encodedText.length, contentType);
							if (contentType.equals("image/jpg"))
								minioClient.putObject("call",
										"/" + unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".jpg", fi,
										encodedText.length, contentType);
							if (contentType.equals("image/jpeg"))
								minioClient.putObject("call",
										"/" + unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".jpeg", fi,
										encodedText.length, contentType);
						} catch (MinioException e) {
							System.out.println("Error occurred: " + e);
						} catch (InvalidKeyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}
			if (citizenName != null) {
				citizen.setPublicKey(sha1(citizen.getCitizen_cpf()));
				System.out.println("SHA1: " + citizen.getPublicKey());
				ctb.merge(citizen);
				long stopTime2 = System.currentTimeMillis();
				System.out.println((stopTime2 - startTime2) / 1000);
				long startTime3 = System.currentTimeMillis();
				sendEmail(citizen.getName(), citizen.getEmail(), citizen.getPublicKey(),
						unsolvedCall.getUnsolvedCallId());
				long stopTime3 = System.currentTimeMillis();
				System.out.println((stopTime3 - startTime3) / 1000);
				return Response.status(Status.CREATED)
						.entity(makeMessage(Messages.getInstance().getMessage(IMessages.WU_CALL_SAVE_SUCCESS))).build();
			} else
				return Response.status(Status.CREATED)
						.entity(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_SAVE_SUCCESS))).build();

		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@PUT // Salvar (está funcionando corretamente)
	public Response save(UnsolvedCall unsolvedCall) {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb = new UnreadCallBusiness();
		EntityBusiness eb = new EntityBusiness();
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		SystemUserBusiness sub = new SystemUserBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		AddressBusiness adb = new AddressBusiness();
		NeighborhoodBusiness nb = new NeighborhoodBusiness();
		try {
			UnsolvedCall t = scb.getById(unsolvedCall.getUnsolvedCallId());
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			Long parentId = t.getParentCallId().getUnsolvedCallId();
			t.setUnsolvedCallId(null);
			t.setParentCallId(null);
			scb.save(t);
			UnsolvedCall parent = scb.getById(parentId);
			t.setParentCallId(parent);
			AdditionalInformation desc = unsolvedCall.getDescription();
			aib.save(desc);
			t.setDescription(desc);
			if (unsolvedCall.getObservation() != null) {
				AdditionalInformation obs = unsolvedCall.getObservation();
				aib.save(obs);
				t.setObservation(obs);
			}
			t.setEntityEntityCategoryMaps(eecb.getByEEC(
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityCategoryId(),
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId()));
			t.setCallClassificationId(ccb.getById(unsolvedCall.getCallClassificationId().getCallClassificationId()));
			t.setUpdatedOrModeratedBy(sub.getById(unsolvedCall.getUpdatedOrModeratedBy().getSystemUserUsername()));
			t.setPriority(unsolvedCall.getPriority());
			t.setDescription(unsolvedCall.getDescription());
			t.setCreationOrUpdateDate(new Date());
			if (unsolvedCall.getAddressId() != null) {
				Address address = unsolvedCall.getAddressId();
				address.setNeighborhoodId(
						nb.getById(unsolvedCall.getAddressId().getNeighborhoodId().getNeighborhoodId()));
				adb.save(address);
				t.setAddressId(address);
			}
			scb.merge(t);
			UnreadCall unreadCall = urb.findByUnsolvedcallId(parentId);
			if (unreadCall == null) {
				unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(parentId);
				unreadCall.setUnsolvedCallId(parent);
			}
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_EDIT_SUCCESS))).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));

		}
	}

	@GET
	@Path("/enum/{priority}")
	public Response enumTeste(@PathParam("priority") Priority priority) {
		return Response.ok(priority).build();
	}

	@PUT // Salvar e Enviar (está funcionando corretamente)
	@Path("/savesend")
	public Response savesend(UnsolvedCall unsolvedCall) {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		UnreadCallBusiness urb = new UnreadCallBusiness();
		EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
		CallClassificationBusiness ccb = new CallClassificationBusiness();
		SystemUserBusiness sub = new SystemUserBusiness();
		AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
		AddressBusiness adb = new AddressBusiness();
		NeighborhoodBusiness nb = new NeighborhoodBusiness();
		try {
			UnsolvedCall t = scb.getById(unsolvedCall.getUnsolvedCallId());
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			Long parentId = t.getParentCallId().getUnsolvedCallId();
			t.setUnsolvedCallId(null);
			t.setParentCallId(null);
			scb.save(t);
			UnsolvedCall parent = scb.getById(parentId);
			t.setParentCallId(parent);
			t.setEntityEntityCategoryMaps(eecb.getByEEC(
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityCategoryId(),
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId()));
			t.setCallClassificationId(ccb.getById(unsolvedCall.getCallClassificationId().getCallClassificationId()));
			t.setUpdatedOrModeratedBy(sub.getById(unsolvedCall.getUpdatedOrModeratedBy().getSystemUserUsername()));
			t.setPriority(unsolvedCall.getPriority());
			AdditionalInformation desc = unsolvedCall.getDescription();
			aib.save(desc);
			t.setDescription(desc);
			if (unsolvedCall.getObservation() != null) {
				AdditionalInformation obs = unsolvedCall.getObservation();
				aib.save(obs);
				t.setObservation(obs);
			}
			t.setCreationOrUpdateDate(new Date());
			t.setCallProgress(CallProgress.ENCAMINHADO);
			if (unsolvedCall.getAddressId() != null) {
				Address address = unsolvedCall.getAddressId();
				address.setNeighborhoodId(
						nb.getById(unsolvedCall.getAddressId().getNeighborhoodId().getNeighborhoodId()));
				adb.save(address);
				t.setAddressId(address);
			}
			scb.merge(t);
			UnreadCall unreadCall = urb.findByUnsolvedcallId(parentId);
			if (unreadCall == null) {
				unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(parentId);
				unreadCall.setUnsolvedCallId(parent);
			}
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_SEND_SUCCESS))).build();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		}
	}

	/*
	 * @GET
	 * 
	 * @Path("/child/{id}") public Response getLastChild(@PathParam("id") Long
	 * id) throws HibernateException, SQLException, ValidationException,
	 * DataAccessLayerException, ModelException { Response response = new
	 * Response(); UnsolvedCall t = scb.getLastChild(id); if (t == null) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * RESULT_NOT_FOUND)); } else { String jsonList = new
	 * JSONSerializer().exclude("*.class").serialize(t);
	 * response.setJsonList(jsonList);
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); } return response; }
	 * 
	 * @GET
	 * 
	 * @Path("/nc/{cityId}") public javax.ws.rs.core.Response
	 * getNc(@PathParam("cityId") Integer cityId) throws HibernateException,
	 * SQLException, ValidationException, DataAccessLayerException,
	 * ModelException { try { Map<String, List> map = new HashMap<String,
	 * List>(); List<EntityCategory> ecList = ecb.findAll(); List<Entity> enList
	 * = enb.findAll(); List<CallClassification> ccList = ccb.findAll();
	 * List<Neighborhood> nbList = nb.findByCity(cityId); map.put("ec", ecList);
	 * map.put("en", enList); map.put("cc", ccList); map.put("nb", nbList);
	 * return javax.ws.rs.core.Response.ok(map).build(); } catch
	 * (HibernateException e) { e.printStackTrace(); throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST,
	 * "HibernateException", e.getMessage())); } catch (DataAccessLayerException
	 * e) { e.printStackTrace(); throw new ZEMException(
	 * ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException",
	 * e.getMessage())); } catch (SQLException e) { e.printStackTrace(); throw
	 * new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST,
	 * "SQLException", e.getMessage())); } catch (ModelException e) {
	 * e.printStackTrace(); throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST,
	 * "ModelException", e.getMessage())); } }
	 * 
	 * @POST
	 * 
	 * @Path("/mobile") public Response addNewCall(UnsolvedCall unsolvedCall)
	 * throws HibernateException, SQLException, IOException,
	 * DataAccessLayerException { Response response = new Response(); try {
	 * unsolvedCall.setEntityEntityCategoryMaps(
	 * eecb.getByEEC(unsolvedCall.getEntityEntityCategoryMaps().
	 * getEntityCategory().getEntityCategoryId(),
	 * unsolvedCall.getEntityEntityCategoryMaps().getEntity().getEntityId()));
	 * 
	 * } catch (ModelException e) { response.setMessage(e.getMessage()); } try {
	 * unsolvedCall.setCallClassificationId(
	 * ccb.getById(unsolvedCall.getCallClassificationId().
	 * getCallClassificationId())); } catch (ModelException e) {
	 * response.setMessage(e.getMessage()); } try {
	 * unsolvedCall.setCitizenCpf(cb.getById(unsolvedCall.getCitizenCpf().
	 * getCitizen_cpf())); } catch (ModelException e) {
	 * response.setMessage(e.getMessage()); }
	 * unsolvedCall.setPriority(Priority.MEDIA);
	 * unsolvedCall.setCreationOrUpdateDate(new Date());
	 * unsolvedCall.setCallProgress(CallProgress.NOVO);
	 * unsolvedCall.setSecret(Secret.DISABLED);
	 * unsolvedCall.setCallSource(CallSource.WEB);
	 * unsolvedCall.setParentCallId(unsolvedCall);
	 * unsolvedCall.setWebUser(null); Address address =
	 * unsolvedCall.getAddressId(); ab.save(address);
	 * unsolvedCall.setAddressId(address);
	 * 
	 * Long id; try { if ((id = scb.saveUN(unsolvedCall)) != (long) -1) {
	 * Collection<String> medias = unsolvedCall.getMediasPath(); if
	 * (!unsolvedCall.isNoMidia()) { if (!medias.isEmpty()) { int count = 1; for
	 * (String s : medias) { byte[] encodedText =
	 * Base64.decodeBase64(s.replaceAll("data:image/.+;base64,", "")); try {
	 * File files = new
	 * File(SystemConfiguration.getInstance().getSystemConfiguration(
	 * ISystemConfiguration.CALL_DIRECTORY) + "/" +
	 * unsolvedCall.getUnsolvedCallId()); files.mkdirs(); OutputStream stream =
	 * new FileOutputStream(SystemConfiguration.getInstance()
	 * .getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY) + "/" +
	 * unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".png"); InputStream
	 * in = new ByteArrayInputStream(encodedText); BufferedImage bi =
	 * ImageIO.read(in); ImageIO.write(bi, "png", stream); in.close();
	 * stream.close(); } catch (Exception e) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SAVE_MEDIA_ERROR)); } } } } response.setJsonList("citizen");
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); response.setSuccess(true); } else {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SAVE_ERROR)); response.setSuccess(false); } } catch (ModelException e) {
	 * response.setMessage(e.getMessage()); } return response; }
	 * 
	 * @POST
	 * 
	 * @Path("/anonymity") public Response addAnonymityCall(UnsolvedCall
	 * unsolvedCall) throws HibernateException, SQLException, IOException,
	 * DataAccessLayerException { Response response = new Response(); try {
	 * unsolvedCall.setEntityEntityCategoryMaps(
	 * eecb.getByEEC(unsolvedCall.getEntityEntityCategoryMaps().
	 * getEntityCategory().getEntityCategoryId(),
	 * unsolvedCall.getEntityEntityCategoryMaps().getEntity().getEntityId()));
	 * 
	 * } catch (ModelException e) { response.setMessage(e.getMessage()); } try {
	 * unsolvedCall.setCallClassificationId(
	 * ccb.getById(unsolvedCall.getCallClassificationId().
	 * getCallClassificationId())); } catch (ModelException e) {
	 * response.setMessage(e.getMessage()); }
	 * unsolvedCall.setPriority(Priority.MEDIA);
	 * unsolvedCall.setCreationOrUpdateDate(new Date());
	 * unsolvedCall.setCallProgress(CallProgress.NOVO);
	 * unsolvedCall.setSecret(Secret.DISABLED);
	 * unsolvedCall.setCallSource(CallSource.WEB);
	 * unsolvedCall.setParentCallId(unsolvedCall); Address address =
	 * unsolvedCall.getAddressId(); ab.save(address);
	 * unsolvedCall.setAddressId(address); Long id; try { if ((id =
	 * scb.saveUN(unsolvedCall)) != (long) -1) { Collection<String> medias =
	 * unsolvedCall.getMediasPath(); if (!unsolvedCall.isNoMidia()) { if (medias
	 * != null) { int count = 1; for (String s : medias) { byte[] encodedText =
	 * Base64.decodeBase64(s.replaceAll("data:image/.+;base64,", "")); try {
	 * File files = new
	 * File(SystemConfiguration.getInstance().getSystemConfiguration(
	 * ISystemConfiguration.CALL_DIRECTORY) + "/" +
	 * unsolvedCall.getUnsolvedCallId()); files.mkdirs(); OutputStream stream =
	 * new FileOutputStream(SystemConfiguration.getInstance()
	 * .getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY) + "/" +
	 * unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".png"); InputStream
	 * in = new ByteArrayInputStream(encodedText); BufferedImage bi =
	 * ImageIO.read(in); ImageIO.write(bi, "png", stream); in.close();
	 * stream.close(); } catch (Exception e) {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SAVE_MEDIA_ERROR)); } } } } response.setJsonList("anonymity");
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SUCCESS_OPERATION)); response.setSuccess(true); } else {
	 * response.setMessage(Messages.getInstance().getMessage(IMessages.
	 * SAVE_ERROR)); response.setSuccess(false); } } catch (ModelException e) {
	 * response.setMessage(e.getMessage()); } return response; }
	 */
	@GET
	@Path("/search")
	public Response searchPag2(@BeanParam CallMonitoringFilterBean filter) {
		try {
			EntityBusiness eb = new EntityBusiness();
			Result<UnsolvedCall> result = callModerationFacade.searchPag2(filter);
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			List<UnsolvedCall> list = new ArrayList<UnsolvedCall>(result.getList());
			for (UnsolvedCall un : list) {
				EntityEntityCategoryMaps emaps = un.getEntityEntityCategoryMaps();
				emaps.setEntity(eb.getById(emaps.getEntityEntityCategoryMapsPK().getEntityId()));
				un.setEntityEntityCategoryMaps(emaps);
			}
			return Response.ok(result.getList()).header("X-Total-Count", result.getCount()).header("X-Per-Page",
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
					.build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidEndpointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InsufficientDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ErrorResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") Long id) {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		EntityBusiness eb = new EntityBusiness();
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		UnsolvedCall t;
		try {
			t = scb.getById(id);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			EntityEntityCategoryMaps emaps = t.getEntityEntityCategoryMaps();
			emaps.setEntity(eb.getById(emaps.getEntityEntityCategoryMapsPK().getEntityId()));
			emaps.setEntityCategory(ecb.getById(emaps.getEntityEntityCategoryMapsPK().getEntityCategoryId()));
			t.setEntityEntityCategoryMaps(emaps);
			return Response.ok(t).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/hist/{id}")
	public Response history(@PathParam("id") Long id) {
		UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
		List<UnsolvedCall> tList;
		try {
			tList = scb.history(id);
			// tList = callModerationFacade.history(id);
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(tList).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	public Response getAll() {
		List<UnsolvedCall> tList;
		try {
			tList = callModerationFacade.getAll();
			if (tList.isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(tList).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		} catch (InvalidEndpointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		} catch (InvalidPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		} catch (InsufficientDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		} catch (ErrorResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/combos")
	public Response getCombos() {
		try {
			EntityCategoryBusiness ecb = new EntityCategoryBusiness();
			CallClassificationBusiness ccb = new CallClassificationBusiness();
			// EntityEntityCategoryMapsBusiness eecb=new
			// EntityEntityCategoryMapsBusiness();
			List<CallClassification> ccList = ccb.findAll();
			List<EntityCategory> ecList = ecb.findActives();
			Map<String, List> map = new HashMap<String, List>();
			map.put("cc", ccList);
			map.put("ec", ecList);
			return Response.ok(map).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		}
	}

	@GET
	@Path("/combos2")
	public Response getCombos2() {
		try {
			EntityCategoryBusiness ecb = new EntityCategoryBusiness();
			EntityBusiness enb = new EntityBusiness();
			CallClassificationBusiness ccb = new CallClassificationBusiness();
			List<CallClassification> ccList = ccb.findAll();
			List<EntityCategory> ecList = ecb.findActives();
			List<Entity> enList = enb.findActives();
			Map<String, List> map = new HashMap<String, List>();
			map.put("cc", ccList);
			map.put("ec", ecList);
			map.put("en", enList);
			return Response.ok(map).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		}
	}

	@PUT
	@Path("/ed/{scid}")
	public Response mergeED(@PathParam("scid") Long scid) {
		try {
			// Map<String, String> map = callModerationFacade.mergeED(scid,
			// username);
			// return Response.ok(map).build();
			UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
			UnreadCallBusiness urb = new UnreadCallBusiness();
			AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
			SystemUserBusiness sub = new SystemUserBusiness();			
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = user.getUsername();
			UnsolvedCall t = (UnsolvedCall) scb.getById(scid);
			Long parentId = t.getParentCallId().getUnsolvedCallId();
			t.setUnsolvedCallId(null);
			t.setParentCallId(null);
			scb.save(t);
			UnsolvedCall parent = scb.getById(parentId);
			t.setParentCallId(parent);
			AdditionalInformation ai = new AdditionalInformation();
			t.changeCallStatus();
			t.setUpdatedOrModeratedBy(sub.getById(username));
			t.setCreationOrUpdateDate(new Date());
			String code;
			if (t.getCallStatus() == CallStatus.ATIVO) {
				ai.setInformation(Messages.getInstance().getMessage(IMessages.CALL_ACTIVATE_SUCCESS));
				code = IMessages.CALL_ACTIVATE_SUCCESS;
			} else {
				// t.setCallProgress(CallProgress.REJEITADO);
				ai.setInformation(Messages.getInstance().getMessage(IMessages.CALL_INACTIVATE_SUCCESS));
				code = IMessages.CALL_INACTIVATE_SUCCESS;
			}
			aib.save(ai);
			t.setObservation(ai);
			scb.mergeUN(t);
			UnreadCall unreadCall = urb.findByUnsolvedcallId(parentId);
			if (unreadCall == null) {
				unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(parentId);
				unreadCall.setUnsolvedCallId(parent);
			}
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			return Response.ok(makeMessage(Messages.getInstance().getMessage(code))).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@PUT
	@Path("/send/{scid}")
	public Response sendUC(@PathParam("scid") Long scid) {
		try {
			// Map<String, String> map = callModerationFacade.sendUC(scid,
			// username);
			UnreadCallBusiness urb = new UnreadCallBusiness();
			UnsolvedCallBusiness2 scb = new UnsolvedCallBusiness2();
			SystemUserBusiness sub = new SystemUserBusiness();
			AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = user.getUsername();			
			UnsolvedCall t = (UnsolvedCall) scb.getById(scid);
			Long parentId = t.getParentCallId().getUnsolvedCallId();
			t.setUnsolvedCallId(null);
			t.setParentCallId(null);
			scb.save(t);
			UnsolvedCall parent = scb.getById(parentId);
			t.setParentCallId(parent);
			t.setCallProgress(CallProgress.ENCAMINHADO);
			AdditionalInformation obs = new AdditionalInformation();
			obs.setInformation(Messages.getInstance().getMessage(IMessages.CALL_SEND_SUCCESS));
			aib.save(obs);
			t.setObservation(obs);
			t.setCreationOrUpdateDate(new Date());
			t.setUpdatedOrModeratedBy(sub.getById(username));
			scb.merge(t);
			UnreadCall unreadCall = urb.getById(parentId);
			if (unreadCall == null) {
				unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(parentId);
				unreadCall.setUnsolvedCallId(parent);
			}
			unreadCall.setRead(false);
			urb.saveOrUpdate(unreadCall);
			return Response.ok(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_SEND_SUCCESS))).build();
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));

		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	// Lista todos os bairros de determinada cidade.
	@GET
	@Path("/neighborhood/{cityId}")
	public javax.ws.rs.core.Response getNeighborhoods(@PathParam("cityId") Integer cityId)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		EntityEntityCategoryMapsBusiness eecm = new EntityEntityCategoryMapsBusiness();
		NeighborhoodBusiness nb = new NeighborhoodBusiness();
		try {
			Map<String, List> map = new HashMap<String, List>();
			List<Neighborhood> nbList = nb.findByCity(cityId);
			map.put("nb", nbList);
			return javax.ws.rs.core.Response.ok(map).build();
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
		}
	}

	@POST
	@Path("/rc/{uid}/{reply}")
	public Response replyCall(@PathParam("uid") Long uid, @PathParam("reply") String reply) {
		try {
			UnreadCallBusiness urb = new UnreadCallBusiness();
			UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();
			SolvedCallBusiness scb = new SolvedCallBusiness();
			WebUserBusiness wub = new WebUserBusiness();
			AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
			
			SystemUserBusiness sub = new SystemUserBusiness();
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String username = user.getUsername();		
	        
			AdditionalInformation ai = new AdditionalInformation();
			EntityBusiness eb = new EntityBusiness();
			EntityCategoryBusiness ecb = new EntityCategoryBusiness();
			UnsolvedCall t = (UnsolvedCall) ucb.getById(uid);
			Long parentId = t.getParentCallId().getUnsolvedCallId();
			int entityCategoryId = t.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK()
					.getEntityCategoryId();
			int entityId = t.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId();
			t.addEntityEntityCategoryMaps(ecb.getById(entityCategoryId), eb.getById(entityId));
			t.setCallProgress(CallProgress.FINALIZADO);
			// if
			// (t.getEntityEntityCategoryMaps().getEntityCategory().getSend_message().getValue()
			// == 0)
			// t.setCallProgress(CallProgress.FINALIZADO);
			// else
			// t.setCallProgress(CallProgress.ENCAMINHADO);
			t.setParentCallId(null);
			t.setUnsolvedCallId(null);
			t.setCreationOrUpdateDate(new Date());
			if (!reply.isEmpty()) {
				ai.setInformation(reply);
				aib.save(ai);
				t.setObservation(ai);
			}
			SystemUser su = sub.getById(username);
			t.setUpdatedOrModeratedBy(su);
			ucb.save(t);
			UnsolvedCall parent = (UnsolvedCall) ucb.getById(parentId);
			t.setParentCallId(parent);
			ucb.merge(t);
			// if
			// (t.getEntityEntityCategoryMaps().getEntityCategory().getSend_message().getValue()
			// == 0) {
			List<UnsolvedCall> uclist = ucb.history(t.getParentCallId().getUnsolvedCallId());
			List<SolvedCall> sc2list = new ArrayList<SolvedCall>();
			SolvedCall sc = uclist.get(0).toSolvedCall();
			scb.save(sc);
			sc2list.add(sc);

			for (int ii = 1; ii < uclist.size(); ii++) {
				UnsolvedCall sc0 = uclist.get(ii);
				SolvedCall sc2 = sc0.toSolvedCall();
				scb.save2(sc2);
				sc2list.add(sc2);
			}
			// Alteração do status do chamado (lido ou não lido) na tabela
			// Unread Call.
			UnreadCall unreadCall = urb.getById(parentId);
			if (unreadCall == null) {
				unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(parentId);
				unreadCall.setSolvedCallId(scb.getById(parentId));
			}
			unreadCall.setRead(false);
			unreadCall.setUnsolvedCallId(null);
			unreadCall.setSolvedCallId(scb.getById(parentId));

			urb.saveOrUpdate(unreadCall);
			ucb.delete(uclist.get(0));

			for (SolvedCall solved : sc2list) {
				solved.setParentCallId(sc);
				scb.merge(solved);
			}

			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_REPLY_SUCCESS))).build();
			// }
			// /*
			// * UnreadCall unreadCall = urb.getById(parentId); if(unreadCall
			// * ==null){ unreadCall = new UnreadCall();
			// * unreadCall.setUnreadCallId(parentId);
			// * unreadCall.setUnsolvedCallId(parent); }
			// * unreadCall.setRead(false); urb.saveOrUpdate(unreadCall);
			// */
			// return Response.status(Status.CREATED)
			// .entity(makeMessage(Messages.getInstance().getMessage(IMessages.CALL_REPLY_SUCCESS))).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}
}
