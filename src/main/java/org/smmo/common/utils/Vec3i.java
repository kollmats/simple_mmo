package org.smmo.common.util;

public class Vec3i {

	private final Vec3d vec3d;
	
	public Vec3i(int x, int y, int z) {
		vec3d = new Vec3d((double) x, (double) y, (double) z);
	}

	public int getX() {
		return (int) vec3d.getX();
	}
	
	public int getY() {
		return (int) vec3d.getY();
	}
	
	public int getZ() {
		return (int) vec3d.getZ();
	}
}

