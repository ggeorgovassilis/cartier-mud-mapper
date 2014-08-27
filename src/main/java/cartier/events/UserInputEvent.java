package cartier.events;

public class UserInputEvent extends AbstractTextEvent{

	public UserInputEvent(){
	}
	
	public UserInputEvent(String line){
		setLine(line);
	}

}
