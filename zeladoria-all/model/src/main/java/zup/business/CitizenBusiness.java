package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.BroadcastMessage;
import zup.bean.Citizen;
import zup.bean.Result;
import zup.dao.CitizenDAO;
import zup.enums.Enabled;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class CitizenBusiness extends AbstractBusiness<Citizen, String> {
	private CitizenDAO citizenDao;
	private static Logger logger = Logger.getLogger(Citizen.class);

	public CitizenBusiness() {
		super(new CitizenDAO());
		this.citizenDao = (CitizenDAO) getDao();
	}

	public static String getHash(String txt, String hashType) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
			byte[] array = md.digest(txt.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {

		}
		return null;
	}

	public static String md5(String txt) {
		return getHash(txt, "MD5");
	}

	public static String sha1(String txt) {
		return getHash(txt, "SHA1");
	}
	
	public Citizen getByFacebookId(String facebookId) throws SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return citizenDao.getByFacebookId(facebookId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	public Citizen findByPublicKey(String publicKey) throws SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return citizenDao.getByPublicKey(publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	public Citizen getByEmail(String email) throws SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return citizenDao.getByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public Map<Integer, List<Citizen>> searchPag(int pagina, String name, String email, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return citizenDao.searchPag(pagina, name, email, enabledS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<Citizen>>();
	}
	
	public Result<Citizen> searchPag2(int pagina, String name, String email, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return citizenDao.searchPag2(pagina, name, email, enabledS);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	

	public Citizen login(String email) throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return citizenDao.login(email);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	
	@Override
	public Citizen getById(String id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		Citizen t;
		try {

			HibernateUtil.beginTransaction();
			t = citizenDao.getById(id);
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
	
	
	public Citizen getByIdRead(String id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Citizen t;
		try {
			HibernateUtil.beginTransaction();
			t = citizenDao.getByIdRead(id);
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

	
	public List<Integer> getRead(String id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		List<Integer> t;
		try {
			HibernateUtil.beginTransaction();
			t = citizenDao.getRead(id);
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
	
	//Count citizen
	public Long countCitizen() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return citizenDao.countCitizen();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	

	public static void main(String[] args) throws HibernateException, SQLException, ModelException {
		CitizenBusiness mmb = new CitizenBusiness();
		
	/*
		Map<Integer, List<Citizen>> tMap = mmb.searchPag(2, "z", "z","z");
		//	List<UnsolvedCall> tList= sb.searchCitizenFinal("2016-06-16T00:00:00.000Z", "2016-06-15T00:00:00.000Z", "z", "z", "z", "z", "12345678913");
		List<Citizen> tList = new ArrayList<Citizen>();
		int count;
		for (Integer key : tMap.keySet()) {
			count = key;
			tList = tMap.get(key);
			break;
		}
		for(Citizen uc:tList)			
        	System.out.println(uc.getCitizen_cpf()+", "+uc.getName());
	
		Citizen c= new Citizen();
		c.setEmail("c3@gmail.com");
		c.setCitizen_cpf("60605228400");
		c.setName("Teste cidadao webuser4");
		c.setEnabled(Enabled.ENABLED);
		mmb.save(c);
	*/
		List<Integer> c= mmb.getRead("12345678913");
		System.out.println(c.toString());
	}

}
