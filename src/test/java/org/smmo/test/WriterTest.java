package org.smmo.test;



import org.smmo.server.*;
import org.smmo.common.*;
import org.smmo.common.actions.*;
import org.smmo.common.writers.*;
import org.smmo.common.util.*;

import java.io.ByteArrayOutputStream;
import com.google.common.collect.HashBiMap;
import org.apache.commons.codec.binary.Hex;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class WriterTest {

	private MoveEntityAction action;
	private WorldMap worldMap;
	private Context context;
	private HashBiMap<UniqueEntity, Vec4i> cache;
	private UniqueEntity entity;
	
	@Before
	public void setUp() {
		entity = new IdEntity(1);	
		worldMap = new WorldMapBuilder(10, 20, 30)
			.placeEntity(2, 3, 4, 0, entity)			
			.build();

		cache = HashBiMap.create();
		cache.put(entity, new Vec4i(2, 3, 4, 5));
		context = new Context(worldMap, cache);
		action = new MoveEntityAction(0, new Vec4i(2, 3, 4, 5), new Vec4i(1, 3, 4, 0));
	}
	
	@Test
	public void testWrite() {
		MoveEntityWriter mew = new MoveEntityWriter(action, context);
		try {		
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mew.writeTo(baos);
			byte [] bytes = baos.toByteArray();

			assertEquals(bytes[0], 2);
		} catch (Exception e) {
			fail();
		}		
	}	
}
