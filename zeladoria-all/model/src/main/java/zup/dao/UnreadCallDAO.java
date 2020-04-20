package zup.dao;

import zup.bean.Entity;
import zup.bean.MessageModel;
import zup.bean.UnreadCall;
import zup.enums.Enabled;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

public class UnreadCallDAO extends AbstractDAO<UnreadCall, Long> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public UnreadCallDAO() {
		super(UnreadCall.class);
	}
	
	public void saveOrUpdate(UnreadCall unreadCall) throws SQLException {
		HibernateUtil.getSession().flush();
		HibernateUtil.getSession().clear();
		HibernateUtil.getSession().saveOrUpdate(unreadCall);
	}

	public List<UnreadCall> findUnread() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnreadCall.class);
		select.add(Restrictions.eq("read", false));
		return (List<UnreadCall>) select.list();
	}

	public List<UnreadCall> findList(List<Long> calls) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnreadCall.class);
		select.setFetchMode("unsolvedCallId", FetchMode.JOIN);
		select.setFetchMode("solvedCallId", FetchMode.JOIN);
		select.add(Restrictions.in("unreadCallId", calls));
		return (List<UnreadCall>) select.list();
	}
	
	public List<UnreadCall> findRead() throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnreadCall.class);
		select.add(Restrictions.eq("read", true));
		return (List<UnreadCall>) select.list();
	}

	public UnreadCall findByUnsolvedcallId(Long unsolvedcallId) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnreadCall.class);
		select.add(Restrictions.eq("unsolvedCallId.unsolvedCallId", unsolvedcallId));
		return (UnreadCall) select.uniqueResult();
	}

	public UnreadCall findBySolvedcallId(Long solvedcallId) throws HibernateException, SQLException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnreadCall.class);
		select.add(Restrictions.eq("solvedCallId.solvedCallId", solvedcallId));
		return (UnreadCall) select.uniqueResult();
	}
	

	
	@Override
	public UnreadCall getById(Long callId) throws SQLException, ModelException {
		Criteria select = HibernateUtil.getSession().createCriteria(UnreadCall.class);
		select.setFetchMode("unsolvedCallId", FetchMode.JOIN);
		select.setFetchMode("solvedCallId", FetchMode.JOIN);
		select.add(Restrictions.eq("unreadCallId", callId));
		UnreadCall t = (UnreadCall) select.uniqueResult();
		if (t == null)
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND));
		return t;
	}
}
