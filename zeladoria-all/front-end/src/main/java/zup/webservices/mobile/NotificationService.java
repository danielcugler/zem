package zup.webservices.mobile;

import javax.ws.rs.client.Client;	import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotificationService {
	
	public static void main(String[] args) {
	/*
		FCMRequest fcmRequest = new FCMRequest();
		fcmRequest.setTitle("Titulo notificação");
		fcmRequest.setBody("Teste Notificação");
		fcmRequest.setSubscriberId("e-aHhw4oILI:APA91bHUJKrPHXsoYd-oJ2Z9vB_R-f3YkASDnZKahFPfZfNU2GQjs19oEZXauwrJEA5F5SZqwbwrVVzbl0VfkKZ3vVvSMUI0eZiwH1mATpg_qv8FKAR6VxIus6YWLgqqJ09iW7U7Vn_W");
		fcmRequest.setExpiry("expiry");
		fcmRequest.setType("application/json");
		//set the rquest data
		Client client = ClientBuilder.newClient();
		    WebTarget target = client.target("https://fcm.googleapis.com/fcm/send");
		                Entity<FCMRequest> entity = Entity.entity(fcmRequest, MediaType.APPLICATION_JSON_TYPE);
		                Response authResponse = target.request()
		                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
		                        .header(HttpHeaders.AUTHORIZATION, "AAAAodQAfU8:APA91bHPZHVfcRaxMuB0Rhve3vzsA9Fl_eBycasZ0dlU0CHC7FR4nBr-_3qy1fnR1ohrV1ECT4pm8vMFF_qkPBxN1QU1XTl7wrm53JAFggNnn_gTV0yxGUoQuBkoObW1yvLQTmkPzqEj")
		                        .post(entity, Response.class);
	*/
NotificationService ns = new NotificationService();
ns.doPost();
	}
	




	    public void doPost() {
	    	HttpClient client = HttpClientBuilder.create().build();
	    	HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
	    	post.setHeader("Content-type", "application/json");
	    	post.setHeader("Authorization", "key=AAAASxbAkYs:APA91bE5L8nRzN9y3zfRFtJcxyr5AHCz9WJfBUK69TNZtHolF7gTXrPudD3Bt8eeMAC6rKks4xhAVnwKZ46PkFEzPhabQpxs7VxooQOAxvu0sFVh1mHx4owhjgGxlkVCuLsuFNkJBvAw");
	    	JSONObject message = new JSONObject();
	    	message.put("to", "ffpqwxZHW6I:APA91bG3jJK6Z-wu5ZZoxtcqvQF4jKqiTANcUkN4ZpzcjJdkUD-ibSFbPs1f5EyE0Vrivl4QK3zjYlMh_ZLoQ19B7ksUwj8HZtqye_IULCt4wKIQGdernCWmu5w5Dc6iL4S-X667TwUs");
	    	//message.put("to", "f2zLyFxE0dU:APA91bFlCBobK9fJnlUeieGBjp9bN4FLoelrqblC7-YrkLLP-hBSd9SMKCZGBRGNB95-K14swTJVK3hUuy3Y1pNwfG9swbXQ3PjZmnCwCqfY61C3uXOtxdhal5en8b3BB_k04AJy-grB");
	    	message.put("priority", "high");
	    	message.put("sound", "default");
	    	JSONObject notification = new JSONObject();
	    	notification.put("title", "Notificação zem");
	    	notification.put("body", "Novo comunicado");
	    	message.put("notification", notification);
	    	post.setEntity(new StringEntity(message.toString(), "UTF-8"));
	    	HttpResponse response;
			try {
				response = client.execute(post);
				System.out.println(response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	System.out.println(message);
	    }


	
	public void sendNotification(){
		
	}
	}
