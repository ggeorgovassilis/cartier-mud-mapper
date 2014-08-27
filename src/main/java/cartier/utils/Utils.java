package cartier.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cartier.events.Event;

public class Utils {

	public static void close(Closeable c) {
		try {
			c.close();
		} catch (Exception e) {
		}
	}

	public static void terminate(Thread t) {
		try {
			t.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Event clone(Event event) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(event);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Event)ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
