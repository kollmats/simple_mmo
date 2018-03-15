package org.smmo.common.actions;

import org.smmo.common.*;
import org.smmo.common.util.*;
import com.google.common.collect.BiMap;

public class MoveEntityAction extends Action {

	public final UniqueEntity subject;
	public final UniqueEntity object;
	public final Vec4i dst;
	
	public MoveEntityAction(UniqueEntity subject, UniqueEntity object, Vec4i dst) {
		this.subject = subject;
		this.object = object;
		this.dst = dst;
	}
	
	public boolean isValid(Context c) {
		final Vec4i sPos = c.getEntityPosition(subject);
		final Vec4i oPos = c.getEntityPosition(object);
		
		if ((sPos == null) || (oPos == null))
			return false;

		double d = Vec.norm(Vec.subtract(sPos, oPos));

		if (d > 1.4)
			return false;
		
		return true;
	}
	
	public Context execute(Context c) {
		final Vec4i src = c.getEntityPosition(object);
		
		WorldMap wm = new WorldMapBuilder(c.getWorldMap())
			.moveEntity(src.getX(),  src.getY(),  src.getZ(),  src.getW(),
						dst.getX(),  dst.getY(),  dst.getZ(),  dst.getW())
			.build();

		BiMap<UniqueEntity, Vec4i> cache = c.getPositionCache();
		cache.forcePut(object, dst);
		return new Context(wm, cache);
	}

	public Action switchContext(Context src, Context dst) {
		throw new NotImplementedException();
	}
}
