package zup.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import zup.bean.Address;
import zup.model.utils.HibernateUtil;

public class ExpiredDAO {

	public void delAuto() throws HibernateException, SQLException {
		String queryString = "SELECT cast(public.delauto() as text)";
		Query query = HibernateUtil.getSession().createQuery(queryString);
		query.uniqueResult();
	}
}
