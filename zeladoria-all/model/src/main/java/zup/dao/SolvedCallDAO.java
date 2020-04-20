package zup.dao;

import java.sql.SQLException;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.SolvedCall;
import zup.bean.UnsolvedCall;
import zup.dto.NeighborhoodChartDTO;
import zup.dto.QualificationChartDTO;
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

public class SolvedCallDAO extends AbstractDAO<SolvedCall, Long> {
	private static Logger logger = Logger.getLogger(SolvedCallDAO.class);

	public SolvedCallDAO() {
		super(SolvedCall.class);
	}

	public SolvedCall getLastChild(Long solvedCallId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(SolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.add(Restrictions.eq("parentCallId.solvedCallId", solvedCallId));
		select.addOrder(Order.desc("solvedCallId"));
		select.setMaxResults(1);
		SolvedCall t = (SolvedCall) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public Map<Integer, List<SolvedCall>> searchPag(int page, String periodod, String periodoa, String callSource,
			String callclassificationid, String entityid, String priority, String userid, String callStatus)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<SolvedCall>> map = new HashMap<Integer, List<SolvedCall>>();
		Date periodod2 = new Date();
		Date periodoa2 = new Date();
		Criteria select = HibernateUtil.getSession().createCriteria(SolvedCall.class);
		if (periodod.equals("z") && periodoa.equals("z") && callSource.equals("z") && callclassificationid.equals("z")
				&& entityid.equals("z") && priority.equals("z") && userid.equals("z") && callStatus.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);

			select.addOrder(Order.asc("solvedCallId"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<SolvedCall> bms = (List<SolvedCall>) select.list();
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
		if (!callclassificationid.equals("z"))
			select.add(Restrictions.eq("callClassificationId.callClassificationId",
					Integer.parseInt(callclassificationid)));
		if (!entityid.equals("z"))
			select.add(Restrictions.eq("secretaryCategoryTarget.EntityCategoryId", Integer.parseInt(entityid)));
		if (!priority.equals("z"))
			select.add(Restrictions.eq("priority", Priority.fromValue(Integer.parseInt(priority))));
		if (!userid.equals("z"))
			select.add(Restrictions.eq("updatedOrModeratedBy.systemUserUsername", userid));
		if (!callStatus.equals("z"))
			select.add(Restrictions.eq("callStatus", CallStatus.fromValue(Integer.parseInt(callStatus))));

		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("solvedCallId"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<SolvedCall> bms = (List<SolvedCall>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	public List<Long> findAllLastChild() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("SolvedCall.findAllLastChild");
		List<Long> tList = query.list();
		return tList;
	}

	public List<Long> findAllLastChildId(Long id) throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("SolvedCall.findAllLastChildId");
		query.setLong("parentCallId", id);
		List<Long> tList = query.list();
		return tList;
	}

	public List<SolvedCall> searchCitizenFinal(String periodod, String periodoa, String entityid,
			String entityCategoryId, String callProgress, String description, String citizenCpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<SolvedCall>> map = new HashMap<Integer, List<SolvedCall>>();
		List<Long> llist = findAllLastChild();
		System.out.println(llist.toString());
		Criteria select = HibernateUtil.getSession().createCriteria(SolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.createAlias("parentCallId", "parent");
		select.add(Restrictions.in("solvedCallId", llist));
		select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.addOrder(Order.desc("parent.creationOrUpdateDate"));
		System.out.println((periodod == "") + ", " + (periodoa == "") + ", " + entityid.equals("z") + ", "
				+ description.equals("z") + ", " + callProgress.equals("z"));
		if (periodod.equals("") && periodoa.equals("") && entityid.equals("z") && description.equals("z")
				&& callProgress.equals("z")) {
			return (List<SolvedCall>) select.list();
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

		return (List<SolvedCall>) select.list();
	}

	public List<SolvedCall> searchCitizenFinal2(String periodod, String periodoa, String entityid,
			String entityCategoryId, String callProgress, String description, String citizenCpf,
			List<Long> excludeCalls) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<SolvedCall>> map = new HashMap<Integer, List<SolvedCall>>();
		List<Long> llist = findAllLastChild();
		if (llist.isEmpty())
			return new ArrayList<SolvedCall>();
		Criteria select = HibernateUtil.getSession().createCriteria(SolvedCall.class);
		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.createAlias("parentCallId", "parent");
		select.add(Restrictions.in("solvedCallId", llist));
		select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		select.add(Restrictions.eq("parent.remove", false));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.addOrder(Order.desc("parent.creationOrUpdateDate"));
		if (!excludeCalls.isEmpty())
			select.add(Restrictions.not(Restrictions.in("parent.solvedCallId", excludeCalls)));
		if (!periodod.isEmpty()) {
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
		if (!periodoa.isEmpty()) {
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
		if (!description.isEmpty()) {
			select.createAlias("description", "desc");
			select.add(Restrictions.ilike("desc.information", "%" + description + "%"));
		}
		if (!entityid.isEmpty()) {
			select.createAlias("entityEntityCategoryMaps", "ent");
			select.add(Restrictions.eq("ent.entityEntityCategoryMapsPK.entityId", Integer.parseInt(entityid)));
		}
		if (!entityCategoryId.isEmpty()) {
			select.createAlias("entityEntityCategoryMaps", "entc");
			select.add(Restrictions.eq("entc.entityEntityCategoryMapsPK.entityCategoryId",
					Integer.parseInt(entityCategoryId)));
		}
		if (!callProgress.isEmpty())
			select.add(Restrictions.eq("callProgress", CallProgress.fromValue(Integer.parseInt(callProgress))));
		if (!citizenCpf.isEmpty())
			select.add(Restrictions.eq("citizenCpf.citizen_cpf", citizenCpf));
		return (List<SolvedCall>) select.list();
	}

	public SolvedCall searchCitizenPk(Long solvedCallId, String publicKey)
			throws HibernateException, SQLException, ParseException, ModelException {
		List<Long> llist = findAllLastChildId(solvedCallId);
		Criteria select = HibernateUtil.getSession().createCriteria(SolvedCall.class);
		select.add(Restrictions.in("solvedCallId", llist));
		select.createAlias("citizenCpf", "citizenCpf");
		select.add(Restrictions.eq("citizenCpf.publicKey", publicKey));
		select.createAlias("parentCallId", "parentdate");
		select.addOrder(Order.desc("parentdate.creationOrUpdateDate"));
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		SolvedCall bms = (SolvedCall) select.uniqueResult();
		return bms;
	}

	@Override
	public SolvedCall getById(Long solvedCallId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(SolvedCall.class);

		select.setFetchMode("parentCallId", FetchMode.JOIN);
		select.setFetchMode("citizenCpf", FetchMode.JOIN);

		select.add(Restrictions.eq("solvedCallId", solvedCallId));
		SolvedCall t = (SolvedCall) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	// Count Chamados Finalizados
	public Long countCallFinalized() throws HibernateException, SQLException, ParseException, ModelException {
		LinkedHashMap<String, Long> counts = new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("SolvedCall.countCallFinalized");
		Long quantCallFinalized = (Long) query.uniqueResult();
		return quantCallFinalized;
	}
	
	public List<QualificationChartDTO> countQualificationChart() throws SQLException, ModelException {
		Query query = HibernateUtil.getSession().getNamedQuery("SolvedCall.countQualification");
		query.setMaxResults(5);
		List<QualificationChartDTO> list = new ArrayList<QualificationChartDTO>();
		List<Object[]> tList = query.list();
		for (Object[] obj : tList) {
			QualificationChartDTO n = new QualificationChartDTO();
			n.setCountQualification((Long) obj[0]);
			n.setValueQualification((Short) obj[1]);
			list.add(n);
		}
		return list;
	}

}
