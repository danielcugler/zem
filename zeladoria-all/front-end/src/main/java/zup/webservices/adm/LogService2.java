package zup.webservices.adm;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;

import filters.LogFilterBean;
import zup.bean.Log;
import zup.bean.Result;
import zup.bean.SystemUser;
import zup.business.LogBusiness;
import zup.business.SystemUserBusiness;
import zup.enums.InformationType;
import zup.enums.OperationType;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.exception.ZEMException;
import zup.messages.IMessages;
import zup.messages.ISystemConfiguration;
import zup.messages.Messages;
import zup.messages.SystemConfiguration;

@Path("/log2")
public class LogService2 {

	@GET
	@Path("/combos")
	public Response getCombos() throws HibernateException, SQLException, ValidationException, ModelException {
		LogBusiness lb = new LogBusiness();
		SystemUserBusiness sb = new SystemUserBusiness();
		try {
			List<InformationType> itlist = new ArrayList<InformationType>();
			for (InformationType ot : InformationType.values())
				itlist.add(ot);
			itlist.sort(new Comparator<InformationType>() {
				public int compare(InformationType i1, InformationType i2) {
					return i1.getStr().compareTo(i2.getStr());
				}
			});
			List<OperationType> otlist = new ArrayList<OperationType>();
			for (OperationType ot : OperationType.values())
				otlist.add(ot);
			otlist.sort(new Comparator<OperationType>() {
				public int compare(OperationType i1, OperationType i2) {
					return i1.getStr().compareTo(i2.getStr());
				}
			});
			List<SystemUser> sulist = sb.findAll();
			Map<String, List> map = new HashMap<String, List>();
			map.put("it", itlist);
			map.put("ot", otlist);
			map.put("su", sulist);
			return Response.ok(map).build();
		} catch (ModelException me) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", me.getMessage()));
		}
	}

	@GET
	@Path("/search")
	public Response searchPag2(@BeanParam LogFilterBean filter) {
		LogBusiness lb = new LogBusiness();

		try {
			if (!filter.getFromDate().isEmpty() && !filter.getToDate().isEmpty()) {
				Date periodod2 = new SimpleDateFormat("dd/MM/yyyy").parse(filter.getFromDate());
				Date periodoa2 = new SimpleDateFormat("dd/MM/yyyy").parse(filter.getToDate());
				if (periodod2.after(periodoa2))
					throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "DateException",
							Messages.getInstance().getMessage(IMessages.DATE_ERROR)));
			}
			Result<Log> result = lb.searchPag2(filter.getPage(), filter.getUsername(), filter.getInformationType(),
					filter.getOperationType(), filter.getFromDate(), filter.getToDate());
			if (result.getList().isEmpty())
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(result.getList()).header("X-Total-Count", result.getCount()).header("X-Per-Page",
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.RECORDED_PER_PAGE))
					.build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ParseException", e.getMessage()));
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}

	@GET
	@Path("/{id}")
	public Response getById(@PathParam("id") Long id) {
		LogBusiness lb = new LogBusiness();

		try {
			Log t = lb.getById(id);
			if (t == null)
				throw new ZEMException(ZEMException.makeResponse(Status.NOT_FOUND, IMessages.RESULT_NOT_FOUND,
						Messages.getInstance().getMessage(IMessages.RESULT_NOT_FOUND)));
			return Response.ok(t).build();
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		}
	}
}
