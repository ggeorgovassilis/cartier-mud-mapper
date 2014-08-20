package cartier;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import cartier.configuration.Configuration;
import cartier.webserver.WebServer;

public class MudMapper {

	public static void main(String... args) throws Exception {
		Configuration config = new Configuration();
		config.loadConfiguration();
		WebServer server = new WebServer(config.getServerPort());
		server.start();
	}
}
