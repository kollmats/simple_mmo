package org.smmo.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.HashBiMap;
import org.smmo.common.*;

public class ContextTest {

	@Test
	public void testCacheValidWithoutEntity() {
		HashBiMap<Entity, Vec4> cache = HashBiMap.create();
		
		cache.put(new Entity(), new Vec4(0.0, 0.0, 0.0, 0.0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30).build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(context.isValid());
	}
	
	@Test
	public void testCacheValidWithEntity() {
		Entity entity = new Entity();
		HashBiMap<Entity, Vec4> cache = HashBiMap.create();
		
		cache.put(entity, new Vec4(0.0, 0.0, 0.0, 0.0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(0, 0, 0, 0, entity)
			.build();
		
		Context context = new Context(worldMap, cache);
		assertTrue(context.isValid());
	}
	
	@Test
	public void testCacheValidWithEntityInWrongPosition() {
		Entity entity = new Entity();
		HashBiMap<Entity, Vec4> cache = HashBiMap.create();
		
		cache.put(entity, new Vec4(1.0, 0.0, 0.0, 0.0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(0, 0, 0, 0, entity)
			.build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(context.isValid());
	}

	@Test
	public void testCacheValidWithWrongEntity() {
		HashBiMap<Entity, Vec4> cache = HashBiMap.create();
		
		cache.put(new Entity() , new Vec4(0.0, 0.0, 0.0, 0.0));
		WorldMap worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(0, 0, 0, 0, new Entity())
			.build();
		
		Context context = new Context(worldMap, cache);
		assertFalse(context.isValid());
	}
}
