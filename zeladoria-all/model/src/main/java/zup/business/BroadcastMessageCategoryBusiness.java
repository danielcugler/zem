package zup.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.BroadcastMessageCategory;
import zup.dao.BroadcastMessageCategoryDAO;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class BroadcastMessageCategoryBusiness extends AbstractBusiness<BroadcastMessageCategory, Integer> {
	private static Logger logger = Logger.getLogger(BroadcastMessageCategory.class);

	private BroadcastMessageCategoryDAO bmcDao;

	public BroadcastMessageCategoryBusiness() {
		super(new BroadcastMessageCategoryDAO());
		this.bmcDao = (BroadcastMessageCategoryDAO) getDao();
	}

	@Override
	public List<BroadcastMessageCategory> findAll() throws HibernateException, SQLException, ModelException {
		List<BroadcastMessageCategory> list = super.findAll();
		if (list.isEmpty()) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return list;
	}

	public List<BroadcastMessageCategory> findByName(String name)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return bmcDao.findByName(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new ArrayList<BroadcastMessageCategory>();
	}

}
