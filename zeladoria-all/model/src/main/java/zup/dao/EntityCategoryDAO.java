package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class EntityCategoryDAO extends AbstractDAO<EntityCategory, Integer> {
	public EntityCategoryDAO() {
		super(EntityCategory.class);
	}

	public Result<EntityCategory> searchPag2(int page, String name, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
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
		return new Result<EntityCategory>(count,(List<EntityCategory>) select.list());
	}
	
	public Map<Integer, List<EntityCategory>> searchPag(int page, String name, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<EntityCategory>> map = new HashMap<Integer, List<EntityCategory>>();
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
		if (name.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<EntityCategory> bms = (List<EntityCategory>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		if (!enabledS.equals("z"))
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<EntityCategory> bms = (List<EntityCategory>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	@Override
	public EntityCategory getById(Integer EntityCategoryId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
		select.setFetchMode("entityCollection", FetchMode.JOIN);
		select.add(Restrictions.eq("entityCategoryId", EntityCategoryId));
		EntityCategory t = (EntityCategory) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public EntityCategory getById2(Integer EntityCategoryId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
		select.add(Restrictions.eq("entityCategoryId", EntityCategoryId));
		EntityCategory t = (EntityCategory) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public EntityCategory findByName(String name) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
		select.add(Restrictions.eq("name", name));
		return (EntityCategory) select.uniqueResult();
	}

	public List<EntityCategory> findByEntity(Integer entityId) throws SQLException, ModelException {
		//HQL Named Query Example
		Query query = HibernateUtil.getSession().getNamedQuery("EntityEntityCategoryMaps.findEcByEntityId");
		query.setInteger("entityId", entityId);
		/*
		Criteria select = HibernateUtil.getSession().createCriteria(EntityEntityCategoryMaps.class);
		select.createAlias("entityCollection", "entity");
		select.add(Restrictions.eq("entity.entityId", entityId));
		select.addOrder(Order.asc("name"));
		List<EntityCategory> t = (List<EntityCategory>) select.list();		
*/
		return  (List<EntityCategory>) query.list();
	}

	public List<EntityCategory> findByEntityAndEnabled(Integer entityId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
		select.createAlias("entityCollection", "entity");
		select.add(Restrictions.eq("entity.entityId", entityId));
		select.add(Restrictions.eq("enabled", Enabled.fromValue(0)));
		select.addOrder(Order.asc("name"));

		List<EntityCategory> t = (List<EntityCategory>) select.list();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public List<EntityCategory> findActives() throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
		select.add(Restrictions.eq("enabled", Enabled.fromValue(0)));
		select.addOrder(Order.asc("name"));
		List<EntityCategory> t = (List<EntityCategory>) select.list();
			return t;
	}
	
	

	@Override
	public List<EntityCategory> findAll() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityCategory.class);
		select.addOrder(Order.asc("name"));
		return (List<EntityCategory>) select.list();
	}

}
