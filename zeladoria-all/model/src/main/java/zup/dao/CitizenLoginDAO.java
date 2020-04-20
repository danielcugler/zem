package zup.dao;



import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import zup.bean.CitizenLogin;
import zup.bean.EntityCategory;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class CitizenLoginDAO extends AbstractDAO<CitizenLogin, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public CitizenLoginDAO() {
		super(CitizenLogin.class);
	}
	public CitizenLogin findByToken(String token) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(CitizenLogin.class);
		select.add(Restrictions.eq("token", token));
		CitizenLogin t = (CitizenLogin) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}

	public CitizenLogin findByCpf(String cpf) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(CitizenLogin.class);
		select.add(Restrictions.eq("citizenId", cpf));
		CitizenLogin t = (CitizenLogin) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}
	
}