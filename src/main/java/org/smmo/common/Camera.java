package org.smmo.common;

import org.smmo.common.util.Vec3i;

public class Camera {
	public static final int ROW_COUNT    = 9;
	public static final int COLUMN_COUNT = 11;
	public static final int LAYER_COUNT  = 7;

	public static final int HEIGHT = ROW_COUNT;
	public static final int WIDTH  = COLUMN_COUNT;
	public static final int DEPTH  = LAYER_COUNT;


	public static Vec3i getBounds() {
		return new Vec3i(ROW_COUNT, COLUMN_COUNT, LAYER_COUNT);
	}	
}
