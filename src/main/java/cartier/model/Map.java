package cartier.model;

import java.util.ArrayList;
import java.util.List;

public class Map {

	protected List<Tile> tiles = new ArrayList<Tile>();

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}
}
