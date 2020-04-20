package zup.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import zup.bean.Report3;
import zup.bean.Result;
import zup.bean.SolvedCall;
import zup.bean.UnsolvedCall;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class ReportDAO2 {
	SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

	public 	Map<Integer,String> getOrder(){
	Map<Integer,String> order=new HashMap<Integer, String>();
	order.put(0, "parentCallId.creationOrUpdateDate");
	order.put(1, "callClassification.name");
	order.put(2, "callSource");
	order.put(3, "entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId");
	order.put(4, "entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId");
	order.put(5, "callProgress");
	order.put(6, "priority");
	order.put(7, "neighborhood.name");
	return order;
	
	}
	
	public List<Report3> searchSolved(String iniDate, String endDate, String callClassification, String callSource,
			String entity, String category, String priority, List<Integer> fields)
			throws HibernateException, SQLException, ParseException {
		Map<Integer,String> order=getOrder();
		Criteria select = HibernateUtil.getSession().createCriteria(SolvedCall.class);
		select.createAlias("parentCallId", "parentCallId");
		select.createAlias("description", "description");
		select.createAlias("entityEntityCategoryMaps", "entityEntityCategoryMaps");
		select.createAlias("callClassificationId", "callClassification");
		select.createAlias("addressId", "addressId");
		select.createAlias("addressId.neighborhoodId", "neighborhood");
		if (!iniDate.isEmpty())
			select.add(Restrictions.ge("parentCallId.creationOrUpdateDate", f.parse(iniDate)));
		if (!endDate.isEmpty()){
			GregorianCalendar finalDate = new GregorianCalendar();
			finalDate.setTime(f.parse(endDate));
			finalDate.add(GregorianCalendar.HOUR_OF_DAY, 23);
			finalDate.add(GregorianCalendar.MINUTE, 59);
			select.add(Restrictions.le("parentCallId.creationOrUpdateDate", finalDate.getTime()));
		}
		// select.setFetchMode("parentCallId", FetchMode.JOIN);
		Query query = HibernateUtil.getSession().getNamedQuery("SolvedCall.findAllMon");
		List<Long> tList = query.list();
		select.add(Restrictions.in("solvedCallId", tList));
		if (!callSource.isEmpty())
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		if (!callClassification.isEmpty())
			select.add(
					Restrictions.eq("callClassificationId.callClassificationId", Integer.parseInt(callClassification)));
		if (!entity.isEmpty()) {
			select.add(Restrictions.eq("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId", Integer.parseInt(entity)));
		}
		if (!category.isEmpty()) {
			select.add(Restrictions.eq("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId",
					Integer.parseInt(category)));
		}
		if (!priority.isEmpty())
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
//		if (!callSource.isEmpty())
//			select.add(Restrictions.eq("callStatus", CallSource.fromValue(Integer.parseInt(callSource))));
		for (Integer field : fields)
			select.addOrder(Order.asc(order.get(field)));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.setProjection(Projections.projectionList()
				.add(Projections.property("parentCallId.creationOrUpdateDate"), "creationOrUpdateDate")
				.add(Projections.property("callClassification.name"), "callClassification")
				.add(Projections.property("description.information"), "description")
				.add(Projections.property("callSource"), "callSource")
				.add(Projections.property("callProgress"), "callProgress")
				.add(Projections.property("priority"), "priority")
				.add(Projections.property("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId"), "entityId")
				.add(Projections.property("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId"), "entityCategoryId")
				.add(Projections.property("neighborhood.name"), "neighborhood"));
		select.setResultTransformer(Transformers.aliasToBean(Report3.class));		
		return select.list();
	}

	public List<Report3> searchUnsolved(String iniDate, String endDate, String callClassification, String callSource,
			String callProgress, String entity, String category, String priority, List<Integer> fields)
			throws HibernateException, SQLException, ParseException {
		Map<Integer,String> order=getOrder();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.createAlias("parentCallId", "parentCallId");
		select.createAlias("description", "description");
		select.createAlias("entityEntityCategoryMaps", "entityEntityCategoryMaps");
		select.createAlias("callClassificationId", "callClassification");
		select.createAlias("addressId", "addressId");
		select.createAlias("addressId.neighborhoodId", "neighborhood");	
		
		if (!iniDate.isEmpty())
			select.add(Restrictions.ge("parentCallId.creationOrUpdateDate", f.parse(iniDate)));
			
		if (!endDate.isEmpty()){
			GregorianCalendar finalDate = new GregorianCalendar();
			finalDate.setTime(f.parse(endDate));
			finalDate.add(GregorianCalendar.HOUR_OF_DAY, 23);
			finalDate.add(GregorianCalendar.MINUTE, 59);
			select.add(Restrictions.le("parentCallId.creationOrUpdateDate", finalDate.getTime()));
		}
			
		//select.add(Restrictions.le("parentCallId.creationOrUpdateDate", f.parse(endDate)));
		// select.setFetchMode("parentCallId", FetchMode.JOIN);
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllMon");
		List<Long> tList = query.list();
		select.add(Restrictions.in("unsolvedCallId", tList));
		if (!callSource.isEmpty())
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		if (!callClassification.isEmpty())
			select.add(
					Restrictions.eq("callClassificationId.callClassificationId", Integer.parseInt(callClassification)));
		if (!entity.isEmpty()) {
			select.add(Restrictions.eq("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId", Integer.parseInt(entity)));
		}
		if (!category.isEmpty()) {
			select.add(Restrictions.eq("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId",
					Integer.parseInt(category)));
		}
		if (!priority.isEmpty())
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
//		if (!callSource.isEmpty())
//			select.add(Restrictions.eq("callStatus", CallSource.fromValue(Integer.parseInt(callSource))));
		if (!callProgress.isEmpty())
			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
		for (Integer field : fields)
		select.addOrder(Order.asc(order.get(field)));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.setProjection(Projections.projectionList()
				.add(Projections.property("parentCallId.creationOrUpdateDate"), "creationOrUpdateDate")
				.add(Projections.property("callClassification.name"), "callClassification")
				.add(Projections.property("description.information"), "description")
				.add(Projections.property("callSource"), "callSource")
				.add(Projections.property("callProgress"), "callProgress")
				.add(Projections.property("priority"), "priority")
				.add(Projections.property("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId"), "entityId")
				.add(Projections.property("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId"), "entityCategoryId")
				//.add(Projections.property("entity.name"), "entity")
				//.add(Projections.property("entityCategory.name"), "entityCategory")
				.add(Projections.property("neighborhood.name"), "neighborhood"));
		select.setResultTransformer(Transformers.aliasToBean(Report3.class));	
		return select.list(); 
	}

}
