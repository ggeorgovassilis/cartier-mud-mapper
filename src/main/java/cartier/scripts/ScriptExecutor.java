package cartier.scripts;

import java.io.File;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;

import cartier.configuration.AppContext;
import cartier.events.EventBus;
import cartier.events.Listener;
import cartier.events.UserInputEvent;

public class ScriptExecutor implements Listener<UserInputEvent>{

	public final static String prefix = "cartier:";
	protected ScriptEngine scriptEngine;
	protected String scriptLocation;
	protected long scriptLastModifiedTimestamp;
	protected Logger log = Logger.getLogger(ScriptExecutor.class);
	protected Bindings bindings;
	
	protected void loadScriptIfChanged() throws Exception{
		File file = new File(scriptLocation);
		if (file.lastModified()>scriptLastModifiedTimestamp){
			log.info("Reloading "+scriptLocation);
			FileReader reader = new FileReader(scriptLocation);
			bindings = scriptEngine.createBindings();
			EventBus bus = AppContext.get("eventbus");
			ScriptsLib lib = new ScriptsLib(bus);
			bindings.put("lib", lib);
			
			scriptEngine.eval(reader, bindings);
			
			reader.close();
			scriptLastModifiedTimestamp = file.lastModified();
		}
	}
	
	public ScriptExecutor(String scriptLocation) throws Exception{
		this.scriptLocation = scriptLocation;
		ScriptEngineManager factory = new ScriptEngineManager();
		scriptEngine = factory.getEngineByName("JavaScript");
		loadScriptIfChanged();
	}

	public void executeCommand(String javascript) {
		try {
			loadScriptIfChanged();
			scriptEngine.eval(javascript, bindings);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void process(UserInputEvent event) {
		if (event.getLine().startsWith(prefix))
			executeCommand(event.getLine().substring(prefix.length()));
	}
}
