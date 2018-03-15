package org.smmo.common;

import java.io.InputStream;
import java.io.IOException;

public class PacketInputStream extends InputStream {

	private InputStream stream;

	public PacketInputStream(InputStream stream) {
		this.stream = stream;
	}

	@Override
    public int read() throws IOException {
        return stream.read();		
    }
}
