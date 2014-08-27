package cartier.events;

public class TextToClientEvent extends AbstractTextEvent{

	public TextToClientEvent(){
	}

	public TextToClientEvent(String line){
		setLine(line);
	}
}
