package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.Neighborhood;
import zup.bean.Result;
import zup.exception.ModelException;
import zup.messages.ISystemConfiguration;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class NeighborhoodDAO extends AbstractDAO<Neighborhood, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public NeighborhoodDAO() {
		super(Neighborhood.class);
	}

	public List<Neighborhood> findByName(String name) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(Neighborhood.class);
		select.setFetchMode("cityId", FetchMode.SELECT);
		select.add(Restrictions.eq("name", name));
		return (List<Neighborhood>) select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public List<Neighborhood> findByCity(Integer cidade) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(Neighborhood.class);
		select.setFetchMode("cityId", FetchMode.JOIN);
		select.add(Restrictions.eq("cityId.cityId", cidade));
		select.addOrder(Order.asc("name"));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Neighborhood>) select.list();
	}

	public Neighborhood findByIdCity(Integer cidade, Integer neighborhoodId) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(Neighborhood.class);
		select.add(Restrictions.eq("cityId.cityId", cidade));
		select.add(Restrictions.eq("neighborhoodId", neighborhoodId));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Neighborhood) select.uniqueResult();
	}

	public Neighborhood findByCityNeighborhood(Integer cidade, String neighborhood)
			throws HibernateException, SQLException {
		Query query = HibernateUtil.getSession().getNamedQuery("Neighborhood.findByCityAndNeighborhood");
		// named query com unaccent para remover acentos dos dados do banco
		// Remove acentos do nome do bairro
		query.setString("name", StringUtils.upperCase(StringUtils.stripAccents(neighborhood)));
		query.setInteger("cityId", cidade);
		return (Neighborhood) query.uniqueResult();
		/*
		 * Criteria select =
		 * HibernateUtil.getSession().createCriteria(Neighborhood.class);
		 * select.add(Restrictions.eq("cityId.cityId", cidade));
		 * select.add(Restrictions.eq("name", neighborhood));
		 * select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); return
		 * (Neighborhood) select.uniqueResult();
		 */
	}

	public Result<Neighborhood> searchPag(int page, String cityId, String name)
			throws HibernateException, SQLException, ModelException, ParseException {
		Criteria select = HibernateUtil.getSession().createCriteria(Neighborhood.class);
		select.add(Restrictions.eq("cityId.cityId", Integer.parseInt(cityId)));
		if (!name.isEmpty())
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<Neighborhood>(count, (List<Neighborhood>) select.list());
	}
}