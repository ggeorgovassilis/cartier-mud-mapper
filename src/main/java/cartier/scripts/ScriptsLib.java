package cartier.scripts;

import cartier.events.EventBus;
import cartier.events.ReloadScriptsEvent;
import cartier.events.TextToClientEvent;
import cartier.events.TextToMudEvent;

public class ScriptsLib {

	protected EventBus bus;
	
	public ScriptsLib(EventBus bus){
		this.bus = bus;
	}
	
	public void sendTextToClient(Object message){
		bus.post(new TextToClientEvent(message.toString()));
	}

	public void sendTextToMud(Object message){
		bus.post(new TextToMudEvent(message.toString()));
	}
	
	public void reloadScripts(){
		bus.post(new ReloadScriptsEvent());
	}
}
