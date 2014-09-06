package cartier.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import cartier.utils.Utils;

public class EventBus extends Thread {

	protected Map<Class<? extends Event>, List<Listener<? extends Event>>> listeners = new HashMap<>();
	protected Logger log = Logger.getLogger(EventBus.class);
	protected AtomicBoolean active = new AtomicBoolean(false);
	protected ArrayBlockingQueue<Event> queue = new ArrayBlockingQueue<Event>(
			10);

	protected void enter() {
		if (active.get())
			log.error("Recursive call in event bus for stack: ",
					new RuntimeException());
		active.set(true);
	}

	protected void leave() {
		active.set(false);
	}

	public <T extends Event> void register(Class<T> eventType,
			Listener<T> listener) {
		synchronized (listeners) {
			enter();
			List<Listener<?>> list = listeners.get(eventType);
			if (list == null) {
				list = new ArrayList<Listener<?>>();
				listeners.put(eventType, list);
			}
			if (list.contains(listener))
				throw new IllegalArgumentException("Listener " + listener
						+ " already registered for event " + eventType);
			list.add(listener);
			leave();
		}
	}

	public <T extends Event> void unregister(Class<T> eventType,
			Listener<T> listener) {
		synchronized (listeners) {
			enter();
			List<Listener<?>> list = listeners.get(eventType);
			if (list == null) {
				return;
			}
			list.remove(listener);
			leave();
		}
	}

	public void unregister(Listener<?> listener) {
		synchronized (listeners) {
			enter();
			for (List<Listener<?>> list : listeners.values())
				list.remove(listener);
			leave();
		}
	}

	public <T extends Event> void post(T event) {
		try {
			Event copy = Utils.clone(event);
			queue.put(copy);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void run() {
		while (true) {
			try {
				Event event = queue.take();
				@SuppressWarnings({ "unchecked", "rawtypes" })
				List<Listener<Event>> l = (List) listeners
						.get(event.getClass());
				List<Listener<Event>> copy = null;
				if (l != null)
					synchronized (l) {
						copy = new ArrayList<Listener<Event>>(l);
					}
				else
					copy = new ArrayList<Listener<Event>>();
				for (Listener<Event> listener : copy)
					try {
						listener.process(event);
					} catch (Exception e) {
						e.printStackTrace();
					}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void terminate(){
		post(new ShutdownEvent());
		Utils.terminate(this);
	}
}
