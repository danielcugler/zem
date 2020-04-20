package business;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import dao.AbstractDAO;
import utils.HibernateUtil;



public class AbstractBusiness<T, PK extends Serializable> {
	private static Logger logger = Logger.getLogger(AbstractBusiness.class);
	private AbstractDAO<T, PK> dao;

	public AbstractBusiness(AbstractDAO<T, PK> dao) {
		this.dao = dao;
	}



	public boolean save(T t) throws HibernateException, SQLException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			dao.save(t);
			HibernateUtil.commitTransaction();
			success = true;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			success = false;
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}
	
	public boolean persist(T t) throws HibernateException, SQLException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			dao.persist(t);
			HibernateUtil.commitTransaction();
			success = true;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			success = false;
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}
	

	public boolean merge(T t) throws HibernateException, SQLException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			dao.merge(t);
			HibernateUtil.commitTransaction();
			success = true;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			success = false;
			throw e;
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}

	public boolean merge2(T t) throws HibernateException, SQLException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			dao.merge2(t);
			HibernateUtil.commitTransaction();
			success = true;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			success = false;
			throw e;
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}

	public boolean update(T t) throws HibernateException, SQLException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			dao.update(t);
			HibernateUtil.commitTransaction();
			success = true;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			success = false;
			throw e;
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}

	public boolean delete(T t) throws HibernateException, SQLException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			dao.delete(t);
			HibernateUtil.commitTransaction();
			success = true;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			success = false;
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return success;
	}

	public List<T> findAll() throws HibernateException, SQLException {

		List<T> tList;
		try {

			HibernateUtil.beginTransaction();
			tList = dao.findAll();
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public List<T> findAll2(int page, int recordePerPage)
			throws HibernateException, SQLException {

		List<T> tList;
		try {

			HibernateUtil.beginTransaction();
			tList = dao.findAll2(page, recordePerPage);
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public int countAll() throws HibernateException, SQLException {
		int count = 0;
		try {

			HibernateUtil.beginTransaction();
			count = dao.countAll();
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return count;
	}

	public T getById(PK id) throws HibernateException, SQLException {

		T t;
		try {

			HibernateUtil.beginTransaction();
			t = dao.getById(id);
			HibernateUtil.commitTransaction();
			return t;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
		
	public AbstractDAO<T, PK> getDao() {
		return dao;
	}

}
