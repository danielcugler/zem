package zup.business;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import zup.bean.Firebase;
import zup.dao.FirebaseDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class FirebaseBusiness extends AbstractBusiness<Firebase, Integer> {

	private FirebaseDAO FirebaseDao;
	private static Logger logger = Logger.getLogger(Firebase.class);

	public FirebaseBusiness() {
		super(new FirebaseDAO());
		this.FirebaseDao = (FirebaseDAO) getDao();
	}
	public Firebase findByFirebaseId(Integer FirebaseId) throws SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return FirebaseDao.findByFirebaseId(FirebaseId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
		}
	public Firebase findByFirebaseToken(String token) throws SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return FirebaseDao.findByFirebaseToken(token);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
		}
	
	public static void main(String[] args) {
		FirebaseBusiness cb= new FirebaseBusiness();
			List<Firebase> cl;
			try {
				cl = cb.findAll();
				for(Firebase fire: cl)
				System.out.println(fire.getFirebaseId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
}

