package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;

import io.minio.messages.Item;
import zup.bean.AttendanceTime;
import zup.bean.Entity;
import zup.bean.Result;
import zup.bean.UnsolvedCall;
import zup.business.CitizenBusiness;
import zup.business.EntityBusiness;
import zup.business.SolvedCallBusiness;
import zup.business.UnsolvedCallBusiness;
import zup.business.UnsolvedCallBusiness2;
import zup.dto.DelayCountDTO;
import zup.dto.NeighborhoodChartDTO;
import zup.dto.QualificationChartDTO;
import zup.enums.CallProgress;
import zup.enums.Priority;
import zup.exception.ModelException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.Messages;

@Path("/dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DashboardService {	
	
	@GET
	public Response dashboard(){
		CitizenBusiness cb = new CitizenBusiness();
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();		
		SolvedCallBusiness scb = new SolvedCallBusiness();		
		Date currentDate = new Date(System.currentTimeMillis());
		try {
			Map<String,Long> map= new HashMap<String, Long>();
			Long countCitizen = cb.countCitizen();
			Long countSolved = scb.countCallFinalized();
			Long countInProgress = ucb.countInprogress();
			List<DelayCountDTO> list = ucb.countDelay();
			Long countDelayed = (long) 0;
			if (!list.isEmpty()){				
				for (DelayCountDTO uc : list)
					if ((currentDate.after(adicionarDiasUteis(uc.getCreationDate(), uc.getTime())))) 
						countDelayed++;			
			}				
			map.put("countCitizen", countCitizen);
			map.put("countSolved", countSolved);
			map.put("countDelayed", countDelayed);
			map.put("countInProgress", countInProgress);
			return Response.ok(map).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));			
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ParseException", pe.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		}
	}
	
	@GET
	@Path("/progressChart")
	public Response progressChart(){
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();		
		SolvedCallBusiness scb = new SolvedCallBusiness();
		try {
			Map<String,Long> map= new HashMap<String, Long>();
			Long allCall = ucb.countProgressCalls("All");
			Long newCall = ucb.countProgressCalls("New");
			Long forwardedCall = ucb.countProgressCalls("Forwarded");
			Long visualizedCall = ucb.countProgressCalls("Visualized");
			Long inProgressCall = ucb.countProgressCalls("InProgress");
			Long proccessedCall = ucb.countProgressCalls("Proccessed");		
			Long rejectedCall = ucb.countProgressCalls("Rejected");
			Long finalizedCall = scb.countCallFinalized();
			
			map.put("allCall", allCall + finalizedCall);
			map.put("newCall", newCall);
			map.put("forwardedCall", forwardedCall);
			map.put("visualizedCall", visualizedCall);
			map.put("inProgressCall", inProgressCall);
			map.put("proccessedCall", proccessedCall);
			map.put("rejectedCall", rejectedCall);
			map.put("finalizedCall", finalizedCall);
			return Response.ok(map).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));			
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", se.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", pe.getMessage()));
		}
	}	
	
	@GET
	@Path("/neighborhoodChart")
	public Response neighborhoodChart(){
		UnsolvedCallBusiness2 ucb = new UnsolvedCallBusiness2();		
		SolvedCallBusiness scb = new SolvedCallBusiness();		
		try {
			List<NeighborhoodChartDTO> list = ucb.countNeighborhoodChart();
			return Response.ok(list).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", se.getMessage()));
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", pe.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", me.getMessage()));
		}
	}	

	@GET
	@Path("/qualificationChart")
	public Response qualificationChart(){		
		SolvedCallBusiness scb = new SolvedCallBusiness();		
		try {
			List<QualificationChartDTO> list = scb.countQualificationChart();
			return Response.ok(list).build();
		} catch (HibernateException he) {
			he.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", he.getMessage()));
		} catch (SQLException se) {
			se.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", se.getMessage()));
		} catch (ParseException pe) {
			pe.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", pe.getMessage()));
		} catch (ModelException me) {
			me.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", me.getMessage()));
		}
	}
	
	public static Date adicionarDiasUteis(Date data, Integer qtdeDiasAcrescentados) {
		Calendar dataInicial = Calendar.getInstance();
		dataInicial.setTime(data);
		while (qtdeDiasAcrescentados > 0) {
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
			int diaDaSemana = dataInicial.get(Calendar.DAY_OF_WEEK);
			if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY) {
				--qtdeDiasAcrescentados;
			}
		}
		return dataInicial.getTime();
	}
	
	
}
