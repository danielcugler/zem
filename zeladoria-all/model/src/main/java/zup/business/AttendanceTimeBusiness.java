package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.AttendanceTime;
import zup.bean.Entity;
import zup.dao.AttendanceTimeDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class AttendanceTimeBusiness extends AbstractBusiness<AttendanceTime, Integer> {

	private AttendanceTimeDAO AttendanceTimeDao;
	private static Logger logger = Logger.getLogger(AttendanceTime.class);

	public AttendanceTimeBusiness() {
		super(new AttendanceTimeDAO());
		this.AttendanceTimeDao = (AttendanceTimeDAO) getDao();
	}

	public AttendanceTime findById(Integer id) throws HibernateException, SQLException, ModelException, ParseException {
		AttendanceTime t = null;
		try {
			HibernateUtil.beginTransaction();
			t = AttendanceTimeDao.findById(id);
			return t;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public AttendanceTime findByEntityCategoryId(Integer id) throws SQLException {
		AttendanceTime t = null;
		try {
			HibernateUtil.beginTransaction();
			t = AttendanceTimeDao.findByEntityCategoryId(id);
			return t;
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return t;
	}

	public Map<Integer, List<AttendanceTime>> searchPag(int pagina, String name, String enabledS)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return AttendanceTimeDao.searchPag(pagina, name, enabledS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return new HashMap<Integer, List<AttendanceTime>>();
	}

	public static void main(String[] args) throws HibernateException, SQLException, ModelException {

		AttendanceTimeBusiness ab = new AttendanceTimeBusiness();
		EntityBusiness eb = new EntityBusiness();
		AttendanceTime at = new AttendanceTime();
		Entity et = eb.getById(1);
		at = ab.getById(1);
		System.out.println(at.getHighPriorityTime());
		System.out.println(et.getName() + ", " + et.getAttendanceTime().getLowPriorityTime());

	}
}
