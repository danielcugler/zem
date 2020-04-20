package zup.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.SolvedCall;
import zup.bean.UnsolvedCall;
import zup.dao.SolvedCallDAO;
import zup.dto.NeighborhoodChartDTO;
import zup.dto.QualificationChartDTO;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.model.utils.HibernateUtil;

public class SolvedCallBusiness extends AbstractBusiness<SolvedCall, Long> {
	private static Logger logger = Logger.getLogger(SolvedCall.class);

	private SolvedCallDAO solvedCallDao;

	public SolvedCallBusiness() {
		super(new SolvedCallDAO());
		this.solvedCallDao = (SolvedCallDAO) getDao();
	}

	public SolvedCall getLastChild(Long solvedCallId) throws SQLException, ModelException {
		SolvedCall t = null;
		try {
			HibernateUtil.beginTransaction();
			t = solvedCallDao.getLastChild(solvedCallId);

			if (t == null) {
				logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
				throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			}
			return t;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();

		}

	}

	@Override
	public List<SolvedCall> findAll() throws HibernateException, SQLException, ModelException {
		List<SolvedCall> list = super.findAll();
		if (list.isEmpty()) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return list;
	}

	@Override
	public SolvedCall getById(Long id) throws HibernateException, SQLException, DataAccessLayerException, ModelException {

		SolvedCall t;
		try {

			HibernateUtil.beginTransaction();
			t = solvedCallDao.getById(id);
			HibernateUtil.commitTransaction();
			return t;
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND),
					e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	
	public Long save2(SolvedCall t) throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Long id = (long) -1;
		try {
			HibernateUtil.beginTransaction();
			solvedCallDao.save2(t);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();

		}
		id = t.getSolvedCallId();
		return id;
	}
	
	
	
	
	public List<Long> getLastChild() throws SQLException, ModelException {
		List<Long> t = null;
		try {
			HibernateUtil.beginTransaction();
			t = solvedCallDao.findAllLastChild();
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

		if (t.isEmpty()) {
			logger.info(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
			throw new ModelException(Messages.getInstance().getMessage(IMessages.FIELD_NOT_FOUND));
		}
		return t;
	}	

	public Long saveUN(SolvedCall t) throws HibernateException, SQLException, DataAccessLayerException, ModelException {
		Long id = (long) -1;
		try {
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().clear();
			HibernateUtil.getSession().flush();
			solvedCallDao.save(t);
			HibernateUtil.commitTransaction();
		} catch (HibernateException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new DataAccessLayerException(Messages.getInstance().getMessage(IMessages.SAVE_ERROR), e.getCause());
		} finally {
			HibernateUtil.closeSession();

		}
		id = t.getSolvedCallId();
		return id;
	}

	public Map<Integer, List<SolvedCall>> searchPag(int pagina, String periodod, String periodoa, String callSource,
			String callclassificationid, String entityid, String priority, String userid, String callStatus)
					throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return solvedCallDao.searchPag(pagina, periodod, periodoa, callSource, callclassificationid, entityid,
					priority, userid, callStatus);
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw new ModelException(Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND), e.getCause());
		} finally {
			HibernateUtil.closeSession();
		}

	}
	
	public List<SolvedCall> searchCitizenFinal(String periodod, String periodoa, 
			 String entityId,String entityCategoryId, String callProgress,String description, String citizenCpf) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return solvedCallDao.searchCitizenFinal(periodod, periodoa,
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

	public List<SolvedCall> searchCitizenFinal2(String periodod, String periodoa, 
			 String entityId,String entityCategoryId, String callProgress,String description, String citizenCpf,List<Long> excludeCalls) throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return solvedCallDao.searchCitizenFinal2(periodod, periodoa,
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
	
	
	public SolvedCall searchCitizenPk(Long solvedCallId,String publicKey) throws SQLException, ModelException, HibernateException, ParseException {
		SolvedCall t;
		try {
			HibernateUtil.beginTransaction();
			t = solvedCallDao.searchCitizenPk(solvedCallId, publicKey);
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
	
	public Long countCallFinalized() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return solvedCallDao.countCallFinalized();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	public List<QualificationChartDTO> countQualificationChart() throws HibernateException, SQLException, ParseException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return solvedCallDao.countQualificationChart();
		} catch (ModelException e) {
			HibernateUtil.rollBackTransaction();
			logger.info(e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			HibernateUtil.closeSession();
	}
	}

	public static void main(String[] args) throws HibernateException, SQLException, ModelException {
		SolvedCallBusiness scb = new SolvedCallBusiness();
		UnsolvedCallBusiness scb2 = new UnsolvedCallBusiness();
		List<Long> list=scb.getLastChild();
		for(Long l:list)
			System.out.println(l);
		/*
		List<UnsolvedCall> sclist2 = scb2.findAll();
		List<SolvedCall> sclist = scb.findAll();
		SolvedCall sc = new SolvedCall();
		SolvedCall sc2 = new SolvedCall();
		sc2 = scb.getLastChild((long) 370);
		System.out.println("aisoj");
*/
	}

}
