package org.smmo.server;

public class Main {
	public static final String HOSTNAME = "0.0.0.0";
	public static final int PORT = 7171;
	
	public Main() {
		ConnectionManager connectionManager = new ConnectionManager();
		Server server = new Server(connectionManager, HOSTNAME, PORT);
		server.start();

		while (true) {
			connectionManager.processBuffers((connection, buffer) -> {

				});
			
			try {
				Thread.sleep(100);
			} catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}		
	}
	
	public static void main(String args[]) {
		new Main();		
	}
	
}



