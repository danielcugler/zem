package zup.messages;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Messages {

	private Properties properties;

	private static Messages messages;

	private Messages() {

		properties = new Properties();

		try {
			properties.load(this.getClass().getResourceAsStream("/messages.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Messages getInstance() {
		if (messages == null) {
			messages = new Messages();
		}

		return messages;
	}

	public String getMessage(String key) {

		if (properties == null) {
			new Messages();
		}

		return properties.getProperty(key);
	}

	public String getMessage(String key, Object... args) {

		return MessageFormat.format(properties.getProperty(key), args);
	}
	
	public Map<String, String> makeMessage(String code) {
		Map<String, String> message = new HashMap<String, String>();
		message.put("code", code);
		message.put("message", Messages.getInstance().getMessage(code));
		return message;
	}

}
