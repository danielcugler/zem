package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import zup.bean.UnsolvedCall;
import zup.dao.UnsolvedCallDAO;
import zup.dto.DelayCountDTO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class UnsolvedCallBusiness extends AbstractBusiness<UnsolvedCall, Long> {
	private static Logger logger = Logger.getLogger(UnsolvedCall.class);

	private UnsolvedCallDAO unsolvedCallDao;

	public UnsolvedCallBusiness() {
		super(new UnsolvedCallDAO());
		this.unsolvedCallDao = (UnsolvedCallDAO) getDao();
	}

	
	public Map<Integer, List<UnsolvedCall>> searchCitizen(String cpf) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchCitizen(cpf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}


	
	public List<UnsolvedCall> findByCitizen(String cpf) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.findByCitizen(cpf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}
	
	
	public List<UnsolvedCall> searchCitizenFinal(String periodod, String periodoa, 
			 String entityId,String entityCategoryId, String callProgress,String description, String citizenCpf) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchCitizenFinal(periodod, periodoa,
					  entityId, entityCategoryId,callProgress, description, citizenCpf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}

	
	public List<UnsolvedCall> searchCitizenFinal2(String periodod, String periodoa, 
			 String entityId,String entityCategoryId, String callProgress,String description, String citizenCpf,List<Long> excludeCalls) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchCitizenFinal2(periodod, periodoa,
					  entityId, entityCategoryId,callProgress, description, citizenCpf,excludeCalls);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}
	
	public Map<Integer, List<UnsolvedCall>> searchCitizen2(String periodod, String periodoa, 
			 String entityId,String entityCategoryId, String callProgress,String description, String citizenCpf) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchCitizen2(periodod, periodoa,
					  entityId, entityCategoryId,callProgress, description, citizenCpf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}
	
	public List<Long> newFind() throws SQLException, ModelException {
		List<Long> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = unsolvedCallDao.newFind();
			HibernateUtil.commitTransaction();

		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public UnsolvedCall getLastChild(Long unsolvedCallId) throws SQLException, ModelException {
		UnsolvedCall t = null;
		try {
			HibernateUtil.beginTransaction();
			t = unsolvedCallDao.getLastChild(unsolvedCallId);
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

		if (t == null) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return t;
	}

	public List<Long> findAll2(Integer entityId) throws SQLException, ModelException {
		List<Long> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = unsolvedCallDao.findAll2(entityId);
			HibernateUtil.commitTransaction();

		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public List<Long> findAll3(Integer entityId) throws SQLException, ModelException {
		List<Long> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = unsolvedCallDao.findAll3();
			HibernateUtil.commitTransaction();

		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public List<UnsolvedCall> history(Long id) throws SQLException, ModelException {
		List<UnsolvedCall> tList;
		try {
			HibernateUtil.beginTransaction();
			tList = unsolvedCallDao.history(id);
			HibernateUtil.commitTransaction();

		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return tList;
	}

	public boolean deleteUN(UnsolvedCall t)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		boolean success = false;
		try {
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().flush();
			unsolvedCallDao.delete(t);
			HibernateUtil.commitTransaction();
			success = true;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			success = false;
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.UPDATE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		return success;
	}

	public Long saveUN(UnsolvedCall t)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Long id = (long) -1;
		try {
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().flush();
			unsolvedCallDao.save(t);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();

		}
		id = t.getUnsolvedCallId();
		return id;
	}
	
	public boolean evaluation(String eval)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		boolean result=  false;
		try {
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().flush();
			result = unsolvedCallDao.evaluation(eval);
			//unsolvedCallDao.save(t);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
			} finally {
			HibernateUtil.closeSession();
		}
		return result;
	}
	

	public Long mergeUN(UnsolvedCall t)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Long id = (long) -1;
		try {
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().flush();
			unsolvedCallDao.merge(t);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();

		}
		id = t.getUnsolvedCallId();
		return id;
	}

	public Long save2(UnsolvedCall t)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Long id = (long) -1;
		try {
			HibernateUtil.beginTransaction();
			unsolvedCallDao.save2(t);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();

		}
		id = t.getUnsolvedCallId();
		return id;
	}

	@Override
	public List<UnsolvedCall> findAll() throws HibernateException, SQLException, ModelException {
		List<UnsolvedCall> list = super.findAll();
		if (list.isEmpty()) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return list;
	}

	public UnsolvedCall getByIdRC(Long unsolvedCallId) throws HibernateException, SQLException, ModelException {
		UnsolvedCall t = null;
		try {
			HibernateUtil.beginTransaction();
			t = unsolvedCallDao.getByIdRC(unsolvedCallId);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();

		}
		/*if (t == null) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}*/
		return t;
	}

	public Map<Integer, List<UnsolvedCall>> searchPag(int pagina, String periodod, String periodoa, String callSource,
			String callclassificationid, String entityid, String priority, String userid, String callStatus, String unsolvedCallId, String citizenCpf)
					throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPag(pagina, periodod, periodoa, callSource, callclassificationid, entityid,
					priority, userid, callStatus,unsolvedCallId, citizenCpf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public Map<Integer, List<UnsolvedCall>> searchPag2(int pagina, String periodod, String periodoa, String callSource,
			String callclassificationid, String entityid, String priority, String userid, String callStatus,
			List<Long> tList) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPag2(pagina, periodod, periodoa, callSource, callclassificationid, entityid,
					priority, userid, callStatus, tList);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

	}

	public List<UnsolvedCall> teste() throws SQLException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.teste();
		} finally {
			HibernateUtil.closeSession();
		}

	}

	public Map<Integer, List<UnsolvedCall>> searchPaginaCF(int entity, int pagina, String periodod, String periodoa,
			String callSource, String callclassificationid, String priority, String unsolvedCallId, String citizenCpf, List<Long> llist)
					throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPaginaCF(entity, pagina, periodod, periodoa, callSource, callclassificationid,
					priority, unsolvedCallId, citizenCpf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

	}

	// MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchPaginaMO(int page, String periodod, String periodoa,
			String callSource, String entity, String callClassification, String priority, String callProgress,String sortColumn,int order) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPaginaMO(page, periodod, periodoa, callSource, entity, callClassification,
					priority, callProgress,sortColumn,order);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public List<Long> findAllLastChild() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.findAllLastChild();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	
	// MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchPgMO(int page, String periodod, String periodoa,
			String callSource, String entity, String callClassification, String priority, String callProgress,String sortColumn,int order) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPgMO(page, periodod, periodoa, callSource, entity, callClassification,
					priority, callProgress,sortColumn,order);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	// MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchMO(String periodod, String periodoa,
			String callSource, String entity, String callClassification, String priority, String callProgress,int sortColumn,int order) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchMO(periodod, periodoa, callSource, entity, callClassification,
					priority, callProgress,sortColumn,order);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	/*
	// MONITOR DE CHAMADOS
	public Map<Integer, List<UnsolvedCall>> searchMO2(int page,CallMonitorFilter cmf) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchMO2(page,cmf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	*/
	public static void main(String[] args) throws HibernateException, SQLException, ModelException, ParseException {
		UnsolvedCallBusiness sb = new UnsolvedCallBusiness();
	//	Map<Integer, List<UnsolvedCall>> tMap = sb.searchPag(2, "z", "z", "z", "z", "z", "z", "z", "z", "z", "z");
		//	List<UnsolvedCall> tList= sb.searchCitizenFinal("2016-06-16T00:00:00.000Z", "2016-06-15T00:00:00.000Z", "z", "z", "z", "z", "12345678913");
	//	List<UnsolvedCall> tList = new ArrayList<UnsolvedCall>();
	//	int count;
	//	for (Integer key : tMap.keySet()) {
	//		count = key;
	//		tList = tMap.get(key);
	//		break;
	//	}
	//	for(UnsolvedCall uc:tList)			
     //   	System.out.println(uc.getUnsolvedCallId()+", "+uc.getCreationOrUpdateDate());
	//	for(Long l:sb.findAllLastChild())
	//		System.out.println(l);
	
	}

}
