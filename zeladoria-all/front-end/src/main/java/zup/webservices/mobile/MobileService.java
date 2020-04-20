package zup.webservices.mobile;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.MapKeyEnumerated;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.nimbusds.jose.JOSEException;

import dto.EvalDTO;
import dto.ReadCallDTO;
import dto.ReadDTO;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import zup.bean.AdditionalInformation;
import zup.bean.Address;
import zup.bean.BroadcastMessage;
import zup.bean.CallClassification;
import zup.bean.Citizen;
import zup.bean.CitizenLogin;
import zup.bean.City;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.Firebase;
import zup.bean.Neighborhood;
import zup.bean.SolvedCall;
import zup.bean.State;
import zup.bean.SystemUser;
import zup.bean.UnreadCall;
import zup.bean.UnsolvedCall;
import zup.business.AdditionalInformationBusiness;
import zup.business.AddressBusiness;
import zup.business.BroadcastMessageBusiness;
import zup.business.CallClassificationBusiness;
import zup.business.CitizenBusiness;
import zup.business.CitizenLoginBusiness;
import zup.business.CityBusiness;
import zup.business.EntityBusiness;
import zup.business.EntityCategoryBusiness;
import zup.business.EntityEntityCategoryMapsBusiness;
import zup.business.FirebaseBusiness;
import zup.business.NeighborhoodBusiness;
import zup.business.SolvedCallBusiness;
import zup.business.StateBusiness;
import zup.business.SystemUserBusiness;
import zup.business.UnreadCallBusiness;
import zup.business.UnsolvedCallBusiness;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.CallStatus;
import zup.enums.Priority;
import zup.enums.Secret;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.formatter.FormatterMd5;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.utils.MinioServer;
import zup.utils.ResponseMessage;
import zup.validate.CitizenValidate;
import zup.validate.CityValidate;
import zup.webservices.adm.PasswordGenerator;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/mobile")
public class MobileService {
	private static Logger logger = Logger.getLogger(Citizen.class);
	SolvedCallBusiness scb = new SolvedCallBusiness();
	EntityEntityCategoryMapsBusiness eecb = new EntityEntityCategoryMapsBusiness();
	CallClassificationBusiness ccb = new CallClassificationBusiness();
	CitizenBusiness cb = new CitizenBusiness();
	NeighborhoodBusiness nb = new NeighborhoodBusiness();
	AddressBusiness ab = new AddressBusiness();
	CitizenValidate cv = new CitizenValidate();
	NeighborhoodBusiness bb = new NeighborhoodBusiness();
	CityBusiness cdb = new CityBusiness();
	CityValidate cdv = new CityValidate();
	StateBusiness eb = new StateBusiness();
	UnsolvedCallBusiness uscb = new UnsolvedCallBusiness();
	CitizenLoginBusiness clb = new CitizenLoginBusiness();
	EntityBusiness etb = new EntityBusiness();
	EntityCategoryBusiness etcb = new EntityCategoryBusiness();
	AdditionalInformationBusiness aib = new AdditionalInformationBusiness();
	MinioServer minioServer = new MinioServer();

	public Map<String, String> makeMessage(String code) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("code", code);
		message.put("message", Messages.getInstance().getMessage(code));
		return message;
	}

	public void sendNotify(String title, String body) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		// Chave do servidor firebase
		post.setHeader("Authorization",
				"key=AAAASxbAkYs:APA91bE5L8nRzN9y3zfRFtJcxyr5AHCz9WJfBUK69TNZtHolF7gTXrPudD3Bt8eeMAC6rKks4xhAVnwKZ46PkFEzPhabQpxs7VxooQOAxvu0sFVh1mHx4owhjgGxlkVCuLsuFNkJBvAw");
		FirebaseBusiness fb = new FirebaseBusiness();
		try {
			List<Firebase> citizens = fb.findAll();
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("body", body);
			JSONObject message = new JSONObject();
			message.put("priority", "high");
			message.put("sound", "default");
			message.put("notification", notification);
			for(Firebase fr: citizens){
			// token do usuario
			message.put("to", fr.getToken());
			post.setEntity(new StringEntity(message.toString(), "UTF-8"));
			HttpResponse response;
			response = client.execute(post);
		    message.remove("to");
			}
		} catch (ClientProtocolException e) {
			ZEMException.makeResponse(Status.BAD_REQUEST, "ClientProtocolException", e.getMessage());
		} catch (IOException e) {
			ZEMException.makeResponse(Status.BAD_REQUEST, "IOException", e.getMessage());
		} catch (HibernateException e) {
			ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage());
		} catch (DataAccessLayerException e) {
			ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage());
		} catch (SQLException e) {
			ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage());
		} catch (ModelException e) {
			ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage());
		}
	}

	@GET
	@Path("/notify")
	public Response sendNotification(@QueryParam("title") String title, @QueryParam("body") String body) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		// Chave do servidor firebase
		post.setHeader("Authorization",
				"key=AAAASxbAkYs:APA91bE5L8nRzN9y3zfRFtJcxyr5AHCz9WJfBUK69TNZtHolF7gTXrPudD3Bt8eeMAC6rKks4xhAVnwKZ46PkFEzPhabQpxs7VxooQOAxvu0sFVh1mHx4owhjgGxlkVCuLsuFNkJBvAw");
		FirebaseBusiness fb = new FirebaseBusiness();
		try {
			List<Firebase> citizens = fb.findAll();
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("body", body);
			JSONObject message = new JSONObject();
			message.put("priority", "high");
			message.put("sound", "default");
			message.put("notification", notification);
			List<String> tokens = new ArrayList<String>();
			for(Firebase fr: citizens){
			tokens.add(fr.getToken());
			}
				// token do usuario
			message.put("registration_ids", tokens);
			post.setEntity(new StringEntity(message.toString(), "UTF-8"));
			HttpResponse response;
			response = client.execute(post);
		    message.remove("to");
			
			return Response.status(Status.OK).entity(makeMessage(IMessages.FIREBASE_SAVE_SUCCESS)).build();
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

	@POST
	@Path("/deviceid")
	public Response findByCityNeighborhood(Firebase device) {
		FirebaseBusiness fb = new FirebaseBusiness();
		try {
			Firebase t = fb.findByFirebaseToken(device.getToken());
			if (t != null)
				return Response.status(Status.CREATED)
						.entity(Messages.getInstance().makeMessage(IMessages.FIREBASE_SAVE_SUCCESS)).build();
			fb.save(device);
			return Response.status(Status.CREATED)
					.entity(Messages.getInstance().makeMessage(IMessages.FIREBASE_SAVE_SUCCESS)).build();
		} catch (HibernateException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@DELETE
	@Path("/rmdevice")
	public Response removeFirebase(String token) {
		FirebaseBusiness fb = new FirebaseBusiness();
		try {
			Firebase t = fb.findByFirebaseToken(token);
			fb.delete(t);
			return Response.status(Status.OK)
					.entity(Messages.getInstance().makeMessage(IMessages.FIREBASE_REMOVE_SUCCESS)).build();
		} catch (HibernateException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}

	@GET
	@Path("/city/{cityId}/{neighborhoodId}")
	public Response findByCityNeighborhood(@PathParam("cityId") Integer cityId,
			@PathParam("neighborhoodId") String neighborhoodId) {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		try {
			Neighborhood t = mb.findByCityNeighborhood(cityId, neighborhoodId);
			return Response.ok(t).build();
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

	@GET
	@Path("/bairro/{name}")
	public Response getByName(@PathParam("name") String name)
			throws ValidationException, HibernateException, SQLException {
		try {
			List<Neighborhood> n = nb.findByName(name);// .add
			return Response.ok(n).build();
		} catch (Exception e) {
			logger.info(Messages.getInstance().getMessage(e.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/bm")
	public Response searchBMMobile() throws ValidationException, HibernateException, SQLException {
		BroadcastMessageBusiness bm = new BroadcastMessageBusiness();
		try {
			List<BroadcastMessage> list = bm.searchBMMobile();// .add
			return Response.ok(list).build();
		} catch (Exception e) {
			logger.info(Messages.getInstance().getMessage(e.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/bm/{page}")
	public Response searchBMMobile(@PathParam("page") int page)
			throws ValidationException, HibernateException, SQLException {
		BroadcastMessageBusiness bm = new BroadcastMessageBusiness();
		try {
			List<BroadcastMessage> list = bm.searchBMMobile(page);// .add
			return Response.ok(list).build();
		} catch (Exception e) {
			logger.info(Messages.getInstance().getMessage(e.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/bm/{page}/{category}")
	public Response searchBMMobile(@PathParam("page") int page, @PathParam("category") int category)
			throws ValidationException, HibernateException, SQLException {
		BroadcastMessageBusiness bm = new BroadcastMessageBusiness();
		try {
			List<BroadcastMessage> list = bm.searchBMMobile(page, category);// .add
			return Response.ok(list).build();
		} catch (Exception e) {
			logger.info(Messages.getInstance().getMessage(e.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/bm2")
	public Response searchBMMobile2(@QueryParam("excluded") List<Integer> excluded)
			throws ValidationException, HibernateException, SQLException {
		BroadcastMessageBusiness bm = new BroadcastMessageBusiness();
		try {
			List<BroadcastMessage> list = bm.searchBMMobile2(excluded);// .add
			return Response.ok(list).build();
		} catch (Exception e) {
			logger.info(Messages.getInstance().getMessage(e.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	/*
	 * @POST
	 * 
	 * @Path("/call/{token}") public Response addAnonymityCall(UnsolvedCall
	 * unsolvedCall, @PathParam("token") String token) throws
	 * HibernateException, SQLException, IOException, DataAccessLayerException,
	 * InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException {
	 * CitizenLogin citi; Citizen cit; if (!token.equals("z")) try { citi =
	 * clb.findByToken(token); cit = cb.getById(citi.getCitizenId());
	 * unsolvedCall.setCitizenCpf(cit); } catch (ModelException e1) {
	 * e1.printStackTrace(); }
	 * 
	 * if (unsolvedCall.getCitizenCpf() != null) try { if
	 * (unsolvedCall.getCitizenCpf().getFacebookId() != null) { cit =
	 * cb.getByFacebookId(unsolvedCall.getCitizenCpf().getFacebookId());
	 * unsolvedCall.setCitizenCpf(cit); }
	 * unsolvedCall.setCitizenCpf(cb.getById(unsolvedCall.getCitizenCpf().
	 * getCitizen_cpf())); } catch (Exception e) { throw new ZEMException(
	 * ZEMException.makeResponse(Status.BAD_REQUEST, "Exception citizen",
	 * e.getMessage())); } try { unsolvedCall.setEntityEntityCategoryMaps(
	 * eecb.getByEEC(unsolvedCall.getEntityEntityCategoryMaps().
	 * getEntityCategory().getEntityCategoryId(),
	 * unsolvedCall.getEntityEntityCategoryMaps().getEntity().getEntityId()));
	 * 
	 * } catch (Exception e) { throw new ZEMException(
	 * ZEMException.makeResponse(Status.BAD_REQUEST,
	 * "Exception entitycategoryMaps", e.getMessage())); } try {
	 * unsolvedCall.setCallClassificationId(
	 * ccb.getById(unsolvedCall.getCallClassificationId().
	 * getCallClassificationId())); } catch (ModelException e) { throw new
	 * ZEMException( ZEMException.makeResponse(Status.BAD_REQUEST,
	 * "ModelException callClassification", e.getMessage())); }
	 * 
	 * try { AdditionalInformation ai = unsolvedCall.getDescription();
	 * aib.save(ai); unsolvedCall.setDescription(ai); } catch (Exception e) {
	 * throw new ZEMException( ZEMException.makeResponse(Status.BAD_REQUEST,
	 * "ModelException callClassification", e.getMessage())); }
	 * unsolvedCall.setPriority(Priority.MEDIA);
	 * unsolvedCall.setCreationOrUpdateDate(new Date());
	 * unsolvedCall.setCallProgress(CallProgress.NOVO);
	 * unsolvedCall.setSecret(Secret.DISABLED);
	 * unsolvedCall.setCallSource(CallSource.MOVEL);
	 * unsolvedCall.setParentCallId(unsolvedCall); Address address =
	 * unsolvedCall.getAddressId(); Tika tika=new Tika(); ab.save(address);
	 * unsolvedCall.setAddressId(address); Long id; try { if ((id =
	 * uscb.saveUN(unsolvedCall)) != (long) -1) { Collection<String> medias =
	 * unsolvedCall.getMediasPath(); if (!unsolvedCall.isNoMidia()) { if (medias
	 * != null) { int count = 1;
	 * 
	 * for (String s : medias) { byte[] encodedText = Base64.decodeBase64(s);
	 * try { MinioClient minioClient = minioServer.getMinioClient(); InputStream
	 * fi = new ByteArrayInputStream(encodedText); String
	 * contentType=tika.detect(fi); if(contentType.equals("image/png"))
	 * minioClient.putObject("call", "/" + unsolvedCall.getUnsolvedCallId() +
	 * "/" + (count++) + ".png", fi, encodedText.length, contentType);
	 * if(contentType.equals("image/jpg")) minioClient.putObject("call", "/" +
	 * unsolvedCall.getUnsolvedCallId() + "/" + (count++) + ".jpg", fi,
	 * encodedText.length, contentType); if(contentType.equals("image/jpeg"))
	 * minioClient.putObject("call", "/" + unsolvedCall.getUnsolvedCallId() +
	 * "/" + (count++) + ".jpeg", fi, encodedText.length, contentType); } catch
	 * (MinioException e) { throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
	 * e.getMessage())); } catch (InvalidKeyException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
	 * e.getMessage())); } catch (NoSuchAlgorithmException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
	 * e.getMessage())); } catch (XmlPullParserException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
	 * e.getMessage())); } catch (IOException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
	 * e.getMessage())); } }
	 * 
	 * } } String message =
	 * Messages.getInstance().getMessage(IMessages.MESSAGE_UPDATE_SUCCESS);
	 * return Response.status(Status.CREATED).entity(unsolvedCall).build(); }
	 * else { throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
	 * Messages.getInstance().getMessage(IMessages.SAVE_ERROR))); } } catch
	 * (ModelException e) { throw new
	 * ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST,
	 * "ModelException", e.getMessage())); } // return response; }
	 * 
	 */

	@POST
	@Path("/call/{token}")
	public Response addAnonymityCall(UnsolvedCall unsolvedCall, @PathParam("token") String token) {
		CitizenLogin citi;
		Citizen cit;
		UnreadCallBusiness urb = new UnreadCallBusiness();
		EntityBusiness eb = new EntityBusiness();
		EntityCategoryBusiness ecb = new EntityCategoryBusiness();
		NeighborhoodBusiness nb = new NeighborhoodBusiness();
		try {
			if (!token.equals("z")) {
				citi = clb.findByToken(token);
				cit = cb.getById(citi.getCitizenId());
				unsolvedCall.setCitizenCpf(cit);
			}
			/*
			 * if(unsolvedCall.getCitizenCpf().getFacebookId()!=null) try {
			 * cit=cb.getByFacebookId(unsolvedCall.getCitizenCpf().
			 * getFacebookId()); unsolvedCall.setCitizenCpf(cit); } catch
			 * (ModelException e1) { // catch block e1.printStackTrace(); }
			 */
			if (unsolvedCall.getCitizenCpf() != null)
				if (unsolvedCall.getCitizenCpf().getFacebookId() != null) {
					cit = cb.getByFacebookId(unsolvedCall.getCitizenCpf().getFacebookId());
					unsolvedCall.setCitizenCpf(cit);
				}
			// unsolvedCall.setCitizenCpf(cb.getById(unsolvedCall.getCitizenCpf().getCitizen_cpf()));
			unsolvedCall.setEntityEntityCategoryMaps(eecb.getByEEC(
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityCategoryId(),
					unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId()));
			unsolvedCall.setCallClassificationId(
					ccb.getById(unsolvedCall.getCallClassificationId().getCallClassificationId()));
			AdditionalInformation ai = unsolvedCall.getDescription();
			aib.save(ai);
			unsolvedCall.setDescription(ai);
			unsolvedCall.setPriority(Priority.MEDIA);
			unsolvedCall.setCreationOrUpdateDate(new Date());
			unsolvedCall.setCallProgress(CallProgress.NOVO);
			unsolvedCall.setSecret(Secret.DISABLED);
			unsolvedCall.setCallSource(CallSource.MOVEL);
			unsolvedCall.setRemove(false);
			Tika tika = new Tika();

			Address address = unsolvedCall.getAddressId();
			if (address.getNeighborhoodId() != null) {
				address.setNeighborhoodId(
						nb.getById(unsolvedCall.getAddressId().getNeighborhoodId().getNeighborhoodId()));
				ab.save(address);
				unsolvedCall.setAddressId(address);
			} else {
				unsolvedCall.setAddressId(null);
			}

			Long id;
			if ((id = uscb.saveUN(unsolvedCall)) != (long) -1) {
				unsolvedCall.setParentCallId(unsolvedCall);
				uscb.merge(unsolvedCall);
				Collection<String> medias = unsolvedCall.getMediasPath();
				if (!unsolvedCall.isNoMidia()) {
					if (medias != null) {
						int count = 1;
						for (String s : medias) {
							byte[] encodedText = Base64.decodeBase64(s);

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
						}
					}
				}
				UnreadCall unreadCall = new UnreadCall();
				unreadCall.setUnreadCallId(unsolvedCall.getUnsolvedCallId());
				unreadCall.setUnsolvedCallId(unsolvedCall);
				unreadCall.setRead(false);
				urb.saveOrUpdate(unreadCall);
				EntityEntityCategoryMaps mp = unsolvedCall.getEntityEntityCategoryMaps();
				mp.setEntity(eb.getById(
						unsolvedCall.getEntityEntityCategoryMaps().getEntityEntityCategoryMapsPK().getEntityId()));
				mp.setEntityCategory(ecb.getById(unsolvedCall.getEntityEntityCategoryMaps()
						.getEntityEntityCategoryMapsPK().getEntityCategoryId()));
				unsolvedCall.setEntityEntityCategoryMaps(mp);
				String message = Messages.getInstance().getMessage(IMessages.MESSAGE_UPDATE_SUCCESS);
				return Response.status(Status.CREATED).entity(unsolvedCall).build();
			} else {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
						Messages.getInstance().getMessage(IMessages.SAVE_ERROR)));
			}
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (InsufficientDataException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (NoResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (ErrorResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (InternalException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (InvalidEndpointException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (InvalidPortException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@PUT
	@Path("/eval")
	public Response evaluation(EvalDTO evalDTO) {
		SolvedCallBusiness sb = new SolvedCallBusiness();
		try {
			Citizen cit;
			SolvedCall solvedCall = sb.getById(evalDTO.getSolvedCallId());
			if (evalDTO.getToken().length() == 32) {
				CitizenLogin citi = clb.findByToken(evalDTO.getToken());
				cit = cb.getById(citi.getCitizenId());
			} else
				cit = cb.getByFacebookId(evalDTO.getToken());
			if (!solvedCall.getCitizenCpf().getCitizen_cpf().equals(cit.getCitizen_cpf()))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.CALL_EVAL_ERROR,
						Messages.getInstance().getMessage(IMessages.CALL_EVAL_ERROR)));
			solvedCall.setQualification(evalDTO.getEvaluation());
			sb.merge(solvedCall);
			return Response.ok(makeMessage(IMessages.CALL_EVAL_SUCCESS)).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@PUT
	@Path("/remove")
	public Response remove(Long parentId) {
		UnsolvedCallBusiness sb = new UnsolvedCallBusiness();
		SolvedCallBusiness solvedBusiness = new SolvedCallBusiness();
		try {
			Citizen cit;
			UnsolvedCall unsolvedCall = sb.getByIdRC(parentId);
			if (unsolvedCall == null){
				SolvedCall solvedCall = solvedBusiness.getById(parentId);
				if(solvedCall == null)				
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
				else {
					solvedCall.setRemove(true);
					solvedBusiness.merge(solvedCall);
				}
			} else {
				unsolvedCall.setRemove(true);
				sb.merge(unsolvedCall);
			}			
			return Response.ok(makeMessage(IMessages.CALL_REMOVE_SUCCESS)).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@GET
	@Path("/callclassification")
	public Response getCallClassification()
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<CallClassification> tList = new ArrayList<CallClassification>();
			tList = ccb.findAll();
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

	public HashMap<String, String> responseMessage(String atributo, String value) {
		HashMap<String, String> response = new HashMap<String, String>();
		response.put(atributo, value);
		return response;
	}

	@GET
	@Path("/citizent/{token}")
	public Response getCitizenToken(@PathParam("token") String token)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			CitizenLogin citi = clb.findByToken(token);
			if (citi == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(responseMessage("cpf", citi.getCitizenId())).build();
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ME", me.getMessage()));
		}
	}

	@GET
	@Path("/citizentf/{ftoken}")
	public Response getCitizenFToken(@PathParam("ftoken") String ftoken)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			Citizen citi = cb.getByFacebookId(ftoken);
			if (citi == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(responseMessage("cpf", citi.getCitizen_cpf())).build();
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(me.getMessage()));
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ME", me.getMessage()));
		}
	}

	@PUT
	public Response update(Citizen t)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException {
		try {
			t.setPassword(FormatterMd5.md5(t.getPassword()));
			cb.update(t);
			String message = Messages.getInstance().getMessage(IMessages.MESSAGE_UPDATE_SUCCESS);
			return Response.status(Status.CREATED).entity(message).build();
		} catch (DataAccessLayerException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", me.getMessage()));
		}
	}

	@POST
	@Path("/photos")
	public Response savePhotos(Collection<String> medias) {
		if (!medias.isEmpty()) {
			int count = 1;
			for (String s : medias) {
				try {
					byte[] data = Base64.decodeBase64(s);
					File files = new File(SystemConfiguration.getInstance()
							.getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY) + "/" + 111);
					files.mkdirs();
					OutputStream stream = new FileOutputStream(SystemConfiguration.getInstance().getSystemConfiguration(
							ISystemConfiguration.CALL_DIRECTORY) + "/" + 111 + "/" + (count++) + ".jpg");
					stream.write(data);
					stream.close();
					return Response.status(Status.OK)
							.entity(new ResponseMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION),
									Status.OK.toString()))
							.build();
				} catch (Exception e) {
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception",
							Messages.getInstance().getMessage(IMessages.SAVE_MEDIA_ERROR)));
				}
			}
		}
		return null;
	}

	@PUT
	@Path("/changepass")
	public Response changePass(@QueryParam("oldPass") String oldPass, @QueryParam("newPass") String newPass,
			@QueryParam("token") String token)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, IOException {
		Citizen t = null;
		try {
			CitizenLogin cl = clb.findByToken(token);
			String oldPassMD5 = FormatterMd5.md5(oldPass);
			if (cl == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			t = cb.getById(cl.getCitizenId());
			if (!oldPassMD5.equals(t.getPassword()))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.PASSWORD_ERROR,
						Messages.getInstance().getMessage(IMessages.PASSWORD_ERROR)));
			t.setPassword(FormatterMd5.md5(newPass));
			cb.merge(t);

			SecureRandom random = new SecureRandom();
			byte[] sharedSecret = new byte[32];
			random.nextBytes(sharedSecret);
			String tokenGeneration = t.getEmail() + newPass + sharedSecret;
			String tokenMD5 = FormatterMd5.md5(tokenGeneration);
			cl.setToken(tokenMD5);
			clb.merge(cl);
			Map<String, String> message = new HashMap<String, String>();
			message.put("token", tokenMD5);
			return Response.status(Status.CREATED).entity(message).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@PUT
	@Path("/citizen")
	public Response updateCitizen(@QueryParam("cpf") String cpf, @QueryParam("facebookId") String facebookId)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, IOException {
		Citizen t = null;
		try {
			t = cb.getById(cpf);
		} catch (ModelException e) {
			e.printStackTrace();
		}
		t.setFacebookId(facebookId);
		try {
			cb.merge(t);
		} catch (ConstraintViolationException se) {
			if (se.getSQLState().equals("23505")) {
				logger.info(Messages.getInstance().getMessage(IMessages.DUPLICATED_FIELD_NAME));
				throw new ZEMException(
						ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", IMessages.DUPLICATED_FIELD_NAME));
			}
		} catch (DataAccessLayerException me) {
			logger.info(me.getMessage());
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", me.getMessage()));
		}
		return Response.status(Status.CREATED)
				.entity(new ResponseMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION),
						Status.CREATED.toString()))
				.build();
	}

	@POST
	@Path("/citizen")
	public Response add(Citizen t) throws HibernateException, SQLException, ValidationException,
			DataAccessLayerException, IOException, ModelException {
		if (t.getPassword() != null) {
			t.setPassword(FormatterMd5.md5(t.getPassword()));
		}

		String erros = cv.errosStr(t);
		String valida = cv.valida(t).toString();

		if (!erros.isEmpty())
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "ValidationException", erros + valida));

		try {
			if (cb.getById(t.getCitizen_cpf()) != null) {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_CPF,
						Messages.getInstance().getMessage(IMessages.DUPLICATED_CPF)));
			}

			if (t.getCitizen_cpf().length() == 11) {
				if (cb.getByEmail(t.getEmail()) != null) {
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_EMAIL,
							Messages.getInstance().getMessage(IMessages.DUPLICATED_EMAIL)));
				}
			}

			cb.save(t); // caso não exista cpf igual o cidadão será
						// cadastrado com sucesso.

		} catch (ConstraintViolationException se) {
			if (se.getSQLState().equals("23505")) {
				logger.info(Messages.getInstance().getMessage(IMessages.DUPLICATED_FIELD_NAME));
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_FIELD_NAME,
						Messages.getInstance().getMessage(IMessages.DUPLICATED_FIELD_NAME)));
			}
		} catch (DataAccessLayerException me) {
			logger.info(me.getMessage());
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", me.getMessage()));
		} catch (HibernateException he) {
			logger.info(he.getMessage());
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", he.getMessage()));
		}

		return Response.status(Status.CREATED)
				.entity(new ResponseMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION),
						Status.CREATED.toString()))
				.build();
	}

	@GET
	@Path("/login/{email}/{password}")
	public Response login(@PathParam("email") String email, @PathParam("password") String password)
			throws ParseException, HibernateException, SQLException, ModelException, JOSEException {
		Citizen c;
		c = cb.login(email);
		System.out.println(c.getCitizen_cpf());
		if (c == null)
			throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, "SQLException",
					Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
		String md5 = FormatterMd5.md5(password).toString();
		String pass = c.getPassword().toString();
		System.out.println(md5);
		System.out.println(pass);
		if (md5.equals(pass))
			System.out.println("ok");
		else
			System.out.println("nok");
		if (pass.equals(md5)) {
			System.out.println("Autenticado com sucesso.");
			// gerando o token que sera adicionado ao response
			CitizenLogin userLogin = clb.findByCpf(c.getCitizen_cpf());
			if (userLogin != null)
				return Response.ok(new ResponseMessage("token", userLogin.getToken())).build();
			userLogin = new CitizenLogin();
			String tokenMD5 = generationToken(email, password);
			userLogin.setCitizenId(c.getCitizen_cpf());
			userLogin.setToken(tokenMD5);
			clb.save(userLogin);

			// dar o response com o token e armazenar no servidor.
			return Response.ok(new ResponseMessage("token", tokenMD5)).build();
		} else
			throw new ZEMException(ZEMException.makeResponse(Status.UNAUTHORIZED, "SQLException",
					Messages.getInstance().getMessage(IMessages.PASSWORD_ERROR)));
	}

	@GET
	@Path("/entity")
	public Response getEntities() throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<Entity> tList = new ArrayList<Entity>();
			tList = etb.findActives();
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
	@Path("/entitycategory")
	public Response getEntityCategory() throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<EntityCategory> tList = etcb.findAll();
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
	@Path("/entitycategory/{entityId}")
	public Response getEntityCategoryByEntity(@PathParam("entityId") Integer entityId)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<EntityCategory> tList = etcb.findByEntityAndEnabled(entityId);
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

	// Verifica se o usuário já possui conta ZEM.
	@GET
	@Path("/citizen/{cpf}")
	public Response getCitizen(@PathParam("cpf") String cpf)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			Citizen t = cb.getById(cpf);
			if (t != null) {
				Map<String, String> message = new HashMap<String, String>();
				message.put("message", "Usuário já existente");
				message.put("code", "1");
				return Response.ok(message).build();
			} else {
				Map<String, String> message = new HashMap<String, String>();
				message.put("message", "Novo usuário");
				message.put("code", "0");
				return Response.ok(message).build();
			}
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}

	}

	// Verifica se o usuário já logou através do Facebook.
	@GET
	@Path("/citizenf/{facebookId}")
	public Response getCitizenByFacebook(@PathParam("facebookId") String facebookId)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			Citizen t = cb.getByFacebookId(facebookId);
			if (t != null) {
				Map<String, String> message = new HashMap<String, String>();
				message.put("message", "Cidadão já possui perfil ZEM com facebook");
				message.put("code", "1");
				return Response.ok(message).build();
			} else {
				Map<String, String> message = new HashMap<String, String>();
				message.put("message", "Novo usuário");
				message.put("code", "0");
				return Response.ok(message).build();
			}
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}

	}

	@GET
	@Path("/states")
	public Response findStates() throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<State> tList = eb.findAll();
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/city/{estado}")
	public Response findByName(@PathParam("estado") String estado)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<City> tList = cdb.getByEstado(estado);
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/neighborhood/{cidade}")
	public Response findByName(@PathParam("cidade") Integer cidade)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<Neighborhood> tList = bb.findByCity(cidade);
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/bairros")
	public Response findNeigh(@PathParam("cidade") Integer cidade)
			throws HibernateException, SQLException, ValidationException, ModelException {
		try {
			List<Neighborhood> tList = nb.findAll();
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/stream")
	public Response streamExample() {
		StreamingOutput stream = new StreamingOutput() {
			public void write(OutputStream out) throws IOException, WebApplicationException {
				Writer writer = new BufferedWriter(new OutputStreamWriter(out));
				for (int i = 0; i < 10000000; i++) {
					writer.write(i + " ");
				}
				writer.flush();
			}
		};
		return Response.ok(stream).build();
	}

	// Início da autenticação por Token.
	@GET
	@Path("/token/{token}")
	public Response verifyToken(@PathParam("token") String token)
			throws HibernateException, SQLException, ModelException, ValidationException {
		// verificar se o token e válido
		CitizenLogin t = clb.findByToken(token);
		if (t == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, "SQLException",
					Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
		} else {
			try {
				Map<String, String> message = new HashMap<String, String>();
				message.put("message", "Token encontrado");
				message.put("code", "1");
				return Response.ok(message).build();
			} catch (Exception e) {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
			}
		}

	}

	@GET
	@Path("/header")
	public Response addUser(@Context HttpHeaders headers) {
		return Response.status(200).entity(headers.getRequestHeaders()).build();
	}

	@GET
	@Path("/callstatus")
	public Response getCallStatus() {
		Map<String, CallStatus[]> entity = new HashMap<String, CallStatus[]>();
		entity.put("list", CallStatus.values());
		return Response.status(200).entity(entity).build();
	}

	@GET
	@Path("/callprogress")
	public Response getCallProgress() {
		Map<String, CallProgress[]> entity = new HashMap<String, CallProgress[]>();
		entity.put("list", CallProgress.values());
		return Response.status(200).entity(entity).build();
	}

	@DELETE
	@Path("/token/")
	public Response delete(@QueryParam("token") String token)
			throws HibernateException, SQLException, ModelException, ValidationException {
		try {
			CitizenLogin t = clb.findByToken(token);
			clb.delete(t);
			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "Registro apagado com sucesso!");
			return Response.ok(message).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	public String generationToken(String email, String password) throws JOSEException, ParseException {
		// cria um numero aleatorio de 256-bit
		SecureRandom random = new SecureRandom();
		byte[] sharedSecret = new byte[32];
		random.nextBytes(sharedSecret);
		String tokenGeneration = email + password + sharedSecret;
		String tokenMD5 = FormatterMd5.md5(tokenGeneration);
		return tokenMD5;
	}
	// Final da autenticação por token.

	// Recebe o CPF do usuário e busca o e-mail no banco para enviar
	// o link de confirmação de vinculo.
	@GET
	@Path("/email/{cpf}")
	public Response sendEmail(@PathParam("cpf") String cpf)
			throws ParseException, HibernateException, SQLException, ModelException {

		Citizen c = cb.getById(cpf);

		if (c == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, "SQLException",
					Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
		} else {
			sendEmail(c.getName(), c.getEmail(), c.getCitizen_cpf());

			String email = c.getEmail();
			System.out.println("E-mail: " + email);

			Map<String, String> message = new HashMap<String, String>();
			message.put("message", "E-mail enviado com sucesso!");
			message.put("nome", c.getName());
			message.put("email", c.getEmail());
			return Response.ok(message).build();
		}
	}

	// Efetua o merge dos dois cadastros quando o usuário clica no link
	// do e-mail e confirma o vínculo das contas Facebook e ZEM.
	@GET
	@Path("/vincularcontas/{cpf}/{email}")
	public Response vincularContas(@PathParam("cpf") String cpf, @PathParam("email") String email)
			throws ParseException, HibernateException, SQLException, ModelException {

		Citizen citizenFacebook = cb.getById(cpf + "f");
		Citizen citizenZem = cb.getById(cpf);

		if (citizenFacebook == null || citizenZem == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, "SQLException",
					Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
		} else {
			try {
				String facebookId = citizenFacebook.getFacebookId();
				System.out.println("Facebook ID: " + facebookId);
				List<UnsolvedCall> tList = uscb.findByCitizen(cpf);
				List<UnsolvedCall> tListf = uscb.findByCitizen(cpf + "f");

				// Alterei aqui (nathália)
				if (!tList.isEmpty() && !tListf.isEmpty()) {
					for (UnsolvedCall call2 : tListf)
						call2.setCitizenCpf(citizenZem);
					tList.addAll(tListf);
					for (UnsolvedCall call : tList)
						uscb.save(call);
				}

				citizenZem.setFacebookId(facebookId);
				cb.merge(citizenZem);
				cb.delete(citizenFacebook);
				Map<String, String> message = new HashMap<String, String>();
				message.put("message", "Contas vinculadas com sucesso!");
				return Response.ok(message).build();
			} catch (Exception e) {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
			}
		}
	}

	@GET
	@Path("/media")
	public Response getMedias(@QueryParam("callId") Long callId)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException,
			InvalidEndpointException, InvalidPortException, XmlPullParserException, InvalidKeyException,
			InvalidBucketNameException, NoSuchAlgorithmException, InsufficientDataException, NoResponseException,
			ErrorResponseException, InternalException, IOException, InvalidExpiresRangeException {
		/*
		 * CacheControl cacheControl = new CacheControl();
		 * cacheControl.setMaxAge((int) TimeUnit.MINUTES.toSeconds(1)); File
		 * folder = new File(
		 * SystemConfiguration.getInstance().getSystemConfiguration(
		 * ISystemConfiguration.CALL_DIRECTORY) + callId); Map<String,
		 * ArrayList<String>> photos = new HashMap<String, ArrayList<String>>();
		 * File[] files = folder.listFiles(); ArrayList<String> media = new
		 * ArrayList<String>(); if (files == null) { photos.put("photos",
		 * media); return
		 * Response.status(Status.OK).entity(photos).cacheControl(cacheControl).
		 * build(); } for (File file : files) { if (file.exists()) {
		 * 
		 * byte[] data = null; try { data = Files.readAllBytes(file.toPath()); }
		 * catch (IOException e) { e.printStackTrace(); } String base64 =
		 * Base64.encodeBase64String(data); media.add(base64); } } if
		 * (media.isEmpty()) { throw new
		 * ZEMException(ZEMException.makeResponse(Status.NOT_FOUND,
		 * IMessages.RESULT_NOT_FOUND,
		 * Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND))); }
		 * else { photos.put("photos", media); return
		 * Response.status(Status.OK).entity(photos).cacheControl(cacheControl).
		 * build(); }
		 */
		List<String> medias = new ArrayList<String>();
		// MinioClient minioClient = new
		// MinioClient("http://192.168.4.100:9000", "6BTP2IW1DFNTEAIBVPT2",
		// "VamB9WGfXikQYn+QTB6NO13lszJX29vOHP48IPmk");
		MinioClient minioClient = minioServer.getMinioClient();
		Iterable<Result<Item>> myObjects = minioClient.listObjects("call", "/" + callId + "/");
		for (Result<Item> result : myObjects) {
			Item item = result.get();
			String callImage = minioClient.presignedGetObject("call", item.objectName(), 60 * 60 * 24);
			medias.add(callImage);
		}
		return Response.ok(medias).build();
	}

	// Envia o e-mail de confirmação de vínculo ao usuário.
	public void sendEmail(String user, String email, String cpf) {

		// String link =
		// SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.IP_SERVER)
		// + "/rest/mobile/vincularcontas/" + cpf + "/" +
		// FormatterMd5.md5(email);

		String link = SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.IP_SERVER)
				+ "/accountlink.jsp?cpf=" + cpf + "&code=" + FormatterMd5.md5(email);

		final String username = SystemConfiguration.getInstance()
				.getSystemConfiguration(ISystemConfiguration.USERNAME_EMAIL);
		final String password = SystemConfiguration.getInstance()
				.getSystemConfiguration(ISystemConfiguration.PASSWORD_EMAIL);

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
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setSubject("ZEM - Recuperação de Senha");
			message.setText("Olá, " + user
					+ "! \n Para confirmar a vinculação de sua conta no Facebook com a sua Conta ZEM, clique no link abaixo:\n\n"
					+ link + "\n\nObrigado, \n Equipe ZEM");

			Transport.send(message);

			System.out.println("Mail sent succesfully!");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@GET
	@Path("/search/{cpf}")
	public Response searchPag2(@PathParam("cpf") String citizenCpf)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {

		try {
			Map<Integer, List<UnsolvedCall>> tMap = uscb.searchCitizen(citizenCpf);
			Integer count = 0;
			List<UnsolvedCall> tList = new ArrayList<UnsolvedCall>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/search5/{token}")
	public Response searchPag5(@PathParam("token") String token)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {
		CitizenLogin citizenLogin = clb.findByToken(token);
		try {
			Map<Integer, List<UnsolvedCall>> tMap = uscb.searchCitizen(citizenLogin.getCitizenId());
			Integer count = 0;
			List<UnsolvedCall> tList = new ArrayList<UnsolvedCall>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/evaluation")
	public Response evaluation(@QueryParam("eval") String eval)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {
		try {
			Map<String, String> message = new HashMap<String, String>();
			if (uscb.evaluation(eval)) {
				message.put("message", "Avaliação feita com sucesso!");
				return Response.ok(message).build();
			} else {
				message.put("message", "Erro ao fazer avaliação!");
				return Response.ok(message).build();
			}
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/search6")
	public Response searchPag6(@QueryParam("token") String token, @QueryParam("facebookId") String facebookId)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {
		CitizenLogin citizenLogin = clb.findByToken(token);
		Citizen cit = cb.getByFacebookId(facebookId);

		try {
			Map<Integer, List<UnsolvedCall>> tMap;
			if (citizenLogin != null) {
				tMap = uscb.searchCitizen(citizenLogin.getCitizenId());
			} else {
				tMap = uscb.searchCitizen(cit.getCitizen_cpf());
			}
			Integer count = 0;
			List<UnsolvedCall> tList = new ArrayList<UnsolvedCall>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/search2")
	public Response searchPag2(@QueryParam("periodod") String periodod, @QueryParam("periodoa") String periodoa,
			@QueryParam("entityId") String entityId, @QueryParam("entityCategoryId") String entityCategoryId,
			@QueryParam("callProgress") String callProgress, @QueryParam("description") String description,
			@QueryParam("citizenCpf") String citizenCpf)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {

		try {

			Map<Integer, List<UnsolvedCall>> tMap = uscb.searchCitizen2(periodod, periodoa, entityId, entityCategoryId,
					callProgress, description, citizenCpf);
			Integer count = 0;
			List<UnsolvedCall> tList = new ArrayList<UnsolvedCall>();
			for (Integer key : tMap.keySet()) {
				count = key;
				tList = tMap.get(key);
				break;
			}
			for (UnsolvedCall call : tList) {
				File file = null;
				if (!call.isNoMidia()) {
					for (int jj = 1; jj < 4; jj++) {
						file = new File(SystemConfiguration.getInstance()
								.getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY)
								+ call.getParentCallId().getUnsolvedCallId() + "/" + jj + ".png");
						if (file.exists()) {
							byte[] data = null;

							try {
								data = Files.readAllBytes(file.toPath());
							} catch (IOException e) {
								e.printStackTrace();
							}
							Collection<byte[]> cbyte = new ArrayList<byte[]>();
							cbyte.add(data);
							call.setMedias(cbyte);
						}

					}
					// else
					// throw new
					// ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST,
					// "Exception",
					// Messages.getInstance().getMessage(IMessages.MEDIA_ERROR)));

				}
			}
			return Response.ok(tList).build();
		} catch (Exception e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/search3") // Busca chamados na Unsolved Call
	public Response searchPag3(@QueryParam("periodod") String periodod, @QueryParam("periodoa") String periodoa,
			@QueryParam("entityId") String entityId, @QueryParam("entityCategoryId") String entityCategoryId,
			@QueryParam("callProgress") String callProgress, @QueryParam("description") String description,
			@QueryParam("citizenToken") String citizenToken, @QueryParam("facebookId") String facebookId)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {
		String citizenCPF = "";
		if (citizenToken != null)
			citizenCPF = clb.findByToken(citizenToken).getCitizenId();
		if (facebookId != null)
			citizenCPF = cb.getByFacebookId(facebookId).getCitizen_cpf();
		List<UnsolvedCall> tList = uscb.searchCitizenFinal(periodod, periodoa, entityId, entityCategoryId, callProgress,
				description, citizenCPF);
		// for (UnsolvedCall call : tList) {
		// File file = null;
		// if (!call.isNoMidia()) {
		// for (int jj = 1; jj < 4; jj++) {
		// file = new File(SystemConfiguration.getInstance()
		// .getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY)
		// + call.getParentCallId().getUnsolvedCallId() + "/" + jj + ".png");
		// if (file.exists()) {
		// byte[] data = null;
		//
		// try {
		// data = Files.readAllBytes(file.toPath());
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// Collection<byte[]> cbyte = new ArrayList<byte[]>();
		// cbyte.add(data);
		// call.setMedias(cbyte);
		// }
		// }
		// }
		// }
		return Response.ok(tList).build();
	}

	@GET
	@Path("/search4") // Busca chamados na Solved Call
	public Response searchPag4(@QueryParam("periodod") String periodod, @QueryParam("periodoa") String periodoa,
			@QueryParam("entityId") String entityId, @QueryParam("entityCategoryId") String entityCategoryId,
			@QueryParam("callProgress") String callProgress, @QueryParam("description") String description,
			@QueryParam("citizenToken") String citizenToken, @QueryParam("facebookId") String facebookId)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {
		String citizenCPF = "";
		if (citizenToken != null)
			citizenCPF = clb.findByToken(citizenToken).getCitizenId();
		if (facebookId != null)
			citizenCPF = cb.getByFacebookId(facebookId).getCitizen_cpf();
		List<SolvedCall> tListSolved = scb.searchCitizenFinal(periodod, periodoa, entityId, entityCategoryId,
				callProgress, description, citizenCPF);
		/*
		 * for (SolvedCall call : tListSolved) { File file = null; if
		 * (!call.isNoMidia()) { for (int jj = 1; jj < 4; jj++) { file = new
		 * File(SystemConfiguration.getInstance()
		 * .getSystemConfiguration(ISystemConfiguration.CALL_DIRECTORY) +
		 * call.getParentCallId().getSolvedCallId() + "/" + jj + ".png"); if
		 * (file.exists()) { byte[] data = null; try { data =
		 * Files.readAllBytes(file.toPath()); } catch (IOException e) {
		 * e.printStackTrace(); } Collection<byte[]> cbyte = new
		 * ArrayList<byte[]>(); cbyte.add(data); call.setMedias(cbyte); } } } }
		 */
		return Response.ok(tListSolved).build();
	}

	@GET
	@Path("/searchso") // Busca chamados na Solved Call
	public Response searchSolved(@QueryParam("periodod") String periodod, @QueryParam("periodoa") String periodoa,
			@QueryParam("entityId") String entityId, @QueryParam("entityCategoryId") String entityCategoryId,
			@QueryParam("callProgress") String callProgress, @QueryParam("description") String description,
			@QueryParam("citizenToken") String citizenToken, @QueryParam("facebookId") String facebookId,
			@QueryParam("excluded") List<Long> excluded)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {
		UnreadCallBusiness ub = new UnreadCallBusiness();
		EntityBusiness eb = new EntityBusiness();
		String citizenCPF = "";
		if (citizenToken != null)
			citizenCPF = clb.findByToken(citizenToken).getCitizenId();
		if (facebookId != null)
			citizenCPF = cb.getByFacebookId(facebookId).getCitizen_cpf();
		List<SolvedCall> tListSolved = scb.searchCitizenFinal2(periodod, periodoa, entityId, entityCategoryId,
				callProgress, description, citizenCPF, excluded);
		if (tListSolved.isEmpty())
			return Response.status(Status.NOT_FOUND).entity(Messages.getInstance().getMessage(IMessages.CALL_NOT_FOUND))
					.type(MediaType.APPLICATION_JSON).build();

		List<Long> callids = new ArrayList<Long>();
		for (SolvedCall solved : tListSolved) {
			callids.add(solved.getParentCallId().getSolvedCallId());
			EntityEntityCategoryMaps emaps = solved.getEntityEntityCategoryMaps();
			emaps.setEntity(eb.getById(emaps.getEntityEntityCategoryMapsPK().getEntityId()));
			solved.setEntityEntityCategoryMaps(emaps);
		}

		List<UnreadCall> unreadList = ub.findList(callids);

		Map<String, List> map = new HashMap<String, List>();
		map.put("unreadList", unreadList);
		map.put("callList", tListSolved);
		return Response.ok(map).build();
	}

	@GET
	@Path("/searchun") // Busca chamados na Unsolved Call
	public Response searchUnsolved(@QueryParam("periodod") String periodod, @QueryParam("periodoa") String periodoa,
			@QueryParam("entityId") String entityId, @QueryParam("entityCategoryId") String entityCategoryId,
			@QueryParam("callProgress") String callProgress, @QueryParam("description") String description,
			@QueryParam("citizenToken") String citizenToken, @QueryParam("facebookId") String facebookId,
			@QueryParam("excluded") List<Long> excluded)
			throws ParseException, HibernateException, SQLException, ModelException, NoSuchFileException {
		String citizenCPF = "";
		EntityBusiness eb = new EntityBusiness();
		UnreadCallBusiness ub = new UnreadCallBusiness();
		if (citizenToken != null)
			citizenCPF = clb.findByToken(citizenToken).getCitizenId();
		if (facebookId != null)
			citizenCPF = cb.getByFacebookId(facebookId).getCitizen_cpf();
		List<UnsolvedCall> tList = uscb.searchCitizenFinal2(periodod, periodoa, entityId, entityCategoryId,
				callProgress, description, citizenCPF, excluded);
		List<Long> callids = new ArrayList<Long>();
		for (UnsolvedCall un : tList) {
			callids.add(un.getParentCallId().getUnsolvedCallId());
			EntityEntityCategoryMaps emaps = un.getEntityEntityCategoryMaps();
			emaps.setEntity(eb.getById(emaps.getEntityEntityCategoryMapsPK().getEntityId()));
			un.setEntityEntityCategoryMaps(emaps);
		}
		if (callids.isEmpty())
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.CALL_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.CALL_NOT_FOUND)));

		List<UnreadCall> unreadList = ub.findList(callids);
		Map<String, List> map = new HashMap<String, List>();
		map.put("unreadList", unreadList);
		map.put("callList", tList);
		return Response.ok(map).build();
		// return Response.ok(tList).build();
	}

	@GET
	@Path("/password/{cpf}")
	public Response forgotPassword(@PathParam("cpf") String cpf)
			throws ParseException, HibernateException, SQLException, ModelException {
		CitizenBusiness cb = new CitizenBusiness();
		try {
			Citizen c = cb.getById(cpf);
			if (c == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.USER_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
			String newPass = PasswordGenerator.randomPassword();
			c.setPassword(FormatterMd5.md5(newPass));
			cb.merge(c);
			sendEmailForgotPassword(c.getName(), c.getEmail(), newPass);
			return Response.ok(makeMessage(Messages.getInstance().getMessage(IMessages.RECOVER_PASSWORD_SUCCESS)))
					.build();
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
	@Path("/read/{cpf}")
	public Response getByIdRead(@PathParam("cpf") String cpf)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		CitizenBusiness cb = new CitizenBusiness();
		List<Integer> t = cb.getRead(cpf);
		if (t == null) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		}
		return Response.ok(t).build();
	}

	@GET
	@Path("/readlist")
	public Response getByIdRead(@QueryParam("calls") List<Long> calls)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		UnreadCallBusiness ucb = new UnreadCallBusiness();
		List<UnreadCall> tList = ucb.findList(calls);
		return Response.ok(tList).build();
	}

	@PUT
	@Path("/read")
	public Response markRead(ReadDTO readParams)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		CitizenBusiness cb = new CitizenBusiness();
		BroadcastMessageBusiness bmb = new BroadcastMessageBusiness();
		Citizen citizen = cb.getByIdRead(readParams.getCpf());
		BroadcastMessage bme = bmb.getById(readParams.getBmid());
		List<BroadcastMessage> listbm = citizen.getBroadcastMessageCollection();
		listbm.add(bme);
		citizen.setBroadcastMessageCollection(listbm);
		List<Integer> t = cb.getRead(readParams.getCpf());
		cb.merge(citizen);
		return Response.status(Status.CREATED)
				.entity(new ResponseMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION),
						Status.CREATED.toString()))
				.build();
	}

	@PUT
	@Path("/readcall")
	public Response markReadCall(Long callId)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
		UnreadCallBusiness bmb = new UnreadCallBusiness();
		UnreadCall unread = bmb.getById(callId);
		// if (unread.getSolvedCallId() != null)
		// bmb.delete(unread);
		// else {
		unread.setRead(true);
		bmb.merge(unread);
		// }
		return Response.status(Status.CREATED)
				.entity(new ResponseMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION),
						Status.CREATED.toString()))
				.build();
	}

	public void sendEmailForgotPassword(String user, String email, String senha) {
		MinioServer minioServer = new MinioServer();
		final String username = SystemConfiguration.getInstance()
				.getSystemConfiguration(ISystemConfiguration.USERNAME_EMAIL);
		final String password = SystemConfiguration.getInstance()
				.getSystemConfiguration(ISystemConfiguration.PASSWORD_EMAIL);
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
			message.setSubject("ZEM - Recuperação de Senha");
			MimeMultipart multipart = new MimeMultipart("related");
			// Primeira parte: texto
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
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			String htmlText = "<div><h1><img style=\"vertical-align:middle\" width=\"100\" height=\"100\" src=\"" + url
					+ "\"></img>" + "<span>Zeladoria Municipal</span></h1></div><br>" + "Olá " + user + ",<br/>"
					+ "<p>Você soliticou a recuperação de senha do seu usuário ZEM.<br/>" + "Sua nova senha é: " + senha
					+ "<br/>" + "Você pode alterá-la assim que fizer login!<br/></p>" + "Obrigado!<br/><br/>"
					+ "Equipe ZEM.<br/>";
			messageBodyPart.setContent(htmlText, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
			System.out.println("E-mail enviado com sucesso para " + email + ".");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
