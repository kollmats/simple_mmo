package simple_mmo;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionManagerTest {

	@Test
	public void testTrackConnection() {
		Connection conn = new Connection(123);
		ConnectionManager cm = new ConnectionManager();
		cm.trackConnection(conn);
		
		assertTrue(cm.getConnections().contains(conn));
	}

	@Test
	public void testUntrackConnection() {
		Connection conn = new Connection(123);
		ConnectionManager cm = new ConnectionManager();
		cm.trackConnection(conn);
		cm.untrackConnection(conn);
		
		assertFalse(cm.getConnections().contains(conn));
	}

	@Test
	public void testProcessBuffers() {
		Connection conn = new Connection(123);
		ConnectionManager cm = new ConnectionManager();
		cm.trackConnection(conn);

		ByteBuffer bb = ByteBuffer.allocate(1);
		bb.put((byte) 10);
		bb.flip();

		cm.addBuffer(conn, bb);
		
		AtomicInteger value = new AtomicInteger();		
		cm.processBuffers((x, y) -> {
				value.set(y.get());
			});
		assertTrue(value.get() == 10);		
	}
	
	@Test
	public void testBufferCopied() {
		Connection conn = new Connection(123);
		ConnectionManager cm = new ConnectionManager();
		cm.trackConnection(conn);

		ByteBuffer bb = ByteBuffer.allocate(1);
		bb.put((byte) 10);
		bb.flip();

		cm.addBuffer(conn, bb);
		bb.clear();

		AtomicInteger value = new AtomicInteger();
		
		cm.processBuffers((x, y) -> {
				value.set(y.get());
			});

		assertEquals(10, value.get());
	}

	@Test
	public void testBufferReadOnly() {
		Connection conn = new Connection(123);
		ConnectionManager cm = new ConnectionManager();
		cm.trackConnection(conn);

		ByteBuffer bb = ByteBuffer.allocate(1);
		bb.put((byte) 10);
		bb.flip();

		cm.addBuffer(conn, bb);
		bb.clear();
		
		cm.processBuffers((x, y) -> {
				assertTrue(y.isReadOnly());
			});
	}

	
}
