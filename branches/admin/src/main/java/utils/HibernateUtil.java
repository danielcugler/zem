package utils;

import java.sql.SQLException;
import java.util.Properties;

import javax.validation.ConstraintViolationException;

import org.hibernate.HibernateException;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.stat.Statistics;

public class HibernateUtil {

	private static SessionFactory sessionFactory;
	private static Configuration configuration;
	private static Logger logger = Logger.getLogger(HibernateUtil.class);
	private static Properties prop;

	static {
		try {
			configuration = new Configuration().configure("hibernate.cfg.xml");
			prop = configuration.getProperties();
			configuration.setProperties(prop);
			logger.info("Hibernate Configuration loaded");
			StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
			serviceRegistryBuilder.applySettings(configuration.getProperties());
			ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
			logger.info("Hibernate serviceRegistry created");
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (Throwable ex) {
			logger.error("Initial SessionFactory creation failed." + ex);
			System.out.println(ex.getMessage());
			throw new ExceptionInInitializerError(ex);
		}
	}

	 public static Statistics getStatistics() {
		    if (!sessionFactory.getStatistics().isStatisticsEnabled()) {
		    	sessionFactory.getStatistics().setStatisticsEnabled(true);
		    }
		    return sessionFactory.getStatistics();
		  }
	
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Session getSession() throws SQLException {
		Session session = null;

		session = sessionFactory.getCurrentSession();

		if (session == null || !session.isOpen()) {
			session = sessionFactory.openSession();
		}

		return session;
	}

	public static void beginTransaction() throws SQLException {

		getSession().beginTransaction();

	}

	public static void commitTransaction() throws SQLException, ConstraintViolationException {
		getSession().getTransaction().commit();
	}

	public static void rollBackTransaction() throws SQLException {

		getSession().getTransaction().rollback();

	}

	public static void closeSession() throws HibernateException, SQLException {
		getSession().close();

	}
}
