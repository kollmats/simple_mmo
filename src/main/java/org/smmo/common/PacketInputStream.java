package org.smmo.common;

import java.io.InputStream;
import java.io.IOException;
import org.smmo.common.util.*;

public class PacketInputStream extends InputStream {

	private InputStream stream;

	public PacketInputStream(InputStream stream) {
		this.stream = stream;
	}

	@Override
    public int read() throws IOException {
        return stream.read();		
    }
	
    public int readByte() throws IOException {
        return read() & 0xFF;
    }
	
    public int readU16() throws IOException {
        int first = readByte();
        int second = readByte();
        int ret = (first) | (second << 8);
        return ret;
    }

    public long readU32() throws IOException {
        int a = readByte();
        int b = readByte();
        int c = readByte();
        int d = readByte();
        long ret = ((long) (a | b << 8 | c << 16 | d << 24)) & 0xFFFFFFFFL;
        return ret;
    }
	
	public Vec4i readVec4i() throws IOException {
		int x = readU16();
		int y = readU16();
		int z = readU16();
		int w = readU16();
		return new Vec4i(x, y, z, w);		
	}

	
	
}
