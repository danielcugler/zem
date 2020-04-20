package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.Log;
import zup.bean.Result;
import zup.bean.SystemUserProfile;
import zup.dao.EntityDAO;
import zup.enums.Enabled;
import zup.enums.OperationType;
import zup.enums.SendMessage;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class EntityBusiness extends AbstractBusiness<Entity, Integer> {

	private EntityDAO entityDao;
	private static Logger logger = Logger.getLogger(Entity.class);

	public EntityBusiness() {
		super(new EntityDAO());
		this.entityDao = (EntityDAO) getDao();
	}
	
	
	public Result<Entity> searchPag2(int pagina, String name, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityDao.searchPag2(pagina, name, enabledS);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	
	public List<EntityCategory> getByIdECLock(Integer entityId) throws SQLException, ModelException {
		List<EntityCategory> tList = null;
		try {
			HibernateUtil.beginTransaction();
			tList = entityDao.getByIdECLock( entityId);
			return tList;
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}
	
	
	@Override
	public boolean merge(Entity t) throws HibernateException, SQLException, DataAccessLayerException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			Hibernate.initialize(t.getEntityCategoryCollection());
			entityDao.merge(t);
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
	

	
	public boolean saveEnt(Entity t) throws HibernateException, SQLException, DataAccessLayerException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			Hibernate.initialize(t.getEntityCategoryCollection());
			entityDao.saveEnt(t);
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
	
	public Entity getById(Integer id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		Entity t;
		try {

			HibernateUtil.beginTransaction();
			t = entityDao.getById(id);
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
	
	public Entity findById(Integer id) throws HibernateException, SQLException, ModelException, ParseException {
		Entity t = null;
		try {
			HibernateUtil.beginTransaction();
			t = entityDao.findById(id);
			return t;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public Entity findByName(String name) throws HibernateException, SQLException, ModelException, ParseException {
		Entity t = null;
		try {
			HibernateUtil.beginTransaction();
			t = entityDao.findByName(name);
			return t;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public Map<Integer, List<Entity>> searchPag(int pagina, String name, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityDao.searchPag(pagina, name, enabledS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<Entity>>();
	}

	public Map<Integer, List<Entity>> searchPagAT(int pagina, String name, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityDao.searchPagAT(pagina, name, enabledS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<Entity>>();
	}

	public List<Entity> findActives() throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return entityDao.findActives();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public static void main(String[] args) throws HibernateException, SQLException, ModelException {

		EntityBusiness eb = new EntityBusiness();
	/*
		Entity e1 = new Entity(66, "uiuiui", Enabled.ENABLED);
		EntityCategory ec1 = new EntityCategory("Zeladoria2142113", Enabled.ENABLED, SendMessage.ENABLED);
		Entity e2 = new Entity("Zeladoria11121234", Enabled.ENABLED);
		EntityCategory ec2 = new EntityCategory("Zeladoria3211314", Enabled.ENABLED, SendMessage.ENABLED);
		EntityCategory ec3 = new EntityCategory("Zeladoria4112314", Enabled.ENABLED, SendMessage.ENABLED);
		
		System.out.println(eb.getById(1).makeLog());
*/
		//eb.getByIdECLock(284);
		List<EntityCategory> list=eb.getByIdECLock(284);
		list.forEach(System.out::println);
		for(EntityCategory eem:list)
			System.out.println(eem.getName());
	}
}
