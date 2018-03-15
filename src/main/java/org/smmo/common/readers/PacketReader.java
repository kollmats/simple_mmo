package org.smmo.common.readers;


import java.io.InputStream;
import java.lang.ClassNotFoundException;
import java.io.IOException;

public interface PacketReader {
	public Object readFrom(InputStream inputStream) throws IOException, ClassNotFoundException;
}
