package cartier.maps;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import cartier.events.CommandForBrowserEvent;
import cartier.events.EventBus;
import cartier.model.Map;
import cartier.model.Tile;
import cartier.utils.Utils;

public class MapsManager {

	private File mapsDir;
	private ObjectMapper json = new ObjectMapper();
	private EventBus bus;
	
	public MapsManager(File mapsDir, EventBus bus){
		this.mapsDir = mapsDir;
		this.bus = bus;
	}
	
	public String[] getMapNames(){
		return mapsDir.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".js");
			}
		});
	}

	public void loadMap(String fileName){
		byte[] b = Utils.loadFile(new File(mapsDir, fileName));
		try {
			Map map = json.readValue(b, Map.class);
			bus.post(new CommandForBrowserEvent("loadmap", map));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
