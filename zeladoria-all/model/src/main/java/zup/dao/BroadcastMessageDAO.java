package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import zup.bean.BroadcastMessage;
import zup.bean.BroadcastMessageCategory;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.enums.BroadcastMessageCategoryName;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class BroadcastMessageDAO extends AbstractDAO<BroadcastMessage, Integer> {
	private static Logger logger = Logger.getLogger(BroadcastMessageDAO.class);

	public BroadcastMessageDAO() {
		super(BroadcastMessage.class);
	}

	@Override
	public List<BroadcastMessage> findAll() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
		return select.addOrder(Order.asc("creationDate")).list();
	}

	public void removeExpired() throws SQLException {
	    String queryString = "SELECT count(*) FROM public.delauto();";
	    Query query = HibernateUtil.getSession().createQuery(queryString);
			query.uniqueResult();
	}
	
	public Map<Integer, List<BroadcastMessage>> search(int page, String subject, String bmc, String enabled,
			String datec, String datep) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<BroadcastMessage>> map = new HashMap<Integer, List<BroadcastMessage>>();
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
		select.setFetchMode("systemUserProfileId", FetchMode.JOIN);
		select.setFetchMode("broadcastMessageCategoryId", FetchMode.JOIN);
		if (subject.equals("z") && enabled.equals("z") && bmc.equals("z") && datec.equals("z") && datep.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("creationDate"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<BroadcastMessage> bms = (List<BroadcastMessage>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}

		if (!subject.equals("z"))
			select.add(Restrictions.ilike("subject", "%" + subject + "%"));
		if (!enabled.equals("z"))
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabled))));
		if (!bmc.equals("z")) {
			DetachedCriteria dc = DetachedCriteria.forClass(BroadcastMessageCategory.class, "bmc");
			dc.add(Restrictions.eq("name", BroadcastMessageCategoryName.fromValue(Integer.parseInt(bmc))));
			dc.setProjection(Projections.property("bmc.broadcastMessageCategoryId"));
			select.add(Subqueries.propertyIn("broadcastMessageCategoryId", dc));
		}
		if (!datec.equals("z")) {
			Date cdate = new SimpleDateFormat("dd/MM/yyyy").parse(datec);
			select.add(Restrictions.ge("creationDate", cdate));
		}
		if (!datep.equals("z")) {
			Date pdate = new SimpleDateFormat("dd/MM/yyyy").parse(datep);
			select.add(Restrictions.le("creationDate", pdate));
		}

		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("creationDate"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<BroadcastMessage> bms = (List<BroadcastMessage>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}
	
	
	public Result<BroadcastMessage> searchPag2(int page, String subject,String bmc, String enabled,String datec, String datep)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
		if (!subject.isEmpty())
			select.add(Restrictions.ilike("subject", "%" + subject + "%"));
		if (!bmc.isEmpty()) {
			DetachedCriteria dc = DetachedCriteria.forClass(BroadcastMessageCategory.class, "bmc");
			dc.add(Restrictions.eq("name", BroadcastMessageCategoryName.fromValue(Integer.parseInt(bmc))));
			dc.setProjection(Projections.property("bmc.broadcastMessageCategoryId"));
			select.add(Subqueries.propertyIn("broadcastMessageCategoryId", dc));
		}
		if (!enabled.isEmpty())
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabled))));
		if (!datec.isEmpty()) 
			select.add(Restrictions.ge("creationDate", new SimpleDateFormat("ddMMyyyy").parse(datec)));
		if (!datep.isEmpty()) 
			select.add(Restrictions.le("creationDate", new SimpleDateFormat("ddMMyyyy HH:mm:ss").parse(datep+" 23:59:59")));		

		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.desc("creationDate"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<BroadcastMessage>(count,(List<BroadcastMessage>) select.list());
	}
	

	
	public List<BroadcastMessage> searchMobile()
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
			select.add(Restrictions.eq("enabled",Enabled.ENABLED));
			select.add(Restrictions.isNotNull(("publicationDate")));
			select.add(Restrictions.ge("expirationDate", new Date()));
		select.addOrder(Order.desc("publicationDate"));		
		return (List<BroadcastMessage>) select.list();
	}

	public List<BroadcastMessage> searchMobile(int page)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
			select.add(Restrictions.eq("enabled",Enabled.ENABLED));
			select.add(Restrictions.isNotNull(("publicationDate")));
			select.add(Restrictions.ge("expirationDate", new Date()));
		select.addOrder(Order.desc("publicationDate"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return (List<BroadcastMessage>) select.list();
	}
	
	public List<BroadcastMessage> searchMobile(int page, int category)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
		select.createAlias("broadcastMessageCategoryId","broadcastMessageCategoryId");
		select.add(Restrictions.eq("enabled",Enabled.ENABLED));		
		if(category!= -1)
		select.add(Restrictions.eq("broadcastMessageCategoryId.name",BroadcastMessageCategoryName.fromValue(category)));		
			select.add(Restrictions.isNotNull(("publicationDate")));
			select.add(Restrictions.ge("expirationDate", new Date()));
		select.addOrder(Order.desc("publicationDate"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return (List<BroadcastMessage>) select.list();
	}
	
	public List<BroadcastMessage> searchMobile2(List<Integer> excluded)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
			select.add(Restrictions.eq("enabled",Enabled.ENABLED));
			if(!excluded.isEmpty())
				select.add(Restrictions.not(Restrictions.in("broadcastMessageId", excluded)));
			select.add(Restrictions.isNotNull(("publicationDate")));
		select.addOrder(Order.desc("publicationDate"));		
		return (List<BroadcastMessage>) select.list();
	}
	
	@Override
	public BroadcastMessage getById(Integer broadcastMessageId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(BroadcastMessage.class);
		select.add(Restrictions.eq("broadcastMessageId", broadcastMessageId));
		BroadcastMessage t = (BroadcastMessage) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

}
