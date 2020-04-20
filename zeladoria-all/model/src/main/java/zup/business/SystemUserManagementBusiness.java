package zup.business;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;

import zup.bean.SystemUser;
import zup.dao.SystemUserManagmentDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class SystemUserManagementBusiness {
	private SystemUserManagmentDAO systemUserMDao;

	public List<SystemUser> findByNameAndPerfil(String name, int idPerfil)
			throws HibernateException, SQLException, ModelException {

		try {

			HibernateUtil.beginTransaction();

			return systemUserMDao.findByNameAndPerfil(name, idPerfil);

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			HibernateUtil.closeSession();
		}

		return null;
	}

}
