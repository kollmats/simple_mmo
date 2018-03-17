package org.smmo.common;

import org.smmo.common.util.Vec4i;
import org.smmo.common.util.Vec3i;
import org.smmo.common.util.Vec;
import org.smmo.common.util.Interval3i;
import java.util.Map;
import java.util.HashMap;

public class ContextReducer {

	public static Context reduceToPerspectiveOf(UniqueEntity observer, Context context) {
		Vec4i obsPos4 = context.getEntityPosition(observer);
		Vec3i obsPos3 = new Vec3i(obsPos4.getX(), obsPos4.getY(), obsPos4.getZ());
		
		Vec3i obsLocalPos  = Vec.divide(Vec.subtract(Camera.getBounds(), 1), 2);
		Vec3i origin = Vec.subtract(obsPos3, obsLocalPos);
		Vec3i end = Vec.add(origin, Camera.getBounds());

		Interval3i interval = new Interval3i(origin.getX(), end.getX(),
											 origin.getY(), end.getY(),
											 origin.getZ(), end.getZ());

		Context newContext = reduceToInterval(interval, context);
		return newContext;
	}

	public static Context reduceToInterval(Interval3i interval, Context context) {
		Map<UniqueEntity, Vec4i> newCache = reduceCacheToInterval(interval, context.getPositionCache());
		WorldMap newWorldMap = reduceWorldMapToInterval(interval, context.getWorldMap());
		Vec3i newOrigin = new Vec3i(interval.xMin, interval.yMin, interval.zMin);
		Context newContext = new Context(newWorldMap, newCache, newOrigin, context);
		return newContext;
	}

	private static Map<UniqueEntity, Vec4i> reduceCacheToInterval(Interval3i interval, Map<UniqueEntity, Vec4i> cache) {
		Map<UniqueEntity, Vec4i> result = new HashMap<UniqueEntity, Vec4i>();
		
		for (Map.Entry<UniqueEntity, Vec4i> entry : cache.entrySet()) {
			Vec4i v = entry.getValue();

			if (interval.contains(v.getX(), v.getY(), v.getZ())) {
				UniqueEntity e = entry.getKey();

				v = Vec.subtract(v, new Vec4i(interval.xMin, interval.yMin, interval.zMin, 0));				
				result.put(e, v);
			}
		}
		return result;
	}

	private static WorldMap reduceWorldMapToInterval(Interval3i interval, WorldMap worldMap) {

		int iStart = interval.xMin;
		int jStart = interval.yMin;
		int kStart = interval.zMin;
		
		int nRows = interval.xMax - iStart;
		int nCols = interval.yMax - jStart;
		int nLays = interval.zMax - kStart;
		
		WorldMap result = worldMap.getSubmap(iStart, jStart, kStart, nRows, nCols, nLays);
		return result;		

	}	
}

	/*
	public static Context toPerspectiveOf(UniqueEntity observer, Context context) {
		final Map<UniqueEntity, Vec4i> posCache = context.getPositionCache();
		final WorldMap wm = context.getWorldMap();
		Vec4i obsPos = posCache.get(observer);
		
		final Vec3i submapOrigin = getSubmapOrigin(obsPos, wm);
		final Vec3i submapDims = getSubmapDimensions(submapOrigin, wm);

		Map<UniqueEntity, Vec4i> newCache = getSubCache(posCache, submapOrigin, submapDims);
		WorldMap newMap = getSubmap(wm, submapOrigin, submapDims);

		// TODO: bryt ut augmenteringen så den kan testas helt!!!
		if (isMapTooSmall(newMap)) {
			obsPos = newCache.get(observer);
			Vec4i offset = getOffset(obsPos);
			
			newCache = augmentCache(newCache, offset);
			newMap = augmentMap(newMap, offset);
		}

		return new Context(newMap, newCache, submapOrigin, context);			
	}

	private static WorldMap augmentMap(WorldMap wm, final Vec4i offset) {
		wm = augmentRows(wm, offset);
		wm = augmentCols(wm, offset);
		wm = augmentLays(wm, offset);
		return wm;
	}

	private static WorldMap augmentRows(final WorldMap wm, final Vec4i offset) {
		int missingTotal = Camera.HEIGHT - wm.getRows();
		int missingBefore = offset.getX();
		int missingAfter  = missingTotal - missingBefore;
		int axis = 0;
		return augmentAxis(wm, offset, axis, missingBefore, missingAfter);
	}
	
	private static WorldMap augmentCols(final WorldMap wm, final Vec4i offset) {
		int missingTotal = Camera.WIDTH - wm.getColumns();
		int missingBefore = offset.getY();
		int missingAfter  = missingTotal - missingBefore;
		int axis = 1;
		return augmentAxis(wm, offset, axis, missingBefore, missingAfter);
	}
	
	private static WorldMap augmentLays(final WorldMap wm, final Vec4i offset) {
		int missingTotal = Camera.DEPTH - wm.getLayers();
		int missingBefore = offset.getZ();
		int missingAfter  = missingTotal - missingBefore;
		int axis = 2;
		return augmentAxis(wm, offset, axis, missingBefore, missingAfter);
	}

	private static WorldMap augmentAxis(final WorldMap wm, final Vec4i offset, final int axis, int missingBefore, int missingAfter) {
		WorldMapBuilder wmb = new WorldMapBuilder(wm);
		
		if (missingBefore > 0) {
			int r = axis == 0 ? missingBefore : wm.getRows();
			int c = axis == 1 ? missingBefore : wm.getColumns();
			int l = axis == 2 ? missingBefore : wm.getLayers();
			
			WorldMap empty = new WorldMapBuilder(r, c, l).build();
			wmb = wmb.prepend(empty, axis);
		}

		if (missingAfter > 0) {
			int r = axis == 0 ? missingAfter : wm.getRows();
			int c = axis == 1 ? missingAfter : wm.getColumns();
			int l = axis == 2 ? missingAfter : wm.getLayers();
			
			WorldMap empty = new WorldMapBuilder(r, c, l).build(); 
			wmb = wmb.append(empty, axis);			
		}
		return wmb.build();
	}

	private static Map<UniqueEntity, Vec4i> augmentCache(final Map<UniqueEntity, Vec4i> cache, final Vec4i offset) {
		
		final Map<UniqueEntity, Vec4i> newCache = new HashMap();
		for (Map.Entry<UniqueEntity, Vec4i> entry : cache.entrySet()) {
			UniqueEntity e = entry.getKey();			
			Vec4i v = entry.getValue();
			v = Vec.add(v, offset);
			newCache.put(e, v);
		}
		return newCache;
	}
	
	private static Vec4i getOffset(final Vec4i obsPos) {
		final int di = (int)((Camera.HEIGHT - 1) / 2.0) - obsPos.getX();
		final int dj = (int)((Camera.WIDTH  - 1) / 2.0) - obsPos.getY();
		final int dk = (int)((Camera.DEPTH  - 1) / 2.0) - obsPos.getZ();
		
		Vec4i offset = new Vec4i(di, dj, dk, 0);
		return offset;
	}
	
	private static boolean isMapTooSmall(WorldMap wm) {
		return ((wm.getRows() < Camera.HEIGHT) || (wm.getColumns() < Camera.WIDTH) || (wm.getLayers() < Camera.DEPTH));
	}

	private static Vec3i getSubmapDimensions(Vec3i submapOrigin, WorldMap worldMap) {
		final int nRows  = Math.min(Camera.HEIGHT, worldMap.getRows() - submapOrigin.getX());
		final int nCols  = Math.min(Camera.WIDTH, worldMap.getColumns() - submapOrigin.getY());
		final int nLays  = Math.min(Camera.DEPTH, worldMap.getLayers() - submapOrigin.getZ());
		return new Vec3i(nRows, nCols, nLays);
	}

	private static Vec3i getSubmapOrigin(Vec4i observerPos, WorldMap wm) {
		final int di = (int)((Camera.HEIGHT - 1) / 2.0);
		final int dj = (int)((Camera.WIDTH  - 1) / 2.0);
		final int dk = (int)((Camera.DEPTH  - 1) / 2.0);

		final int iStart = (int) Math.max(observerPos.getX() - di, 0);
		final int jStart = (int) Math.max(observerPos.getY() - dj, 0);
		final int kStart = (int) Math.max(observerPos.getZ() - dk, 0);

		return new Vec3i(iStart, jStart, kStart);
	}

	private static Map<UniqueEntity, Vec4i> getSubCache(final Map<UniqueEntity, Vec4i> posCache, final Vec3i origin, Vec3i dims) {
		Interval3i i3 = new Interval3i(origin.getX(), origin.getX() + dims.getX(),
									   origin.getY(), origin.getY() + dims.getY(),
									   origin.getZ(), origin.getZ() + dims.getZ());

		final Map<UniqueEntity, Vec4i> newCache = new HashMap();
		for (Map.Entry<UniqueEntity, Vec4i> entry : posCache.entrySet()) {		
			Vec4i v = entry.getValue();

			if (i3.contains(v.getX(), v.getY(), v.getZ())) {
				UniqueEntity e = entry.getKey();
				v = Vec.subtract(v, new Vec4i(origin, 0));
				newCache.put(e, v);
			}
		}

		return newCache;
	}

	private static WorldMap getSubmap(final WorldMap wm, final Vec3i origin, Vec3i dims) {		
		final WorldMap newMap = wm.getSubmap(origin.getX(), origin.getY(), origin.getZ(),
											 dims.getX(), dims.getY(), dims.getZ());
		return newMap;		
	}
*/

