package zup.webservices.adm;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import zup.bean.AttendanceTime;
import zup.business.AttendanceTimeBusiness;
import zup.business.EntityBusiness;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ValidationException;
import zup.messages.ISystemConfiguration;
import zup.messages.SystemConfiguration;
import zup.validate.AttendanceTimeValidate;
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/attendancetime2")
public class AttendanceTimeService2 {
	private static Logger logger = Logger.getLogger(AttendanceTimeService2.class);

	AttendanceTimeBusiness mb = new AttendanceTimeBusiness();
	EntityBusiness eb = new EntityBusiness();
	AttendanceTimeValidate mv = new AttendanceTimeValidate();
	@GET
	@Path("/at")
	public Response getDefaultAttendanceTime()
			throws HibernateException, SQLException, ValidationException, DataAccessLayerException, ModelException {
			AttendanceTime at = new AttendanceTime();
			at.setLowPriorityTime(Integer
					.parseInt(SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.LOW_PRIORITY)));
			at.setMediumPriorityTime(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.MEDIUM_PRIORITY)));
			at.setHighPriorityTime(Integer.parseInt(
					SystemConfiguration.getInstance().getSystemConfiguration(ISystemConfiguration.HIGH_PRIORITY)));
			return Response.ok(at).build();	
	}
}
