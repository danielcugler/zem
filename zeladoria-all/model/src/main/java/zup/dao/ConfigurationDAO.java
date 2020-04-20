package zup.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;

import zup.bean.BroadcastMessage;
import zup.bean.Configuration;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class ConfigurationDAO extends AbstractDAO<Configuration, Integer>{
	
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public ConfigurationDAO() {
		super(Configuration.class);
	}
	
	public Configuration search() throws SQLException, ModelException{		
		Query query = HibernateUtil.getSession().getNamedQuery("Configuration.findAll");
		Configuration configuration = (Configuration) query.uniqueResult();
		if(configuration == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return configuration;
	}
	
}
