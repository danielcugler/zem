package zup.webservices.adm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.xmlpull.v1.XmlPullParserException;

import filters.SystemUserFilterBean;
import io.minio.MinioClient;
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
import zup.bean.Entity;
import zup.bean.Log;
import zup.bean.Result;
import zup.bean.SystemUser;
import zup.business.EntityBusiness;
import zup.business.LogBusiness;
import zup.business.SystemUserBusiness;
import zup.business.SystemUserProfileBusiness;
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
import zup.utils.MinioServer;
import zup.utils.ServiceUriBuilder;
import zup.validate.SystemUserValidate;

@Path("/systemuser2")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemUserService2 {
	private static Logger logger = Logger.getLogger(SystemUserService2.class);
	private ServiceUriBuilder sub = new ServiceUriBuilder();
	private @Context UriInfo uriInfo;

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}

	@GET
	@Path("/login/{login}/{password}")
	public Response login(@PathParam("login") String login, @PathParam("password") String password)
			throws ParseException, HibernateException, SQLException, ModelException {
		SystemUserBusiness mb = new SystemUserBusiness();
		try {
			SystemUser c = mb.getById(login);
			if (c == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.USER_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
			if (c.getEnabled().equals(Enabled.DISABLED))
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.INACTIVE_USER,
						Messages.getInstance().getMessage(IMessages.INACTIVE_USER)));
			if (c.getPassword().equals(FormatterMd5.md5(password))) {
				return Response.ok(makeMessage(Messages.getInstance().getMessage(IMessages.LOGIN_SUCCESS))).build();
			} else {
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.PASSWORD_ERROR,
						Messages.getInstance().getMessage(IMessages.PASSWORD_ERROR)));
			}
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
	@Path("/password")
	public Response forgotPassword()
			throws ParseException, HibernateException, SQLException, ModelException {
		SystemUserBusiness mb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		try {
			SystemUser c = mb.getById(username);
			if (c == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.USER_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
			String newPass = PasswordGenerator.randomPassword();
			c.setPassword(FormatterMd5.md5(newPass));
			mb.merge(c);
			sendEmail(c.getName(), c.getEmail(), newPass);
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

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Path("/changepass")
	public Response changePassword(@FormParam("username") String username, @FormParam("oldPass") String oldPass,
			@FormParam("newPass") String newPass, @FormParam("confirmPass") String confirmPass)
			throws ParseException, HibernateException, SQLException, ModelException {
		SystemUserBusiness mb = new SystemUserBusiness();
		LogBusiness lb = new LogBusiness();
		try {
			SystemUser c = mb.getById(username);
			if (c == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.USER_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.USER_NOT_FOUND)));
			if (c.getPassword().equals(oldPass)) {
				if (newPass.equals(confirmPass)) {
					c.setPassword(newPass);
					mb.merge(c);
					Log log = new Log(InformationType.SYSTEMUSER, c.makeLog());
					log.setOperationType(OperationType.ALTERARSENHA);
					log.setSystemUserUsername(mb.getById(username));
					lb.save(log);
					return Response
							.ok(makeMessage(Messages.getInstance().getMessage(IMessages.CHANGE_PASSWORD_SUCCESS)))
							.build();
				} else
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.USER_NOT_FOUND,
							Messages.getInstance().getMessage(IMessages.CONFIRM_PASSWORD_ERROR)));
			} else
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.USER_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.CURRENT_PASSWORD_ERROR)));
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
	@Path("/minio/{usn}")
	public Response getUserMinio(@PathParam("usn") String usn)  {
		MinioServer minioServer = new MinioServer();
		try {
			// Create a minioClient with the Minio Server name, Port, Access key
			// and Secret key.
			// MinioClient minioClient = new
			// MinioClient("http://192.168.4.100:9000", "6BTP2IW1DFNTEAIBVPT2",
			// "VamB9WGfXikQYn+QTB6NO13lszJX29vOHP48IPmk");
			/*
			 * // Check if the bucket already exists. boolean isExist =
			 * minioClient.bucketExists("userphoto"); if(isExist) {
			 * System.out.println("Bucket already exists."); } else { // Make a
			 * new bucket called asiatrip to hold a zip file of photos.
			 * minioClient.makeBucket("userphoto"); } File file = new
			 * File("C://Users/Labitec01//Pictures//pic.jpg"); FileInputStream
			 * fi = new FileInputStream(file); // Upload the zip file to the
			 * bucket with putObject minioClient.putObject("userphoto",
			 * "user01.jpg", fi, file.length(), "image/jpeg"); System.out.
			 * println(
			 * "user01.jpg is successfully uploaded as user01.jpg in user." ); }
			 * catch(MinioException e) { System.out.println("Error occurred: " +
			 * e); }
			 */
			Map<String, String> resp = new HashMap<String, String>();
			Iterable<io.minio.Result<Item>> result = minioServer.getMinioClient().listObjects("user", usn + ".jpg");		
			if (result.iterator().hasNext()) {
				String url = minioServer.getMinioClient().presignedGetObject("user",
						result.iterator().next().get().objectName());
				// String url =
				// minioServer.getMinioClient().presignedGetObject("user", usn +
				// ".jpg", 60 * 60 * 24);
				resp.put("url", url);
			}
			return Response.ok(resp).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	@Path("/fileis/{usn}")
	public Response getFileInputStream(@PathParam("usn") String usn) {
		try {
			File file = new File(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.USER_DIRECTORY) + "/"
							+ usn + ".png");
			byte[] data = Files.readAllBytes(file.toPath());
			String b64 = "data:image/.png;base64," + Base64.encodeBase64String(data);
			Map<String, String> map = new HashMap<String, String>();
			map.put("photo", b64);
			return Response.ok(map).build();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@POST
	@Path("/svsu")
	public Response add(SystemUser t)
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException {
		SystemUserBusiness mb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		SystemUserProfileBusiness supb = new SystemUserProfileBusiness();
		LogBusiness lb = new LogBusiness();
		MinioServer minioServer = new MinioServer();
		try {
			t.setSystemUserProfileId(supb.getById(t.getSystemUserProfileId().getSystemUserProfileId()));
			t.setPassword(FormatterMd5.md5(t.getPassword()));
			mb.save(t);
			if (t.getPhotoPath() != null) {
				byte[] encodedText = Base64.decodeBase64(t.getPhotoPath().replaceAll("data:image/png;base64,", ""));
				MinioClient minioClient = minioServer.getMinioClient();
				InputStream fi = new ByteArrayInputStream(encodedText);
				minioClient.putObject("user", t.getSystemUserUsername() + ".jpg", fi, encodedText.length, "image/jpg");
				fi.close();
			}
			Log log = new Log(InformationType.SYSTEMUSER, t.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(mb.getById(username));
			lb.save(log);
			return Response.ok(makeMessage(Messages.getInstance().getMessage(IMessages.MESSAGE_SAVE_SUCCESS))).build();
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", e.getMessage()));
		}
	}

	@GET
	public Response findAll() {
		SystemUserBusiness mb = new SystemUserBusiness();
		try {
			List<SystemUser> tList = mb.findAll();
			for (SystemUser systemUser : tList) {
				systemUser.addLinks("self", sub.selfSystemUser(systemUser.getSystemUserUsername(), uriInfo));
				systemUser.addLinks("ed", sub.edSystemUser(systemUser.getSystemUserUsername(), uriInfo));
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
	@Path("/ed/{suusername}")
	public Response mergeED(@PathParam("suusername") String suusername) {
		SystemUserBusiness mb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			SystemUser t = mb.getById(suusername);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			t.changeEnabled();
			mb.merge(t);
			Log log = new Log(InformationType.SYSTEMUSER, t.makeLog());
			log.setSystemUserUsername(mb.getById(username));
			String message = "";
			if (t.getEnabled() == Enabled.ENABLED) {
				message = Messages.getInstance().getMessage(IMessages.USER_ACTIVATE_SUCCESS);
				log.setOperationType(OperationType.ATIVACAO);
			} else {
				message = Messages.getInstance().getMessage(IMessages.USER_INACTIVATE_SUCCESS);
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
	public Response merge(SystemUser systemUser)
			throws HibernateException, DataAccessLayerException, SQLException, ModelException {
		SystemUserBusiness mb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		EntityBusiness eb = new EntityBusiness();
		SystemUserProfileBusiness supb = new SystemUserProfileBusiness();
		LogBusiness lb = new LogBusiness();
		MinioServer minioServer = new MinioServer();
		try {
			SystemUser t = mb.getById(systemUser.getSystemUserUsername());
			t.setName(systemUser.getName());
			t.setEmail(systemUser.getEmail());
			t.setCommercialPhone(systemUser.getCommercialPhone());
			t.setPersonalPhone(systemUser.getPersonalPhone());
			Entity entityid = null;
			if(systemUser.getWorksAtEntity().getEntityId() != null)
				entityid = eb.getById(systemUser.getWorksAtEntity().getEntityId());
			t.setWorksAtEntity(entityid);
			t.setSystemUserProfileId(supb.getById(systemUser.getSystemUserProfileId().getSystemUserProfileId()));
			t.setJobPosition(systemUser.getJobPosition());
			t.setSector(systemUser.getSector());
			t.setSystemUserUsername(systemUser.getSystemUserUsername());
			if (systemUser.getPhotoPath() != null) {
				byte[] encodedText = Base64
						.decodeBase64(systemUser.getPhotoPath().replaceAll("data:image/png;base64,", ""));
				MinioClient minioClient = minioServer.getMinioClient();
				InputStream fi = new ByteArrayInputStream(encodedText);
				minioClient.putObject("user", systemUser.getSystemUserUsername() + ".jpg", fi, encodedText.length,
						"image/jpg");
				fi.close();
			}
			mb.merge(t);
			Log log = new Log(InformationType.SYSTEMUSER, t.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(mb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.USER_UPDATE_SUCCESS))).build();
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
	public Response saveEAT(SystemUser systemUser)
			throws HibernateException, ModelException, SQLException {
		SystemUserBusiness mb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		EntityBusiness eb = new EntityBusiness();
		SystemUserProfileBusiness supb = new SystemUserProfileBusiness();
		LogBusiness lb = new LogBusiness();
		MinioServer minioServer = new MinioServer();
		try {
			if (mb.findByUsername(systemUser.getSystemUserUsername()) != null)
				throw new ZEMException(
						ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.DUPLICATED_USER,
								Messages.getInstance().getMessage(IMessages.DUPLICATED_USER)));
			systemUser.setName(systemUser.getName());
			systemUser.setEmail(systemUser.getEmail());
			systemUser.setCommercialPhone(systemUser.getCommercialPhone());
			systemUser.setPersonalPhone(systemUser.getPersonalPhone());
			if (systemUser.getWorksAtEntity().getEntityId() != null)
				systemUser.setWorksAtEntity(eb.getById(systemUser.getWorksAtEntity().getEntityId()));
			else
				systemUser.setWorksAtEntity(null);
			systemUser.setEnabled(Enabled.ENABLED);
			systemUser
					.setSystemUserProfileId(supb.getById(systemUser.getSystemUserProfileId().getSystemUserProfileId()));
			systemUser.setJobPosition(systemUser.getJobPosition());
			systemUser.setSector(systemUser.getSector());
			systemUser.setSystemUserUsername(systemUser.getSystemUserUsername());
			systemUser.setPassword(systemUser.getPassword());
			if (systemUser.getPhotoPath() != null) {
				byte[] encodedText = Base64
						.decodeBase64(systemUser.getPhotoPath().replaceAll("data:image/png;base64,", ""));
				MinioClient minioClient = null;
				try {
					minioClient = minioServer.getMinioClient();
				} catch (InvalidEndpointException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				InputStream fi = new ByteArrayInputStream(encodedText);
				try {
					minioClient.putObject("user", systemUser.getSystemUserUsername() + ".jpg", fi, encodedText.length,
							"image/jpg");
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
				} catch (InvalidArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fi.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			mb.save(systemUser);
			Log log = new Log(InformationType.SYSTEMUSER, systemUser.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(mb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.USER_SAVE_SUCCESS))).build();
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
		}
	}

	@GET
	@Path("/search")
	public Response searchPag2(@BeanParam SystemUserFilterBean filter) {
		SystemUserBusiness mb = new SystemUserBusiness();
		try {
			Result<SystemUser> result = mb.searchPag2(filter.getPage(), filter.getName(), filter.getPerfil(),
					filter.getEnabled());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			for (SystemUser systemUser : result.getList()) {
				systemUser.addLinks("self", sub.selfSystemUser(systemUser.getSystemUserUsername(), uriInfo));
				systemUser.addLinks("ed", sub.edSystemUser(systemUser.getSystemUserUsername(), uriInfo));
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
		SystemUserBusiness mb = new SystemUserBusiness();
		try {
			List<SystemUser> tList = new ArrayList<SystemUser>();
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
		SystemUserBusiness mb = new SystemUserBusiness();
		List<SystemUser> tList = mb.findAll();
		if (tList == null)
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
					Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
		return Response.ok(tList).build();
	}

	@GET
	@Path("/{id}")
	public Response getById22(@PathParam("id") String id)
			 {
		SystemUserBusiness mb = new SystemUserBusiness();
		MinioServer minioServer = new MinioServer();
		SystemUser t;
		try {
			t = mb.getById(id);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			Iterable<io.minio.Result<Item>> result = minioServer.getMinioClient().listObjects("user",
					t.getSystemUserUsername() + ".jpg");
			if (result.iterator().hasNext()) {
				String url = minioServer.getMinioClient().presignedGetObject("user",
						result.iterator().next().get().objectName());
				t.setPhotoPath(url);
			}
			String link = sub.selfSystemUser(t.getSystemUserUsername(), uriInfo);
			t.addLinks("ed", link);
			return Response.ok(t).build();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "InvalidKeyException", e.getMessage()));
		} catch (InvalidBucketNameException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "InvalidBucketNameException", e.getMessage()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "NoSuchAlgorithmException", e.getMessage()));
		} catch (InsufficientDataException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "InsufficientDataException", e.getMessage()));
		} catch (NoResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (ErrorResponseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ErrorResponseException", e.getMessage()));
		} catch (InternalException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "InternalException", e.getMessage()));
		} catch (InvalidExpiresRangeException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "InvalidExpiresRangeException", e.getMessage()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "IOException", e.getMessage()));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "XmlPullParserException", e.getMessage()));
		} catch (InvalidEndpointException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "InvalidEndpointException", e.getMessage()));
		} catch (InvalidPortException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "InvalidPortException", e.getMessage()));
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
		return null;

	}

	public void sendEmail(String user, String email, String senha) {
		MinioServer minioServer= new MinioServer();
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
			MimeMultipart multipart = new MimeMultipart("related");
			// Primeira parte: texto
			String url="";
			try {
				url = minioServer.getMinioClient().presignedGetObject("zem",
						"zem.png");
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
			String htmlText = "<div><h1><img style=\"vertical-align:middle\" width=\"100\" height=\"100\" src=\""+url+"\"></img>"+"<span>Zeladoria Municipal</span></h1></div><br>"+"Olá " + user + ",<br/>"
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
