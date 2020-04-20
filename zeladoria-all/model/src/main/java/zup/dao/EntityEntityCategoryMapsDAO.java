package zup.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NamedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.EntityEntityCategoryMapsPK;
import zup.bean.MessageModel;
import zup.bean.UnsolvedCall;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class EntityEntityCategoryMapsDAO extends AbstractDAO<EntityEntityCategoryMaps, EntityEntityCategoryMapsPK> {
	private static Logger logger = Logger.getLogger(EntityEntityCategoryMapsDAO.class);

	public EntityEntityCategoryMapsDAO() {
		super(EntityEntityCategoryMaps.class);
	}

	public EntityEntityCategoryMaps getByEEC(Integer entityCategoryId, Integer entityId)
			throws SQLException, ModelException {
		EntityEntityCategoryMapsPK id = new EntityEntityCategoryMapsPK(entityCategoryId, entityId);
		EntityEntityCategoryMaps entityMaps = (EntityEntityCategoryMaps) HibernateUtil.getSession()
				.get(EntityEntityCategoryMaps.class, id);

		if (entityMaps == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return entityMaps;
	}
/*
	@Override
	public EntityEntityCategoryMaps getById(EntityEntityCategoryMapsPK id) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(EntityEntityCategoryMaps.class);
		select.add(Restrictions.eq("entityId", id.getEntityId()));
		select.add(Restrictions.eq("entityCategoryId", id.getEntityCategoryId()));
		EntityEntityCategoryMaps entityMaps = (EntityEntityCategoryMaps) select.uniqueResult();
		return entityMaps;
	}
*/

	public List<Entity> getByEntity() throws SQLException, ModelException {
		String hql = "select distinct e from Entity e, EntityEntityCategoryMaps eecm where e.entityId = eecm.entityEntityCategoryMapsPK.entityId and e.enabled = 0 ORDER BY e.name ASC";
		Query query = HibernateUtil.getSession().createQuery(hql);
		return (List<Entity>) query.list();
	}	



	public List<EntityCategory> getByIdEC(Integer entityId) throws SQLException, ModelException {
		String hql = "SELECT DISTINCT ec FROM UnsolvedCall u, EntityCategory ec WHERE u.entityEntityCategoryMaps.entity.entityId = :entityId AND u.entityEntityCategoryMaps.entityCategory.entityCategoryId = ec.entityCategoryId ORDER BY ec.name";
		Query query = HibernateUtil.getSession().createQuery(hql);
		query.setInteger("entityId", entityId);
		return (List<EntityCategory>) query.list();
	}


	public EntityEntityCategoryMaps getByIdUns(Integer entityCategoryId, Integer entityId) throws SQLException, ModelException {
		EntityEntityCategoryMapsPK id = new EntityEntityCategoryMapsPK(entityCategoryId, entityId);
		Criteria select = HibernateUtil.getSession().createCriteria(EntityEntityCategoryMaps.class);
		select.add(Restrictions.idEq(id));
		//select.add(Restrictions.eq("entityId", entityId));
		//select.add(Restrictions.eq("entityCategoryId", entityCategoryId));
		select.setFetchMode("unsolvedCallCollection", FetchMode.JOIN);
		EntityEntityCategoryMaps entityMaps = (EntityEntityCategoryMaps) select.uniqueResult();
		return entityMaps;
	}

}