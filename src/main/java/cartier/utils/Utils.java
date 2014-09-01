package cartier.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import cartier.events.Event;

public class Utils {

	static Logger log = Logger.getLogger(Utils.class);
	
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
			ByteArrayInputStream bais = new ByteArrayInputStream(
					baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Event) ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] getClassPathResource(String cp) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			InputStream is = Utils.class.getClassLoader().getResourceAsStream(
					cp);
			int i = 0;
			while (-1 != (i = is.read())) {
				baos.write(i);
			}
			is.close();
			byte[] content = baos.toByteArray();
			baos.close();
			return content;
		} catch (Exception e) {
			log.error(e.getMessage()+" when accessing "+cp);
			throw new RuntimeException(e);
		}
	}
}
