package zup.dao;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;

import zup.bean.Report;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;
import zup.model.utils.HibernateUtil;

public class ReportDAO {
	DateFormat df = new SimpleDateFormat("dd/MM/YYYY - hh:mm:ss");

	public String getSelectRelacionado(int checkDate, int checkCallClassification, int checkCallSource,
			int checkEntity, int checkCategory, int checkCallProgress, int checkCallPriority, int checkNeighborhood) {
		String select = "";
		List<String> selectArray = new ArrayList<String>();

			if (checkDate == 1)
				selectArray.add("call.creation_or_update_date");
			if (checkCallClassification == 1)
				selectArray.add("cc.name as cname");
			if (checkCallSource == 1)
				selectArray.add("call.call_source");
			if (checkEntity == 1)
				selectArray.add("en.name as ename");
			if (checkCategory == 1)
				selectArray.add("ec.name as ecname");
			if (checkCallProgress == 1)
				selectArray.add("call.call_progress");
			if (checkCallPriority == 1)
				selectArray.add("call.priority");
			if (checkNeighborhood == 1)
				selectArray.add("nb.neighborhood_name");
	
		if (selectArray.size() == 0)
			return "";
		if (selectArray.size() == 1)
			select += selectArray.get(0);
		else {
			for (String s : selectArray)
				select += s + ",";
			select = select.substring(0, select.length() - 1);
		}
		System.out.println(select);
		return select;
	}

	public String getSelectConsolidado(int checkDate, int checkCallClassification, int checkCallSource,
			int checkEntity, int checkCategory, int checkCallProgress, int checkCallPriority, int checkNeighborhood) {
		String select = "";
		List<String> selectArray = new ArrayList<String>();

			if (checkDate == 1)
				selectArray.add("call.creation_or_update_date");
			if (checkCallClassification == 1)
				selectArray.add("cc.name as cname");
			if (checkCallSource == 1)
				selectArray.add("call.call_source");
			if (checkEntity == 1)
				selectArray.add("en.name as ename");
			if (checkCategory == 1)
				selectArray.add("ec.name as ecname");
			if (checkCallProgress == 1)
				selectArray.add("call.call_progress");
			if (checkCallPriority == 1)
				selectArray.add("call.priority");
			if (checkNeighborhood == 1)
				selectArray.add("nb.neighborhood_name");

		selectArray.add("count(*) as count");
		if (selectArray.size() == 0)
			return "";
		if (selectArray.size() == 1)
			select += selectArray.get(0);
		else {
			for (String s : selectArray)
				select += s + ",";
			select = select.substring(0, select.length() - 1);
		}
		System.out.println(select);
		return select;
	}

	public String getGroupByConsolidado(int checkDate, int checkCallClassification, int checkCallSource,
			int checkEntity, int checkCategory, int checkCallProgress, int checkCallPriority, int checkNeighborhood) {
		String select = " GROUP BY ";
		List<String> selectArray = new ArrayList<String>();

			if (checkDate == 1)
				selectArray.add("call.creation_or_update_date");
			if (checkCallClassification == 1)
				selectArray.add("cc.name");
			if (checkCallSource == 1)
				selectArray.add("call.call_source");
			if (checkEntity == 1)
				selectArray.add("en.name");
			if (checkCategory == 1)
				selectArray.add("ec.name");
			if (checkCallProgress == 1)
				selectArray.add("call.call_progress");
			if (checkCallPriority == 1)
				selectArray.add("call.priority");
			if (checkNeighborhood == 1)
				selectArray.add("nb.neighborhood_name");
		
		if (selectArray.size() == 0)
			return "";
		if (selectArray.size() == 1)
			select += selectArray.get(0);
		else {
			for (String s : selectArray)
				select += s + ",";
			select = select.substring(0, select.length() - 1);
		}
		System.out.println(select);
		return select;
	}

	public String getWhere(Date initDate, Date endDate, Integer cId, Integer source, Integer eId,
			Integer ecId, Integer progress, Integer priorityI) {
		String where = "";
		// String creation_or_update_date=true;
		List<String> whereArray = new ArrayList<String>();

			if (initDate != null)
				whereArray.add(" AND call.creation_or_update_date > :initDate");
			if (endDate != null)
				whereArray.add(" AND call.creation_or_update_date < :endDate");
			if (cId >= 0)
				whereArray.add(" AND call.call_classification_id = :cId");
			if (source >= 0)
				whereArray.add(" AND call.call_source = :source");
			if (eId >= 0)
				whereArray.add(" AND call.entity_id = :eId");
			if (ecId >= 0)
				whereArray.add(" AND call.entity_category_id = :ecId");
			if (progress >= 0)
				whereArray.add(" AND call.call_progress = :progress");
			if (priorityI >= 0)
				whereArray.add(" AND call.priority = :priorityI");

		if (whereArray.size() == 0)
			return "";
		for (String s : whereArray)
			where += s;
		System.out.println(where);
		return where;
	}

	public List<Report> findAllRelacionado(int table, Date initDate, Date endDate, Integer callClassification,
			Integer callSource, Integer callProgress, Integer entity, Integer category, Integer priority, int checkDate,
			int checkCallClassification, int checkCallSource, int checkEntity, int checkCategory, int checkCallProgress,
			int checkCallPriority, int checkNeighborhood) throws HibernateException, SQLException {

		String select = getSelectRelacionado(checkDate, checkCallClassification, checkCallSource, checkEntity,
				checkCategory, checkCallProgress, checkCallPriority, checkNeighborhood);

		Integer cId = callClassification;
		Integer source = callSource;
		Integer progress = callProgress;
		Integer eId = entity;
		Integer ecId = category;
		Integer priorityI = priority;
		String where = getWhere(initDate, endDate, cId, source, eId, ecId, progress, priorityI);
		String selectInicial = "";

		if (table == 3) {// finalizado (solved_call)
			selectInicial = "SELECT ai.information as cdescription," + select
					+ " FROM solved_call call, entity en, entity_category ec, neighborhood nb, call_classification cc, address ad, additional_information ai WHERE call.address_id = ad.address_id AND ad.neighborhood_id = nb.neighborhood_id AND call.call_classification_id= cc.call_classification_id AND call.entity_id = en.entity_id AND ai.additional_information_id = call.description AND call.entity_category_id = ec.entity_category_id"
					+ where + " ORDER BY ";
		} else {
			if (table == 0) {// andamento não é finalizado e nem todas.
				selectInicial = "SELECT ai.information as cdescription," + select
						+ " FROM unsolved_call call, entity en, entity_category ec, neighborhood nb, call_classification cc, address ad, additional_information ai WHERE call.address_id = ad.address_id AND ad.neighborhood_id = nb.neighborhood_id AND call.call_classification_id= cc.call_classification_id AND call.entity_id = en.entity_id AND ai.additional_information_id = call.description AND call.entity_category_id = ec.entity_category_id"
						+ where + " ORDER BY ";
			}
		}
		// ORDER BY
		if (checkDate == 1)
			selectInicial += "creation_or_update_date asc";
		else if (checkCallClassification == 1)
			selectInicial += "cname asc";
		else if (checkCallSource == 1)
			selectInicial += "call_source asc";
		else if (checkEntity == 1)
			selectInicial += "ename asc";
		else if (checkCategory == 1)
			selectInicial += "ecname asc";
		else if (checkCallProgress == 1)
			selectInicial += "call_progress asc";
		else if (checkCallPriority == 1)
			selectInicial += "priority asc";
		else if (checkNeighborhood == 1)
			selectInicial += "neighborhood_name asc";

		// String selectInicial="SELECT "+select+" FROM unsolved_call uc, entity
		// en, entity_category ec, neighborhood nb, call_classification cc,
		// address ad WHERE uc.address_id = ad.address_id AND ad.neighborhood_id
		// = nb.neighborhood_id AND uc.call_classification_id=
		// cc.call_classification_id AND uc.entity_id = en.entity_id AND
		// uc.entity_category_id = ec.entity_category_id"+where;
		Query query = HibernateUtil.getSession().createSQLQuery(selectInicial);
		if (initDate != null)
			query.setParameter("initDate", new java.sql.Date(initDate.getTime()));
		if (endDate != null)
			query.setParameter("endDate", new java.sql.Date(endDate.getTime()));
		if (cId >= 0)
			query.setParameter("cId", cId);
		if (source >= 0)
			query.setParameter("source", source);
		if (eId >= 0)
			query.setParameter("eId", eId);
		if (ecId >= 0)
			query.setParameter("ecId", ecId);
		if (progress >= 0)
			query.setParameter("progress", progress);
		if (priorityI >= 0)
			query.setParameter("priorityI", priorityI);

		// query.setResultTransformer(new MyResultTransformer());
		query.setResultTransformer(Transformers.aliasToBean(Report.class));
		List<Report> rList = (List<Report>) query.list();
		System.out.println(query);
		return rList;
	}

	public List<Report> findAllConsolidado(int table, Date initDate, Date endDate, Integer callClassification,
			Integer callSource, Integer callProgress, Integer entity, Integer category, Integer priority, int checkDate,
			int checkCallClassification, int checkCallSource, int checkEntity, int checkCategory, int checkCallProgress,
			int checkCallPriority, int checkNeighborhood) throws HibernateException, SQLException {

		String select = getSelectConsolidado(checkDate, checkCallClassification, checkCallSource, checkEntity,
				checkCategory, checkCallProgress, checkCallPriority, checkNeighborhood);
		String groupby = getGroupByConsolidado(checkDate, checkCallClassification, checkCallSource, checkEntity,
				checkCategory, checkCallProgress, checkCallPriority, checkNeighborhood);
		String selectInicial = "";

		Integer cId = callClassification;
		Integer source = callSource;
		Integer progress = callProgress;
		Integer eId = entity;
		Integer ecId = category;
		Integer priorityI = priority;
		String where = getWhere(initDate, endDate, cId, source, eId, ecId, progress, priorityI);

		if (table == 3) {// finalizado (solved_call)
			selectInicial = "SELECT " + select
					+ " FROM solved_call call, entity en, entity_category ec, neighborhood nb, call_classification cc, address ad, additional_information ai WHERE call.address_id = ad.address_id AND ad.neighborhood_id = nb.neighborhood_id AND call.call_classification_id= cc.call_classification_id AND call.entity_id = en.entity_id AND ai.additional_information_id = call.description AND call.entity_category_id = ec.entity_category_id"
					+ where + groupby + " ORDER BY ";
		} else {
			if (table == 0) {// andamento não é finalizado e nem todas.
				selectInicial = "SELECT " + select
						+ " FROM unsolved_call call, entity en, entity_category ec, neighborhood nb, call_classification cc, address ad, additional_information ai WHERE call.address_id = ad.address_id AND ad.neighborhood_id = nb.neighborhood_id AND call.call_classification_id= cc.call_classification_id AND call.entity_id = en.entity_id AND ai.additional_information_id = call.description AND call.entity_category_id = ec.entity_category_id"
						+ where + groupby + " ORDER BY ";
			}
		}

		// ORDER BY
		if (checkDate == 1)
			selectInicial += "creation_or_update_date asc";
		else if (checkCallClassification == 1)
			selectInicial += "cname asc";
		else if (checkCallSource == 1)
			selectInicial += "call_source asc";
		else if (checkEntity == 1)
			selectInicial += "ename asc";
		else if (checkCategory == 1)
			selectInicial += "ecname asc";
		else if (checkCallProgress == 1)
			selectInicial += "call_progress asc";
		else if (checkCallPriority == 1)
			selectInicial += "priority asc";
		else if (checkNeighborhood == 1)
			selectInicial += "neighborhood_name asc";

		Query query = HibernateUtil.getSession().createSQLQuery(selectInicial);
		if (initDate != null)
			query.setParameter("initDate", new java.sql.Date(initDate.getTime()));
		if (endDate != null)
			query.setParameter("endDate", new java.sql.Date(endDate.getTime()));
		if (cId >= 0)
			query.setParameter("cId", cId);
		if (source >= 0)
			query.setParameter("source", source);
		if (eId >= 0)
			query.setParameter("eId", eId);
		if (ecId >= 0)
			query.setParameter("ecId", ecId);
		if (progress >= 0)
			query.setParameter("progress", progress);
		if (priorityI >= 0)
			query.setParameter("priorityI", priorityI);
		// query.setResultTransformer(new MyResultTransformer());

		query.setResultTransformer(Transformers.aliasToBean(Report.class));
		List<Report> rList = (List<Report>) query.list();
		System.out.println(query);
		return rList;
	}
}
