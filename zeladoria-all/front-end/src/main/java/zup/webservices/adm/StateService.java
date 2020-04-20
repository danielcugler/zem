package zup.webservices.adm;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.hibernate.HibernateException;

import flexjson.JSONSerializer;
import zup.action.Response;
import zup.bean.State;
import zup.business.StateBusiness;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.validate.StateValidate;

@Path("/state")
public class StateService extends AbstractService<State, Integer> {

	StateBusiness mb = new StateBusiness();
	StateValidate mv = new StateValidate();

	public StateService() {
		super(State.class);
		super.setBusiness(mb);
		super.setValidate(mv);
	}

	



}
