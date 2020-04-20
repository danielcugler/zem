package zup.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import zup.bean.Citizen;
import zup.bean.ReadBroadcastMessage;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;


public class ReadBroadcastMessageDAO extends AbstractDAO<ReadBroadcastMessage, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public ReadBroadcastMessageDAO() {
		super(ReadBroadcastMessage.class);
	}
	public List<ReadBroadcastMessage> getRead(String citizen_cpf) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(ReadBroadcastMessage.class);
		select.add(Restrictions.eq("citizen_cpf", citizen_cpf));
		List<ReadBroadcastMessage> t = (List<ReadBroadcastMessage>) select.list();
		return t;
	}
	

}
