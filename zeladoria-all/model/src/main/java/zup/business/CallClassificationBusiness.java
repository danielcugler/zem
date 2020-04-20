package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.CallClassification;
import zup.bean.Result;
import zup.dao.CallClassificationDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class CallClassificationBusiness extends AbstractBusiness<CallClassification, Integer> {
	private static Logger logger = Logger.getLogger(CallClassification.class);

	private CallClassificationDAO dao;

	public CallClassificationBusiness() {
		super(new CallClassificationDAO());
		this.dao = (CallClassificationDAO) getDao();
	}

	public Result<CallClassification> search(int page, String name)
			throws HibernateException, SQLException, ModelException, ParseException {
		Result<CallClassification> result= null;
		try {
			HibernateUtil.beginTransaction();
			result = dao.search(page,name);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
	return result;
	}

}
