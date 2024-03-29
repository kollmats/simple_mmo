package org.smmo.common.actions;

import org.smmo.common.*;
import org.smmo.common.util.*;
import com.google.common.collect.BiMap;

import java.util.List;
import com.google.common.collect.ImmutableList;


public class MoveEntityAction extends Action {

	public final long actorId;
	public final Vec4i objectPos;
	public final Vec4i targetPos;
	
	public MoveEntityAction(long actorId, Vec4i objectPos, Vec4i targetPos) {
		this.actorId = actorId;
		this.objectPos = objectPos;
		this.targetPos = targetPos;
	}
	
	public boolean isValid(Context c) {
		final Vec4i actorPos = c.getEntityPosition(actorId);
		double d = Vec.norm(Vec.subtract(actorPos, objectPos));

		if (d > 1.4)
			return false;
		
		return true;
	}
	
	public Context execute(Context c) {
		
		WorldMap wm = new WorldMapBuilder(c.getWorldMap())
			.moveEntity(objectPos.getX(),  objectPos.getY(),  objectPos.getZ(),  objectPos.getW(),
						targetPos.getX(),  targetPos.getY(),  targetPos.getZ(),  targetPos.getW())
			.build();

		BiMap<UniqueEntity, Vec4i> cache = c.getPositionCache();

		Entity object = wm.getEntity(targetPos.getX(), targetPos.getY(), targetPos.getZ(), targetPos.getW()).get();
		if (object instanceof UniqueEntity)
			cache.forcePut((UniqueEntity) object, targetPos);
		
		return new Context(wm, cache);
	}

	public List<Vec3i> getAffectedLocations() {
		List<Vec3i> list = ImmutableList.of(objectPos.getXYZ(), targetPos.getXYZ());
		return list;
	}
	
}
