package cartier.webserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import cartier.configuration.AppContext;
import cartier.events.CommandForBrowserEvent;
import cartier.events.Listener;

public class JsonServlet extends HttpServlet implements
		Listener<CommandForBrowserEvent> {

	private ObjectMapper json = new ObjectMapper();
	private List<CommandForBrowserEvent> commandQueue = new ArrayList<CommandForBrowserEvent>();

	protected void showMapOverview(HttpServletResponse resp)
			throws ServletException, IOException {
		String[] maps = new File("personalization/maps")
				.list(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".js");
					}
				});
		PrintWriter pw = resp.getWriter();
		json.writeValue(pw, maps);
		pw.flush();
	}

	protected void showCommandQueue(HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("utf-8");
		ServletOutputStream sos = resp.getOutputStream();
		CommandForBrowserEvent cmd = null;
		synchronized (commandQueue) {
			if (!commandQueue.isEmpty()) {
				cmd = commandQueue.remove(0);
				json.writeValue(sos, cmd);
			}
		}
		sos.flush();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json");
		String url = req.getRequestURI();
		if ("/json/queue".equals(url))
			showCommandQueue(resp);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		AppContext.eventBus.register(CommandForBrowserEvent.class, this);
	}

	@Override
	public void destroy() {
		super.destroy();
		AppContext.eventBus.unregister(this);
	}

	@Override
	public void process(CommandForBrowserEvent event) {
		synchronized (commandQueue) {
			commandQueue.add(event);
		}
	}
}
