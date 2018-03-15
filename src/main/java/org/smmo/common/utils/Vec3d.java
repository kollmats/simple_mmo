package org.smmo.common.util;

import java.lang.Math;

public class Vec3d {
	
	public final double x;
	public final double y;
	public final double z;
	
	public Vec3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof Vec3d))
			return false;
			
		Vec3d other = (Vec3d) o;
		return ((x == other.x) && (y == other.y) && (z == other.z));
	}
	
	@Override
	public int hashCode() {
		int code = (int)((5 + x) * (3 + y) * 20  +  (1 + z) * 32);
		return code;
	}		
	
}
