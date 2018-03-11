package org.smmo.common;

import java.util.Map;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.HashBiMap;
import java.util.Optional;

public class Context {

	private final WorldMap worldMap;
	private final BiMap<Entity, Vec4> entityPositionCache;
	
	public Context(WorldMap worldMap, BiMap<Entity, Vec4> entityPositionCache) {
		this.worldMap = worldMap;
		this.entityPositionCache = ImmutableBiMap.copyOf(entityPositionCache);
	}

	public WorldMap getWorldMap() {
		return worldMap;
	}

	public BiMap<Entity, Vec4> getEntityPositionCache() {
		return HashBiMap.create(entityPositionCache);
	}

	public boolean isValid() {

		// Check that all positions in the cache exist in the 
		for (Map.Entry<Entity, Vec4> entry : entityPositionCache.entrySet()) {
			Entity e = entry.getKey();
			Vec4 v = entry.getValue();

			int i   = (int) v.x;
			int j   = (int) v.y;
			int k   = (int) v.z;
			int idx = (int) v.w;
			Optional<Entity> opt = worldMap.getEntity(i, j, k, idx);

			if (!opt.isPresent())
				return false;

			if (!opt.get().equals(e))
				return false;			
		}

		return true;
	}
}
