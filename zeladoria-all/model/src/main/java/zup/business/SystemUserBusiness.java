package zup.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import zup.bean.Citizen;
import zup.bean.Result;
import zup.bean.SystemUser;
import zup.bean.SystemUserProfile;
import zup.dao.SystemUserDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class SystemUserBusiness extends AbstractBusiness<SystemUser, String> {
	private static Logger logger = Logger.getLogger(SystemUserBusiness.class);
	private SystemUserDAO systemUserDao;

	public SystemUserBusiness() {
		super(new SystemUserDAO());
		this.systemUserDao = (SystemUserDAO) getDao();
	}

	public SystemUser findByUsername(String username) throws SQLException {
		SystemUser t;
		try {
			HibernateUtil.beginTransaction();
			t = systemUserDao.findByUsername(username);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	@Override
	public SystemUser getById(String id)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		SystemUser t;
		try {
			HibernateUtil.beginTransaction();
			t = systemUserDao.getById(id);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}
	
	
	public List<String> getRoles(Integer systemUserProfileId) throws SQLException, ModelException {
		List<String> roles;
		try {
			HibernateUtil.beginTransaction();
			roles = systemUserDao.getRoles(systemUserProfileId);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return roles;
	}

	
	
	
	public List<SystemUser> getByEnity(int id)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		List<SystemUser> t;
		try {
			HibernateUtil.beginTransaction();
			t = systemUserDao.getByEntity(id);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public List<SystemUser> getByProfile(Integer systemUserProfileId)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		List<SystemUser> t;
		try {
			HibernateUtil.beginTransaction();
			t = systemUserDao.getByProfile(systemUserProfileId);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public Result<SystemUser> searchPag2(int pagina, String name,String perfil, String enabled)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return systemUserDao.searchPag2(pagina, name, perfil, enabled);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	
	public Map<Integer, List<SystemUser>> searchPag(int pagina, String name, String perfil, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return systemUserDao.searchPag(pagina, name, perfil, enabledS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<SystemUser>>();
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

	public static void main(String[] args) throws HibernateException, SQLException, ModelException {

		SystemUserBusiness mmb = new SystemUserBusiness();
	/*	List<SystemUser> tList2 = null;
		try {
			tList2 = mmb.findAll();
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
		} catch (ModelException e) {
			e.printStackTrace();
		}
		List<SystemUser> tList = new ArrayList<SystemUser>(tList2);
		for (SystemUser wu : tList) {
			wu.setPassword(md5(wu.getPassword()));
			mmb.update(wu);
		}
	*/
		List<String> roles= mmb.getRoles(1);
		System.out.println(roles.toString());
	}

}
