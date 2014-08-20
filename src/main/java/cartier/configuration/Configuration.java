package cartier.configuration;

import java.io.InputStream;
import java.util.Properties;

public class Configuration extends Properties {

	public void loadConfiguration() {
		try {
			try (InputStream is = getClass().getClassLoader()
					.getResourceAsStream("config.properties")) {
				load(is);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public int getInt(String property){
		return Integer.parseInt(getProperty(property));
	}

	public int getServerPort(){
		return getInt("webserver.port");
	}
}
