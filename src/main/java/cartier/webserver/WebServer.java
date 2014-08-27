package cartier.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import cartier.utils.Utils;

public class WebServer extends Thread {

	protected int port;
	protected Server server;
	protected ServletHandler handler;
	protected String mapsDirectory;

	public WebServer(int port, String mapsDirectory) {
		this.port = port;
		this.mapsDirectory = mapsDirectory;
	}

	public void run() {
		try {
			server = new Server(port);
			handler = new ServletHandler();
			server.setHandler(handler);
			ServletHolder fileServlet = handler.addServletWithMapping(FileServlet.class.getName(), "/web/*");
			server.start();
			server.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void terminate() {
		try {
			server.stop();
			Utils.terminate(this);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
