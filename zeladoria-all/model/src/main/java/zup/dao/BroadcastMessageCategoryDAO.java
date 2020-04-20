package zup.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import zup.bean.BroadcastMessageCategory;
import zup.model.utils.HibernateUtil;

public class BroadcastMessageCategoryDAO extends AbstractDAO<BroadcastMessageCategory, Integer> {
	private static Logger logger = Logger.getLogger(BroadcastMessageCategoryDAO.class);

	public BroadcastMessageCategoryDAO() {
		super(BroadcastMessageCategory.class);
	}

	public BroadcastMessageCategory findById(int id) throws HibernateException, SQLException {

		BroadcastMessageCategory t = (BroadcastMessageCategory) HibernateUtil.getSession()
				.get(BroadcastMessageCategory.class, id);
		return t;
	}

	public List<BroadcastMessageCategory> findByName(String name) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessageCategory.class);
		select.add(Restrictions.eq("name", name));
		return (List<BroadcastMessageCategory>) select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

}
