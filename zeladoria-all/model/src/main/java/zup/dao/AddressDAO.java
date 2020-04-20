package zup.dao;

import org.apache.log4j.Logger;

import zup.bean.Address;

public class AddressDAO extends AbstractDAO<Address, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public AddressDAO() {
		super(Address.class);
	}
}