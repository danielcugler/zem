package zup.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.MessageModel;
import zup.bean.Result;
import zup.dao.MessageModelDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class MessageModelBusiness extends AbstractBusiness<MessageModel, Integer> {
	private static Logger logger = Logger.getLogger(MessageModelBusiness.class);
	private MessageModelDAO messageModelDao;

	public MessageModelBusiness() {
		super(new MessageModelDAO());
		this.messageModelDao = (MessageModelDAO) getDao();
	}

	public MessageModel findByName(String name)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return messageModelDao.findByName(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	
	public List<MessageModel> findEnabled()
			throws HibernateException, SQLException, ModelException {		
		List<MessageModel> tList=new ArrayList<MessageModel>();
		try {
			HibernateUtil.beginTransaction();
			tList= messageModelDao.findEnabled();
		} catch (Exception e) {
			e.printStackTrace();
			} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}
	
	public List<MessageModel> getCombos()
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return messageModelDao.getCombos();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	public List<MessageModel> getAsentos(String nome)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return messageModelDao.getAsento(nome);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public Map<Integer, List<MessageModel>> searchPag(int pagina, String name, String subject, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return messageModelDao.searchPag(pagina, name, subject, enabledS);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
		
	
	public Result<MessageModel> searchPag2(int pagina, String name, String subject, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return messageModelDao.searchPag2(pagina, name, subject, enabledS);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	public static void main(String[] args) throws HibernateException, SQLException, ModelException {
		MessageModelBusiness mmb = new MessageModelBusiness();
		List<MessageModel> tList=mmb.getCombos();
		for(MessageModel m:tList)
		System.out.println(m.getName() + " : "+m.getSubject());

	}

}
