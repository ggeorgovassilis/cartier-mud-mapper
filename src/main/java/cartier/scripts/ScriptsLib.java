package cartier.scripts;

import cartier.events.EventBus;
import cartier.events.ReloadScriptsEvent;
import cartier.events.TextToClientEvent;
import cartier.events.TextToMudEvent;
import cartier.maps.MapsManager;

public class ScriptsLib {

	protected EventBus bus;
	protected ScriptExecutor scripts;
	protected MapsManager mapsManager;
	
	public ScriptsLib(EventBus bus, ScriptExecutor scripts, MapsManager mapsManager){
		this.bus = bus;
		this.scripts = scripts;
		this.mapsManager = mapsManager;
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
	
	public String[] getMapNames(){
		return mapsManager.getMapNames();
	}

	public void loadMap(String fileName){
		mapsManager.loadMap(fileName);
	}
	
	public void mapZoomIn(){
		mapsManager.zoomIn();
	}

	public void mapZoomOut(){
		mapsManager.zoomOut();
	}

	public void mapZoomReset(){
		mapsManager.zoomReset();
	}
}
