package cartier.webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		if (URI.startsWith("/"))
			URI = URI.substring(1);
		InputStream is = getClass().getClassLoader().getResourceAsStream(URI);
		int i = 0;
		while (-1!=(i = is.read())){
			baos.write(i);
		}
		is.close();
		byte[] content = baos.toByteArray();
		baos.close();
		resp.setContentLength(content.length);
		ServletOutputStream sos = resp.getOutputStream();
		sos.write(content);
		sos.flush();
	}
}
