package org.smmo.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.smmo.common.util.*;

public class VecTest {
	private static double DELTA = 0.0000001d;
	@Test
	public void subtract() {
		Vec4i v1 = new Vec4i(10, 9, 8, 7);
		Vec4i v2 = new Vec4i(7, 8, 9, 10);
		Vec4i v3 = Vec.subtract(v1, v2);

		assertEquals(v3.getX(), 3);
		assertEquals(v3.getY(), 1);
		assertEquals(v3.getZ(), -1);
		assertEquals(v3.getW(), -3);
	}
	
	@Test
	public void dot() {
		Vec4i v1 = new Vec4i(1, 2, 3, 4);
		Vec4i v2 = new Vec4i(2, 2, 2, 2);
		double dot = Vec.dot(v1, v2);
		assertEquals(dot, 2 + 4 + 6 + 8, DELTA);
	}
	
	@Test
	public void norm() {
		Vec4i v = new Vec4i(1, 1, 1, 1);
		double norm = Vec.norm(v);
		assertEquals(norm, 2, DELTA);
	}

}
