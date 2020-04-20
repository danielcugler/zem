package zup.webservices.mobile;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import zup.bean.Citizen;

//Exercício de Teste do Phonegap
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/android")
public class AndroidService {

	@GET
	@Path("/{name}")
	public Response helloName(@PathParam("name") String name) {		
		Map<String, String> msg = new HashMap<String, String>();		
		msg.put("message", "Bem vindo " + name + "!");
		return Response.status(Status.OK).entity(msg).build();
	}

	@GET
	@Path("/teste")
	public String teste() {
		return "Bem vindo!";
	}

}
