package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import zup.bean.Log;
import zup.bean.Result;
import zup.dao.LogDAO;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class LogBusiness extends AbstractBusiness<Log, Long> {

	private LogDAO logDao;

	public LogBusiness() {
		super(new LogDAO());
		this.logDao = (LogDAO) getDao();
	}

	public Map<Integer, List<Log>> searchPag(int page, String systemUser, String informationType, String operationType,
			String datec, String datef) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return logDao.searchPag(page, systemUser, informationType, operationType, datec, datef);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public Result<Log> searchPag2(int page, String systemUser, String informationType, String operationType,
			String datec, String datef) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return logDao.searchPag2(page, systemUser, informationType, operationType, datec, datef);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	
	public static void main(String[] args) throws HibernateException, SQLException, ParseException, ModelException {
		LogBusiness lb = new LogBusiness();
		Date date= new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2016");
		System.out.println(date.toGMTString());
		//Map<Integer,List<Log>> map=lb.searchPag(1, "z", "z", "z", "z", "z");
	}
}