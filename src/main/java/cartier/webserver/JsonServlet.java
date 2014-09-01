package cartier.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonServlet extends HttpServlet {

	private ObjectMapper json = new ObjectMapper();

	protected void showMapOverview(HttpServletResponse resp)
			throws ServletException, IOException {
		String[] maps = new File("personalization/maps").list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".js");
			}
		});
		PrintWriter pw = resp.getWriter();
		json.writeValue(pw, maps);
		pw.flush();
	}
	
	protected void showMap(HttpServletResponse resp, String map) throws IOException{
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		String url = req.getRequestURI();
		if ("/json/maps/".equals(url))
			showMapOverview(resp); else
		if (url.startsWith("/json/maps/"))
			showMap(resp, url.substring(12));
	}
}
