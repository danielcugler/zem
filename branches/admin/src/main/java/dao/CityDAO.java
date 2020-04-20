package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import bean.City;
import utils.HibernateUtil;


public class CityDAO extends AbstractDAO<City, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public CityDAO() {
		super(City.class);
	}
	
	public List<City> getByEstado(String stateId) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(City.class);
		select.setFetchMode("stateId", FetchMode.JOIN);	
		select.add(Restrictions.eq("stateId.stateId", stateId));
		select.setProjection( Projections.distinct( Projections.projectionList()
				.add( Projections.id(), "cityId")
				.add( Projections.property("name"),"name")
				));				
		select.setResultTransformer(Transformers.aliasToBean(City.class));
		select.addOrder(Order.asc("name"));
	//	select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<City>) select.list();
	}
	public void delAuto() throws HibernateException, SQLException {
		String queryString = "SELECT count(*) FROM delauto();";
		Query query = HibernateUtil.getSession().createSQLQuery(queryString);
		query.uniqueResult();
	}
}
