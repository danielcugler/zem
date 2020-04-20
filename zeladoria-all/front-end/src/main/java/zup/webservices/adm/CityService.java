package zup.webservices.adm;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.hibernate.HibernateException;

import flexjson.JSONSerializer;
import zup.action.Response;
import zup.bean.City;
import zup.bean.Neighborhood;
import zup.business.CityBusiness;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.IMessages;
import zup.messages.Messages;
import zup.validate.CityValidate;

@Path("/city")
public class CityService extends AbstractService<City, Integer> {

	CityBusiness mb = new CityBusiness();
	CityValidate mv = new CityValidate();

	public CityService() {
		super(City.class);
		super.setBusiness(mb);
		super.setValidate(mv);
	}
	
	@GET
	@Path("/state/{estado}")
	public Response findByName(@PathParam("estado") String estado)
			throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		List<City> tList =  mb.getByEstado(estado);
		String json = new JSONSerializer().include("cidade_id", "nome").exclude("*.class").serialize(new ArrayList<City>(tList));
		response.setJsonList(json);
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		return response;
	}	

	@GET
	@Path("/{id}/bairros")
	public Response findByName(@PathParam("id") Integer id)
			throws HibernateException, SQLException, ValidationException, ModelException {
		Response response = new Response();
		try {
			City t =  mb.getById(id);
			String json = new JSONSerializer().include("baiNuSequencial", "nome").exclude("*").serialize(new ArrayList<Neighborhood>(t.getNeighborhoodCollection()));
			response.setJsonList(json);
		} catch (ModelException me) {
			response.setMessage(Messages.getInstance().getMessage(me.getMessage()));
			return response;
		}
		response.setMessage(Messages.getInstance().getMessage(IMessages.SUCCESS_OPERATION));
		return response;
	}

}