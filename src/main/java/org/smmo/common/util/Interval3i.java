package org.smmo.common.util;

public class Interval3i {
	public final int xMin;
	public final int xMax;
	public final int yMin;
	public final int yMax;
	public final int zMin;
	public final int zMax;

	
	public Interval3i(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.zMax = zMax;
	}

	
	public boolean contains(int x, int y, int z) {
		return ((xMin <= x) && (x < xMax) && (yMin <= y) && (y < yMax) && (zMin <= z) && (z < zMax));
	}
}
