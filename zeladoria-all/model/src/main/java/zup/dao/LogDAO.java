package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import zup.bean.Log;
import zup.bean.Result;
import zup.enums.Enabled;
import zup.enums.InformationType;
import zup.enums.OperationType;

import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class LogDAO extends AbstractDAO<Log, Long> {

	public LogDAO() {
		super(Log.class);
	}

	public Map<Integer, List<Log>> searchPag(int page, String systemUser, String informationType, String operationType,
			String datec, String datef) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<Log>> map = new HashMap<Integer, List<Log>>();
		Criteria select = HibernateUtil.getSession().createCriteria(Log.class);
		select.setFetchMode("systemUserUsername", FetchMode.JOIN);

		if (systemUser.equals("z") && informationType.equals("z") && operationType.equals("z") && datec.equals("z")
				&& datef.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.desc("changeDate"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<Log> bms = (List<Log>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!systemUser.equals("z"))
			select.add(Restrictions.ilike("systemUserUsername.systemUserUsername", "%" + systemUser + "%"));
		if (!informationType.equals("z"))
			select.add(
					Restrictions.eq("informationType", InformationType.fromValue(Integer.parseInt(informationType))));
		if (!operationType.equals("z")) {
			select.add(Restrictions.eq("operationType", OperationType.fromValue(Integer.parseInt(operationType))));
		}
		if (!datec.equals("z")) {
			Date cdate = new SimpleDateFormat("ddMMyyyy").parse(datec);
			select.add(Restrictions.ge("changeDate", cdate));
		}
		if (!datef.equals("z")) {
			Date pdate = new SimpleDateFormat("ddMMyyyy").parse(datef);
			select.add(Restrictions.le("changeDate", pdate));
		}

		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.desc("changeDate"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<Log> bms = (List<Log>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	public Result<Log> searchPag2(int page, String systemUser, String informationType, String operationType,
			String fromDate, String toDate) throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<Log>> map = new HashMap<Integer, List<Log>>();
		Criteria select = HibernateUtil.getSession().createCriteria(Log.class);
		if (!fromDate.isEmpty()) {
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);
			select.add(Restrictions.ge("changeDate", date1));
		}
		if (!toDate.isEmpty()) {
			Date date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(toDate+" 23:59:59");
			select.add(Restrictions.le("changeDate", date2));
		}
		if (!systemUser.isEmpty())
			select.add(Restrictions.ilike("systemUserUsername.systemUserUsername", systemUser));
		if (!informationType.isEmpty())
			select.add(
					Restrictions.eq("informationType", InformationType.fromValue(Integer.parseInt(informationType))));
		if (!operationType.isEmpty())
			select.add(Restrictions.eq("operationType", OperationType.fromValue(Integer.parseInt(operationType))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.desc("changeDate"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<Log> bms = (List<Log>) select.list();
		return new Result<Log>(count, bms);
	}

}