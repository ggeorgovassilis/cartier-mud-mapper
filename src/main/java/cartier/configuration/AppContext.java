package cartier.configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cartier.events.EventBus;
import cartier.maps.MapsManager;
import cartier.scripts.ScriptExecutor;

public class AppContext {

	public static Configuration configuration;
	public static EventBus eventBus;
	public static ScriptExecutor scriptExecutor;
	public static MapsManager mapsManager;
}
