package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.EntityCategory;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.bean.SystemUserProfile;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class SystemUserProfileDAO extends AbstractDAO<SystemUserProfile, Integer> {
	private static Logger logger = Logger.getLogger(SystemUserProfileDAO.class);

	public SystemUserProfileDAO() {
		super(SystemUserProfile.class);
	}

	public List<SystemUserProfile> searchAll(String name, String supPerm, String enabledS)
			throws HibernateException, SQLException, ModelException, ParseException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfile.class);
		if (name.equals("z") && enabledS.equals("z"))
			return this.findAll();
		if (!name.equals("z"))
			select.add(Restrictions.like("name", name));
		if (!supPerm.equals("z"))
			select.add(
					Restrictions.like("systemUserProfilePermissionCollection.systemUserProfilePermissionId", supPerm));
		if (!enabledS.equals("z")) {
			boolean enabled = false;
			enabled = Boolean.parseBoolean(enabledS);
			select.add(Restrictions.eq("enabled", enabled));
		}
		return (List<SystemUserProfile>) select.addOrder(Order.asc("name"))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	
	public Result<SystemUserProfile> searchPag2(int page, String name, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfile.class);
		if (!name.isEmpty())
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		if (!enabledS.isEmpty())
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<SystemUserProfile>(count,(List<SystemUserProfile>) select.list());
	}
	
	
	public Map<Integer, List<SystemUserProfile>> searchPag(int page, String name, String enabledS)
			throws HibernateException, SQLException, ModelException, ParseException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfile.class);
		Map<Integer, List<SystemUserProfile>> map = new HashMap<Integer, List<SystemUserProfile>>();
		if (name.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<SystemUserProfile> bms = (List<SystemUserProfile>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		if (!enabledS.equals("z")) {
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		}
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<SystemUserProfile> bms = (List<SystemUserProfile>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	@Override
	public SystemUserProfile getById(Integer id) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfile.class);
		select.setFetchMode("systemUserProfilePermissionCollection", FetchMode.JOIN);
		select.add(Restrictions.eq("systemUserProfileId", id));
		SystemUserProfile t = (SystemUserProfile) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	@Override
	public List<SystemUserProfile> findAll() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfile.class);
		select.setFetchMode("systemUserProfilePermissionCollection", FetchMode.JOIN);
		select.addOrder(Order.asc("name"));
		return (List<SystemUserProfile>) select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public SystemUserProfile findByName(String name) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfile.class);
		select.add(Restrictions.eq("name", name));
		return (SystemUserProfile) select.uniqueResult();
	}

	public List<SystemUserProfile> findActives() throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUserProfile.class);
		select.add(Restrictions.eq("enabled", Enabled.fromValue(0)));
		select.addOrder(Order.asc("name"));
		List<SystemUserProfile> t = (List<SystemUserProfile>) select.list();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));

		return t;
	}

}
