package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.Result;
import zup.bean.SystemUserProfile;
import zup.bean.SystemUserProfilePermission;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.ISystemConfiguration;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class SystemUserProfilePermissionDAO extends AbstractDAO<SystemUserProfilePermission, Integer> {
	private static Logger logger = Logger.getLogger(SystemUserProfilePermissionDAO.class);

	public SystemUserProfilePermissionDAO() {
		super(SystemUserProfilePermission.class);
	}


	
	public List<SystemUserProfilePermission> search(String name, String enabledS)
			throws HibernateException, SQLException, ModelException, ParseException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfilePermission.class);
		if (name.equals("z") && enabledS.equals("z"))
			return this.findAll();
		if (!name.equals("z"))
			select.add(Restrictions.like("name", name));
		if (!enabledS.equals("z")) {
			boolean enabled = false;
			enabled = Boolean.parseBoolean(enabledS);
			select.add(Restrictions.eq("enabled", enabled));
		}
		return (List<SystemUserProfilePermission>) select.addOrder(Order.asc("name"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	@Override
	public List<SystemUserProfilePermission> findAll() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfilePermission.class);
		select.addOrder(Order.asc("name"));
		return select.list();
	}

}
