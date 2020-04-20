package dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import utils.HibernateUtil;
@SuppressWarnings("unchecked")
public abstract class AbstractDAO<T, PK extends Serializable> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	private Class<T> clazz;
	private String campoOrdenacao;

	public AbstractDAO(Class<T> persistenceClass) {
		clazz = persistenceClass;
    //    this.clazz =(Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	public T getById(PK id) throws SQLException {
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
			throws HibernateException, SQLException, ParseException {
		return select;
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
