package zup.business;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.dao.AbstractDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class AbstractBusiness<T, PK extends Serializable> {
	private static Logger logger = Logger.getLogger(AbstractBusiness.class);
	private AbstractDAO<T, PK> dao;

	public AbstractBusiness(AbstractDAO<T, PK> dao) {
		this.dao = dao;
	}

	public Map<Integer, List<T>> search1(String[] searchParams)
			throws HibernateException, SQLException, ModelException, ParseException {
		Map<Integer, List<T>> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = dao.search1(searchParams);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public boolean save(T t) throws HibernateException, SQLException, DataAccessLayerException {
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
	
	public boolean persist(T t) throws HibernateException, SQLException, DataAccessLayerException {
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
	

	public boolean merge(T t) throws HibernateException, SQLException, DataAccessLayerException {
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
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.UPDATE_ERROR));
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}

	public boolean merge2(T t) throws HibernateException, SQLException, DataAccessLayerException {
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
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.UPDATE_ERROR));
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}

	public boolean update(T t) throws HibernateException, SQLException, DataAccessLayerException {
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
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.UPDATE_ERROR));
		} finally {
			HibernateUtil.closeSession();

		}
		return success;
	}

	public boolean delete(T t) throws HibernateException, SQLException, DataAccessLayerException, ModelException {
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
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.UPDATE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return success;
	}

	public List<T> findAll() throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		List<T> tList;
		try {

			HibernateUtil.beginTransaction();
			tList = dao.findAll();
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public List<T> findAll2(int page, int recordePerPage)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		List<T> tList;
		try {

			HibernateUtil.beginTransaction();
			tList = dao.findAll2(page, recordePerPage);
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
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
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return count;
	}

	public T getById(PK id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {

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
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	

	

	public List<T> findAllSort(String campo)
			throws ValidationException, HibernateException, SQLException, DataAccessLayerException {

		try {

			HibernateUtil.beginTransaction();

			return dao.findAllSort(campo);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			HibernateUtil.closeSession();
		}

		return null;
	}

	public AbstractDAO<T, PK> getDao() {
		return dao;
	}

}
