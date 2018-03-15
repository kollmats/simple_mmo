package org.smmo.test;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.smmo.common.PacketOutputStream;

import java.io.ByteArrayOutputStream;

public class PacketOutputStreamTest {
	ByteArrayOutputStream baos;
	PacketOutputStream pos;

	@Before
	public void setup() {
		baos = new ByteArrayOutputStream();
		pos = new PacketOutputStream(baos);
	}
	@Test
	public void writeU32() {
		long value = 1234;

		byte b4 = (byte)((value >> 24) & 0xFF);
		byte b3 = (byte)((value >> 16) & 0xFF);
		byte b2 = (byte)((value >> 8) & 0xFF);
		byte b1 = (byte)(value & 0xFF);
		
		try {
			pos.writeU32(value);
		} catch (Exception e) {
			fail();
		}

		byte[] bytes = baos.toByteArray();
		assertEquals(bytes[0], b1);
		assertEquals(bytes[1], b2);
		assertEquals(bytes[2], b3);
		assertEquals(bytes[3], b4);				
	}
	
	@Test
	public void writeByte() {
		int value = 1234;
		byte bvalue = (byte) value;
		//byte bvalue = (byte)(value & 0xFF);
		try {
			pos.write(value);
		} catch (Exception e) {
			fail();
		}

		byte[] bytes = baos.toByteArray();
		assertEquals(bytes[0], bvalue);
	}
}
