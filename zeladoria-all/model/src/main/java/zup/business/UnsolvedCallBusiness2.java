package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import zup.bean.CallClassification;
import zup.bean.Entity;
import zup.bean.EntityCategory;
import zup.bean.Result;
import zup.bean.UnsolvedCall;
import zup.dao.UnsolvedCallDAO;
import zup.dao.UnsolvedCallDAO2;
import zup.dto.CallProgressDTO;
import zup.dto.DashBoardFinEmAndDTO;
import zup.dto.DelayCountDTO;
import zup.dto.NeighborhoodChartDTO;
import zup.enums.CallProgress;
import zup.enums.CallSource;
import zup.enums.Priority;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class UnsolvedCallBusiness2 extends AbstractBusiness<UnsolvedCall, Long> {
	private static Logger logger = Logger.getLogger(UnsolvedCall.class);

	private UnsolvedCallDAO2 unsolvedCallDao;

	public UnsolvedCallBusiness2() {
		super(new UnsolvedCallDAO2());
		this.unsolvedCallDao = (UnsolvedCallDAO2) getDao();
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
	
	/*
	public List<CallProgressDTO> countCallProgressDash() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countCallProgressDash();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}
	*/
	public List<DelayCountDTO> countDelay() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countDelay();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}
	
	public List<NeighborhoodChartDTO> countNeighborhoodChart() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countNeighborhoodChart();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}
	
	public Long countInprogress() throws  SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countInprogress();
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	public Long countProgressCalls(String progress) throws  SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countProgressCalls(progress);
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
	
	
	public UnsolvedCall searchCitizenPk(Long unsolvedCallId,String publicKey) throws SQLException, ModelException, HibernateException, ParseException {
		UnsolvedCall t;
		try {
			HibernateUtil.beginTransaction();
			t = unsolvedCallDao.searchCitizenPk(unsolvedCallId, publicKey);
			if(t != null)
				HibernateUtil.commitTransaction();

		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
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

	public void saveSD(UnsolvedCall t,UnsolvedCall parent)
			throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().flush();
			unsolvedCallDao.save(t);
			t.setParentCallId(parent);
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

		if (t == null) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return t;
	}
	
	public List<UnsolvedCall> getByEntity(int entityId) throws HibernateException, SQLException, ModelException {
		List<UnsolvedCall> t = null;
		try {
			HibernateUtil.beginTransaction();
			t = unsolvedCallDao.getByEntity(entityId);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();

		}

		if (t == null) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return t;
	}
	
	//Teste Nathália
	public List<UnsolvedCall> getByParentCallId(Long parentCallId) throws HibernateException, SQLException, ModelException {
		List<UnsolvedCall> t = new ArrayList<UnsolvedCall>();
		try {
			HibernateUtil.beginTransaction();
			t = unsolvedCallDao.getByParentCallId(parentCallId);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
		
		if (t == null) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return t;
	}
	//Fim Teste Nathália

	public Result<UnsolvedCall> searchPag(int pagina, Date iniDate, Date endDate, String callSource,
			String callclassificationid, String entityid, String priority, String userid, String callStatus, String unsolvedCallId, String citizenCpf)
					throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPag(pagina, iniDate, endDate, callSource, callclassificationid, entityid,
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
	
	
	
	public List<UnsolvedCall> searchPaginaCF2(int pagina, Date iniDate, Date endDate,
			String callSource, String callclassificationid, String priority, String unsolvedCallId, String citizenCpf)
					throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPagCF2(pagina, iniDate, endDate, callSource, callclassificationid,priority,unsolvedCallId, citizenCpf);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}

	}
	
	public List<UnsolvedCall> searchPaginaCFEN(int pagina, Date iniDate, Date endDate,
			String callSource, String callclassificationid, String priority, String unsolvedCallId, String citizenCpf,String entityId)
					throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchPagCFEN(pagina, iniDate, endDate, callSource, callclassificationid,priority,unsolvedCallId, citizenCpf,entityId);
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
	
	
	// MONITOR DE CHAMADOS
	public Result<UnsolvedCall> searchMO3(int page,List<Integer> entityCategory,List<Integer> callClassification,List<CallProgress> callProgress,List<CallSource> callSource,List<Priority> priority,int orderParam,int order) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchMO3(page,entityCategory,callClassification,callProgress,callSource,priority,orderParam,order);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	// MONITOR DE CHAMADOS FINAL
	public Result<UnsolvedCall> searchMOAll() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchMOALl();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	//Exibe os chamados dos últimos 30 dias - Monitor de Chamados
	public Result<UnsolvedCall> searchCallMonitorLast30Days(Date initDate, Date endDate) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.searchCallMonitorLast30Days(initDate, endDate);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}

	
	//Count CallProgress
	public LinkedHashMap<String,Long> countCallProgress() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countCallProgress();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	

	//Count CallSource
	public LinkedHashMap<String,Long> countCallSource() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countCallSource();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	//Count Priority
	public LinkedHashMap<String,Long> countPriority() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countPriority();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	//Count CallClassification
	public LinkedHashMap<String,Long> countCallClassification() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countCallClassification();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	//Count Entity
	public LinkedHashMap<String,Long> countEntity() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countEntity();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	//Count EntityCategory
	public LinkedHashMap<String,Long> countEntityCategory() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return unsolvedCallDao.countEntityCategory();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	// TEste
		public  List<UnsolvedCall> teste2() throws HibernateException, SQLException, ParseException, ModelException {
			try {
				HibernateUtil.beginTransaction();
				return unsolvedCallDao.teste2();
			} finally {
				HibernateUtil.closeSession();
			}
		}

		public  List<Long> findAll3() throws HibernateException, SQLException, ParseException, ModelException {
			try {
				HibernateUtil.beginTransaction();
				return unsolvedCallDao.findAll3();
			} finally {
				HibernateUtil.closeSession();
			}
		}
		
		public static Date adicionarDiasUteis(Date data, Integer qtdeDiasAcrescentados) {
			Calendar dataInicial = Calendar.getInstance();
			dataInicial.setTime(data);
			while (qtdeDiasAcrescentados > 0) {
				dataInicial.add(Calendar.DAY_OF_MONTH, 1);
				int diaDaSemana = dataInicial.get(Calendar.DAY_OF_WEEK);
				if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY) {
					--qtdeDiasAcrescentados;
				}
			}
			return dataInicial.getTime();
		}
		
		
	public static void main(String[] args) throws HibernateException, SQLException, ModelException, ParseException, JsonProcessingException {
	
		UnsolvedCallBusiness2 sb = new UnsolvedCallBusiness2();
//		for(CallProgressDTO cp:sb.countCallProgressDash())
//			System.out.println(cp.getCallProgress() +", "+cp.getCount());

		//List<DelayCountDTO> list = sb.countDelay();
		//for(DelayCountDTO de:list)
	//	System.out.println(de.getCreationDate().toString());
	/*
		CitizenBusiness cb = new CitizenBusiness();
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();		
		SolvedCallBusiness scb = new SolvedCallBusiness();		
		Date currentDate = new Date(System.currentTimeMillis());
			Map<String,Long> map= new HashMap<String, Long>();
			Long countCitizen = cb.countCitizen();
			Long countSolved = scb.countCallFinalized();
			Long countInProgress = ucb.countInprogress();
			List<DelayCountDTO> list3 = ucb.countDelay();
			Long countDelayed=(long) 0;
			for (DelayCountDTO uc : list3) 
					if ((currentDate.after(adicionarDiasUteis(uc.getCreationDate(), uc.getTime())))) 
						countDelayed++;

			
			map.put("countCitizen", countCitizen);
			map.put("countSolved", countSolved);
			map.put("countDelayed", countDelayed);
			map.put("countInProgress", countInProgress);
			
*/
		
		//	List<UnsolvedCall> list = sb.searchPaginaCFEN(1, new Date(),new Date(), "", "", "", "","", "4");
	
//	for(UnsolvedCall un:list)
//		System.out.println(un.getUnsolvedCallId());
	//	t.setUnsolvedCallId(null);
//	sb.merge(t);
		/*
		Result<UnsolvedCall> res= sb.searchMOAll();
		
		for(UnsolvedCall un:res.getList())
			System.out.println(un.getUnsolvedCallId());
		*/
		/*
		Map<String,Long> map1=sb.countCallProgress();
		Map<String,Long> map2=sb.countCallSource();
		Map<String,Long> map3=sb.countPriority();
		Map<String,Long> map4=sb.countCallClassification();
		Map<String,Long> map5=sb.countEntity();
		System.out.println("Call Progress");
		for(String key:map1.keySet())
			System.out.println("key: "+key+", count: "+map1.get(key));
		System.out.println("Call Source");
		for(String key:map2.keySet())
			System.out.println("key: "+key+", count: "+map2.get(key));
		System.out.println("Priority");
		for(String key:map3.keySet())
			System.out.println("key: "+key+", count: "+map3.get(key));
		System.out.println("Call Classification");
		for(String key:map4.keySet())
			System.out.println("key: "+key+", count: "+map4.get(key));
		System.out.println("Entity");
		for(String key:map5.keySet())
			System.out.println("key: "+key+", count: "+map5.get(key));
*/
/*		
		//	UnsolvedCall un=sb.getById(Long.valueOf(7));
//	un.setUnsolvedCallId(null);
//	sb.merge(un);
		ArrayList<Integer> entity =new ArrayList<Integer>();
		ArrayList<Integer> callClassification =new ArrayList<Integer>();
		ArrayList<CallProgress> callProgress =new ArrayList<CallProgress>();
		ArrayList<CallSource> callSource =new ArrayList<CallSource>();
		ArrayList<Priority> priority =new ArrayList<Priority>();
		callClassification.add(4);
		callClassification.add(5);
		callClassification.add(6);
		entity.add(88);
		entity.add(173);
		entity.add(197);
		callProgress.add(CallProgress.ANDAMENTO);
		//callProgress.add(CallProgress.ENCAMINHADO);
		//callProgress.add(CallProgress.VISUALIZADO);
		callSource.add(CallSource.MOVEL);
		callSource.add(CallSource.WEB);
		priority.add(Priority.ALTA);
		priority.add(Priority.MEDIA);
		priority.add(Priority.BAIXA);
		
		
	  List<Long> list= sb.findAll3();
	for(Long l:list)
		System.out.println(l);
	int order=0;
	Result<UnsolvedCall> result= sb.searchMO3(1, entity, callClassification, callProgress, callSource, priority, order, 0);
	System.out.println(result.getCount());
	for(UnsolvedCall call:result.getList())
		System.out.println(call.getUnsolvedCallId()+", calssificação:"+call.getCallClassificationId().getName()+", entidade: "+call.getEntityEntityCategoryMaps().getEntity().getEntityId()+", callProgress: "+call.getCallProgress()+", callSource: "+call.getCallSource()+", priority: "+call.getPriority());
	
	
	*/
	
	/*
		Date iniDate=  new SimpleDateFormat("ddMMyyyy").parse("01082016");
		//Map<Integer, List<UnsolvedCall>> tMap = sb.searchPag(2, "z", "z", "z", "z", "z", "z", "z", "z", "z", "z");
		//	List<UnsolvedCall> tList= sb.searchCitizenFinal("2016-06-16T00:00:00.000Z", "2016-06-15T00:00:00.000Z", "z", "z", "z", "z", "12345678913");
		for(UnsolvedCall uc:sb.searchPag(1, iniDate, new Date(), "",	"", "", "", "", "", "", "").getList()){
        	uc.setMediasPath(new ArrayList<String>(Arrays.asList("1UnsolvedCallDAO.java","2","3")));
			//System.out.println(uc.getUnsolvedCallId()+", "+uc.getParentCallId().getUnsolvedCallId()+", "+uc.getCreationOrUpdateDate());
	        ObjectMapper mapper = new ObjectMapper();
	        System.out.println(mapper.writeValueAsString(uc)); 
		}
	
	*/
	}

}
