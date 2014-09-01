package cartier.configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration extends Properties {

	Logger log = Logger.getLogger(Configuration.class);
	
	public void loadConfiguration() {
		try {
			try (InputStream is = getClass().getClassLoader()
					.getResourceAsStream("config.properties.default")) {
				load(is);
			}
			try (InputStream is = new FileInputStream("personalization/config.properties")) {
				if (is!=null)
					load(is);
				else log.error("Missing config.properties in personalization");
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
	
	public int getProxyPort(){
		return getInt("proxy.port");
	}
	
	public String getMudAddress(){
		return getProperty("remote.address");
	}
	
	public int getMudPort(){
		return getInt("remote.port");
	}
}
