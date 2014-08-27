package cartier.events;

public class TextToMudEvent extends AbstractTextEvent{

	public TextToMudEvent(){
	}
	
	public TextToMudEvent(String line){
		setLine(line);
	}
}
