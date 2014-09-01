package cartier.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Map implements Serializable{

	protected List<Tile> tiles = new ArrayList<Tile>();

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}
}
