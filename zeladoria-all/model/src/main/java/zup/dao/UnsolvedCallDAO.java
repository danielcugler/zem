package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;

import zup.bean.CallClassification;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.SolvedCall;
import zup.bean.UnsolvedCall;
import zup.enums.CallClassificationName;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.CallStatus;
import zup.enums.Priority;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class UnsolvedCallDAO extends AbstractDAO<UnsolvedCall, Long> {
	private static Logger logger = Logger.getLogger(UnsolvedCallDAO.class);

	public UnsolvedCallDAO() {
		super(UnsolvedCall.class);
	}

	public List<UnsolvedCall> history(Long id) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.add(Restrictions.eq("parentCallId.unsolvedCallId", id));
		select.addOrder(Order.asc("unsolvedCallId"));
		List<UnsolvedCall> children = select.list();

		if (children.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return children;
	}

	public List<Long> newFind() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCall");
		List<Long> tList = query.list();
		if (tList.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return tList;
	}
	
	public List<Long> findAllLastChild() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllLastChild");
		List<Long> tList = query.list();
		return tList;
	}
		
	public boolean evaluation(String eval) throws SQLException, ModelException {
		System.out.println(eval);
		return true;
	}
	
	public Map<Integer, List<UnsolvedCall>> searchCitizen2(String periodod, String periodoa, 
		 String entityid, String entityCategoryId,String callProgress,String description, String citizenCpf)
					throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();

		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllLastChild");
		List<Long> tList = query.list();
		if (tList.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
		select.createAlias("parentCallId", "parent");
		if (periodod == null && periodoa == null 
				&& entityid.equals("z") && description.equals("z") &&  callProgress.equals("z")) {
			select.add(Restrictions.in("unsolvedCallId", tList));
			select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
			select.addOrder(Order.desc("creationOrUpdateDate"));
						List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(1, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (periodod != null) {
			try {
				periodod2 = new SimpleDateFormat("yyyy-MM-dd").parse(periodod);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.le("parent.creationOrUpdateDate", periodod2));
		}
		if (periodoa != null) {
			try {
				periodoa2 = new SimpleDateFormat("yyyy-MM-dd").parse(periodoa);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.ge("parent.creationOrUpdateDate", periodoa2));
		}
		if (!description.equals("z")) {
			select.createAlias("description", "desc");
			select.add(Restrictions.ilike("desc.information", "%" + description + "%"));
		}

		if (!entityid.equals("z")){
			select.createAlias("entityEntityCategoryMaps.entity", "ent");
			select.add(Restrictions.eq("ent.entityId", Integer.parseInt(entityid)));
		}
			if (!entityCategoryId.equals("z")){
				select.createAlias("entityEntityCategoryMaps.entityCategory", "entc");
				select.add(Restrictions.eq("entc.entityCategoryId", Integer.parseInt(entityCategoryId)));
			}
		if (!callProgress.equals("z"))
			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
		if (!citizenCpf.equals("z"))
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));

		select.addOrder(Order.desc("creationOrUpdateDate"));
		select.add(Restrictions.in("unsolvedCallId", tList));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		map.put(1, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}
	
	
	
	public Map<Integer, List<UnsolvedCall>> searchCitizen(String cpf) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		List<Long> llist=findAllLastChild();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.add(Restrictions.in("unsolvedCallId", llist));
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", cpf));
			select.createAlias("parentCallId", "parentdate");
			select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));			
			select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);			
			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
	}
	
	
	
	public List<UnsolvedCall> searchCitizenFinal(String periodod, String periodoa, 
			 String entityid, String entityCategoryId,String callProgress,String description, String citizenCpf) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		List<Long> llist=findAllLastChild();
		System.out.println(llist.toString());
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.createAlias("parentCallId", "parent");	
		select.add(Restrictions.in("unsolvedCallId", llist));
		select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));							
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.addOrder(Order.desc("parent.creationOrUpdateDate"));
		System.out.println((periodod == "") +", "+ (periodoa == "")  +", "+ entityid.equals("z") +", "+ description.equals("z") +", "+  callProgress.equals("z"));
		if (periodod.equals("") && periodoa.equals("")  && entityid.equals("z") && description.equals("z") &&  callProgress.equals("z")) {
				return (List<UnsolvedCall>) select.list();
			}
			if (!periodod.equals("")) {
				try {
					Date pd= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(periodod);
					pd.setHours(23);
					pd.setMinutes(59);
					pd.setSeconds(59);
					select.add(Restrictions.le("parent.creationOrUpdateDate", pd));
					} catch (ParseException pe) {
						throw pe;
					}
			}
			if (!periodoa.equals("")) {
				try {
					Date pa= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(periodoa);
					pa.setHours(00);
					pa.setMinutes(00);
					pa.setSeconds(00);
					select.add(Restrictions.ge("parent.creationOrUpdateDate", pa));					
				} catch (ParseException pe) {
					throw pe;
				}				
			}
			if (!description.equals("z")) {
				select.createAlias("description", "desc");
				select.add(Restrictions.ilike("desc.information", "%" + description + "%"));
			}

			if (!entityid.equals("z")){
				select.createAlias("entityEntityCategoryMaps.entity", "ent");
				select.add(Restrictions.eq("ent.entityId", Integer.parseInt(entityid)));
			}
				if (!entityCategoryId.equals("z")){
					select.createAlias("entityEntityCategoryMaps.entityCategory", "entc");
					select.add(Restrictions.eq("entc.entityCategoryId", Integer.parseInt(entityCategoryId)));
				}
			if (!callProgress.equals("z"))
				select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
			if (!citizenCpf.equals("z"))
				select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
			
			return(List<UnsolvedCall>) select.list();
	}

	
	
	public List<UnsolvedCall> searchCitizenFinal2(String periodod, String periodoa, 
			 String entityid, String entityCategoryId,String callProgress,String description, String citizenCpf, List<Long> excludeCalls) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		List<Long> llist=findAllLastChild();
		if (llist.isEmpty())
			return new ArrayList<UnsolvedCall>();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.createAlias("parentCallId", "parent");	
		select.add(Restrictions.in("unsolvedCallId", llist));
		select.add(Restrictions.eq("parent.remove", false));
		select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));							
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.addOrder(Order.desc("parent.creationOrUpdateDate"));
		if(!excludeCalls.isEmpty())
			select.add(Restrictions.not(Restrictions.in("parent.unsolvedCallId", excludeCalls)));
			if (!periodod.isEmpty()) {
				try {
					Date pd= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(periodod);
					pd.setHours(23);
					pd.setMinutes(59);
					pd.setSeconds(59);
					select.add(Restrictions.le("parent.creationOrUpdateDate", pd));
					} catch (ParseException pe) {
						throw pe;
					}
			}
			if (!periodoa.isEmpty()) {
				try {
					Date pa= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(periodoa);
					pa.setHours(00);
					pa.setMinutes(00);
					pa.setSeconds(00);
					select.add(Restrictions.ge("parent.creationOrUpdateDate", pa));					
				} catch (ParseException pe) {
					throw pe;
				}				
			}
			if (!description.isEmpty()) {
				select.createAlias("description", "desc");
				select.add(Restrictions.ilike("desc.information", "%" + description + "%"));
			}
			if (!entityid.isEmpty()){
				select.createAlias("entityEntityCategoryMaps", "ent");
				select.add(Restrictions.eq("ent.entityEntityCategoryMapsPK.entityId", Integer.parseInt(entityid)));
			}
				if (!entityCategoryId.isEmpty()){
					select.createAlias("entityEntityCategoryMaps", "entc");
					select.add(Restrictions.eq("entc.entityEntityCategoryMapsPK.entityCategoryId", Integer.parseInt(entityCategoryId)));
				}
			if (!callProgress.isEmpty())
				select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
			if (!citizenCpf.isEmpty())
				select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
			
			return(List<UnsolvedCall>) select.list();
	}
	
	
	
	
	public List<UnsolvedCall> findByCitizen(String cpf) throws HibernateException, SQLException, ParseException, ModelException {
		List<Long> llist=findAllLastChild();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.add(Restrictions.in("unsolvedCallId", llist));
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", cpf));
			select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			//if (bms.isEmpty())
			//	throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return bms;
	}
	
	
	public Map<Integer, List<UnsolvedCall>> searchPag2(int page, String periodod, String periodoa, String callSource,
			String callclassificationid, String entityid, String priority, String userid, String callStatus,
			List<Long> tList) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		if (tList.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
		List<UnsolvedCall> teste = (List<UnsolvedCall>) select.list();
		if (periodod.equals("z") && periodoa.equals("z") && callSource.equals("z") && callclassificationid.equals("z")
				&& entityid.equals("z") && priority.equals("z") && userid.equals("z") && callStatus.equals("z")) {
			select.add(Restrictions.in("unsolvedCallId", tList));

			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);

			select.addOrder(Order.desc("creationOrUpdateDate"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!periodod.equals("z")) {
			try {
				periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.ge("creationOrUpdateDate", periodod2));
		}
		if (!periodoa.equals("z")) {
			try {
				periodoa2 = new SimpleDateFormat("ddMMyyyy").parse(periodoa);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.le("creationOrUpdateDate", periodoa2));
		}
		if (!callSource.equals("z")) {
			select.add(Restrictions.eq("callSource", callSource));
		}
		if (!callclassificationid.equals("z")) {
			DetachedCriteria dc = DetachedCriteria.forClass(CallClassification.class, "cc");
			dc.add(Restrictions.eq("name", CallClassificationName.fromValue(Integer.parseInt(callclassificationid))));
			dc.setProjection(Projections.property("cc.callClassificationId"));
			select.add(Subqueries.propertyIn("callClassificationId", dc));
		}
		if (!entityid.equals("z"))
			select.add(Restrictions.eq("entityCategoryTarget.entityCategoryId", Integer.parseInt(entityid)));
		if (!priority.equals("z"))
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		if (!userid.equals("z"))
			select.add(Restrictions.ilike("updatedOrModeratedBy.systemUserUsername", "%" + userid + "%"));
		if (!callStatus.equals("z"))
			select.add(Restrictions.eq("callStatus", CallStatus.fromValue(Integer.parseInt(callStatus))));
		select.addOrder(Order.desc("creationOrUpdateDate"));
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();

		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);

		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.add(Restrictions.in("unsolvedCallId", tList));
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	public Map<Integer, List<UnsolvedCall>> searchPag(int page, String periodod, String periodoa, String callSource,
			String callclassificationid, String entityid, String priority, String userid, String callStatus, String unsolvedCallId, String citizenCpf)
					throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		//select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCall");
		List<Long> tList = query.list();
		if (tList.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
if (periodod.equals("z") && periodoa.equals("z") && callSource.equals("z") && callclassificationid.equals("z")
				&& entityid.equals("z") && priority.equals("z") && userid.equals("z") && callStatus.equals("z")) {

	select.add(Restrictions.in("unsolvedCallId", tList));
			
		//	int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		//	select.setProjection(null);
			select.addOrder(Order.desc("creationOrUpdateDate"));
			List<UnsolvedCall> bms2 = (List<UnsolvedCall>) select.list();
		
			select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			//select.setProjection(Projections.distinct(Projections.property("id")));
			
			//select.setFirstResult(first);
			//select.setMaxResults(perpage);	
/*
			int perpage=Integer.parseInt(SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE));
			int first=(page - 1)*perpage;
			int to=Math.min(first + perpage, bms.size());
			List<UnsolvedCall> bms5=bms.subList(first, to);
	*/
			
			
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<UnsolvedCall> bms =  select.list();
			
			int count =bms.size();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!periodod.equals("z")) {
			try {
				periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.ge("creationOrUpdateDate", periodod2));
		}
		if (!periodoa.equals("z")) {
			try {
				periodoa2 = new SimpleDateFormat("ddMMyyyy").parse(periodoa);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.le("creationOrUpdateDate", periodoa2));
		}
		if (!callSource.equals("z")) {
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		}
		if (!callclassificationid.equals("z")) {
			select.add(Restrictions.eq("callClassificationId.callClassificationId",
					Integer.parseInt(callclassificationid)));
		}
		if (!entityid.equals("z"))
			select.add(Restrictions.eq("entityCategoryTarget.entityCategoryId", Integer.parseInt(entityid)));
		if (!priority.equals("z"))
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		if (!userid.equals("z"))
			select.add(Restrictions.ilike("updatedOrModeratedBy.systemUserUsername", "%" + userid + "%"));
		if (!callStatus.equals("z"))
			select.add(Restrictions.eq("callStatus", CallStatus.fromValue(Integer.parseInt(callStatus))));
		if (!unsolvedCallId.equals("z"))
			select.add(Restrictions.eq("unsolvedCallId", Long.parseLong(unsolvedCallId)));
		if (!citizenCpf.equals("z"))
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));

		select.addOrder(Order.desc("creationOrUpdateDate"));
		select.add(Restrictions.in("unsolvedCallId", tList));
		//select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		int count = select.list().size();
		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	public List<UnsolvedCall> teste() throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		return (List<UnsolvedCall>) select.list();
	}

	//Search CALLFOLLOW
	public Map<Integer, List<UnsolvedCall>> searchPaginaCF(int entity, int page, String periodod, String periodoa,
			String callSource, String callclassificationid, String priority, String unsolvedCallId, String citizenCpf)
					throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		List<UnsolvedCall> ulist2 = (List<UnsolvedCall>) select.list();
		List<Long> llist = findAll3();
		if (llist.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
		select.add(Restrictions.in("unsolvedCallId", llist));
		List<UnsolvedCall> ulist3 = (List<UnsolvedCall>) select.list();
		if (llist.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
		if (periodod.equals("z") && periodoa.equals("z") && callSource.equals("z") && callclassificationid.equals("z")
				&& priority.equals("z") && unsolvedCallId.equals("z") && citizenCpf.equals("z")) {
			int count = select.list().size();
			select.addOrder(Order.desc("creationOrUpdateDate"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));

			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}

		if (!periodod.equals("z")) {
			try {
				periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod);
			} catch (ParseException pe) {
			}

			select.add(Restrictions.ge("creationOrUpdateDate", periodod2));
		}
		if (!periodoa.equals("z")) {
			try {
				periodoa2 = new SimpleDateFormat("ddMMyyyy").parse(periodoa);
			} catch (ParseException pe) {
			}

			select.add(Restrictions.le("creationOrUpdateDate", periodoa2));
		}
		if (!callSource.equals("z")) {
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		}
		if (!callclassificationid.equals("z")) {
			select.add(Restrictions.eq("callClassificationId.callClassificationId",
					Integer.parseInt(callclassificationid)));
		}
		if (!priority.equals("z"))
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		if(!unsolvedCallId.equals("z")){
			select.add(Restrictions.eq("parentCallId.unsolvedCallId", Long.parseLong(unsolvedCallId)));
		}
		if(!citizenCpf.equals("z")){
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		}
		int count = select.list().size();
		select.setProjection(null);
		select.addOrder(Order.desc("creationOrUpdateDate"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}
	
	//MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchPaginaMO(int page, String periodod, String periodoa,
			String callSource, String entity, String callClassification, String priority, String callProgress,String sortColumn,int order)
					throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		List<Long> llist = findAll3();
		if (llist.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
		select.add(Restrictions.in("unsolvedCallId", llist));
		if (periodod.equals("z") && periodoa.equals("z") && callSource.equals("z") && callClassification.equals("z")
				&& priority.equals("z") && callProgress.equals("z")) {
			int count = select.list().size();
			if(order==0)
			select.addOrder(Order.asc(sortColumn));
			else
				select.addOrder(Order.desc(sortColumn));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!periodod.equals("z")) {
			try {
				periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.ge("creationOrUpdateDate", periodod2));
		}
		if (!periodoa.equals("z")) {
			try {
				periodoa2 = new SimpleDateFormat("ddMMyyyy").parse(periodoa);
			} catch (ParseException pe) {
			}
			select.add(Restrictions.le("creationOrUpdateDate", periodoa2));
		}
		if (!callSource.equals("z")) {
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		}
		if (!callClassification.equals("z")) {
			select.add(Restrictions.eq("callClassificationId.callClassificationId",
					Integer.parseInt(callClassification)));
		}
		if (!priority.equals("z"))
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		
		if(!callProgress.equals("z")){
			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
		}		
		int count = select.list().size();
		select.setProjection(null);
		if(order==0)
			select.addOrder(Order.asc(sortColumn));
			else
				select.addOrder(Order.desc(sortColumn));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}
	
	
	//MONITOR DE CHAMADOS
		public Map<Integer, List<UnsolvedCall>> searchPgMO(int page, String periodod, String periodoa,
				String callSource, String entity, String callClassification, String priority, String callProgress,String sortColumn,int order)
						throws HibernateException, SQLException, ParseException, ModelException {
		return null;
	/*		
			Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
			Date periodod2 = new Date();
			Date periodoa2 = new Date();
			Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		
			List<Long> llist = findAll3();
			if (llist.isEmpty()) {
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			}
			select.add(Restrictions.in("unsolvedCallId", llist));
			if (periodod.equals("z") && periodoa.equals("z") && callSource.equals("z") && callClassification.equals("z")
					&& priority.equals("z") && callProgress.equals("z")) {
				int count = select.list().size();
				switch(sortColumn){
				case 0:
					if(order==0)
					select.addOrder(Order.asc("unsolvedCallId"));
					else
						select.addOrder(Order.desc("unsolvedCallId"));
					break;
				case 1:
					if(order==0)
						select.addOrder(Order.asc("callSource"));
						else
							select.addOrder(Order.desc("callSource"));
						break;
				case 2:
					select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
					if(order==0)
					select.addOrder(Order.asc("entitymaps.name"));
					else
						select.addOrder(Order.desc("entitymaps.name"));						
					break;
							case 3:
					select.createAlias("callClassificationId", "classification");
					if(order==0)
					select.addOrder(Order.asc("classification.name"));
					else
						select.addOrder(Order.desc("classification.name"));
					break;
				case 4:
					select.createAlias("parentCallId", "parentdate");
					if(order==0)
					select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
					else
						select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
					break;
				case 5:
					if(order==0)
					select.addOrder(Order.asc("priority"));
					else
						select.addOrder(Order.asc("priority"));				
					break;
				case 6:
					select.createAlias("description", "desc");
					if(order==0)
					select.addOrder(Order.asc("desc.information"));
					else
						select.addOrder(Order.desc("desc.information"));				
					break;
				case 7:
					if(order==0)
					select.addOrder(Order.asc("callProgress"));
					else
						select.addOrder(Order.desc("callProgress"));				
					break;
				}
				List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
				map.put(count, bms);
				if (bms.isEmpty())
					throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
				return map;
			}
			if (!periodod.equals("z")) {
				try {
					periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod);
				} catch (ParseException pe) {
				}
				select.add(Restrictions.ge("creationOrUpdateDate", periodod2));
			}
			if (!periodoa.equals("z")) {
				try {
					periodoa2 = new SimpleDateFormat("ddMMyyyy").parse(periodoa);
				} catch (ParseException pe) {
				}
				select.add(Restrictions.le("creationOrUpdateDate", periodoa2));
			}
			if (!callSource.equals("z")) {
				select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
			}
			if (!callClassification.equals("z")) {
				select.add(Restrictions.eq("callClassificationId.callClassificationId",
						Integer.parseInt(callClassification)));
			}
			if (!priority.equals("z"))
				select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
			
			if(!callProgress.equals("z")){
				select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(priority))));
			}		
			int count = select.list().size();
			select.setProjection(null);
			switch(sortColumn){
			case 0:
				if(order==0)
				select.addOrder(Order.asc("unsolvedCallId"));
				else
					select.addOrder(Order.desc("unsolvedCallId"));
				break;
			case 1:
				if(order==0)
					select.addOrder(Order.asc("callSource"));
					else
						select.addOrder(Order.desc("callSource"));
					break;
			case 2:
				select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
				if(order==0)
				select.addOrder(Order.asc("entitymaps.name"));
				else
					select.addOrder(Order.desc("entitymaps.name"));						
				break;
						case 3:
				select.createAlias("callClassificationId", "classification");
				if(order==0)
				select.addOrder(Order.asc("classification.name"));
				else
					select.addOrder(Order.desc("classification.name"));
				break;
			case 4:
				select.createAlias("parentCallId", "parentdate");
				if(order==0)
				select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
				else
					select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
				break;
			case 5:
				if(order==0)
				select.addOrder(Order.asc("priority"));
				else
					select.addOrder(Order.asc("priority"));				
				break;
			case 6:
				select.createAlias("description", "desc");
				if(order==0)
				select.addOrder(Order.asc("desc.information"));
				else
					select.addOrder(Order.desc("desc.information"));				
				break;
			case 7:
				if(order==0)
				select.addOrder(Order.asc("callProgress"));
				else
					select.addOrder(Order.desc("callProgress"));				
				break;
			}
			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
			*/
		}
	
		//MONITOR DE CHAMADOS
		public Map<Integer, List<UnsolvedCall>> searchMO(String periodod, String periodoa,
				String callSource, String entity, String callClassification, String priority, String callProgress,int sortColumn,int order)
						throws HibernateException, SQLException, ParseException, ModelException {
			Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
			Date periodod2 = new Date();
			Date periodoa2 = new Date();
			Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		
			List<Long> llist = findAll3();
			if (llist.isEmpty()) {
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			}
			select.add(Restrictions.in("unsolvedCallId", llist));
			if (periodod.equals("z") && periodoa.equals("z") && callSource.equals("z") && callClassification.equals("z")
					&& priority.equals("z") && callProgress.equals("z")) {
				int count = select.list().size();
				switch(sortColumn){
				case 0:
					if(order==0)
					select.addOrder(Order.asc("unsolvedCallId"));
					else
						select.addOrder(Order.desc("unsolvedCallId"));
					break;
				case 1:
					if(order==0)
						select.addOrder(Order.asc("callSource"));
						else
							select.addOrder(Order.desc("callSource"));
						break;
				case 2:
					select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
					if(order==0)
					select.addOrder(Order.asc("entitymaps.name"));
					else
						select.addOrder(Order.desc("entitymaps.name"));						
					break;
							case 3:
					select.createAlias("callClassificationId", "classification");
					if(order==0)
					select.addOrder(Order.asc("classification.name"));
					else
						select.addOrder(Order.desc("classification.name"));
					break;
				case 4:
					select.createAlias("parentCallId", "parentdate");
					//select.setFetchMode("parentCallId", FetchMode.JOIN);
					if(order==0)
					select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
					else
						select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
					break;
				case 5:
					if(order==0)
					select.addOrder(Order.asc("priority"));
					else
						select.addOrder(Order.asc("priority"));				
					break;
				case 6:
					select.createAlias("description", "desc");
					if(order==0)
					select.addOrder(Order.asc("desc.information"));
					else
						select.addOrder(Order.desc("desc.information"));				
					break;
				case 7:
					if(order==0)
					select.addOrder(Order.asc("callProgress"));
					else
						select.addOrder(Order.desc("callProgress"));				
					break;
				}
				List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
				map.put(count, bms);
				if (bms.isEmpty())
					throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
				return map;
			}
			if (!periodod.equals("z")) {
				try {
					periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod);
				} catch (ParseException pe) {
				}
				select.add(Restrictions.ge("creationOrUpdateDate", periodod2));
			}
			if (!periodoa.equals("z")) {
				try {
					periodoa2 = new SimpleDateFormat("ddMMyyyy").parse(periodoa);
				} catch (ParseException pe) {
				}
				select.add(Restrictions.le("creationOrUpdateDate", periodoa2));
			}
			if (!callSource.equals("z")) {
				select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
			}
			if (!callClassification.equals("z")) {
				select.add(Restrictions.eq("callClassificationId.callClassificationId",
						Integer.parseInt(callClassification)));
			}
			if (!priority.equals("z"))
				select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
			
			if(!callProgress.equals("z")){
				select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(priority))));
			}		
			int count = select.list().size();
			select.setProjection(null);
			switch(sortColumn){
			case 0:
				if(order==0)
				select.addOrder(Order.asc("unsolvedCallId"));
				else
					select.addOrder(Order.desc("unsolvedCallId"));
				break;
			case 1:
				if(order==0)
					select.addOrder(Order.asc("callSource"));
					else
						select.addOrder(Order.desc("callSource"));
					break;
			case 2:
				select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
				if(order==0)
				select.addOrder(Order.asc("entitymaps.name"));
				else
					select.addOrder(Order.desc("entitymaps.name"));						
				break;
						case 3:
				select.createAlias("callClassificationId", "classification");
				if(order==0)
				select.addOrder(Order.asc("classification.name"));
				else
					select.addOrder(Order.desc("classification.name"));
				break;
			case 4:
				select.createAlias("parentCallId", "parentdate");
				if(order==0)
				select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
				else
					select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
				break;
			case 5:
				if(order==0)
				select.addOrder(Order.asc("priority"));
				else
					select.addOrder(Order.asc("priority"));				
				break;
			case 6:
				select.createAlias("description", "desc");
				if(order==0)
				select.addOrder(Order.asc("desc.information"));
				else
					select.addOrder(Order.desc("desc.information"));				
				break;
			case 7:
				if(order==0)
				select.addOrder(Order.asc("callProgress"));
				else
					select.addOrder(Order.desc("callProgress"));				
				break;
			}
			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
	/*
		
		//MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchMO2(int page,CallMonitorFilter cmf)
					throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		List<Long> llist = findAll3();
		if (llist.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
		select.add(Restrictions.in("unsolvedCallId", llist));
	
		if (cmf.getCallSource().isEmpty() && cmf.getClassification().isEmpty()
				&& cmf.getPriority().isEmpty() && cmf.getCallProgress().isEmpty() && cmf.getEntity().isEmpty()) {
			int count = select.list().size();
			switch(cmf.getOrderParam()){
			case 0:
				if(cmf.getOrder()==0)
				select.addOrder(Order.asc("unsolvedCallId"));
				else
					select.addOrder(Order.desc("unsolvedCallId"));
				break;
			case 1:
				if(cmf.getOrder()==0)
					select.addOrder(Order.asc("callSource"));
					else
						select.addOrder(Order.desc("callSource"));
					break;
			case 2:
				select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
				if(cmf.getOrder()==0)
				select.addOrder(Order.asc("entitymaps.name"));
				else
					select.addOrder(Order.desc("entitymaps.name"));						
				break;
						case 3:
				select.createAlias("callClassificationId", "classification");
				if(cmf.getOrder()==0)
				select.addOrder(Order.asc("classification.name"));
				else
					select.addOrder(Order.desc("classification.name"));
				break;
			case 4:
				select.createAlias("parentCallId", "parentdate");
				if(cmf.getOrder()==0)
				select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
				else
					select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
				break;
			case 5:
				if(cmf.getOrder()==0)
				select.addOrder(Order.asc("priority"));
				else
					select.addOrder(Order.asc("priority"));				
				break;
			case 6:
				select.createAlias("description", "desc");
				if(cmf.getOrder()==0)
				select.addOrder(Order.asc("desc.information"));
				else
					select.addOrder(Order.desc("desc.information"));				
				break;
			case 7:
				if(cmf.getOrder()==0)
				select.addOrder(Order.asc("callProgress"));
				else
					select.addOrder(Order.desc("callProgress"));				
				break;
			}

			List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}

		
		switch(cmf.getOrderParam()){
		case 0:
			if(cmf.getOrder()==0)
			select.addOrder(Order.asc("unsolvedCallId"));
			else
				select.addOrder(Order.desc("unsolvedCallId"));
			break;
		case 1:
			if(cmf.getOrder()==0)
				select.addOrder(Order.asc("callSource"));
				else
					select.addOrder(Order.desc("callSource"));
				break;
		case 2:
			select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
			if(cmf.getOrder()==0)
			select.addOrder(Order.asc("entitymaps.name"));
			else
				select.addOrder(Order.desc("entitymaps.name"));						
			break;
					case 3:
			select.createAlias("callClassificationId", "classification");
			if(cmf.getOrder()==0)
			select.addOrder(Order.asc("classification.name"));
			else
				select.addOrder(Order.desc("classification.name"));
			break;
		case 4:
			select.createAlias("parentCallId", "parentdate");
			if(cmf.getOrder()==0)
			select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
			else
				select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
			break;
		case 5:
			if(cmf.getOrder()==0)
			select.addOrder(Order.asc("priority"));
			else
				select.addOrder(Order.asc("priority"));				
			break;
		case 6:
			select.createAlias("description", "desc");
			if(cmf.getOrder()==0)
			select.addOrder(Order.asc("desc.information"));
			else
				select.addOrder(Order.desc("desc.information"));				
			break;
		case 7:
			if(cmf.getOrder()==0)
			select.addOrder(Order.asc("callProgress"));
			else
				select.addOrder(Order.desc("callProgress"));				
			break;
		}
		
		
		
		if(!cmf.getEntity().isEmpty()){		
		//	if(cmf.getOrderParam()!=2)
		//	Criteria select2 = HibernateUtil.getSession().createCriteria(EntityEntityCategoryMaps.class);
		//	select2.add(Restrictions.in("entity.entityId", cmf.getEntity()));
		//	List<EntityEntityCategoryMaps> enList= (List<EntityEntityCategoryMaps>)select2.list();	
			select.createAlias("entityEntityCategoryMaps", "eemaps");
		//		select.add(Restrictions.in("eemaps", enList));		
			select.add(Restrictions.in("eemaps.entity.entityId", cmf.getEntity()));
		}
			
		if (!cmf.getCallSource().isEmpty()) {
			select.add(Restrictions.in("callSource", cmf.getEnumCallSource()));
		//	for(String s:cmf.getCallSource())
	//		select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(s))));
		}
		if (!cmf.getClassification().isEmpty()) {
			select.add(Restrictions.in("callClassificationId.callClassificationId",	cmf.getClassification()));
		//	for(String s:cmf.getClassification())
//			select.add(Restrictions.eq("callClassificationId.callClassificationId",
	//				Integer.parseInt(s)));
		}
		if (!cmf.getPriority().isEmpty())
			select.add(Restrictions.in("priority", cmf.getEnumPriority()));
			//		for(String s:cmf.getPriority())
//			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(s))));
		
		if(!cmf.getCallProgress().isEmpty()){
			select.add(Restrictions.in("callProgress", cmf.getEnumCallProgress()));
			//			for(String s:cmf.getCallProgres())
//			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(s))));
		}		
		int count = select.list().size();
		
		
		 select.setProjection(Projections.projectionList()
			      .add(Projections.property("unsolvedCallId"), "unsolvedCallId")
			      .add(Projections.property("callStatus"), "callStatus")
			      .add(Projections.property("callProgress"), "callProgress")
			      .add(Projections.property("callSource"), "callSource")
			      .add(Projections.property("creationOrUpdateDate"), "creationOrUpdateDate")
			      .add(Projections.property("priority"), "priority")
			      .add(Projections.property("observation"), "observation")
			      .add(Projections.property("description"), "description")
			      .add(Projections.property("callClassificationId"), "callClassificationId")
			      .add(Projections.property("updatedOrModeratedBy"), "updatedOrModeratedBy")
			      .add(Projections.property("parentCallId"), "parentCallId")
			      .add(Projections.property("entityEntityCategoryMaps"), "entityEntityCategoryMaps")
			      			      
				 );
	    select.setResultTransformer(Transformers.aliasToBean(UnsolvedCall.class));
		//select.setProjection(null);
	
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}
	
*/
	public UnsolvedCall getByIdMedias(Long unsolvedCallId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.setFetchMode("targetNeighborhoodId", FetchMode.JOIN);

		select.add(Restrictions.eq("unsolvedCallId", unsolvedCallId));
		UnsolvedCall t = (UnsolvedCall) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public UnsolvedCall getLastChild(Long unsolvedCallId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.add(Restrictions.eq("parentCallId.unsolvedCallId", unsolvedCallId));
		select.addOrder(Order.desc("unsolvedCallId"));
		select.setMaxResults(1);
		UnsolvedCall t = (UnsolvedCall) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	@Override
	public UnsolvedCall getById(Long unsolvedCallId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);

		select.add(Restrictions.eq("unsolvedCallId", unsolvedCallId));
		UnsolvedCall t = (UnsolvedCall) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}
	
	public UnsolvedCall getByEntity(Long entityId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		//select.setFetchMode("parentCallId", FetchMode.JOIN);

		select.add(Restrictions.eq("entityId", entityId));
		UnsolvedCall t = (UnsolvedCall) select.uniqueResult();
		return t;
	}
	
	public List<Long> findAll2(Integer entityId) throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCall");
		List<Long> tList = query.list();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		DetachedCriteria dc = DetachedCriteria.forClass(EntityCategory.class, "ec");
		dc.createAlias("entityCollection", "ent");
		dc.add(Restrictions.eq("ent.entityId", entityId));
		dc.setProjection(Projections.property("ec.entityCategoryId"));
		select.createAlias("entityCategoryTarget", "ect");
		select.add(Subqueries.propertyIn("ect.entityCategoryId", dc));
		select.setProjection(Projections.property("unsolvedCallId"));
		return (List<Long>) select.list();
	}

	public List<Long> findAll3() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCallFollow");
		List<Long> tList = query.list();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		if (tList.isEmpty()) {
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		}
		select.add(Restrictions.in("unsolvedCallId", tList));
		select.setProjection(Projections.property("unsolvedCallId"));
		return (List<Long>) select.list();
	}

	public UnsolvedCall getByIdRC(Long unsolvedCallId) throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.add(Restrictions.eq("unsolvedCallId", unsolvedCallId));
		UnsolvedCall t = (UnsolvedCall) select.uniqueResult();
		if (t == null)
			return null;
		
		return t;
	}

}
