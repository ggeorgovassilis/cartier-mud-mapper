package cartier;

import java.io.File;

import cartier.configuration.AppContext;
import cartier.configuration.Configuration;
import cartier.events.EventBus;
import cartier.events.Listener;
import cartier.events.MudInputEvent;
import cartier.events.ReloadScriptsEvent;
import cartier.events.UserInputEvent;
import cartier.fakemud.FakeMUD;
import cartier.maps.MapsManager;
import cartier.proxy.Proxy;
import cartier.scripts.ScriptExecutor;
import cartier.webserver.WebServer;

public class MudMapper {

	public static void main(String... args) throws Exception {
		try {
			Configuration config = new Configuration();
			config.loadConfiguration();

			EventBus eventBus = new EventBus();
			eventBus.start();

			MapsManager maps = new MapsManager(
					new File("personalization/maps"), eventBus);

			AppContext.configuration = config;
			AppContext.mapsManager = maps;
			AppContext.eventBus = eventBus;

			ScriptExecutor scriptExecutor = new ScriptExecutor(
					config.getProperty("script.path"));
			eventBus.register(UserInputEvent.class, (Listener) scriptExecutor);
			eventBus.register(ReloadScriptsEvent.class,
					(Listener) scriptExecutor);
			AppContext.scriptExecutor = scriptExecutor;

			WebServer server = new WebServer(config.getServerPort(),
					config.getProperty("maps.dir"));
			server.start();
			Proxy proxy = new Proxy(config.getProxyPort(),
					config.getMudAddress(), config.getMudPort(), eventBus);
			proxy.start();
			eventBus.register(MudInputEvent.class,
					new Listener<MudInputEvent>() {
						@Override
						public void process(MudInputEvent event) {
							System.out.println(">>>" + event.getLine());
						}
					});
			
			if (config.isFakeMudActive()){
				FakeMUD fakeMUD = new FakeMUD(config.getFakeMudPort(), eventBus);
				fakeMUD.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
