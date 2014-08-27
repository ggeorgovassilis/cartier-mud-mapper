package cartier.events;

public interface Listener<T extends Event> {

	void process(T event);
}
