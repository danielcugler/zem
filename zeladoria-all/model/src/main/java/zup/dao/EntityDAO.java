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

import zup.bean.AttendanceTime;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.MessageModel;
import zup.bean.Neighborhood;
import zup.bean.Result;
import zup.bean.SystemUserProfile;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class EntityDAO extends AbstractDAO<Entity, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);
	public EntityDAO() {
		super(Entity.class);
	}
	public Map<Integer, List<Entity>> searchPagAT(int page, String name, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<Entity>> map = new HashMap<Integer, List<Entity>>();
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
		if (name.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<Entity> bms = (List<Entity>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.like("name", "%" + name + "%"));
		if (!enabledS.equals("z"))
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<Entity> bms = (List<Entity>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}
	
	public Result<Entity> searchPag2(int page, String name, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
		if (!name.isEmpty())
			select.add(Restrictions.ilike("name", "%" + name + "%"));
			//select.add(Restrictions.eq("name", name));
		if (!enabledS.isEmpty())
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<Entity>(count,(List<Entity>) select.list());
	}
	

	public Map<Integer, List<Entity>> searchPag(int page, String name, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<Entity>> map = new HashMap<Integer, List<Entity>>();
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
	//	select.setFetchMode("entityCategoryCollection", FetchMode.JOIN);
		if (name.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<Entity> bms = (List<Entity>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.like("name", "%" + name + "%"));
		if (!enabledS.equals("z"))
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		//select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	//	select.setProjection(Projections.distinct(Projections.id()));
		List<Entity> bms = (List<Entity>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}
	

	public Entity findById(Integer id) throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
		select.setFetchMode("entityCategoryCollection", FetchMode.JOIN);
		select.add(Restrictions.eq("entityId", id));
		return (Entity) select.uniqueResult();
	}

	public Entity findByName(String name) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
		select.add(Restrictions.eq("name", name));
		return (Entity) select.uniqueResult();
	}

	public List<Entity> findActives() throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
		select.setFetchMode("entityCategoryCollection", FetchMode.JOIN);
		select.add(Restrictions.eq("enabled", Enabled.ENABLED));
		//select.createAlias("entityCategoryCollection", "ecc");  // Create alias for streets
	    //select.add(Restrictions.eq("ecc.enabled", Enabled.ENABLED));
		select.addOrder(Order.asc("name"));		
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Entity>) select.list();
	}

	@Override
	public List<Entity> findAll() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
		select.setFetchMode("entityCategoryCollection", FetchMode.JOIN);
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.addOrder(Order.asc("name"));
		return select.list();
	}

	@Override
	public Entity getById(Integer entityId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Entity.class);
		select.setFetchMode("entityCategoryCollection", FetchMode.JOIN);

		select.add(Restrictions.eq("entityId", entityId));
		Entity t = (Entity) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}
	
	public List<EntityCategory> getByIdECLock(Integer entityId) throws SQLException, ModelException {
		String hql = "SELECT DISTINCT ec FROM UnsolvedCall u, EntityCategory ec WHERE u.entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId = :entityId AND u.entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId = ec.entityCategoryId ORDER BY ec.name";
		Query query = HibernateUtil.getSession().createQuery(hql);
query.setInteger("entityId", entityId);
return (List<EntityCategory>) query.list();
	}
	
	
	public void saveEnt(Entity t) throws SQLException {
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
		AttendanceTime at= t.getAttendanceTime();
		t.setAttendanceTime(null);
		HibernateUtil.getSession().persist(t);
		at.setEntityId(t.getEntityId());
		t.setAttendanceTime(at);
		HibernateUtil.getSession().save(t);		
	}
	
}
