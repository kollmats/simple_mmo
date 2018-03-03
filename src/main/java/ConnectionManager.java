package simple_mmo;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Iterator;

public class ConnectionManager {


	public interface OnProcessBuffer {
		public void onProcess(Connection connection, ByteBuffer buffer);
	}

	List<Connection> connections = new LinkedList<Connection>();
	Map<Connection, List<ByteBuffer>> bufferQueues = new HashMap<Connection, List<ByteBuffer>>();

	public List<Connection> getConnections() {
		synchronized(this) {
			return new LinkedList<Connection>(connections);
		}
	}
	
	public void trackConnection(Connection connection) {
		synchronized(this) {
			List<ByteBuffer> list = new LinkedList<ByteBuffer>();
			bufferQueues.put(connection, list);
			connections.add(connection);
		}
	}

	public void untrackConnection(Connection connection) {
		synchronized(this) {
			connections.remove(connection);
			bufferQueues.remove(connection);
		}
	}

	public void addBuffer(Connection connection, ByteBuffer buffer) {
		ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
		
		synchronized(this) {
			List<ByteBuffer> queue = bufferQueues.get(connection);
			queue.add(readOnlyBuffer);
		}
	}

	public void processBuffers(OnProcessBuffer onProcessBuffer) {

		if (onProcessBuffer != null) {
			synchronized(this) {
				for (Connection conn : connections) {
					List<ByteBuffer> queue = bufferQueues.get(conn);
					
					Iterator<ByteBuffer> iterator = queue.iterator();
					while (iterator.hasNext()) {
						ByteBuffer buffer = iterator.next();
						onProcessBuffer.onProcess(conn, buffer);
						iterator.remove();
					}
				}				
			}
		}
	}
}
