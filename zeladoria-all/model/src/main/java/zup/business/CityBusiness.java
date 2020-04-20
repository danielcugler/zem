package zup.business;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.City;
import zup.dao.CityDAO;
import zup.exception.DataAccessLayerException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class CityBusiness extends AbstractBusiness<City, Integer> {

	private CityDAO CityDao;
	private static Logger logger = Logger.getLogger(City.class);

	public CityBusiness() {
		super(new CityDAO());
		this.CityDao = (CityDAO) getDao();
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
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public static void main(String[] args) {
		CityBusiness cb = new CityBusiness();
		try {
			List<City> clist = cb.getByEstado("MG");
			for (City c : clist)
				System.out.println(c.getCityId() + " : " + c.getName());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
