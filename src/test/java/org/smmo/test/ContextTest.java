package org.smmo.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.collect.HashBiMap;
import org.smmo.common.*;
import org.smmo.common.actions.*;
import org.smmo.common.util.*;

public class ContextTest {

	public class IdEntity implements UniqueEntity {
		private long id;
		public IdEntity(long id) {
			this.id = id;
		}
		
		public long getId() {
			return id;
		}

		@Override
		public boolean equals(Object other) {
			return ((IdEntity) other).getId() == getId();
		}

		@Override
		public int hashCode() {
			return (int) getId();
		}
	}

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
}
