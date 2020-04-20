package zup.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.HibernateException;
import org.json.JSONObject;

import zup.bean.Firebase;
import zup.business.FirebaseBusiness;
import zup.exception.DataAccessLayerException;
import zup.exception.ModelException;
import zup.exception.ZEMException;
import zup.messages.IMessages;

public class NotifyTeste {
	public void sendNotification(String title, String body) {
		HttpClient client = HttpClientBuilder.create().build();
		try {
			HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
			post.setHeader("Content-type", "application/json");
			// Chave do servidor firebase
			post.setHeader("Authorization",
					"key=AAAASxbAkYs:APA91bE5L8nRzN9y3zfRFtJcxyr5AHCz9WJfBUK69TNZtHolF7gTXrPudD3Bt8eeMAC6rKks4xhAVnwKZ46PkFEzPhabQpxs7VxooQOAxvu0sFVh1mHx4owhjgGxlkVCuLsuFNkJBvAw");
			FirebaseBusiness fb = new FirebaseBusiness();
			List<Firebase> citizens = fb.findAll();
			JSONObject notification = new JSONObject();
			notification.put("title", title);
			notification.put("body", body);
			JSONObject message = new JSONObject();
			message.put("priority", "high");
			message.put("sound", "default");
			message.put("notification", notification);
			List<String> tokens = new ArrayList<String>();
			for(Firebase fr: citizens){
			tokens.add(fr.getToken());
			}
				// token do usuario
			message.put("registration_ids", tokens);
			post.setEntity(new StringEntity(message.toString(), "UTF-8"));
			HttpResponse response;
			response = client.execute(post);
		    message.remove("to");
			System.out.println("terminou");
			return;
		} catch (ClientProtocolException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ClientProtocolException", e.getMessage()));
		} catch (IOException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "IOException", e.getMessage()));
		} catch (HibernateException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "HibernateException", e.getMessage()));
		} catch (DataAccessLayerException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "DataAccessLayerException", e.getMessage()));
		} catch (SQLException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "SQLException", e.getMessage()));
		} catch (ModelException e) {
			throw new ZEMException(ZEMException.makeResponse(Status.BAD_REQUEST, "ModelException", e.getMessage()));
		}
	}
	
	public static void main(String[] args) {
		NotifyTeste nt = new NotifyTeste();
		nt.sendNotification("teste Novo", "teste Novo");
	}
}
