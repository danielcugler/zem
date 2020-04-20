package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import zup.bean.AttendanceTime;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.EntityEntityCategoryMapsPK;
import zup.dao.EntityEntityCategoryMapsDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class EntityEntityCategoryMapsBusiness
		extends AbstractBusiness<EntityEntityCategoryMaps, EntityEntityCategoryMapsPK> {
	private EntityEntityCategoryMapsDAO entityEntityCategoryMapsDao;
	private static Logger logger = Logger.getLogger(EntityEntityCategoryMaps.class);

	public EntityEntityCategoryMapsBusiness() {
		super(new EntityEntityCategoryMapsDAO());
		this.entityEntityCategoryMapsDao = (EntityEntityCategoryMapsDAO) getDao();
	}

	@Override
	public EntityEntityCategoryMaps getById(EntityEntityCategoryMapsPK id)
			throws HibernateException, SQLException, ModelException {
		EntityEntityCategoryMaps t = null;
		try {
			HibernateUtil.beginTransaction();
			t = entityEntityCategoryMapsDao.getById(id);
			return t;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public EntityEntityCategoryMaps getByEEC(Integer entityCategoryId, Integer entityId)
			throws SQLException, ModelException {
		EntityEntityCategoryMaps t = null;
		try {
			HibernateUtil.beginTransaction();
			t = entityEntityCategoryMapsDao.getByEEC(entityCategoryId, entityId);
			return t;
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public EntityEntityCategoryMaps getByIdUns(Integer entityCategoryId, Integer entityId) throws SQLException, ModelException {
		EntityEntityCategoryMaps t = null;
		try {
			HibernateUtil.beginTransaction();
			t = entityEntityCategoryMapsDao.getByIdUns(entityCategoryId, entityId);
			return t;
		} catch (ModelException me) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}
	

	public List<EntityCategory> getByIdEC(Integer entityId) throws SQLException, ModelException {
		List<EntityCategory> tList = null;
		try {
			HibernateUtil.beginTransaction();
			tList = entityEntityCategoryMapsDao.getByIdEC( entityId);
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
	
	public List<Entity> getByEntity() throws SQLException, ModelException {
		List<Entity> tList = null;
		try {
			HibernateUtil.beginTransaction();
			tList = entityEntityCategoryMapsDao.getByEntity();
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
	
	
	
	public static void main(String[] args) throws HibernateException, DataAccessLayerException, SQLException, ModelException {
		EntityEntityCategoryMapsBusiness eemb = new EntityEntityCategoryMapsBusiness();
		EntityEntityCategoryMaps en=eemb.getById(new EntityEntityCategoryMapsPK(2,105));

			System.out.println(en.getEntityEntityCategoryMapsPK().getEntityCategoryId());
	}
	
}
