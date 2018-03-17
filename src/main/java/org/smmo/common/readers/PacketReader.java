package org.smmo.common.readers;


import java.io.InputStream;
import java.lang.ClassNotFoundException;
import java.io.IOException;

import org.smmo.common.actions.Action;

public abstract class PacketReader {
	public abstract Action readFrom(InputStream inputStream) throws IOException, ClassNotFoundException;

	public PacketReader(long actorId) {
		//
	}
}
