package zup.business;

import org.apache.log4j.Logger;

import zup.bean.AdditionalInformation;
import zup.dao.AdditionalInformationDAO;

public class AdditionalInformationBusiness extends AbstractBusiness<AdditionalInformation, Integer> {

	private AdditionalInformationDAO additionalInformationDao;
	private static Logger logger = Logger.getLogger(AdditionalInformation.class);

	public AdditionalInformationBusiness() {
		super(new AdditionalInformationDAO());
		this.additionalInformationDao = (AdditionalInformationDAO) getDao();
	}

}
