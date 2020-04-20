package zup.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.sql.Update;

import zup.bean.Entity;
import zup.bean.UnreadCall;
import zup.bean.UnreadCall;
import zup.bean.UnsolvedCall;
import zup.dao.UnreadCallDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class UnreadCallBusiness extends AbstractBusiness<UnreadCall, Long> {

	private UnreadCallDAO unreadCallDao;
	private static Logger logger = Logger.getLogger(UnreadCall.class);

	public UnreadCallBusiness() {
		super(new UnreadCallDAO());
		this.unreadCallDao = (UnreadCallDAO) getDao();
	}
	@Override
	public UnreadCall getById(Long id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		UnreadCall t;
		try {

			HibernateUtil.beginTransaction();
			t = unreadCallDao.getById(id);
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
	
	public boolean saveOrUpdate(UnreadCall unreadCall) throws HibernateException, SQLException, DataAccessLayerException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			unreadCallDao.saveOrUpdate(unreadCall);
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
	
	public List<UnreadCall> findRead()
			throws HibernateException, SQLException, ModelException {		
		List<UnreadCall> tList=new ArrayList<UnreadCall>();
		try {
			HibernateUtil.beginTransaction();
			tList= unreadCallDao.findRead();
		} catch (Exception e) {
			e.printStackTrace();
			} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}
	public List<UnreadCall> findList(List<Long> calls)
			throws HibernateException, SQLException, ModelException {		
		List<UnreadCall> tList=new ArrayList<UnreadCall>();
		try {
			HibernateUtil.beginTransaction();
			tList= unreadCallDao.findList(calls);
		} catch (Exception e) {
			e.printStackTrace();
			} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}
	public List<UnreadCall> findUnread()
			throws HibernateException, SQLException, ModelException {		
		List<UnreadCall> tList=new ArrayList<UnreadCall>();
		try {
			HibernateUtil.beginTransaction();
			tList= unreadCallDao.findUnread();
		} catch (Exception e) {
			e.printStackTrace();
			} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public UnreadCall findByUnsolvedcallId(Long unsolvedcallId)
			throws HibernateException, SQLException, ModelException {		
		UnreadCall t = null;
		try {
			HibernateUtil.beginTransaction();
			t= unreadCallDao.findByUnsolvedcallId(unsolvedcallId);
		} catch (Exception e) {
			e.printStackTrace();
			} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}
	
	public UnreadCall findBySolvedcallId(Long solvedcallId)
			throws HibernateException, SQLException, ModelException {		
		UnreadCall t = null;
		try {
			HibernateUtil.beginTransaction();
			t= unreadCallDao.findBySolvedcallId(solvedcallId);
		} catch (Exception e) {
			e.printStackTrace();
			} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}
		

public static void save(int id){
	UnreadCallBusiness ub = new UnreadCallBusiness();
	UnsolvedCallBusiness ucb = new UnsolvedCallBusiness();
	try {
		UnsolvedCall uns = ucb.getById(Long.valueOf(id));
		UnreadCall unread= new UnreadCall();
		unread.setUnsolvedCallId(uns);
		unread.setRead(false);
		ub.save(unread);
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DataAccessLayerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ModelException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void update(int id){
	UnreadCallBusiness ub = new UnreadCallBusiness();
	try {
		UnreadCall unread= ub.getById(Long.valueOf(id));
		unread.setRead(false);
		ub.merge(unread);
	} catch (HibernateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DataAccessLayerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ModelException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void main(String[] args) throws HibernateException, SQLException, ModelException {
	UnreadCallBusiness ub = new UnreadCallBusiness();
	System.out.println(ub.findByUnsolvedcallId(Long.valueOf(337)));
	//update(2);
	
	}
}
