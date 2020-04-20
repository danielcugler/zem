package zup.business;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.chainsaw.Main;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import zup.bean.Citizen;
import zup.bean.CitizenLogin;
import zup.dao.CitizenLoginDAO;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class CitizenLoginBusiness extends AbstractBusiness<CitizenLogin, Integer> {

	private CitizenLoginDAO CitizenLoginDao;
	private static Logger logger = Logger.getLogger(CitizenLogin.class);

	public CitizenLoginBusiness() {
		super(new CitizenLoginDAO());
		this.CitizenLoginDao = (CitizenLoginDAO) getDao();
	}

	public CitizenLogin findByToken(String token) throws SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return CitizenLoginDao.findByToken(token);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public CitizenLogin findByCpf(String cpf) throws SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return CitizenLoginDao.findByCpf(cpf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}

	public static void main(String[] args) {
		CitizenLoginBusiness cb = new CitizenLoginBusiness();
		CitizenLogin cl;
		try {
			cl = cb.findByToken("de653d67afd9f8d3901b9da0a14c15e2");
			System.out.println(cl.getCitizenId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
