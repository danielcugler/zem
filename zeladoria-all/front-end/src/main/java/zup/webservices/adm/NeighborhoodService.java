package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import zup.bean.City;
import zup.bean.Log;
import zup.bean.Neighborhood;
import zup.bean.Result;
import zup.business.CityBusiness;
import zup.business.LogBusiness;
import zup.business.NeighborhoodBusiness;
import zup.business.SystemUserBusiness;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;

@Path("/neighborhood")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NeighborhoodService {

	public Map<String, String> makeMessage(String msg) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", msg);
		return message;
	}

	@PUT
	public Response mergeE(@Valid Neighborhood neighborhood)
			 {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		LogBusiness lb = new LogBusiness();
		try {
			Neighborhood t1 = mb.getById(neighborhood.getNeighborhoodId());
			t1.setName(neighborhood.getName());
			mb.merge(t1);
			Log log = new Log(InformationType.NEIGHBORHOOD, t1.makeLog());
			log.setOperationType(OperationType.EDICAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.NEIGHBORHOOD_UPDATE_SUCCESS)))
					.build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		}
	}

	@POST
	public Response saveEAT(@Valid Neighborhood neighborhood)
			 {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		CityBusiness cb=new CityBusiness();
		LogBusiness lb = new LogBusiness();
		try {
			City city= cb.getById(3551);
			neighborhood.setCityId(city);
			mb.save(neighborhood);
			Log log = new Log(InformationType.NEIGHBORHOOD, neighborhood.makeLog());
			log.setOperationType(OperationType.INCLUSAO);
			log.setSystemUserUsername(sb.getById(username));
			lb.save(log);
			return Response.status(Status.CREATED)
					.entity(makeMessage(Messages.getInstance().getMessage(IMessages.NEIGHBORHOOD_SAVE_SUCCESS)))
					.build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (DataAccessLayerException de) {
			de.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", de.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (ConstraintViolationException me) {
			me.printStackTrace();
			String message="";
			for(ConstraintViolation cv:me.getConstraintViolations())
				message+="\n"+cv.getMessage();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ConstraintViolationException", message));
		}
	}
	
	
	@GET
	public Response findAll() {
		try {
			NeighborhoodBusiness mb = new NeighborhoodBusiness();
			List<Neighborhood> tList = mb.findAll();
			return Response.ok(tList).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}
	
	@GET
	@Path("/search")
	public Response search(@QueryParam("page") int page,@QueryParam("cityId") String cityId,@QueryParam("name") String name) {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		try {
			Result<Neighborhood> result = mb.searchPag(page,cityId,name);
			return Response.ok(result.getList()).header("X-Total-Count", result.getCount()).header("X-Per-Page",
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
					.build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ParseException", e.getMessage()));
		} catch (ModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}

	@GET
	@Path("/load")
	public Response findByName(@QueryParam("cityId") Integer cityId,@QueryParam("neighborhoodId") Integer neighborhoodId) {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		try {
			Neighborhood n = mb.findByIdCity(cityId,neighborhoodId);
			return Response.ok(n).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}
	
	
	@GET
	@Path("/name/{name}")
	public Response findByName(@PathParam("name") String name) {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		try {
			List<Neighborhood> tList = mb.findByName(name);
			return Response.ok(tList).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}
	@GET
	@Path("/city/{cityId}")
	public Response findByName(@PathParam("cityId") Integer cityId) {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		try {
			List<Neighborhood> tList = mb.findByCity(cityId);
			return Response.ok(tList).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}

	
	@GET
	@Path("/city/{cityId}/{neighborhoodId}")
	public Response findByCityNeighborhood(@PathParam("cityId") Integer cityId,@PathParam("neighborhoodId") String neighborhoodId) {
		NeighborhoodBusiness mb = new NeighborhoodBusiness();
		try {
			Neighborhood t = mb.findByCityNeighborhood(cityId,neighborhoodId);
			return Response.ok(t).build();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			e.printStackTrace();
			throw new ZEMException(
					ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}

}