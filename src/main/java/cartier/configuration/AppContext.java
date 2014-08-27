package cartier.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppContext {

	protected static Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public static <T>T get(String key){
		return (T)map.get(key);
	}
	
	public static void set(String key, Object value){
		map.put(key, value);
	}
}
