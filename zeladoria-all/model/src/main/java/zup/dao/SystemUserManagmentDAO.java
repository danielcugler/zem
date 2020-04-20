package zup.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import zup.bean.SystemUser;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class SystemUserManagmentDAO {
	private static Logger logger = Logger.getLogger(SystemUserDAO.class);

	public List<SystemUser> findByNameAndPerfil(String name, int perfilId)
			throws HibernateException, SQLException, ModelException {
		Criteria selectSU = HibernateUtil.getSession().createCriteria(SystemUser.class);

		selectSU.createAlias("systemUserProfile", "supid");
		selectSU.add(Restrictions.eq("supid.systemUserProfileId", perfilId));
		selectSU.add(Restrictions.eq("name", name));

		selectSU.addOrder(Order.asc("name"));
		List<SystemUser> tList = new ArrayList<SystemUser>();
		tList = (List<SystemUser>) selectSU.list();
		if (tList.isEmpty()) {
			logger.info(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		} else
			return tList;
	}
}
