package org.smmo.common.writers;

import java.io.IOException;
import java.io.OutputStream;

public interface PacketWriter {
	public void writeTo(OutputStream outputStream) throws IOException;
}
