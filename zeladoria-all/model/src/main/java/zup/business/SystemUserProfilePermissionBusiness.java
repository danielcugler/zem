package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.SystemUserProfilePermission;
import zup.dao.SystemUserProfilePermissionDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class SystemUserProfilePermissionBusiness extends AbstractBusiness<SystemUserProfilePermission, Integer> {
	private static Logger logger = Logger.getLogger(SystemUserProfilePermissionBusiness.class);
	private SystemUserProfilePermissionDAO systemUserProfileDao;

	public SystemUserProfilePermissionBusiness() {
		super(new SystemUserProfilePermissionDAO());
		this.systemUserProfileDao = (SystemUserProfilePermissionDAO) getDao();
	}

	public List<SystemUserProfilePermission> search(String name, String enabledS)
			throws HibernateException, SQLException, ModelException, ParseException {
		List<SystemUserProfilePermission> tList = null;
		try {
			HibernateUtil.beginTransaction();
			tList = systemUserProfileDao.search(name, enabledS);
			return tList;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public static void main(String[] args) throws HibernateException, SQLException, ModelException {
		SystemUserProfilePermissionBusiness mmb = new SystemUserProfilePermissionBusiness();
		System.out.println("getall");
		for (SystemUserProfilePermission mb : mmb.findAll())
			System.out.println("Nome: " + mb.getName());

	}

}
