package cartier.model;

import java.io.Serializable;

public class Exit implements Serializable{

	public enum Direction {
		north(0,-1, 0), north_east(1,-1, 0), east(1,0, 0), south_east(1,1, 0), south(0,1, 0), south_west(-1,-1, 0), west(-1,0, 0), north_west(-1,-1, 0), down(0,0,-1), up(0,0,1);
		
		Direction(int dx, int dy, int dz){
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
		}

		public final int dx;
		public final int dy;
		public final int dz;
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
