package org.smmo.common.util;

import java.lang.Math;

public class Vec {
	
	public static double norm(Vec3d v) {
		return Math.sqrt(Vec.dot(v, v));
	}
	
	public static double norm(Vec4i v) {
		return Math.sqrt(Vec.dot(v, v));
	}

	public static double norm(Vec4d v) {
		return Math.sqrt(Vec.dot(v, v));
	}

	public static double dot(Vec3d a, Vec3d b) {
		return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ();
	}

	public static double dot(Vec4i a, Vec4i b) {
		return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ() + a.getW() * b.getW();
	}
	
	public static double dot(Vec4d a, Vec4d b) {
		return a.getX() * b.getX() + a.getY() * b.getY() + a.getZ() * b.getZ() + a.getW() * b.getW();
	}
	
	public static Vec3i add(Vec3i a, Vec3i b) {
		return new Vec3i(a.getX() + b.getX(),
						 a.getY() + b.getY(),
						 a.getZ() + b.getZ());
	}
	
	public static Vec4i add(Vec4i a, Vec4i b) {
		return new Vec4i(a.getX() + b.getX(),
						 a.getY() + b.getY(),
						 a.getZ() + b.getZ(),						 
						 a.getW() + b.getW());
	}
	
	public static Vec3i subtract(Vec3i a, Vec3i b) {
		return new Vec3i(a.getX() - b.getX(),
						 a.getY() - b.getY(),
						 a.getZ() - b.getZ());
	}
	
	public static Vec3d subtract(Vec3d a, Vec3d b) {
		return new Vec3d(a.getX() - b.getX(),
						 a.getY() - b.getY(),
						 a.getZ() - b.getZ());
	}

	public static Vec4d subtract(Vec4d a, Vec4d b) {
		return new Vec4d(a.getX() - b.getX(),
						 a.getY() - b.getY(),
						 a.getZ() - b.getZ(),
						 a.getW() - b.getW());
	}
	
	public static Vec4i subtract(Vec4i a, Vec4i b) {
		return new Vec4i(a.getX() - b.getX(),
						 a.getY() - b.getY(),
						 a.getZ() - b.getZ(),
						 a.getW() - b.getW());
	}
}
