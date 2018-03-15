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
	
	public Context(WorldMap worldMap, BiMap<UniqueEntity, Vec4i> positionCache) {
		this.worldMap = worldMap;
		this.positionCache = ImmutableBiMap.copyOf(positionCache);
	}
	
	private Context(WorldMap worldMap, BiMap<UniqueEntity, Vec4i> positionCache, Vec3i origin, Context parent) {
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

		// Check that all positions in the cache exist in the 
		for (Map.Entry<UniqueEntity, Vec4i> entry : positionCache.entrySet()) {
			UniqueEntity e = entry.getKey();
			Vec4i v = entry.getValue();

			int i   = v.getX();
			int j   = v.getY();
			int k   = v.getZ();
			int idx = v.getW();
			Optional<Entity> opt = worldMap.getEntity(i, j, k, idx);

			if (!opt.isPresent())
				return false;

			if (!opt.get().equals(e))
				return false;			
		}
		
		return true;
	}

	public Context getPerspective(UniqueEntity observer) {

		Vec4i v = positionCache.get(observer);
		final int di = (int)((Camera.HEIGHT - 1) / 2.0);
		final int dj = (int)((Camera.WIDTH  - 1) / 2.0);
		final int dk = (int)((Camera.DEPTH  - 1) / 2.0);

		final int iStart = (int) Math.max(v.getX() - di, 0);
		final int jStart = (int) Math.max(v.getY() - dj, 0);
		final int kStart = (int) Math.max(v.getZ() - dk, 0);
		
		final int nRows  = Math.min(Camera.HEIGHT, worldMap.getRows());
		final int nCols  = Math.min(Camera.WIDTH, worldMap.getColumns());
		final int nLays  = Math.min(Camera.DEPTH, worldMap.getLayers());

		Interval3i i3 = new Interval3i(iStart, iStart + nRows,
									   jStart, jStart + nCols,
									   kStart, kStart + nLays);

		final Vec3i newOrigin = new Vec3i(iStart, jStart, kStart);
		final BiMap<UniqueEntity, Vec4i> newCache = HashBiMap.create(positionCache);
		for (Map.Entry<UniqueEntity, Vec4i> entry : positionCache.entrySet()) {		
			v = entry.getValue();

			if (i3.contains(v.getX(), v.getY(), v.getZ())) {
				UniqueEntity e = entry.getKey();
				v = Vec.subtract(v, new Vec4i(newOrigin, 0));
				newCache.put(e, v);
			}				
		}

		
		final WorldMap newMap = worldMap.getSubmap(iStart, jStart, kStart, nRows, nCols, nLays);

		return new Context(newMap, newCache, newOrigin, this);			
	}	
}
