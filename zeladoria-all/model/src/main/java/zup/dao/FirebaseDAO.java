package zup.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import zup.bean.Firebase;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class FirebaseDAO extends AbstractDAO<Firebase, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public FirebaseDAO() {
		super(Firebase.class);
	}
	public Firebase findByFirebaseId(Integer FirebaseId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Firebase.class);
		select.add(Restrictions.eq("firebaseId", FirebaseId));
		Firebase t = (Firebase) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}
	
	public Firebase findByFirebaseToken(String token) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Firebase.class);
		select.add(Restrictions.eq("token", token));
		Firebase t = (Firebase) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

}