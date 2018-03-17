package org.smmo.common.writers;

import java.io.IOException;
import java.io.OutputStream;

import org.smmo.common.actions.Action;

public abstract class PacketWriter {
	public abstract void writeTo(OutputStream outputStream) throws IOException;

	public PacketWriter(Action action) {
		// Just to force implementers to write this (don't forget calling super from it)
	}
}
