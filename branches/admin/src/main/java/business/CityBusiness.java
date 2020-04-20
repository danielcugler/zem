package business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import bean.City;
import dao.CityDAO;
import utils.HibernateUtil;


public class CityBusiness extends AbstractBusiness<City, Integer> {

	private CityDAO CityDao;
	private static Logger logger = Logger.getLogger(City.class);

	public CityBusiness() {
		super(new CityDAO());
		this.CityDao = (CityDAO) getDao();
	}

	public void delAuto() throws HibernateException, SQLException, ParseException {
		try {
			HibernateUtil.beginTransaction();
			CityDao.delAuto();
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			e.printStackTrace();
			throw new SQLException("Erro");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public List<City> getByEstado(String estado) throws HibernateException, SQLException {
		List<City> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = CityDao.getByEstado(estado);
			HibernateUtil.commitTransaction();

		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public static void main(String[] args) {
		CityBusiness cb = new CityBusiness();
		try {
			cb.delAuto();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
