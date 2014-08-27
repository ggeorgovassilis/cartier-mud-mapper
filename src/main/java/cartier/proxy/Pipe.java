package cartier.proxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import cartier.events.AbstractTextEvent;
import cartier.events.DisconnectEvent;
import cartier.events.Event;
import cartier.events.EventBus;
import cartier.events.TextToClientEvent;
import cartier.events.Listener;
import cartier.events.TextToMudEvent;
import cartier.scripts.ScriptExecutor;
import cartier.utils.Utils;

public class Pipe extends Thread implements Listener<Event> {

	interface LineCallback {
		void onLineWritten(String line);
	}

	protected Socket sFrom;
	protected Socket sTo;
	protected String name;
	protected Logger log = Logger.getLogger(Pipe.class);
	protected LineCallback callback;
	protected EventBus bus;
	protected boolean waitForEol;
	protected OutputStream to;

	public Pipe(String name, Socket sFrom, Socket sTo, LineCallback callback,
			EventBus bus, boolean waitForEol) {
		this.name = name;
		this.sFrom = sFrom;
		this.sTo = sTo;
		this.callback = callback;
		this.bus = bus;
		this.waitForEol = waitForEol;
	}

	public void terminate() {
		Utils.close(sFrom);
		Utils.close(sTo);
		Utils.terminate(this);
	}

	public void run() {
		InputStream from = null;
		to = null;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream lineBuffer = new ByteArrayOutputStream();
		try {
			from = sFrom.getInputStream();
			to = sTo.getOutputStream();
			while (true) {
				int l = from.read(buffer);
				if (l == -1)
					break;
				for (int i = 0; i < l; i++) {
					byte b = buffer[i];
					if (b == '\r')
						continue;
					if (b == (byte) '\n') {
						byte[] bytes = lineBuffer.toByteArray();
						String line = new String(bytes, "ASCII");
						lineBuffer.reset();
						callback.onLineWritten(line);
						if (waitForEol) {
							if (!line.startsWith(ScriptExecutor.prefix)) {
								synchronized (to) {
									to.write(bytes);
									to.write('\n');
									to.flush();
								}
							}
						}
					} else
						lineBuffer.write(b);
				}
				if (!waitForEol) {
					to.write(buffer, 0, l);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("Disconnecting pipe " + name);
			bus.post(new DisconnectEvent());
			Utils.close(from);
			Utils.close(to);
		}
	}

	protected void inject(String text) {
		try {
			synchronized (to) {
				to.write(text.getBytes("ASCII"));
				to.flush();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void process(Event event) {
		if (event instanceof DisconnectEvent)
			terminate();
		else if (event instanceof TextToClientEvent)
			inject(((AbstractTextEvent) event).getLine());
		else if (event instanceof TextToMudEvent)
			inject(((AbstractTextEvent) event).getLine());
	}

}
