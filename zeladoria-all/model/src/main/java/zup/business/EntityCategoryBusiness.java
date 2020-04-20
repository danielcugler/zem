package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.Result;
import zup.dao.EntityCategoryDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class EntityCategoryBusiness extends AbstractBusiness<EntityCategory, Integer> {
	private EntityCategoryDAO entityCategoryDao;
	private static Logger logger = Logger.getLogger(EntityCategory.class);

	public EntityCategoryBusiness() {
		super(new EntityCategoryDAO());
		this.entityCategoryDao = (EntityCategoryDAO) getDao();
	}

	public Result<EntityCategory> searchPag2(int pagina, String name, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityCategoryDao.searchPag2(pagina, name, enabledS);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	public Map<Integer, List<EntityCategory>> searchPag(int pagina, String name, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityCategoryDao.searchPag(pagina, name, enabledS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<EntityCategory>>();
	}

	public EntityCategory findByName(String name)
			throws HibernateException, SQLException, ModelException, ParseException {
		EntityCategory t = null;
		try {
			HibernateUtil.beginTransaction();
			t = entityCategoryDao.findByName(name);
			return t;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public EntityCategory getById2(Integer id) throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityCategoryDao.getById2(id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	public EntityCategory getById(Integer id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		EntityCategory t;
		try {

			HibernateUtil.beginTransaction();
			t = entityCategoryDao.getById(id);
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

	public List<EntityCategory> findByEntity(Integer id) throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityCategoryDao.findByEntity(id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public List<EntityCategory> findByEntityAndEnabled(Integer id)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityCategoryDao.findByEntityAndEnabled(id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public List<EntityCategory> findActives() throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityCategoryDao.findActives();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public static void main(String[] args) throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		EntityCategoryBusiness mmb = new EntityCategoryBusiness();
		//System.out.println(mmb.countAll());
		//System.out.println(mmb.getById(1).makeLog());
		System.out.println(mmb.findByEntity(282).get(0).getName());
	}

}
