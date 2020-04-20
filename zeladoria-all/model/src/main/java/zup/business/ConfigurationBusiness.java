package zup.business;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.BroadcastMessage;
import zup.bean.Configuration;
import zup.dao.ConfigurationDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class ConfigurationBusiness extends AbstractBusiness<Configuration, Integer> {

	private ConfigurationDAO configurationDao;
	private static Logger logger = Logger.getLogger(Configuration.class);

	public ConfigurationBusiness() {
		super(new ConfigurationDAO());
		this.configurationDao = (ConfigurationDAO) getDao();
	}

	public Configuration search()
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		try{
			HibernateUtil.beginTransaction();
			return configurationDao.search();
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;		
	}

}
