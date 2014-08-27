package cartier.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import cartier.events.EventBus;
import cartier.events.TextToClientEvent;
import cartier.events.Listener;
import cartier.events.MudInputEvent;
import cartier.events.TextToMudEvent;
import cartier.events.UserInputEvent;
import cartier.utils.Utils;

public class Proxy extends Thread{
	
	protected int localPort;
	protected String remoteAddress;
	protected ServerSocket serverSocket;
	protected Socket clientSocket;
	protected Socket mudSocket;
	protected int remotePort;
	protected Pipe clientToMudPipe;
	protected Pipe mudToClientPipe;
	protected Logger log = Logger.getLogger(Proxy.class);
	protected EventBus eventBus;
	
	public Proxy(int localPort, String remoteAddress, int remotePort, EventBus eventBus){
		this.localPort = localPort;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		this.eventBus = eventBus;
	}
	
	public void start(){
		try {
			serverSocket = new ServerSocket(localPort);
			super.start();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void run(){
		while (true){
			try {
				log.info("Waiting for connection from client");
				
				clientSocket = serverSocket.accept();
				log.info("Client connected to proxy");
				log.info("Connecting to "+remoteAddress+":"+remotePort);
				mudSocket = new Socket(remoteAddress, remotePort);
				log.info("Connected to "+remoteAddress+":"+remotePort);
				clientToMudPipe = new Pipe("client->mud", clientSocket, mudSocket, new Pipe.LineCallback() {
					
					@Override
					public void onLineWritten(String line) {
						eventBus.post(new UserInputEvent(line));
					}
				}, eventBus, true);
				mudToClientPipe = new Pipe("mud->client", mudSocket, clientSocket, new Pipe.LineCallback() {
					
					@Override
					public void onLineWritten(String line) {
						eventBus.post(new MudInputEvent(line));
					}
				}, eventBus, false);
				clientToMudPipe.start();
				mudToClientPipe.start();
				eventBus.register(TextToClientEvent.class, (Listener)mudToClientPipe);
				eventBus.register(TextToMudEvent.class, (Listener)clientToMudPipe);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void terminate(){
		Utils.close(serverSocket);
		Utils.terminate(this);
	}

}
