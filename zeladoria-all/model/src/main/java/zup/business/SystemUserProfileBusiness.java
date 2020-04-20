package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.EntityCategory;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.bean.SystemUserProfile;
import zup.dao.SystemUserProfileDAO;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class SystemUserProfileBusiness extends AbstractBusiness<SystemUserProfile, Integer> {
	private static Logger logger = Logger.getLogger(SystemUserProfileBusiness.class);
	private SystemUserProfileDAO systemUserProfileDao;

	public SystemUserProfileBusiness() {
		super(new SystemUserProfileDAO());
		this.systemUserProfileDao = (SystemUserProfileDAO) getDao();
	}

	public List<SystemUserProfile> searchAll(String name, String supPerm, String enabledS)
			throws HibernateException, SQLException, ModelException, ParseException {
		List<SystemUserProfile> tList = null;
		try {
			HibernateUtil.beginTransaction();
			tList = systemUserProfileDao.searchAll(name, supPerm, enabledS);
			return tList;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}
	
	
	public Result<SystemUserProfile> searchPag2(int pagina, String name, String enabled)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return systemUserProfileDao.searchPag2(pagina, name, enabled);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public SystemUserProfile findByName(String name)
			throws HibernateException, SQLException, ModelException, ParseException {
		SystemUserProfile t = null;
		try {
			HibernateUtil.beginTransaction();
			t = systemUserProfileDao.findByName(name);
			return t;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public Map<Integer, List<SystemUserProfile>> searchPag(int pagina, String name, String enabled)
			throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return systemUserProfileDao.searchPag(pagina, name, enabled);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}

	}

	public List<SystemUserProfile> findActives() throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return systemUserProfileDao.findActives();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public static void main(String[] args) throws HibernateException, SQLException, ModelException, ParseException {
		SystemUserProfileBusiness mmb = new SystemUserProfileBusiness();

		SystemUserProfile sup = new SystemUserProfile("root", Enabled.ENABLED);
	//	System.out.println("getall");
		Result<SystemUserProfile> result = mmb.searchPag2(1, "", "");
		for (SystemUserProfile mb : result.getList())
			System.out.println(mb.getSystemUserProfileId());

	}

}
