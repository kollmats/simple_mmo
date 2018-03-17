package org.smmo.test;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.smmo.common.*;
import org.smmo.common.actions.*;
import org.smmo.common.util.*;

import java.util.Map;
import java.util.HashMap;

public class ContextValidatorTest {
	HashMap<UniqueEntity, Vec4i> cache;
	WorldMapBuilder wmb;
	UniqueEntity e1;
	
	@Before
	public void setup() {
		cache = new HashMap();
		wmb = new WorldMapBuilder(10, 20, 30);
		e1 = new UniqueEntity() {
				public long getId() {
					return 1L;
				}
			};
	}

	@Test
	public void testCacheValidWithoutEntity() {
		cache.put(e1, new Vec4i(0, 0, 0, 0));
		WorldMap worldMap = wmb.build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(context.isValid());
	}
	
	@Test
	public void testCacheValidWithEntity() {
		UniqueEntity entity = e1;
		
		cache.put(entity, new Vec4i(0, 0, 0, 0));
		WorldMap worldMap = wmb
			.placeEntity(0, 0, 0, 0, entity)
			.build();
		
		Context context = new Context(worldMap, cache);
		assertTrue(context.isValid());
	}
	
	@Test
	public void testCacheValidWithEntityOnWrongSpot() {
		UniqueEntity entity = e1;
		cache.put(entity , new Vec4i(0, 0, 0, 1));

		WorldMap worldMap = wmb
			.placeEntity(0, 0, 0, 0, entity)
			.build();
		try {
			Context context = new Context(worldMap, cache);
			ContextValidator.isContextValid(context);
			fail();
		} catch (Exception e) {
			//
		}

	}
	
	@Test
	public void testCacheValidWithEntityInWrongPosition() {
		UniqueEntity entity = e1;
		
		cache.put(entity, new Vec4i(1, 0, 0, 0));
		WorldMap worldMap = wmb
			.placeEntity(0, 0, 0, 0, entity)
			.build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(ContextValidator.isContextValid(context));
	}

	@Test
	public void testCacheValidWithWrongEntity() {
		
		cache.put(e1 , new Vec4i(0, 0, 0, 0));
		
		UniqueEntity e2 = new UniqueEntity() {
				public long getId() {
					return 2L;
				}
			};

		
		WorldMap worldMap = wmb
			.placeEntity(0, 0, 0, 0, e2)
			.build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(ContextValidator.isContextValid(context));
	}

	/*
	@Test
	public void testPerspectiveHasCameraBounds() {
		IdEntity entity = e1;
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(20, 20, 20, 0));		
		WorldMap worldMap = new WorldMapBuilder(41, 41, 41)
			.placeEntity(20, 20, 20, 0, entity)			
			.build();
		Context context = new Context(worldMap, cache);
		if (!ContextValidator.isContextValid(context);)
			fail();
		

		Context subContext = context.getPerspective(entity);
		assertEquals(subContext.getWorldMap().getRows(), Camera.HEIGHT);
		assertEquals(subContext.getWorldMap().getColumns(), Camera.WIDTH);
		assertEquals(subContext.getWorldMap().getLayers(), Camera.DEPTH);
	}
	
	@Test
	public void testPerspectiveHasCameraBoundsWhenCloseToLowCorner() {
		IdEntity entity = new IdEntity(1);
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(2, 2, 2, 0));		
		WorldMap worldMap = new WorldMapBuilder(41, 41, 41)
			.placeEntity(2, 2, 2, 0, entity)			
			.build();
		Context context = new Context(worldMap, cache);
		if (!ContextValidator.isContextValid(context);)
			fail();
		

		Context subContext = context.getPerspective(entity);
		assertEquals(subContext.getWorldMap().getRows(), Camera.HEIGHT);
		assertEquals(subContext.getWorldMap().getColumns(), Camera.WIDTH);
		assertEquals(subContext.getWorldMap().getLayers(), Camera.DEPTH);
	}
	
	@Test
	public void testPerspectiveHasCameraBoundsWhenCloseToHighCorner() {
		IdEntity entity = new IdEntity(1);
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(40, 40, 40, 0));		
		WorldMap worldMap = new WorldMapBuilder(41, 41, 41)
			.placeEntity(40, 40, 40, 0, entity)			
			.build();
		Context context = new Context(worldMap, cache);
		if (!ContextValidator.isContextValid(context);)
			fail();
		

		Context subContext = context.getPerspective(entity);
		assertEquals(subContext.getWorldMap().getRows(), Camera.HEIGHT);
		assertEquals(subContext.getWorldMap().getColumns(), Camera.WIDTH);
		assertEquals(subContext.getWorldMap().getLayers(), Camera.DEPTH);
	}
	
	@Test
	public void testPerspectiveHasCameraBoundsWhenMapIsSmall() {
		IdEntity entity = new IdEntity(1);
		
		HashBiMap<UniqueEntity, Vec4i> cache = HashBiMap.create();
		cache.put(entity, new Vec4i(1, 1, 1, 0));		
		WorldMap worldMap = new WorldMapBuilder(4, 4, 4)
			.placeEntity(1, 1, 1, 0, entity)			
			.build();
		
		Context context = new Context(worldMap, cache);
		if (!ContextValidator.isContextValid(context);)
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
		if (!ContextValidator.isContextValid(context);)
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
		
		if (!ContextValidator.isContextValid(context);)
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
		
		if (!ContextValidator.isContextValid(context);)
			fail();
		
		int row = (int)((Camera.HEIGHT - 1) / 2);
		int col = (int)((Camera.WIDTH - 1) / 2);
		int lay = (int)((Camera.DEPTH - 1) / 2);
		
		Context subContext = context.getPerspective(entity);
		assertEquals(subContext.getPositionCache().size(), 1);
	}
	*/
	
}
