package business;

import org.apache.log4j.Logger;

import bean.State;
import dao.StateDAO;



public class StateBusiness extends AbstractBusiness<State, Integer> {

	private StateDAO EstadosDao;
	private static Logger logger = Logger.getLogger(State.class);

	public StateBusiness() {
		super(new StateDAO());
		this.EstadosDao = (StateDAO) getDao();
	}

}
