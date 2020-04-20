package zup.business;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.Citizen;
import zup.bean.ReadBroadcastMessage;
import zup.bean.Result;
import zup.dao.ReadBroadcastMessageDAO;
import zup.exception.ModelException;
import zup.model.utils.HibernateUtil;

public class ReadBroadcastMessageBusiness extends AbstractBusiness<ReadBroadcastMessage, Integer> {

	private ReadBroadcastMessageDAO readBroadcastMessageDao;
	private static Logger logger = Logger.getLogger(ReadBroadcastMessage.class);

	public ReadBroadcastMessageBusiness() {
		super(new ReadBroadcastMessageDAO());
		this.readBroadcastMessageDao = (ReadBroadcastMessageDAO) getDao();
	}
	
	
	public List<ReadBroadcastMessage> searchRead(String cpf)
			throws HibernateException, SQLException, ModelException {
		try {
			HibernateUtil.beginTransaction();
			return readBroadcastMessageDao.getRead(cpf);
		} catch (ModelException me) {
			throw me;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return null;
	}
	public static void main(String[] args) {
		ReadBroadcastMessageBusiness rb = new ReadBroadcastMessageBusiness();
		try {
			List<ReadBroadcastMessage> rbm = rb.searchRead("12345678913");
			System.out.println("ola");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}