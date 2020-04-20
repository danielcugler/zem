package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.AttendanceTime;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class AttendanceTimeDAO extends AbstractDAO<AttendanceTime, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public AttendanceTimeDAO() {
		super(AttendanceTime.class);
	}

	public Map<Integer, List<AttendanceTime>> searchPag(int page, String name, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<AttendanceTime>> map = new HashMap<Integer, List<AttendanceTime>>();
		Criteria select = HibernateUtil.getSession().createCriteria(AttendanceTime.class);
		if (name.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<AttendanceTime> bms = (List<AttendanceTime>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.eq("entity.entityId", Integer.parseInt(name)));
		if (!enabledS.equals("z"))
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<AttendanceTime> bms = (List<AttendanceTime>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	public AttendanceTime findById(Integer id) throws SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(AttendanceTime.class);
		select.add(Restrictions.eq("entityId", id));
		return (AttendanceTime) select.uniqueResult();
	}

	public AttendanceTime findByEntityCategoryId(Integer id) throws SQLException {
		Query query = HibernateUtil.getSession().getNamedQuery("AttendanceTime.findByEntityCategoryId");
		query.setParameter("entityCategoryId", id);
		return (AttendanceTime) query.uniqueResult();
	}

	@Override
	public AttendanceTime getById(Integer AttendanceTimeId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(AttendanceTime.class);
		select.setFetchMode("entity", FetchMode.JOIN);
		select.add(Restrictions.eq("entityId", AttendanceTimeId));
		AttendanceTime t = (AttendanceTime) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

}
