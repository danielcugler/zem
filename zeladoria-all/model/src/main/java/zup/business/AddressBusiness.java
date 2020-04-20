package zup.business;

import org.apache.log4j.Logger;

import zup.bean.Address;
import zup.dao.AddressDAO;

public class AddressBusiness extends AbstractBusiness<Address, Integer> {

	private AddressDAO AddressDao;
	private static Logger logger = Logger.getLogger(Address.class);

	public AddressBusiness() {
		super(new AddressDAO());
		this.AddressDao = (AddressDAO) getDao();
	}

}
