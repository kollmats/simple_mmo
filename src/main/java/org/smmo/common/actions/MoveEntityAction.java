package org.smmo.common.actions;

import com.google.common.collect.BiMap;
import org.smmo.common.Entity;
import org.smmo.common.Context;
import org.smmo.common.Vec4;
import org.smmo.common.WorldMap;
import org.smmo.common.WorldMapBuilder;

public class MoveEntityAction implements Action {

	private final Entity subject;
	private final Entity object;
	private final Vec4 dst;
	
	public MoveEntityAction(Entity subject, Entity object, Vec4 dst) {
		this.subject = subject;
		this.object = object;
		this.dst = dst;
	}
	
	public boolean isValid(Context c) {
		final BiMap<Entity, Vec4> cache = c.getEntityPositionCache();
		final Vec4 sPos = cache.get(subject);
		final Vec4 oPos = cache.get(object);
		
		if ((sPos == null) || (oPos == null))
			return false;

		double d = Vec4.norm(Vec4.subtract(sPos, oPos));

		if (d > 1.4)
			return false;
		
		return true;
	}
	
	public Context execute(Context c) {
		final BiMap<Entity, Vec4> cache = c.getEntityPositionCache();
		final Vec4 src = cache.get(object);
		
		WorldMap wm = new WorldMapBuilder(c.getWorldMap())
			.moveEntity((int) src.x, (int) src.y, (int) src.z, (int) src.w,
						(int) dst.x, (int) dst.y, (int) dst.z, (int) dst.w)
			.build();

		cache.forcePut(object, dst);
		return new Context(wm, cache);
	}
}
