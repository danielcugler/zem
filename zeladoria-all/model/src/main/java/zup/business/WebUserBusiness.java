package zup.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.WebUser;
import zup.dao.WebUserDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class WebUserBusiness extends AbstractBusiness<WebUser, String> {
	private static Logger logger = Logger.getLogger(WebUserBusiness.class);
	private WebUserDAO WebUserDao;

	public WebUserBusiness() {
		super(new WebUserDAO());
		this.WebUserDao = (WebUserDAO) getDao();
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

	public Map<Integer, List<WebUser>> searchPag(int pagina, String name, String email, String webUserCpf)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return WebUserDao.searchPag(pagina, name, email, webUserCpf);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<WebUser>>();
	}

	public static void main(String[] args) throws HibernateException, SQLException, ModelException {
		WebUserBusiness mmb = new WebUserBusiness();
		List<WebUser> tList2 = mmb.findAll();
		List<WebUser> tList = new ArrayList<WebUser>(tList2);
		for (WebUser wu : tList) {
			WebUser novo = new WebUser();
			novo.setName(wu.getName());
			novo.setEmail(wu.getEmail());
			novo.setSolvedCallId(wu.getSolvedCallId());
			novo.setUnsolvedCallId(wu.getUnsolvedCallId());
			novo.setWebUserCpf(wu.getWebUserCpf());
			String pk = novo.getWebUserCpf() + novo.getUnsolvedCallId().getUnsolvedCallId();
			novo.setPublicIdentificationKey(md5(pk));
			System.out.println(pk);
			wu.setUnsolvedCallId(null);
			wu.setSolvedCallId(null);
			mmb.update(wu);
			if (mmb.delete(wu))
				mmb.save(novo);
		}

	}

}