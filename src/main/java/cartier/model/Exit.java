package cartier.model;

public class Exit {

	public enum Direction {
		north, east, south, west, down, up;
	}

	public enum Type {
		door, wall, hidden, plain
	}

	protected Direction direction = Direction.north;
	protected Type type = Type.wall;
	protected String name;

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
