package org.smmo.common;

import java.util.Map;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.HashBiMap;
import java.util.Optional;
import java.lang.Math;

import org.smmo.common.util.*;

public class Context {
	
	private Vec3i origin = null;
	private Context parent = null;
	private final WorldMap worldMap;
	private final BiMap<UniqueEntity, Vec4i> positionCache;
	
	public Context(WorldMap worldMap, Map<UniqueEntity, Vec4i> positionCache) {
		this.worldMap = worldMap;
		this.positionCache = ImmutableBiMap.copyOf(positionCache);

		//if (!isValid()) {
			//throw new IllegalArgumentException("Invalid context!");
		//}
	}
	
	public Context(WorldMap worldMap, Map<UniqueEntity, Vec4i> positionCache, Vec3i origin, Context parent) {
		this(worldMap, positionCache);
		this.origin = origin;
		this.parent = parent;
	}

	public Vec3i getOrigin() {
		return origin;
	}

	public Context getParentContext() {
		return parent;
	}

	public WorldMap getWorldMap() {
		return worldMap;
	}

	public BiMap<UniqueEntity, Vec4i> getPositionCache() {
		return HashBiMap.create(positionCache);
	}

	public Entity getEntityFromPosition(Vec4i vec) {
		return worldMap.getEntity(vec.getX(), vec.getY(), vec.getZ(), vec.getW()).get();
	}


	public Vec4i getEntityPosition(Entity entity) {
		Vec4i pos = null;
		if (entity instanceof UniqueEntity) {
			pos = positionCache.get(entity);
			return pos;
		} else {
			// Search for it in the map (and add to cache??)			
			throw new NotImplementedException();
		}		
	}
	
	public Vec4i getEntityPosition(long entityId) {
		for (Map.Entry<UniqueEntity, Vec4i> entry : positionCache.entrySet()) {
			UniqueEntity e = entry.getKey();

			if (e.getId() == entityId) {
				Vec4i v = entry.getValue();
				return v;
			}
		}
		return null;
	}
	
	public boolean isValid() {
		return ContextValidator.isContextValid(this);
	}

	public Context getPerspective(UniqueEntity observer) {
		return ContextReducer.reduceToPerspectiveOf(observer, this);
	}	
}
