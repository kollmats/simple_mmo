package org.smmo.common.readers;

import org.smmo.common.WorldMap;
import org.smmo.common.PacketInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

public class WorldMapReader implements PacketReader {
	
	@Override
	public Object readFrom(PacketInputStream inputStream) throws IOException, ClassNotFoundException {
		ObjectInputStream oos = new ObjectInputStream(inputStream);
		Object o = oos.readObject();
		return o;
	}
}

