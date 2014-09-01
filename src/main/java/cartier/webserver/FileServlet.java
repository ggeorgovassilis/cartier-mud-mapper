package cartier.webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cartier.utils.Utils;

public class FileServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String URI = req.getRequestURI();
		if (URI.endsWith(".html"))
			resp.setContentType("text/html;charset=UTF-8");
		if (URI.endsWith(".js"))	
			resp.setContentType("application/javascript;charset=UTF-8");
		if (URI.endsWith(".css"))	
			resp.setContentType("text/css;charset=UTF-8");
		if (URI.endsWith(".gif"))
			resp.setContentType("image/gif");
		if (URI.endsWith(".jpeg"))
			resp.setContentType("image/jpeg");
		if (URI.endsWith(".png"))
			resp.setContentType("image/png");
		
		if (URI.startsWith("/"))
			URI = URI.substring(1);
		byte[] content = Utils.getClassPathResource(URI);
		resp.setContentLength(content.length);
		ServletOutputStream sos = resp.getOutputStream();
		sos.write(content);
		sos.flush();
	}
}
