package cartier.fakemud;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import cartier.events.EventBus;
import cartier.events.Listener;
import cartier.events.MudInputEvent;
import cartier.events.ShutdownEvent;
import cartier.events.UserInputEvent;
import cartier.proxy.Pipe;
import cartier.utils.Utils;

public class FakeMUD extends Thread implements Listener<ShutdownEvent>{

	private int port;
	private ServerSocket socket;
	private EventBus bus;
	private Logger log = Logger.getLogger(FakeMUD.class);
	
	public void terminate(){
		log.info("Terminating FakeMUD");
		bus.unregister(this);
		synchronized(socket){
			try {
				socket.close();
			} catch (IOException e) {
			}
			Utils.terminate(this);
			log.info("Terminated FakeMUD");
		}
	}
	
	public FakeMUD(int port, EventBus bus){
		super();
		this.port = port;
		this.bus = bus;
		bus.register(ShutdownEvent.class, this);
	}
	
	@Override
	public void run() {
		log.info("Running FakeMUD");
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true){
				Socket s = serverSocket.accept();
				log.info("FakeMUD accepted connection");
				Pipe clientToMudPipe = new Pipe("client->famekud", s, s, new Pipe.LineCallback() {
					@Override
					public void onLineWritten(String line) {
						bus.post(new UserInputEvent(line));
					}
				}, bus, false);
				clientToMudPipe.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String... args) throws Exception{
		EventBus bus = new EventBus();
		FakeMUD fakeMud = new FakeMUD(3791, bus);
		fakeMud.start();
	}

	@Override
	public void process(ShutdownEvent event) {
		terminate();
	}
}
