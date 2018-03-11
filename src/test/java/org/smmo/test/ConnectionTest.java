package org.smmo.test;

import org.smmo.server.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectionTest {

	@Test
	public void testEquals() {
		Connection c1 = new Connection(123);
		Connection c2 = new Connection(456);
		Connection c3 = new Connection(123);
		assertFalse(c1.equals(c2));
		assertTrue(c1.equals(c3));
	}

	@Test
	public void testHashCode() {
		Connection c1 = new Connection(123);
		Connection c2 = new Connection(456);
		Connection c3 = new Connection(123);
		assertFalse(c1.hashCode() == c2.hashCode());
		assertTrue(c1.hashCode() == c3.hashCode());
	}
	
}
