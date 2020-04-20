package zup.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;

import zup.bean.CallClassification;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.EntityEntityCategoryMaps;
import zup.bean.Result;
import zup.bean.UnsolvedCall;
import zup.dto.CallProgressDTO;
import zup.dto.DashBoardFinEmAndDTO;
import zup.dto.DelayCountDTO;
import zup.dto.NeighborhoodChartDTO;
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

public class UnsolvedCallDAO2 extends AbstractDAO<UnsolvedCall, Long> {
	private static Logger logger = Logger.getLogger(UnsolvedCallDAO2.class);

	public UnsolvedCallDAO2() {
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

	public Long countInprogress() throws SQLException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countInProgress");
		return (Long) query.uniqueResult();
	}
	
	// Contador do Gráfico "Andamento dos Chamados" na Dashboard
	public Long countProgressCalls(String progress) throws SQLException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.count" + progress + "Call");
		return (Long) query.uniqueResult();
	}

	/*
	public List<CallProgressDTO> countCallProgressDash() throws SQLException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countCallProgress");
		query.setResultTransformer(Transformers.aliasToBean(CallProgressDTO.class));
		return query.list();
	}
	*/

	public List<Long> newFind() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCall");
		List<Long> tList = query.list();
		if (tList.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return tList;
	}

	public List<DelayCountDTO> countDelay() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countCallDelay");
		List<DelayCountDTO> list = new ArrayList<DelayCountDTO>();
		List<Object[]> tList = query.list();
		for (Object[] obj : tList) {
			DelayCountDTO delay = new DelayCountDTO();
			delay.setTime((Integer) obj[1]); // id
			// Timestamp
			long millis1 =  new java.util.Date().getTime();
			java.sql.Timestamp ts = (Timestamp) obj[0];
			// Date
			long millis2 = ts.getTime();
			java.util.Date date = new java.util.Date( millis2 );
			delay.setCreationDate(date);
			list.add(delay);
		}
		return list;
	}
	
	public List<NeighborhoodChartDTO> countNeighborhoodChart() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countNeighborhoodChart");
		query.setMaxResults(10);
		List<NeighborhoodChartDTO> list = new ArrayList<NeighborhoodChartDTO>();
		List<Object[]> tList = query.list();
		for (Object[] obj : tList) {
			NeighborhoodChartDTO n = new NeighborhoodChartDTO();
			n.setCountNeighborhood((Long) obj[0]);
			n.setNameNeighborhood((String) obj[1]);
			list.add(n);
		}
		return list;
	}

	public List<Long> findAllLastChild() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllLastChild");
		List<Long> tList = query.list();
		return tList;
	}

	public List<Long> findAllLastChildId(Long id) throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllLastChildId");
		query.setLong("parentCallId", id);
		List<Long> tList = query.list();
		return tList;
	}

	public boolean evaluation(String eval) throws SQLException, ModelException {
		System.out.println(eval);
		return true;
	}

	public Map<Integer, List<UnsolvedCall>> searchCitizen2(String periodod, String periodoa, String entityid,
			String entityCategoryId, String callProgress, String description, String citizenCpf)
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
		if (periodod == null && periodoa == null && entityid.equals("z") && description.equals("z")
				&& callProgress.equals("z")) {
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

		if (!entityid.equals("z")) {
			select.createAlias("entityEntityCategoryMaps.entity", "ent");
			select.add(Restrictions.eq("ent.entityId", Integer.parseInt(entityid)));
		}
		if (!entityCategoryId.equals("z")) {
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

	public Map<Integer, List<UnsolvedCall>> searchCitizen(String cpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		List<Long> llist = findAllLastChild();
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

	public UnsolvedCall searchCitizenPk(Long unsolvedCallId, String publicKey)
			throws HibernateException, SQLException, ParseException, ModelException {
		List<Long> llist = findAllLastChildId(unsolvedCallId);
		UnsolvedCall bms = null;
		if (!llist.isEmpty()) {
			Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
			select.add(Restrictions.in("unsolvedCallId", llist));
			select.createAlias("citizenCpf", "citizenCpf");
			select.add(Restrictions.eq("citizenCpf.publicKey", publicKey));
			select.createAlias("parentCallId", "parentdate");
			select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
			select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			bms = (UnsolvedCall) select.uniqueResult();
		}
		return bms;
	}

	public List<UnsolvedCall> searchCitizenFinal(String periodod, String periodoa, String entityid,
			String entityCategoryId, String callProgress, String description, String citizenCpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		List<Long> llist = findAllLastChild();
		System.out.println(llist.toString());
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.createAlias("parentCallId", "parent");
		select.add(Restrictions.in("unsolvedCallId", llist));
		select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.addOrder(Order.desc("parent.creationOrUpdateDate"));
		System.out.println((periodod == "") + ", " + (periodoa == "") + ", " + entityid.equals("z") + ", "
				+ description.equals("z") + ", " + callProgress.equals("z"));
		if (periodod.equals("") && periodoa.equals("") && entityid.equals("z") && description.equals("z")
				&& callProgress.equals("z")) {
			return (List<UnsolvedCall>) select.list();
		}
		if (!periodod.equals("")) {
			try {
				Date pd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(periodod);
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
				Date pa = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(periodoa);
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

		if (!entityid.equals("z")) {
			select.createAlias("entityEntityCategoryMaps.entity", "ent");
			select.add(Restrictions.eq("ent.entityId", Integer.parseInt(entityid)));
		}
		if (!entityCategoryId.equals("z")) {
			select.createAlias("entityEntityCategoryMaps.entityCategory", "entc");
			select.add(Restrictions.eq("entc.entityCategoryId", Integer.parseInt(entityCategoryId)));
		}
		if (!callProgress.equals("z"))
			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
		if (!citizenCpf.equals("z"))
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));

		return (List<UnsolvedCall>) select.list();
	}

	public List<UnsolvedCall> findByCitizen(String cpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		List<Long> llist = findAllLastChild();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.add(Restrictions.in("unsolvedCallId", llist));
		select.add(Restrictions.eq("citizenCpf.citizen_cpf", cpf));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
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

	public Result<UnsolvedCall> searchPag(int page, Date iniDate, Date endDate, String callSource,
			String callClassificationId, String entityId, String priority, String userid, String callStatus,
			String unsolvedCallId, String citizenCpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.createAlias("parentCallId", "parentCallId");
		select.add(Restrictions.ge("parentCallId.creationOrUpdateDate", iniDate));
		select.add(Restrictions.le("parentCallId.creationOrUpdateDate", endDate));
		// select.setFetchMode("parentCallId", FetchMode.JOIN);
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCall");
		List<Long> tList = query.list();
		if (tList.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		select.add(Restrictions.in("unsolvedCallId", tList));
		if (!callSource.isEmpty())
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		if (!callClassificationId.isEmpty())
			select.add(Restrictions.eq("callClassificationId.callClassificationId",
					Integer.parseInt(callClassificationId)));
		if (!entityId.isEmpty()) {
			select.createAlias("entityEntityCategoryMaps", "entityEntityCategoryMaps");
			select.add(Restrictions.eq("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityCategoryId",
					Integer.parseInt(entityId)));
		}
		if (!priority.isEmpty())
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		if (!userid.isEmpty())
			select.add(Restrictions.ilike("updatedOrModeratedBy.systemUserUsername", "%" + userid + "%"));
		if (!callStatus.isEmpty())
			select.add(Restrictions.eq("callStatus", CallStatus.fromValue(Integer.parseInt(callStatus))));
		if (!unsolvedCallId.isEmpty())
			select.add(Restrictions.eq("parentCallId.unsolvedCallId", Long.parseLong(unsolvedCallId)));
		if (!citizenCpf.isEmpty())
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		select.setProjection(Projections.countDistinct("unsolvedCallId"));
		// int count = ((Number) select.uniqueResult()).intValue();
		// select.setProjection(null);
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.desc("creationOrUpdateDate"));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));

		return new Result<UnsolvedCall>(count, select.list());
	}

	// este
	public List<UnsolvedCall> teste2() throws SQLException {
		DetachedCriteria max = DetachedCriteria.forClass(UnsolvedCall.class, "uns");
		max.setProjection(Projections.projectionList().add(Projections.groupProperty("uns.parentCallId"))
				.add(Projections.max("uns.unsolvedCallId")));
		max.setProjection(Projections.max("uns.unsolvedCallId"));
		max.add(Property.forName("uns.unsolvedCallId").eqProperty("uns2.unsolvedCallId"));
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class, "uns2");
		select.add(Property.forName("unsolvedCallId").eq(max));
		return select.list();
	}

	public List<UnsolvedCall> teste() throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		return (List<UnsolvedCall>) select.list();
	}

	// test
	// Call FOLLOW novo 17/09/2016
	public List<UnsolvedCall> searchPagCF2(int page, Date iniDate, Date endDate, String callSource,
			String callClassificationId, String priority, String unsolvedCallId, String citizenCpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.createAlias("parentCallId", "parentCallId");
		select.add(Restrictions.ge("parentCallId.creationOrUpdateDate", iniDate));
		select.add(Restrictions.le("parentCallId.creationOrUpdateDate", endDate));
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCallFollow");
		List<Long> llist = query.list();
		if (llist.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		select.add(Restrictions.in("unsolvedCallId", llist));
		if (!callSource.isEmpty())
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		if (!callClassificationId.isEmpty())
			select.add(Restrictions.eq("callClassificationId.callClassificationId",
					Integer.parseInt(callClassificationId)));
		if (!priority.isEmpty())
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		if (!unsolvedCallId.isEmpty())
			select.add(Restrictions.eq("parentCallId.unsolvedCallId", Long.parseLong(unsolvedCallId)));
		if (!citizenCpf.isEmpty())
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		select.addOrder(Order.desc("creationOrUpdateDate"));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<UnsolvedCall>) select.list();
	}

	public List<UnsolvedCall> searchPagCFEN(int page, Date iniDate, Date endDate, String callSource,
			String callClassificationId, String priority, String unsolvedCallId, String citizenCpf, String entityId)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.createAlias("parentCallId", "parentCallId");
		// select.createAlias("entityEntityCategoryMaps.entityEntityCategoryMapsPK","entity");
		select.add(Restrictions.ge("parentCallId.creationOrUpdateDate", iniDate));
		select.add(Restrictions.le("parentCallId.creationOrUpdateDate", endDate));
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCallFollow");
		List<Long> llist = query.list();
		if (llist.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		select.add(Restrictions.in("unsolvedCallId", llist));
		if (!callSource.isEmpty())
			select.add(Restrictions.eq("callSource", CallSource.fromValue(Integer.parseInt(callSource))));
		if (!callClassificationId.isEmpty())
			select.add(Restrictions.eq("callClassificationId.callClassificationId",
					Integer.parseInt(callClassificationId)));
		if (!priority.isEmpty())
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		if (!entityId.isEmpty())
			select.add(Restrictions.eq("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId",
					Integer.parseInt(entityId)));
		if (!unsolvedCallId.isEmpty())
			select.add(Restrictions.eq("parentCallId.unsolvedCallId", Long.parseLong(unsolvedCallId)));
		if (!citizenCpf.isEmpty())
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		select.addOrder(Order.desc("creationOrUpdateDate"));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<UnsolvedCall>) select.list();
	}

	// test
	/*
	 * // Search CALLFOLLOW public Result<UnsolvedCall> searchPaginaCF2(int
	 * page, Date iniDate, Date endDate, String callSource, String
	 * callclassificationid, String priority, String unsolvedCallId, String
	 * citizenCpf) throws HibernateException, SQLException, ParseException,
	 * ModelException { Criteria select =
	 * HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
	 * select.add(Restrictions.ge("creationOrUpdateDate", iniDate));
	 * select.add(Restrictions.le("creationOrUpdateDate", endDate)); Query query
	 * =
	 * HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCallFollow"
	 * ); List<Long> llist = query.list(); if (llist.isEmpty()) throw new
	 * ModelException(Messages.getInstance().getMessage(IMessages.
	 * RESULT_NOT_FOUND)); select.add(Restrictions.in("unsolvedCallId", llist));
	 * if (!callSource.isEmpty()) select.add(Restrictions.eq("callSource",
	 * CallSource.fromValue(Integer.parseInt(callSource)))); if
	 * (!callclassificationid.isEmpty())
	 * select.add(Restrictions.eq("callClassificationId.callClassificationId",
	 * Integer.parseInt(callclassificationid))); if (!priority.isEmpty())
	 * select.add(Restrictions.eq("priority",
	 * Priority.fromValue(Integer.parseInt(priority)))); if
	 * (!unsolvedCallId.isEmpty())
	 * select.add(Restrictions.eq("parentCallId.unsolvedCallId",
	 * Long.parseLong(unsolvedCallId))); if (!citizenCpf.isEmpty())
	 * select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
	 * select.addOrder(Order.desc("creationOrUpdateDate"));
	 * select.setFirstResult((page - 1) * Integer.parseInt(
	 * SystemConfiguration.getInstance().getSystemConfiguration(
	 * ISystemConfiguration.RECORDED_PER_PAGE)));
	 * select.setMaxResults(Integer.parseInt(
	 * SystemConfiguration.getInstance().getSystemConfiguration(
	 * ISystemConfiguration.RECORDED_PER_PAGE))); return new
	 * Result<UnsolvedCall>(count, (List<UnsolvedCall>) select.list()); }
	 */
	// Search CALLFOLLOW
	public Map<Integer, List<UnsolvedCall>> searchPaginaCF(int entity, int page, String periodod, String periodoa,
			String callSource, String callclassificationid, String priority, String unsolvedCallId, String citizenCpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer, List<UnsolvedCall>>();
		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		List<UnsolvedCall> ulist2 = (List<UnsolvedCall>) select.list();
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCallFollow");
		List<Long> llist = query.list();
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
		if (!unsolvedCallId.equals("z")) {
			select.add(Restrictions.eq("parentCallId.unsolvedCallId", Long.parseLong(unsolvedCallId)));
		}
		if (!citizenCpf.equals("z")) {
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

	/*
	 * // Search CALLFOLLOW public Map<Integer, List<UnsolvedCall>>
	 * searchPaginaCF2(int entity, int page, Date iniDate, Date endDate, String
	 * callSource, String callclassificationid, String priority, String
	 * unsolvedCallId, String citizenCpf) throws HibernateException,
	 * SQLException, ParseException, ModelException { Criteria select =
	 * HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
	 * select.add(Restrictions.ge("creationOrUpdateDate", iniDate));
	 * select.add(Restrictions.le("creationOrUpdateDate", endDate));
	 * List<UnsolvedCall> ulist2 = (List<UnsolvedCall>) select.list(); Query
	 * query =
	 * HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllCallFollow"
	 * ); List<Long> llist = query.list(); if (llist.isEmpty()) throw new
	 * ModelException(Messages.getInstance().getMessage(IMessages.
	 * RESULT_NOT_FOUND)); select.add(Restrictions.in("unsolvedCallId", llist));
	 * if (!callSource.isEmpty()) select.add(Restrictions.eq("callSource",
	 * CallSource.fromValue(Integer.parseInt(callSource)))); if
	 * (!callclassificationid.isEmpty())
	 * select.add(Restrictions.eq("callClassificationId.callClassificationId",
	 * Integer.parseInt(callclassificationid))); if (!priority.isEmpty())
	 * select.add(Restrictions.eq("priority",
	 * Priority.fromValue(Integer.parseInt(priority)))); if
	 * (!unsolvedCallId.isEmpty())
	 * select.add(Restrictions.eq("parentCallId.unsolvedCallId",
	 * Long.parseLong(unsolvedCallId))); if (!citizenCpf.isEmpty())
	 * select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf)); int
	 * count = select.list().size(); select.setProjection(null);
	 * select.addOrder(Order.desc("creationOrUpdateDate"));
	 * select.setFirstResult((page - 1) * Integer.parseInt(
	 * SystemConfiguration.getInstance().getSystemConfiguration(
	 * ISystemConfiguration.RECORDED_PER_PAGE)));
	 * select.setMaxResults(Integer.parseInt(
	 * SystemConfiguration.getInstance().getSystemConfiguration(
	 * ISystemConfiguration.RECORDED_PER_PAGE))); List<UnsolvedCall> bms =
	 * (List<UnsolvedCall>) select.list(); map.put(count, bms); if
	 * (bms.isEmpty()) throw new
	 * ModelException(Messages.getInstance().getMessage(IMessages.
	 * RESULT_NOT_FOUND)); return map; }
	 */

	// MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchPaginaMO(int page, String periodod, String periodoa,
			String callSource, String entity, String callClassification, String priority, String callProgress,
			String sortColumn, int order) throws HibernateException, SQLException, ParseException, ModelException {
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
			if (order == 0)
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
			select.add(
					Restrictions.eq("callClassificationId.callClassificationId", Integer.parseInt(callClassification)));
		}
		if (!priority.equals("z"))
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));

		if (!callProgress.equals("z")) {
			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
		}
		int count = select.list().size();
		select.setProjection(null);
		if (order == 0)
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

	// MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchPgMO(int page, String periodod, String periodoa, String callSource,
			String entity, String callClassification, String priority, String callProgress, String sortColumn,
			int order) throws HibernateException, SQLException, ParseException, ModelException {
		return null;
		/*
		 * Map<Integer, List<UnsolvedCall>> map = new HashMap<Integer,
		 * List<UnsolvedCall>>(); Date periodod2 = new Date(); Date periodoa2 =
		 * new Date(); Criteria select =
		 * HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		 * 
		 * List<Long> llist = findAll3(); if (llist.isEmpty()) { throw new
		 * ModelException(Messages.getInstance().getMessage(IMessages.
		 * RESULT_NOT_FOUND)); } select.add(Restrictions.in("unsolvedCallId",
		 * llist)); if (periodod.equals("z") && periodoa.equals("z") &&
		 * callSource.equals("z") && callClassification.equals("z") &&
		 * priority.equals("z") && callProgress.equals("z")) { int count =
		 * select.list().size(); switch(sortColumn){ case 0: if(order==0)
		 * select.addOrder(Order.asc("unsolvedCallId")); else
		 * select.addOrder(Order.desc("unsolvedCallId")); break; case 1:
		 * if(order==0) select.addOrder(Order.asc("callSource")); else
		 * select.addOrder(Order.desc("callSource")); break; case 2:
		 * select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
		 * if(order==0) select.addOrder(Order.asc("entitymaps.name")); else
		 * select.addOrder(Order.desc("entitymaps.name")); break; case 3:
		 * select.createAlias("callClassificationId", "classification");
		 * if(order==0) select.addOrder(Order.asc("classification.name")); else
		 * select.addOrder(Order.desc("classification.name")); break; case 4:
		 * select.createAlias("parentCallId", "parentdate"); if(order==0)
		 * select.addOrder(Order.asc("parentdate.creationOrUpdateDate")); else
		 * select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
		 * break; case 5: if(order==0) select.addOrder(Order.asc("priority"));
		 * else select.addOrder(Order.asc("priority")); break; case 6:
		 * select.createAlias("description", "desc"); if(order==0)
		 * select.addOrder(Order.asc("desc.information")); else
		 * select.addOrder(Order.desc("desc.information")); break; case 7:
		 * if(order==0) select.addOrder(Order.asc("callProgress")); else
		 * select.addOrder(Order.desc("callProgress")); break; }
		 * List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		 * map.put(count, bms); if (bms.isEmpty()) throw new
		 * ModelException(Messages.getInstance().getMessage(IMessages.
		 * RESULT_NOT_FOUND)); return map; } if (!periodod.equals("z")) { try {
		 * periodod2 = new SimpleDateFormat("ddMMyyyy").parse(periodod); } catch
		 * (ParseException pe) { }
		 * select.add(Restrictions.ge("creationOrUpdateDate", periodod2)); } if
		 * (!periodoa.equals("z")) { try { periodoa2 = new
		 * SimpleDateFormat("ddMMyyyy").parse(periodoa); } catch (ParseException
		 * pe) { } select.add(Restrictions.le("creationOrUpdateDate",
		 * periodoa2)); } if (!callSource.equals("z")) {
		 * select.add(Restrictions.eq("callSource",
		 * CallSource.fromValue(Integer.parseInt(callSource)))); } if
		 * (!callClassification.equals("z")) { select.add(Restrictions.eq(
		 * "callClassificationId.callClassificationId",
		 * Integer.parseInt(callClassification))); } if (!priority.equals("z"))
		 * select.add(Restrictions.eq("priority",
		 * Priority.fromValue(Integer.parseInt(priority))));
		 * 
		 * if(!callProgress.equals("z")){
		 * select.add(Restrictions.eq("callProgress",
		 * CallProgress.fromValue(Integer.parseInt(priority)))); } int count =
		 * select.list().size(); select.setProjection(null); switch(sortColumn){
		 * case 0: if(order==0) select.addOrder(Order.asc("unsolvedCallId"));
		 * else select.addOrder(Order.desc("unsolvedCallId")); break; case 1:
		 * if(order==0) select.addOrder(Order.asc("callSource")); else
		 * select.addOrder(Order.desc("callSource")); break; case 2:
		 * select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
		 * if(order==0) select.addOrder(Order.asc("entitymaps.name")); else
		 * select.addOrder(Order.desc("entitymaps.name")); break; case 3:
		 * select.createAlias("callClassificationId", "classification");
		 * if(order==0) select.addOrder(Order.asc("classification.name")); else
		 * select.addOrder(Order.desc("classification.name")); break; case 4:
		 * select.createAlias("parentCallId", "parentdate"); if(order==0)
		 * select.addOrder(Order.asc("parentdate.creationOrUpdateDate")); else
		 * select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
		 * break; case 5: if(order==0) select.addOrder(Order.asc("priority"));
		 * else select.addOrder(Order.asc("priority")); break; case 6:
		 * select.createAlias("description", "desc"); if(order==0)
		 * select.addOrder(Order.asc("desc.information")); else
		 * select.addOrder(Order.desc("desc.information")); break; case 7:
		 * if(order==0) select.addOrder(Order.asc("callProgress")); else
		 * select.addOrder(Order.desc("callProgress")); break; }
		 * List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		 * map.put(count, bms); if (bms.isEmpty()) throw new
		 * ModelException(Messages.getInstance().getMessage(IMessages.
		 * RESULT_NOT_FOUND)); return map;
		 */
	}

	// MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchMO(String periodod, String periodoa, String callSource, String entity,
			String callClassification, String priority, String callProgress, int sortColumn, int order)
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
			switch (sortColumn) {
			case 0:
				if (order == 0)
					select.addOrder(Order.asc("unsolvedCallId"));
				else
					select.addOrder(Order.desc("unsolvedCallId"));
				break;
			case 1:
				if (order == 0)
					select.addOrder(Order.asc("callSource"));
				else
					select.addOrder(Order.desc("callSource"));
				break;
			case 2:
				select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
				if (order == 0)
					select.addOrder(Order.asc("entitymaps.name"));
				else
					select.addOrder(Order.desc("entitymaps.name"));
				break;
			case 3:
				select.createAlias("callClassificationId", "classification");
				if (order == 0)
					select.addOrder(Order.asc("classification.name"));
				else
					select.addOrder(Order.desc("classification.name"));
				break;
			case 4:
				select.createAlias("parentCallId", "parentdate");
				// select.setFetchMode("parentCallId", FetchMode.JOIN);
				if (order == 0)
					select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
				else
					select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
				break;
			case 5:
				if (order == 0)
					select.addOrder(Order.asc("priority"));
				else
					select.addOrder(Order.asc("priority"));
				break;
			case 6:
				select.createAlias("description", "desc");
				if (order == 0)
					select.addOrder(Order.asc("desc.information"));
				else
					select.addOrder(Order.desc("desc.information"));
				break;
			case 7:
				if (order == 0)
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
			select.add(
					Restrictions.eq("callClassificationId.callClassificationId", Integer.parseInt(callClassification)));
		}
		if (!priority.equals("z"))
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));

		if (!callProgress.equals("z")) {
			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(priority))));
		}
		int count = select.list().size();
		select.setProjection(null);
		switch (sortColumn) {
		case 0:
			if (order == 0)
				select.addOrder(Order.asc("unsolvedCallId"));
			else
				select.addOrder(Order.desc("unsolvedCallId"));
			break;
		case 1:
			if (order == 0)
				select.addOrder(Order.asc("callSource"));
			else
				select.addOrder(Order.desc("callSource"));
			break;
		case 2:
			select.createAlias("entityEntityCategoryMaps.entity", "entitymaps");
			if (order == 0)
				select.addOrder(Order.asc("entitymaps.name"));
			else
				select.addOrder(Order.desc("entitymaps.name"));
			break;
		case 3:
			select.createAlias("callClassificationId", "classification");
			if (order == 0)
				select.addOrder(Order.asc("classification.name"));
			else
				select.addOrder(Order.desc("classification.name"));
			break;
		case 4:
			select.createAlias("parentCallId", "parentdate");
			if (order == 0)
				select.addOrder(Order.asc("parentdate.creationOrUpdateDate"));
			else
				select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
			break;
		case 5:
			if (order == 0)
				select.addOrder(Order.asc("priority"));
			else
				select.addOrder(Order.asc("priority"));
			break;
		case 6:
			select.createAlias("description", "desc");
			if (order == 0)
				select.addOrder(Order.asc("desc.information"));
			else
				select.addOrder(Order.desc("desc.information"));
			break;
		case 7:
			if (order == 0)
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

	// MONITOR DE CHAMADOS novo 17/09/2016
	public Result<UnsolvedCall> searchMO3(int page, List<Integer> entity, List<Integer> callClassification,
			List<CallProgress> callProgress, List<CallSource> callSource, List<Priority> priority, int orderParam,
			int order) throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("entityEntityCategoryMaps", FetchMode.JOIN);
		select.setFetchMode("callClassificationId", FetchMode.JOIN);
		select.setFetchMode("description", FetchMode.JOIN);
		List<Long> llist = findAll3();
		select.add(Restrictions.in("unsolvedCallId", llist));
		if (!entity.isEmpty()) {
			select.createAlias("entityEntityCategoryMaps", "entityEntityCategoryMaps");
			// select.createAlias("entityEntityCategoryMaps.entity", "entity");
			select.add(Restrictions.in("entityEntityCategoryMaps.entity.entityId", entity));
		}
		if (!callSource.isEmpty())
			select.add(Restrictions.in("callSource", callSource));
		if (!callClassification.isEmpty())
			select.add(Restrictions.in("callClassificationId.callClassificationId", callClassification));
		if (!priority.isEmpty())
			select.add(Restrictions.in("priority", priority));
		if (!callProgress.isEmpty())
			select.add(Restrictions.in("callProgress", callProgress));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);

		switch (orderParam) {
		case 0:
			select.addOrder(order == 0 ? Order.asc("unsolvedCallId") : Order.desc("unsolvedCallId"));
			break;
		case 1:
			select.addOrder(order == 0 ? Order.asc("callSource") : Order.desc("callSource"));
			break;
		case 2:
			select.createAlias("entityEntityCategoryMaps", "entityEntityCategoryMaps");
			select.createAlias("entityEntityCategoryMaps.entity", "entity");
			select.addOrder(order == 0 ? Order.asc("entity.name") : Order.desc("entity.name"));
			break;
		case 3:
			select.createAlias("callClassificationId", "callClassificationId");
			select.addOrder(
					order == 0 ? Order.asc("callClassificationId.name") : Order.desc("callClassificationId.name"));
			break;
		case 4:
			select.createAlias("parentCallId", "parentCallId");
			select.addOrder(order == 0 ? Order.asc("parentCallId.creationOrUpdateDate")
					: Order.desc("parentCallId.creationOrUpdateDate"));
			break;
		case 5:
			select.addOrder(order == 0 ? Order.asc("priority") : Order.desc("priority"));
			break;
		case 6:
			select.createAlias("description", "description");
			select.addOrder(order == 0 ? Order.asc("description.information") : Order.desc("description.information"));
			break;
		case 7:
			select.addOrder(order == 0 ? Order.asc("callProgress") : Order.desc("callProgress"));
			break;
		}

		// select.setResultTransformer(Transformers.aliasToBean(UnsolvedCall.class));//Sem
		// isso aqui impossível de retornar
		// List<UnsolvedCall> bms2 = (List<UnsolvedCall>) select.list();

		select.setProjection(Projections.projectionList().add(Projections.property("unsolvedCallId"), "unsolvedCallId")
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
		List<UnsolvedCall> bms = (List<UnsolvedCall>) select.list();
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return new Result<UnsolvedCall>(count, bms);

	}	

	public Result<UnsolvedCall> searchMOALl() throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		List<UnsolvedCall> bms = new ArrayList<UnsolvedCall>();
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllMon");
		List<Long> llist = query.list();
		if (llist.isEmpty())
			return new Result<UnsolvedCall>(0, bms);
		select.add(Restrictions.in("unsolvedCallId", llist));
		select.createAlias("parentCallId", "parentCallId");
		select.addOrder(Order.desc("parentCallId.creationOrUpdateDate"));
		select.setProjection(Projections.projectionList().add(Projections.property("unsolvedCallId"), "unsolvedCallId")
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
		bms = (List<UnsolvedCall>) select.list();
		return new Result<UnsolvedCall>(0, bms);
	}
	
	//Search - Monitor de Chamados - Últimos 30 dias
	public Result<UnsolvedCall> searchCallMonitorLast30Days(Date initDate, Date endDate) throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		List<UnsolvedCall> bms = new ArrayList<UnsolvedCall>();
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);	
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.findAllMon");
		List<Long> llist = query.list();
		if (llist.isEmpty())
			return new Result<UnsolvedCall>(0,bms);
		select.add(Restrictions.in("unsolvedCallId", llist));
		select.createAlias("parentCallId", "parentCallId");		
		select.add(Restrictions.ge("parentCallId.creationOrUpdateDate", initDate)); // Filtro de data inicial.
		select.add(Restrictions.le("parentCallId.creationOrUpdateDate", endDate)); // Filtro de data final.
		select.addOrder(Order.desc("parentCallId.creationOrUpdateDate"));
		select.setProjection(Projections.projectionList().add(Projections.property("unsolvedCallId"), "unsolvedCallId")
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
		bms = (List<UnsolvedCall>) select.list();
		return new Result<UnsolvedCall>(0, bms);
	}

	public LinkedHashMap<String,Long> countCallProgress() throws HibernateException, SQLException, ParseException, ModelException {			
		LinkedHashMap<String,Long> counts=new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countCPMonitor");
		List<Object> obList = query.list();
		for (Object object : obList) {
			Object[] row = (Object[]) object;
			counts.put((String) ((CallProgress) row[0]).getStr(), (Long) row[1]);
		}
		return counts;
	}

	public LinkedHashMap<String, Long> countCallSource()
			throws HibernateException, SQLException, ParseException, ModelException {
		LinkedHashMap<String, Long> counts = new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countCSMonitor");
		List<Object> obList = query.list();
		for (Object object : obList) {
			Object[] row = (Object[]) object;
			counts.put((String) ((CallSource) row[0]).getStr(), (Long) row[1]);
		}
		return counts;
	}

	public LinkedHashMap<String, Long> countPriority()
			throws HibernateException, SQLException, ParseException, ModelException {
		LinkedHashMap<String, Long> counts = new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countPRMonitor");
		List<Object> obList = query.list();
		for (Object object : obList) {
			Object[] row = (Object[]) object;
			counts.put((String) ((Priority) row[0]).getStr(), (Long) row[1]);
		}
		return counts;
	}

	public LinkedHashMap<String, Long> countCallClassification()
			throws HibernateException, SQLException, ParseException, ModelException {
		LinkedHashMap<String, Long> counts = new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countCCMonitor");
		List<Object> obList = query.list();
		for (Object object : obList) {
			Object[] row = (Object[]) object;
			counts.put((String) row[0], (Long) row[1]);
		}
		return counts;
	}

	public LinkedHashMap<String, Long> countEntity()
			throws HibernateException, SQLException, ParseException, ModelException {
		LinkedHashMap<String, Long> counts = new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countENMonitor");
		query.setMaxResults(5);
		List<Object> obList = query.list();
		for (Object object : obList) {
			Object[] row = (Object[]) object;
			counts.put((String) row[0], (Long) row[1]);
		}
		return counts;
	}

	public LinkedHashMap<String, Long> countEntityCategory()
			throws HibernateException, SQLException, ParseException, ModelException {
		LinkedHashMap<String, Long> counts = new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("UnsolvedCall.countENMonitor");
		List<Object> obList = query.list();
		for (Object object : obList) {
			Object[] row = (Object[]) object;
			counts.put((String) row[0], (Long) row[1]);
		}
		return counts;
	}

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

	public UnsolvedCall getByIdRC(Long unsolvedCallId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);

		select.add(Restrictions.eq("unsolvedCallId", unsolvedCallId));
		UnsolvedCall t = (UnsolvedCall) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public List<UnsolvedCall> getByEntity(int entityId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("entityEntityCategoryMaps", FetchMode.JOIN);
		select.createAlias("entityEntityCategoryMaps", "entityEntityCategoryMaps");
		select.add(Restrictions.eq("entityEntityCategoryMaps.entityEntityCategoryMapsPK.entityId", entityId));

		List<UnsolvedCall> t = select.list();
		return t;
	}

	// Teste Nathalia
	public List<UnsolvedCall> getByParentCallId(Long parentCallId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnsolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);

		select.add(Restrictions.eq("parentCallId", parentCallId));
		List<UnsolvedCall> t = select.list();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}
	// Fim teste Nathália

}
