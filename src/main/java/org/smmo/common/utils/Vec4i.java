package org.smmo.common.util;

public class Vec4i {

	private final Vec4d vec4d;

	public Vec4i(Vec3i vec3i, int w) {
		this(vec3i.getX(), vec3i.getY(), vec3i.getZ(), w);
	}
	
	public Vec4i(int x, int y, int z, int w) {
		vec4d = new Vec4d((double) x, (double) y, (double) z, (double) w);
	}
	
	public int getX() {
		return (int) vec4d.getX();
	}
	
	public int getY() {
		return (int) vec4d.getY();
	}
	
	public int getZ() {
		return (int) vec4d.getZ();
	}
	
	public int getW() {
		return (int) vec4d.getW();
	}
}
