package org.smmo.common;

import org.smmo.common.util.Vec4i;

import java.util.Map;
import java.util.Optional;

public class ContextValidator {

	private static boolean isEntityOnPosition(UniqueEntity e, Vec4i p, WorldMap wm) {
		Optional<Entity> opt = wm.getEntity(p.getX(), p.getY(), p.getZ(), p.getW());

		if (!opt.isPresent()) {
			// Nothing here
			return false;
		}

		Entity presentEntity = opt.get();		
		if (!e.equals(presentEntity)) {
			// Wrong entity
			return false;
		}
		
		return true;		
	}

	private static boolean isPositionCacheValid(Map<UniqueEntity, Vec4i> cache, WorldMap wm) {
		
		for (Map.Entry<UniqueEntity, Vec4i> entry : cache.entrySet()) {
			UniqueEntity e = entry.getKey();
			Vec4i v = entry.getValue();
			if (!isEntityOnPosition(e, v, wm))
				return false;
		}
		return true;
	}
	

	public static boolean isContextValid(Context context) {
		final Map<UniqueEntity, Vec4i> positionCache = context.getPositionCache();
		final WorldMap worldMap = context.getWorldMap();		
		
		if (!isPositionCacheValid(positionCache, worldMap))
			return false;
			
		return true;
	}
}
