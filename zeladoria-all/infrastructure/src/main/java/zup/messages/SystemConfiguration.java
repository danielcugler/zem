package zup.messages;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public class SystemConfiguration {
	private Properties properties;

	private static SystemConfiguration systemConfiguration;

	private SystemConfiguration() {

		properties = new Properties();

		try {
			properties.load(this.getClass().getResourceAsStream("/system.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static SystemConfiguration getInstance() {
		if (systemConfiguration == null) {
			systemConfiguration = new SystemConfiguration();
		}

		return systemConfiguration;
	}

	public String getSystemConfiguration(String key) {

		if (properties == null) {
			new SystemConfiguration();
		}

		return properties.getProperty(key);
	}

}
