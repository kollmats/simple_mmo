package org.smmo.test;

import org.smmo.server.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerTest {
	/*
	public void connectAndSendByte(byte val, String hostname, int port) throws Exception {		
		InetSocketAddress address = new InetSocketAddress(hostname, port);
		
		SocketChannel ss = SocketChannel.open(address);
		ByteBuffer buffer = ByteBuffer.allocate(1024);

		buffer.put(val);
		buffer.flip();
		ss.write(buffer);
	}
	
	@Test
	public void testListen() {
		ConnectionManager cm = new ConnectionManager();

		Server server = new Server(cm, "localhost", 9999);
		server.start();
		try {
			Thread.sleep(100);
			connectAndSendByte((byte) 32, "localhost", 9999);
			Thread.sleep(100);			
		} catch (Exception e) {
			fail();
		}

		AtomicInteger value = new AtomicInteger();		
		cm.processBuffers((x, y) -> {
				value.set(y.get());
			});

		assertEquals(value.get(), 32);
	}


	*/
}
