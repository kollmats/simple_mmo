package simple_mmo;

import java.nio.channels.SocketChannel;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.io.IOException;

class Client {

	public void connect(String hostname, int port) {
		InetSocketAddress address = new InetSocketAddress(hostname, port);
		
		try {
			SocketChannel ss = SocketChannel.open(address);

			ByteBuffer buffer = ByteBuffer.allocate(1024);

			buffer.put((byte) 32);
			buffer.flip();
			ss.write(buffer);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

