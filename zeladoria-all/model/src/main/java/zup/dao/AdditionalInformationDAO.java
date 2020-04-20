package zup.dao;

import org.apache.log4j.Logger;

import zup.bean.AdditionalInformation;

public class AdditionalInformationDAO extends AbstractDAO<AdditionalInformation, Integer> {
	private static Logger logger = Logger.getLogger(AbstractDAO.class);

	public AdditionalInformationDAO() {
		super(AdditionalInformation.class);
	}
}
