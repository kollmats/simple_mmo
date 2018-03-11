package org.smmo.server;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class Server implements Runnable {
	private static final int MAX_BUFFER_SIZE = 1024;

	private ServerSocketChannel ssc;
	private boolean isRunning = false;
	private Thread thread;

	private final String hostname;
	private final int port;

	private final ConnectionManager connectionManager;
	private final Map<Integer, Connection> connections = new ConcurrentHashMap<Integer, Connection>();
	
	public Server(ConnectionManager connectionManager, String hostname, int port) {
		this.connectionManager = connectionManager;
		this.hostname = hostname;
		this.port = port;
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
		isRunning = true;		
	}

	public void stop() {
		isRunning = false;

		for (Connection conn : connectionManager.getConnections()) {
			onCloseConnection(conn);
		}

		thread.interrupt();		
	}

	private void onCloseConnection(Connection connection) {
		connection.getThread().interrupt();
		connections.remove(connection.getId());		
		connectionManager.untrackConnection(connection);
	}

	private void onAccept(SocketChannel ss) {
		int connectionId = ss.socket().getRemoteSocketAddress().hashCode();
				
		ClientHandler handler = new ClientHandler(ss, connectionId);
		Thread clientThread = new Thread(handler);
		
		Connection connection = new Connection(connectionId, clientThread);
		connectionManager.trackConnection(connection);		
		connections.put(connectionId, connection);		
		clientThread.start();				
	}

	@Override
	public void run() {
		InetSocketAddress address = new InetSocketAddress(hostname, port);

		try {
			ssc = ServerSocketChannel.open();
			ssc.bind(address);
		} catch (Exception e) {
			isRunning = false;
			e.printStackTrace();			
		}

		while (isRunning) {
			try {
				SocketChannel ss = ssc.accept();
				onAccept(ss);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}

	
 	private class ClientHandler implements Runnable {
		private final SocketChannel ss;
		private final int id;

		public ClientHandler(SocketChannel ss, int id) {
			this.ss = ss;
			this.id = id;
		}

		public void run() {
			ByteBuffer buffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
			Connection connection = connections.get(id);
			try {			
				while (true) {
					int bytesRead = ss.read(buffer);
					
					if (bytesRead > 0) {
						buffer.flip();
						connectionManager.addBuffer(connection, buffer);						
						buffer.clear();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
