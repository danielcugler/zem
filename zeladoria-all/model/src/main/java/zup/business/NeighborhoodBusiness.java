package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import zup.bean.Neighborhood;
import zup.bean.Result;
import zup.dao.NeighborhoodDAO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class NeighborhoodBusiness extends AbstractBusiness<Neighborhood, Integer> {

	private NeighborhoodDAO NeighborhoodDao;
	private static Logger logger = Logger.getLogger(Neighborhood.class);

	public NeighborhoodBusiness() {
		super(new NeighborhoodDAO());
		this.NeighborhoodDao = (NeighborhoodDAO) getDao();
	}

	public List<Neighborhood> findByCity(Integer cidade) throws HibernateException, SQLException {
		List<Neighborhood> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = NeighborhoodDao.findByCity(cidade);
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

	public Neighborhood findByIdCity(Integer cidade, Integer neighborhoodId) throws HibernateException, SQLException {
		Neighborhood n;
		try {
			HibernateUtil.beginTransaction();
			n = NeighborhoodDao.findByIdCity(cidade, neighborhoodId);
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
		return n;
	}

	
	public Neighborhood findByCityNeighborhood(Integer cidade, String neighborhood) throws HibernateException, SQLException {
		Neighborhood n;
		try {
			HibernateUtil.beginTransaction();
			n = NeighborhoodDao.findByCityNeighborhood(cidade, neighborhood);
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
		return n;
	}
	
	public List<Neighborhood> findByName(String name) throws HibernateException, SQLException {
		List<Neighborhood> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = NeighborhoodDao.findByName(name);
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

	public Result<Neighborhood> searchPag(int page, String cityId, String name)
			throws HibernateException, SQLException, ParseException, ModelException {
		Result<Neighborhood> result;
		try {
			HibernateUtil.beginTransaction();
			result = NeighborhoodDao.searchPag(page, cityId, name);
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
		return result;
	}
	public static void main(String[] args) {
		NeighborhoodBusiness nb=new NeighborhoodBusiness();
		CityBusiness cb=new CityBusiness();
		Neighborhood n=new Neighborhood();
		n.setName("TEste");
		try {
			n=nb.findByCityNeighborhood(3551, "Sao Cristovao");
			System.out.println(n.getName());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataAccessLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
