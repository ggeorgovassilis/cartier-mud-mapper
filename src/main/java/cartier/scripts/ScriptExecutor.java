package cartier.scripts;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;

import cartier.configuration.AppContext;
import cartier.events.Event;
import cartier.events.EventBus;
import cartier.events.Listener;
import cartier.events.ReloadScriptsEvent;
import cartier.events.TextToClientEvent;
import cartier.events.UserInputEvent;
import cartier.utils.Utils;

public class ScriptExecutor implements Listener<Event> {

	public final static String prefix = "c:";
	protected ScriptEngine scriptEngine;
	protected String scriptLocation;
	protected Logger log = Logger.getLogger(ScriptExecutor.class);
	protected Bindings bindings;

	public String[] getScriptNames(){
		String[] files = new File("personalization/scripts")
		.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".js");
			}
		});
		Arrays.sort(files);
		return files;
	}
	
	protected void readAndLoad(String[] files) throws Exception {
		bindings = scriptEngine.createBindings();
		EventBus bus = AppContext.eventBus;
		ScriptsLib lib = new ScriptsLib(bus, this, AppContext.mapsManager);
		bindings.put("lib", lib);
		log.info("Loading main.js");
		String mainScript = new String(Utils.getClassPathResource("main.js"));
		scriptEngine.eval(mainScript, bindings);
		for (String fileName : files) {
			log.info("Loading " + fileName);
			log.info("Reloading " + fileName);
			FileReader reader = new FileReader("personalization/scripts/"+fileName);
			scriptEngine.eval(reader, bindings);
			reader.close();
		}
	}

	protected void loadScripts() throws Exception {
		log.info("Reloading scripts");
		readAndLoad(getScriptNames());
	}

	public ScriptExecutor(String scriptLocation) throws Exception {
		this.scriptLocation = scriptLocation;
		ScriptEngineManager factory = new ScriptEngineManager();
		scriptEngine = factory.getEngineByName("JavaScript");
		loadScripts();
	}

	public void executeCommand(String javascript) {
		try {
			scriptEngine.eval(javascript, bindings);
		} catch (Exception e) {
			AppContext.eventBus.post(new TextToClientEvent("* ERROR *, see logs "+e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	protected void handleUserInputEvent(UserInputEvent event) {
		if (event.getLine().startsWith(prefix))
			executeCommand(event.getLine().substring(prefix.length()));
	}

	protected void handleReloadScriptsEvent() {
		try {
			loadScripts();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void process(Event event) {
		if (event instanceof UserInputEvent) {
			handleUserInputEvent((UserInputEvent) event);
		}
		if (event instanceof ReloadScriptsEvent) {
			handleReloadScriptsEvent();
		}
	}
}
