package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import zup.bean.BroadcastMessage;
import zup.bean.BroadcastMessageCategory;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.dao.BroadcastMessageDAO;
import zup.enums.BroadcastMessageCategoryName;
import zup.enums.Enabled;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class BroadcastMessageBusiness extends AbstractBusiness<BroadcastMessage, Integer> {

	private BroadcastMessageDAO BroadcastMessageDao;

	public BroadcastMessageBusiness() {
		super(new BroadcastMessageDAO());
		this.BroadcastMessageDao = (BroadcastMessageDAO) getDao();
	}



	public Result<BroadcastMessage> searchPag2(int page, String subject, String bmc, String enabled,
			String datec, String datep)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return BroadcastMessageDao.searchPag2(page, subject, bmc, enabled, datec, datep);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	
	public List<BroadcastMessage> searchBMMobile() throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return BroadcastMessageDao.searchMobile();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new ArrayList<BroadcastMessage>();
	}

	public List<BroadcastMessage> searchBMMobile(int page) throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return BroadcastMessageDao.searchMobile(page);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new ArrayList<BroadcastMessage>();
	}
	
	public List<BroadcastMessage> searchBMMobile(int page, int category) throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return BroadcastMessageDao.searchMobile(page, category);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new ArrayList<BroadcastMessage>();
	}
	
	public List<BroadcastMessage> searchBMMobile2(List<Integer> excluded) throws HibernateException, SQLException, ModelException {

		try {
			HibernateUtil.beginTransaction();
			return BroadcastMessageDao.searchMobile2(excluded);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new ArrayList<BroadcastMessage>();
	}

	public boolean removeExpired() throws HibernateException, SQLException, ModelException {
		boolean bool = false;
		try {
			HibernateUtil.beginTransaction();
			BroadcastMessageDao.removeExpired();
			bool= true;
		} catch (Exception e) {
			e.printStackTrace();
			bool= false;
		} finally {
			HibernateUtil.closeSession();
		}
		return bool;
	}
	
	
	
	public Map<Integer, List<BroadcastMessage>> search(int page, String subject, String bmc, String enabled,
			String datec, String datep) throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return BroadcastMessageDao.search(page, subject, bmc, enabled, datec, datep);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<BroadcastMessage>>();
	}

	public Integer save2(BroadcastMessage t)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Integer id = -1;
		try {
			HibernateUtil.beginTransaction();
			BroadcastMessageDao.save(t);
			id = t.getBroadcastMessageId();
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR));
		} finally {
			HibernateUtil.closeSession();
		}
		return id;
	}

	@Override
	public BroadcastMessage getById(Integer id)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		return super.getById(id);
	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws HibernateException, SQLException, ModelException, ParseException {
		BroadcastMessageBusiness mmb = new BroadcastMessageBusiness();
BroadcastMessage bm= new BroadcastMessage(1, "subject", "sajdhaksh", Enabled.ENABLED);
bm.setCreationDate(new Date());
bm.setPublicationDate(new Date());
bm.setBroadcastMessageCategoryId(new BroadcastMessageCategory(1, BroadcastMessageCategoryName.EMERGENCIAL));
//System.out.println(bm.makeLog());
for(BroadcastMessage bm2:mmb.searchBMMobile(2))
	System.out.println(bm2.getSubject());
	}

}
