package org.smmo.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.HashBiMap;
import org.smmo.common.*;
import org.smmo.common.actions.*;
import org.smmo.common.util.*;

public class ContextTest {

	@Test
	public void testCacheValidWithoutEntity() {
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		
		cache.put(new IdEntity(1), new Vec4i(0, 0, 0, 0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30).build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(context.isValid());
	}
	
	@Test
	public void testCacheValidWithEntity() {
		UniqueEntity entity = new IdEntity(1);
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		
		cache.put(entity, new Vec4i(0, 0, 0, 0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(0, 0, 0, 0, entity)
			.build();
		
		Context context = new Context(worldMap, cache);
		assertTrue(context.isValid());
	}
	
	@Test
	public void testCacheValidWithEntityInWrongPosition() {
		UniqueEntity entity = new IdEntity(1);
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		
		cache.put(entity, new Vec4i(1, 0, 0, 0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(0, 0, 0, 0, entity)
			.build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(context.isValid());
	}

	@Test
	public void testCacheValidWithWrongEntity() {
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		
		cache.put(new IdEntity(1) , new Vec4i(0, 0, 0, 0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(0, 0, 0, 0, new IdEntity(2))
			.build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(context.isValid());
	}

	@Test
	public void testOriginIsNull() {
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		WorldMap worldMap = new WorldMapBuilder(9, 9, 9).build();
		Context context = new Context(worldMap, cache);

		assertNull(context.getOrigin());
	}
	
	@Test
	public void testPerspectiveHasCameraBounds() {
		IdEntity entity = new IdEntity(1);
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(20, 20, 20, 0));		
		WorldMap worldMap = new WorldMapBuilder(41, 41, 41)
			.placeEntity(20, 20, 20, 0, entity)			
			.build();
		Context context = new Context(worldMap, cache);
		if (!context.isValid())
			fail();
		

		Context subContext = context.getPerspective(entity);
		assertEquals(subContext.getWorldMap().getRows(), Camera.HEIGHT);
		assertEquals(subContext.getWorldMap().getColumns(), Camera.WIDTH);
		assertEquals(subContext.getWorldMap().getLayers(), Camera.DEPTH);
	}
	
	@Test
	public void testPerspectiveHasEntityInCenter() {
		IdEntity entity = new IdEntity(2);
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(20, 20, 20, 0));		
		WorldMap worldMap = new WorldMapBuilder(41, 41, 41)
			.placeEntity(20, 20, 20, 0, entity)			
			.build();
		Context context = new Context(worldMap, cache);
		if (!context.isValid())
			fail();
		
		int row = (int)((Camera.HEIGHT - 1) / 2);
		int col = (int)((Camera.WIDTH - 1) / 2);
		int lay = (int)((Camera.DEPTH - 1) / 2);
		
		Context subContext = context.getPerspective(entity);
		assertEquals(subContext.getWorldMap().getEntity(row, col, lay, 0).get(), entity);
		assertEquals(subContext.getEntityPosition(entity).getX(), row);
		assertEquals(subContext.getEntityPosition(entity).getY(), col);
		assertEquals(subContext.getEntityPosition(entity).getZ(), lay);
	}
	
	@Test
	public void testPerspectiveHasCorrectOrigin() {
		IdEntity entity = new IdEntity(2);
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(20, 20, 20, 0));		
		WorldMap worldMap = new WorldMapBuilder(41, 41, 41)
			.placeEntity(20, 20, 20, 0, entity)			
			.build();
		Context context = new Context(worldMap, cache);
		
		if (!context.isValid())
			fail();
		
		int row = (int)((Camera.HEIGHT - 1) / 2);
		int col = (int)((Camera.WIDTH - 1) / 2);
		int lay = (int)((Camera.DEPTH - 1) / 2);
		
		Context subContext = context.getPerspective(entity);
		
		assertEquals(subContext.getOrigin().getX(), 20 - row);
		assertEquals(subContext.getOrigin().getY(), 20 - col);
		assertEquals(subContext.getOrigin().getZ(), 20 - lay);
	}
	
	@Test
	public void testPerspectiveHasFewerEntities() {
		IdEntity entity = new IdEntity(2);
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(20, 20, 20, 0));
		cache.put(entity, new Vec4i(40, 20, 20, 0));				
		WorldMap worldMap = new WorldMapBuilder(41, 41, 41)
			.placeEntity(20, 20, 20, 0, entity)
			.placeEntity(40, 20, 20, 0, entity)			
			.build();
		Context context = new Context(worldMap, cache);
		
		if (!context.isValid())
			fail();
		
		int row = (int)((Camera.HEIGHT - 1) / 2);
		int col = (int)((Camera.WIDTH - 1) / 2);
		int lay = (int)((Camera.DEPTH - 1) / 2);
		
		Context subContext = context.getPerspective(entity);
		assertEquals(subContext.getPositionCache().size(), 1);
	}
	
	
}
