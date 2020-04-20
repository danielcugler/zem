package zup.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.stat.Statistics;

import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;
import java.lang.reflect.ParameterizedType;
@SuppressWarnings("unchecked")
public abstract class AbstractDAO<T, PK extends Serializable> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	private Class<T> clazz;
	private String campoOrdenacao;

	public AbstractDAO(Class<T> persistenceClass) {
		clazz = persistenceClass;
    //    this.clazz =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	public T getById(PK id) throws SQLException, ModelException {
		T entity = (T) HibernateUtil.getSession().get(clazz, id);
		if (entity == null) {
			return null;
		}
		return entity;
	}



	
	
	public void persist(T t) throws SQLException {
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
		HibernateUtil.getSession().persist(t);
	}
	
	
	public void save(T t) throws SQLException {
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
		HibernateUtil.getSession().save(t).toString();
	}

	public void save2(T t) throws SQLException {
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
		HibernateUtil.getSession().save(t).toString();
	}

	public void update(T t) throws SQLException {
		HibernateUtil.getSession().update(t);
	}

	public void merge(T t) throws SQLException {
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
		HibernateUtil.getSession().merge(t);
	}

	public void merge2(T t) throws SQLException {
		HibernateUtil.getSession().merge(t);
	}

	public List<T> findAll() throws HibernateException, SQLException {
	//	Statistics statistics = HibernateUtil.getStatistics();
	  //  long antes = statistics.getEntityLoadCount();
		List<T> tList=HibernateUtil.getSession().createCriteria(this.clazz).list();
		//long depois = statistics.getEntityLoadCount();
	//	System.out.println(antes+" "+depois);
		
		return tList;
	}

	public List<T> findAll2(int page, int recordePerPage) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(this.clazz);
		select.setFirstResult(page);
		select.setMaxResults(recordePerPage);
		select.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return select.list();
	}

	public int countAll() throws HibernateException, SQLException {
		return ((Number) HibernateUtil.getSession().createCriteria(this.clazz).setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
	}

	public Criteria getCriteria(Criteria select, String[] searchParams)
			throws HibernateException, SQLException, ParseException, ModelException {
		return select;
	}

	public Map<Integer, List<T>> search1(String[] searchParams)
			throws HibernateException, SQLException, ModelException, ParseException {
		Map<Integer, List<T>> map = new HashMap<Integer, List<T>>();
		Criteria select = HibernateUtil.getSession().createCriteria(clazz);
		int page = -1;

		try {
			page = Integer.parseInt(searchParams[0]);
			select = getCriteria(select, searchParams);
		} catch (ParseException e) {
			throw e;
		}
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<T> bms = (List<T>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;

	}

	public List<T> findAllSort(String campo) throws HibernateException, SQLException {
		return HibernateUtil.getSession().createCriteria(this.clazz).addOrder(Order.asc(campo)).list();
	}

	public void delete(T t) throws SQLException {

		HibernateUtil.getSession().delete(t);

	}

	public String getCampoOrdenacao() {
		return campoOrdenacao;
	}

	public void setCampoOrdenacao(String campoOrdenacao) {
		this.campoOrdenacao = campoOrdenacao;
	}

}
