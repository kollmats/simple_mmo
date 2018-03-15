package org.smmo.common.readers;

import org.smmo.common.PacketInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public interface PacketReader {
	public Object readFrom(PacketInputStream inputStream) throws IOException, ClassNotFoundException;
}
