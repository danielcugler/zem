package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.HibernateException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import zup.bean.Report3;
import zup.dao.ReportDAO2;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class ReportBusiness2 {
	ReportDAO2 rdao= new ReportDAO2();
	
	public List<Report3> searchUnsolved(String iniDate,String endDate,String callClassification, String callSource, String callProgress, String entity, String category, String priority, List<Integer> fields)
			throws HibernateException, SQLException, ModelException, ParseException {
		List<Report3> tList= new ArrayList<Report3>();
		try {
			HibernateUtil.beginTransaction();
			tList.addAll(rdao.searchUnsolved(iniDate, endDate, callClassification, callSource, callProgress, entity, category, priority, fields));
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

	public List<Report3> searchAll(String iniDate,String endDate,String callClassification, String callSource, String callProgress, String entity, String category, String priority, List<Integer> fields)
			throws HibernateException, SQLException, ModelException, ParseException {

		ArrayList<Report3> list=new ArrayList<Report3>();
		try {
			HibernateUtil.beginTransaction();
			list.addAll(rdao.searchUnsolved(iniDate, endDate, callClassification, callSource, callProgress, entity, category, priority, fields));
			list.addAll(rdao.searchSolved(iniDate, endDate, callClassification, callSource, entity, category, priority, fields));
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		
		Collections.sort(list);
		
		return list;
	}
	
	
	public List<Report3> searchSolved(String iniDate,String endDate,String callClassification, String callSource, String entity, String category, String priority, List<Integer> fields)
			throws HibernateException, SQLException, ModelException, ParseException {
		List<Report3> tList = new ArrayList<Report3>();
		try {
			HibernateUtil.beginTransaction();
			tList.addAll(rdao.searchSolved(iniDate, endDate, callClassification, callSource, entity, category, priority, fields));
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
	public static void main(String[] args) {
		ReportBusiness2 rb = new ReportBusiness2();
		List<Integer> check= new ArrayList<Integer>();
		check.add(0);
		try {
			
			
			List<Report3> rep= rb.searchUnsolved("", "", "", "", "", "", "","", check);
	//	List<UnsolvedCall> tList = rb.teste(Long.getLong("1000"));
			ObjectMapper mapper = new ObjectMapper();
			for(Report3 r:rep){
			String json= mapper.writeValueAsString(r);
				System.out.println(json);
			}
			
			
			List<Report3> rep1= rb.searchSolved("", "", "", "",  "", "","", check);
	//	List<UnsolvedCall> tList = rb.teste(Long.getLong("1000"));
			ObjectMapper mapper1 = new ObjectMapper();
			for(Report3 r:rep1){
			String json= mapper1.writeValueAsString(r);
				System.out.println(json);
			}
			
			
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
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
