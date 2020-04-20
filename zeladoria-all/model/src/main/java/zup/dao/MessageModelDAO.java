package zup.dao;

import java.sql.SQLException;
import java.text.Normalizer;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.StringType;

import zup.bean.MessageModel;
import zup.bean.Result;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;
import zup.model.utils.HibernateUtil;

public class MessageModelDAO extends AbstractDAO<MessageModel, Integer> {
	private static Logger logger = Logger.getLogger(MessageModelDAO.class);

	public MessageModelDAO() {
		super(MessageModel.class);
	}

	@Override
	public Criteria getCriteria(Criteria select, String[] searchParams)
			throws HibernateException, SQLException, ModelException, ParseException {
		String name = searchParams[1];
		String subject = searchParams[2];
		String enabled = searchParams[3];
		if (!name.equals("z"))
			select.add(Restrictions.eq("name", name));
		if (!subject.equals("z"))
			select.add(Restrictions.eq("subject", subject));
		if (!enabled.equals("z"))
			select.add(Restrictions.eq("enabled", enabled));
		select.addOrder(Order.asc("name"));
		return select;
	}

	
	public Result<MessageModel> searchPag2(int page, String name, String subject, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(MessageModel.class);
		if (!name.isEmpty())
			select.add(Restrictions.ilike("name", "%" + name + "%"));
			//select.add(Restrictions.eq("name", name));
		if (!subject.isEmpty())
			select.add(Restrictions.ilike("subject", "%" + subject + "%"));
			//select.add(Restrictions.eq("subject", subject));
		if (!enabledS.isEmpty())
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));		
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		return new Result<MessageModel>(count,(List<MessageModel>) select.list());
	}

	
	
	
	
	public Map<Integer, List<MessageModel>> searchPag(int page, String name, String subject, String enabledS)
			throws HibernateException, SQLException, ParseException, ModelException {
		Map<Integer, List<MessageModel>> map = new HashMap<Integer, List<MessageModel>>();
		Criteria select = HibernateUtil.getSession().createCriteria(MessageModel.class);
		if (name.equals("z") && subject.equals("z") && enabledS.equals("z")) {
			int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
			select.setProjection(null);
			select.addOrder(Order.asc("name"));
			select.setFirstResult((page - 1) * Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			select.setMaxResults(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
			List<MessageModel> bms = (List<MessageModel>) select.list();
			map.put(count, bms);
			if (bms.isEmpty())
				throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
			return map;
		}
		if (!name.equals("z"))
			select.add(Restrictions.eq("name", name));
		if (!subject.equals("z"))
			select.add(Restrictions.eq("subject", subject));
		if (!enabledS.equals("z"))
			select.add(Restrictions.eq("enabled", Enabled.fromValue(Integer.parseInt(enabledS))));
		int count = ((Number) select.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		select.setProjection(null);
		select.addOrder(Order.asc("name"));
		select.setFirstResult((page - 1) * Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		select.setMaxResults(Integer.parseInt(
				SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE)));
		List<MessageModel> bms = (List<MessageModel>) select.list();
		map.put(count, bms);
		if (bms.isEmpty())
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return map;
	}

	public MessageModel findByName(String name) throws HibernateException, SQLException {
		Criteria select= HibernateUtil.getSession().createCriteria(MessageModel.class);
		select.add(Restrictions.eq("name", name));		
				return (MessageModel) select.uniqueResult();
	}
	
	
	public List<MessageModel> findEnabled() throws HibernateException, SQLException {
		Criteria select= HibernateUtil.getSession().createCriteria(MessageModel.class);
		select.add(Restrictions.eq("enabled", Enabled.ENABLED));		
				return (List<MessageModel>) select.list();
	}
	
	@Override
	public List<MessageModel> findAll() throws HibernateException, SQLException {
		return HibernateUtil.getSession().createCriteria(MessageModel.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).addOrder(Order.asc("name")).list();
	}
	
	
	
	public List<MessageModel> getAsento(String nome) throws SQLException {		 
		Criteria select= HibernateUtil.getSession().createCriteria(MessageModel.class); 		 		 
		ProjectionList retorno = Projections.projectionList().create();

		retorno.add(Projections.property("messageModelId"), "messageModelId");
		retorno.add(Projections.property("name"), "name");
		retorno.add(Projections.property("messageBody"), "messageBody");
		retorno.add(Projections.property("enabled"), "enabled");
		select.setProjection(retorno);
		select.add( Restrictions.sqlRestriction("to_ascii({alias}.name) ilike to_ascii(?)", "%"+nome+"%", StringType.INSTANCE));
		select.setResultTransformer(new AliasToBeanResultTransformer(MessageModel.class));
		return (List<MessageModel>) select.list();		 
		 
		}
	
	public List<MessageModel> getCombos() throws HibernateException, SQLException {
		Criteria select= HibernateUtil.getSession().createCriteria(MessageModel.class);
		ProjectionList proList = Projections.projectionList();
        proList.add(Projections.property("name"));
        proList.add(Projections.property("subject"));
        select.setProjection(proList);
        select.setResultTransformer(new ResultTransformer() {

            public Object transformTuple(Object[] row, String[] arg1) {
                MessageModel m = new MessageModel();
                m.setName((String)row[0]);
                m.setSubject((String)row[1]);
                return m;
            }

            public List transformList(List arg0) {

                return arg0;
            }
        });
        select.addOrder(Order.asc("name"));
				return select.list();
	}
}
