package org.smmo.common;

import java.io.OutputStream;
import java.io.IOException;
import org.smmo.common.util.*;

public class PacketOutputStream extends OutputStream {

	private OutputStream stream;

	public PacketOutputStream(OutputStream stream) {
		this.stream = stream;
	}

	@Override
    public void write(int value) throws IOException {
		// The contract is to write only the last byte of the int, but whatever implementation of stream that was passed should handle that.
        stream.write(value);		
    }

    public void writeByte(int value) throws IOException {
        stream.write(value & 0xFF);
    }

    public void writeU16(int value) throws IOException {
        stream.write((int)  (value & 0x00FF));
        stream.write((int) ((value & 0xFF00) >> 8));
    }	

	public void writeU32(long value) throws IOException {
        stream.write((int)  (value & 0x000000FFL));
        stream.write((int) ((value & 0x0000FF00L) >> 8));
        stream.write((int) ((value & 0x00FF0000L) >> 16));
        stream.write((int) ((value & 0xFF000000L) >> 24));
	}

	public void writeVec4i(Vec4i vec) throws IOException {
		writeU16(vec.getX());
		writeU16(vec.getY());
		writeU16(vec.getZ());
		writeU16(vec.getW());		
	}
}
