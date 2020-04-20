package zup.business;

import java.sql.SQLException;
import java.text.ParseException;

import org.hibernate.HibernateException;

import zup.dao.ExpiredDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class ExpiredBusiness {
	public void delAuto()
			throws HibernateException, SQLException, ModelException, ParseException {
		ExpiredDAO dao = new ExpiredDAO();
		try {
			HibernateUtil.beginTransaction();
			dao.delAuto();
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
	}
	public static void main(String[] args) {
		ExpiredBusiness eb= new ExpiredBusiness();
		try {
			eb.delAuto();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
