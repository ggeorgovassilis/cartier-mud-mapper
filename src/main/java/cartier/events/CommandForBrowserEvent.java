package cartier.events;

public class CommandForBrowserEvent extends Event {

	private String name;
	private Object payload;

	public CommandForBrowserEvent() {
	}

	public CommandForBrowserEvent(String name, Object payload) {
		this.name = name;
		this.payload = payload;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
}
