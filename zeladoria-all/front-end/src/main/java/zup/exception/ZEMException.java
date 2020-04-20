package zup.exception;

import java.sql.SQLException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.hibernate.HibernateException;

public class ZEMException extends WebApplicationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ZEMException(Response response) {
		// TODO Auto-generated constructor stub
	super(response);
	}


	public static Response makeResponse(Status status, String code, String message) {
		ErrorMessage errorMessage = new ErrorMessage(message, code, "http://zem.org");
		return Response.status(status)
				.entity(errorMessage).type(MediaType.APPLICATION_JSON)
				.build();
	}
	
	public static ZEMException exceptionHandle(Exception ex){
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));
		if (ex instanceof DataAccessLayerException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", ex.getMessage()));
		if (ex instanceof ZEMException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ZEMException", ex.getMessage()));
		if (ex instanceof ModelException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", ex.getMessage()));
		if (ex instanceof SQLException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", ex.getMessage()));
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));
		if (ex instanceof HibernateException)
			return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", ex.getMessage()));

     	return new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "Exception", ex.getMessage()));
	}
	
}
