package rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;

import bean.City;
import business.CityBusiness;


@Path("/city")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CityService  {
	public CityService() {
	}
	
	@GET
	@Path("/state/{estado}")
	public Response findByName(@PathParam("estado") String estado)
			throws HibernateException, SQLException{
		CityBusiness mb = new CityBusiness();
		List<City> tList =  mb.getByEstado(estado);
		return Response.ok(tList).build();
	}	

	@GET
	public Response findAll()
			throws HibernateException, SQLException{
		CityBusiness mb = new CityBusiness();
		List<City> tList =  mb.findAll();
		return Response.ok(tList).build();
	}
}