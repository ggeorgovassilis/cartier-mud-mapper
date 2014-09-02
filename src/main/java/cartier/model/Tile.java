package cartier.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tile implements Serializable{

	protected List<Exit> exits = new ArrayList<Exit>();
	protected List<String> tags = new ArrayList<String>();
	protected Set<Exit.Direction> arrows = new HashSet();
	protected String title;
	protected String text;
	protected int row;
	protected int column;
	protected int level;

	public List<Exit> getExits() {
		return exits;
	}

	public void setExits(List<Exit> exits) {
		this.exits = exits;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Set<Exit.Direction> getArrows() {
		return arrows;
	}

	public void setArrows(Set<Exit.Direction> arrows) {
		this.arrows = arrows;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
