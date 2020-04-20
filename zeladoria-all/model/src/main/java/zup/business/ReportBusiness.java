package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import zup.bean.Report;
import zup.dao.ReportDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class ReportBusiness {
	ReportDAO rdao= new ReportDAO();
	
	public List<Report> findAllRelacionado(int table, Date initDate,Date endDate,Integer callClassification, Integer callSource, Integer callProgress, Integer entity, Integer category, Integer priority, int checkDate, int checkCallClassification, int checkCallSource, int checkEntity, int checkCategory, int checkCallProgress, int checkCallPriority, int checkNeighborhood)
			throws HibernateException, SQLException, ModelException, ParseException {
		List<Report> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = rdao.findAllRelacionado(table, initDate,endDate,callClassification, callSource, callProgress, entity, category, priority, checkDate, checkCallClassification, checkCallSource, checkEntity, checkCategory, checkCallProgress, checkCallPriority, checkNeighborhood);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}
	public List<Report> findAllConsolidado(int table, Date initDate,Date endDate,Integer callClassification, Integer callSource, Integer callProgress, Integer entity, Integer category, Integer priority, int checkDate, int checkCallClassification, int checkCallSource, int checkEntity, int checkCategory, int checkCallProgress, int checkCallPriority, int checkNeighborhood)
			throws HibernateException, SQLException, ModelException, ParseException {
		List<Report> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = rdao.findAllConsolidado(table,initDate,endDate,callClassification, callSource, callProgress, entity, category, priority, checkDate, checkCallClassification, checkCallSource, checkEntity, checkCategory, checkCallProgress, checkCallPriority, checkNeighborhood);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}
	/*public static void main(String[] args) throws HibernateException, SQLException, ModelException, ParseException {
		RelatorioBusiness rb= new RelatorioBusiness();
		for (Relatorio r:rb.findAll())
			System.out.println(r.toString());
	}*/
}
