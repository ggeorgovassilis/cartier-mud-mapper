package cartier.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import cartier.configuration.Configuration;

public class WebServer {

	protected int port;
	protected Server server;
	protected ServletHandler handler;

	public WebServer(int port) {
		this.port = port;
	}

	public void start() {
		server = new Server(port);
		handler = new ServletHandler();
		server.setHandler(handler);
		handler.addServletWithMapping(FileServlet.class.getName(), "/web/*");
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void stop(){
		try {
			server.stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
