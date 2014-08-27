package cartier.events;

public class MudInputEvent extends AbstractTextEvent{

	public MudInputEvent(){
	}

	public MudInputEvent(String line){
		setLine(line);
	}

}
