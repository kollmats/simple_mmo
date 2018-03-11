package org.smmo.common;

import java.lang.Math;

public class Vec4 {
	
	public final double x;
	public final double y;
	public final double z;
	public final double w;
	
	public Vec4(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public static Vec4 subtract(Vec4 a, Vec4 b) {
		return new Vec4(a.x - b.x,
						a.y - b.y,
						a.z - b.z,
						a.w - b.w);
	}

	public static double dot(Vec4 a, Vec4 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
	}

	public static double norm(Vec4 v) {
		return Math.sqrt(Vec4.dot(v, v));
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof Vec4))
			return false;
			
		Vec4 other = (Vec4) o;
		return ((x == other.x) && (y == other.y) && (z == other.z) && (w == other.w));
	}
	
	@Override
	public int hashCode() {
		int code = (int)((5 + x) * (3 + y) * 20  +  (1 + z) * (w + 17) * 32);
		return code;
	}		
	
}
