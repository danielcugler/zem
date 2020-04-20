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
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.Result;
import zup.bean.SystemUser;
import zup.bean.SystemUserProfile;
import zup.bean.SystemUser;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class SystemUserDAO extends AbstractDAO<SystemUser, String> {
	private static Logger logger = Logger.getLogger(SystemUserDAO.class);

	public SystemUserDAO() {
		super(SystemUser.class);
	}

	public SystemUser findByUsername(String username) throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);
		select.add(Restrictions.eq("systemUserUsername", username));
		return (SystemUser) select.uniqueResult();
	}

	public Result<SystemUser> searchPag2(int page, String name,String perfil, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);
		select.setFetchMode("systemUserProfileId", FetchMode.JOIN);
		if (!name.isEmpty())
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		if (!perfil.isEmpty()) 
			select.add(Restrictions.eq("systemUserProfileId.systemUserProfileId", Integer.parseInt(perfil)));
		if (!enabledS.isEmpty())
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<SystemUser>(count,(List<SystemUser>) select.list());
	}
	
	
	public Map<Integer, List<SystemUser>> searchPag(int page, String name, String perfil, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<SystemUser>> map = new HashMap<Integer, List<SystemUser>>();
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);
		select.setFetchMode("systemUserProfileId", FetchMode.JOIN);
		if (name.equals("z") && perfil.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<SystemUser> bms = (List<SystemUser>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z")) {
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		}
		if (!perfil.equals("z")) {
			select.add(Restrictions.eq("systemUserProfileId.systemUserProfileId", Integer.parseInt(perfil)));
		}
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
		List<SystemUser> bms = (List<SystemUser>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	@Override
	public SystemUser getById(String systemUserUsername) throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);
		select.setFetchMode("worksAtEntity", FetchMode.JOIN);
		select.add(Restrictions.eq("systemUserUsername", systemUserUsername));
		return (SystemUser) select.uniqueResult();
	}

	public List<SystemUser> getByEntity(int entityId) throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);
		select.setFetchMode("worksAtEntity", FetchMode.JOIN);
		select.add(Restrictions.eq("worksAtEntity.entityId", entityId));
		return (List<SystemUser>) select.list();
	}

	public SystemUser getById2(String systemUserUsername) throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);
		select.setFetchMode("broadcastMessageCollection", FetchMode.SELECT);

		select.add(Restrictions.eq("systemUserUsername", systemUserUsername));
		return (SystemUser) select.uniqueResult();
	}

	public SystemUser getById3(String systemUserUsername) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);

		select.setFetchMode("unsolvedCallCollection", FetchMode.SELECT);
		select.setFetchMode("solvedCallCollection", FetchMode.SELECT);
		select.add(Restrictions.eq("systemUserUsername", systemUserUsername));
		SystemUser t = (SystemUser) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public List<String> getRoles(Integer systemUserProfileId) throws SQLException, ModelException {
		Query select = HibernateUtil.getSession().getNamedQuery("SystemUserProfile.findRoles");
		select.setParameter("systemUserProfileId", systemUserProfileId);
		return (List<String>) select.list();
	}

	
	
	public List<SystemUser> getByProfile(Integer systemUserProfileId) throws SQLException {
		Query select = HibernateUtil.getSession().getNamedQuery("SystemUser.findByProfile");
		select.setParameter("systemUserProfileId", systemUserProfileId);
		return (List<SystemUser>) select.list();
	}
	
	@Override
	public List<SystemUser> findAll() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(SystemUser.class);
		select.setFetchMode("systemUserCollection", FetchMode.JOIN);
		select.addOrder(Order.asc("systemUserUsername"));
		return (List<SystemUser>) select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

}