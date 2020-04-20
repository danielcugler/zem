package zup.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.BroadcastMessage;
import zup.bean.Citizen;
import zup.bean.Result;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class CitizenDAO extends AbstractDAO<Citizen, String> {

	public CitizenDAO() {
		super(Citizen.class);
	}

	public Result<Citizen> searchPag2(int page, String name, String email, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		if (!name.isEmpty())
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		if (!email.isEmpty())
			select.add(Restrictions.ilike("email", "%" + email + "%"));
		if (!enabledS.isEmpty())
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<Citizen>(count, (List<Citizen>) select.list());
	}

	public Map<Integer, List<Citizen>> searchPag(int page, String name, String email, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<Citizen>> map = new HashMap<Integer, List<Citizen>>();
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		if (name.equals("z") && email.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));

			List<Citizen> bms = (List<Citizen>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.ilike("name", "%" + name + "%"));
		if (!email.equals("z"))
			select.add(Restrictions.ilike("email", "%" + email + "%"));
		if (!enabledS.equals("z"))
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<Citizen> bms = (List<Citizen>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	@Override
	public Citizen getById(String citizen_cpf) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		select.setFetchMode("neighborhoodId", FetchMode.JOIN);
		select.add(Restrictions.eq("citizen_cpf", citizen_cpf));
		Citizen t = (Citizen) select.uniqueResult();
		return t;
	}

	public Citizen getByIdRead(String citizen_cpf) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		select.setFetchMode("neighborhoodId", FetchMode.JOIN);
		select.setFetchMode("broadcastMessageCollection", FetchMode.JOIN);		
		select.add(Restrictions.eq("citizen_cpf", citizen_cpf));
		Citizen t = (Citizen) select.uniqueResult();
		return t;
	}
	
	public List<Integer> getRead(String citizen_cpf) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		select.setFetchMode("neighborhoodId", FetchMode.JOIN);
		select.createAlias("broadcastMessageCollection", "broadcastMessageCollection");		
		select.add(Restrictions.eq("citizen_cpf", citizen_cpf));
		select.setProjection(Projections.property("broadcastMessageCollection.broadcastMessageId"));
		return select.list();
	}
	
	
	public Citizen getByFacebookId(String facebookId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		select.setFetchMode("neighborhood", FetchMode.JOIN);
		select.add(Restrictions.eq("facebookId", facebookId));
		Citizen t = (Citizen) select.uniqueResult();
		return t;
	}

	public Citizen getByPublicKey(String publicKey) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		select.add(Restrictions.eq("publicKey", publicKey));
		Citizen t = (Citizen) select.uniqueResult();
		return t;
	}

	public Citizen getByEmail(String email) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		select.setFetchMode("neighborhood", FetchMode.JOIN);
		select.add(Restrictions.eq("email", email));
		Citizen t = (Citizen) select.uniqueResult();
		return t;
	}

	public Citizen login(String email) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(Citizen.class);
		select.add(Restrictions.like("email", email));
		List<Citizen> t = select.list();
		if (t.size() > 1) {
			for (Citizen t1 : t) {
				if (t1.getCitizen_cpf().length() == 11)
					return t1;
			}
		} else {
			if (t.isEmpty())
				return null;
			else
				return t.get(0);
		}
		return null;
	}

	// Count Citizen
	public Long countCitizen() throws HibernateException, SQLException, ParseException, ModelException {
		LinkedHashMap<String, Long> counts = new LinkedHashMap<String, Long>();
		Query query = HibernateUtil.getSession().getNamedQuery("Citizen.countCitizen");
		Long quantCitizen = (Long) query.uniqueResult();
		return quantCitizen;
	}

}
