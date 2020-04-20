package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.WebUser;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class WebUserDAO extends AbstractDAO<WebUser, String> {
	private static Logger logger = Logger.getLogger(WebUserDAO.class);

	public WebUserDAO() {
		super(WebUser.class);
	}

	public Map<Integer, List<WebUser>> searchPag(int page, String name, String email, String webUserCpf)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<WebUser>> map = new HashMap<Integer, List<WebUser>>();
		Criteria select = HibernateUtil.getSession().createCriteria(WebUser.class);
		if (name.equals("z") && email.equals("z") && webUserCpf.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<WebUser> bms = (List<WebUser>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.eq("name", name));
		if (!email.equals("z"))
			select.add(Restrictions.eq("email", email));
		if (!webUserCpf.equals("z"))
			select.add(Restrictions.eq("webUserCpf", webUserCpf));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<WebUser> bms = (List<WebUser>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	public WebUser login(String webUserCpf) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(WebUser.class);
		select.add(Restrictions.eq("webUserCpf", webUserCpf));
		WebUser t = (WebUser) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

}