package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.CallClassification;
import zup.bean.MessageModel;
import zup.bean.Result;
import zup.bean.SystemUserProfile;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class CallClassificationDAO extends AbstractDAO<CallClassification, Integer> {
	private static Logger logger = Logger.getLogger(CallClassificationDAO.class);

	public CallClassificationDAO() {
		super(CallClassification.class);
	}

	public Result<CallClassification> search(int page,String name)
			throws HibernateException, SQLException, ModelException, ParseException {
		Criteria select = HibernateUtil.getSession().createCriteria(CallClassification.class);
		if (!name.isEmpty())
			select.add(Restrictions.like("name", name));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<CallClassification>(count,(List<CallClassification>) select.list());
		}
	
	@Override
	public List<CallClassification> findAll() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(CallClassification.class);
		select.addOrder(Order.asc("name"));
		return (List<CallClassification>) select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

}
