package org.smmo.common.util;

public class Vec4d {
	
	private final double x;
	private final double y;
	private final double z;
	private final double w;
	
	public Vec4d(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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
	
	public double getW() {
		return w;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (!(o instanceof Vec4d))
			return false;
			
		Vec4d other = (Vec4d) o;
		return ((x == other.x) && (y == other.y) && (z == other.z) && (w == other.w));
	}
	
	@Override
	public int hashCode() {
		int code = (int)((5 + x) * (3 + y) * 20  +  (1 + z) * (w + 17) * 32);
		return code;
	}		
	
}
