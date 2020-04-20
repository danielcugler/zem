package zup.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;


import zup.bean.State;
import zup.model.utils.HibernateUtil;

public class StateDAO extends AbstractDAO<State, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public StateDAO() {
		super(State.class);
	}
	public List<State> findByName(String name) throws HibernateException, SQLException {
		Criteria select= HibernateUtil.getSession().createCriteria(State.class);

		select.setResultTransformer(Transformers.aliasToBean(State.class));
		select.addOrder(Order.asc("name"));
		return select.list();
	}
}