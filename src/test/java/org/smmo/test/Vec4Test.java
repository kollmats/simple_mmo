package org.smmo.test;

import org.junit.Test;
import static org.junit.Assert.*;

import org.smmo.common.Vec4;

public class Vec4Test {
	static double DELTA = 0.00000001d;
	
	@Test
	public void subtract() {
		Vec4 v1 = new Vec4(10, 9, 8, 7);
		Vec4 v2 = new Vec4(7, 8, 9, 10);
		Vec4 v3 = Vec4.subtract(v1, v2);

		assertEquals(v3.x, 3.0d, DELTA);
		assertEquals(v3.y, 1.0d, DELTA);
		assertEquals(v3.z, -1.0d, DELTA);
		assertEquals(v3.w, -3.0d, DELTA);
	}
	
	@Test
	public void dot() {
		Vec4 v1 = new Vec4(1, 2, 3, 4);
		Vec4 v2 = new Vec4(2, 2, 2, 2);
		double dot = Vec4.dot(v1, v2);
		assertEquals(dot, 2.0d + 4.0d + 6.0d + 8.0d, DELTA);
	}
	
	@Test
	public void norm() {
		Vec4 v = new Vec4(1, 1, 1, 1);
		double norm = Vec4.norm(v);
		assertEquals(norm, 2.0d, DELTA);
	}

}
