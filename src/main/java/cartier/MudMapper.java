package cartier;

import cartier.configuration.AppContext;
import cartier.configuration.Configuration;
import cartier.events.EventBus;
import cartier.events.Listener;
import cartier.events.MudInputEvent;
import cartier.events.UserInputEvent;
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

			AppContext.set("configuration", config);
			AppContext.set("eventbus", eventBus);

			ScriptExecutor scriptExecutor = new ScriptExecutor(
					config.getProperty("script.path"));
			eventBus.register(UserInputEvent.class, scriptExecutor);
			AppContext.set("scripts", scriptExecutor);

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
		} catch (Exception e) {
			System.exit(1);
		}
	}
}
