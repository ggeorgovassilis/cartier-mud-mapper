package cartier.events;

public class AbstractTextEvent extends Event{

	protected String line;

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

}
