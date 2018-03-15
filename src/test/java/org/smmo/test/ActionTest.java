package org.smmo.test;

import org.smmo.server.*;
import org.smmo.common.*;
import org.smmo.common.actions.*;
import org.smmo.common.util.*;

import com.google.common.collect.HashBiMap;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ActionTest {

	private WorldMap worldMap;
	private Context context;
	private HashBiMap<UniqueEntity, Vec4i> cache;
	private UniqueEntity entity;
	@Before
	public void setUp() {
		entity = new IdEntity(1);	
		worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(0, 0, 0, 0, entity)			
			.build();

		cache = HashBiMap.create();
		cache.put(entity, new Vec4i(0, 0, 0, 0));
		context = new Context(worldMap, cache);
	}

	@Test
	public void testMoveEntityReturnsValidContext() {
		Action a = new MoveEntityAction(1, new Vec4i(0, 0, 0, 0), new Vec4i(1, 0, 0, 0));

		if (a.isValid(context)) {
			Context newContext = a.execute(context);
			assertTrue(newContext.isValid());			
		} else {
			fail();
		}		
	}
	
	@Test
	public void testMoveEntityPositionChanged() {
		Vec4i dst = new Vec4i(1, 0, 0, 0);
		Action a = new MoveEntityAction(1, new Vec4i(0, 0, 0, 0), dst);		

		if (a.isValid(context)) {
			Context newContext = a.execute(context);
			assertTrue(newContext.getPositionCache().get(entity).equals(dst));			
		} else {
			fail();
		}		
	}
	

}
