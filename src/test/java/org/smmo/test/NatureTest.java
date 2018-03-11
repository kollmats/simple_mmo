package org.smmo.test;

import org.smmo.common.*;
import org.smmo.server.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.junit.Test;
import static org.junit.Assert.*;


public class NatureTest {

	@Test
	public void testMoveEntity() {
		WorldMap wm = new WorldMapBuilder(30,30,30)
			.placeEntity(15, 15, 15, 0, new Entity())
			.build();
		Nature n = new Nature(wm);
		
		Spirit s = new Spirit(12) {
				public void onMapUpdate(WorldMap newMap) {
					//
				}
			};
		
		n.possessEntity(15, 15, 15, 0, s);
		n.moveEntity(s, 15, 15, 15, 0, 14, 15, 15, 0);

	}

	@Test
	public void testForsakeWhereNone() {
		WorldMap wm = new WorldMapBuilder(1,1,1).build();
		Nature n = new Nature(wm);
		try {
			n.forsakeEntity(0, 0, 0, 0);
			fail();
		} catch (Exception e) {
			assertEquals("Entity not possessed!", e.getMessage());
		}
	}

	@Test
	public void testPossessWhereNone() {
		Spirit s = new Spirit(12) {
				public void onMapUpdate(WorldMap newMap) {
					//
				}
			};

		
		WorldMap wm = new WorldMapBuilder(1,1,1).build();
		Nature n = new Nature(wm);
		
		try {
			n.possessEntity(0, 0, 0, 0, s);
			fail();
		} catch (Exception e) {
			assertEquals("No entity at position!", e.getMessage());
		}
	}
	
	@Test
	public void testPossessTwice() {
		Spirit s1 = new Spirit(12) {
				public void onMapUpdate(WorldMap newMap) {
					//
				}
			};
		Spirit s2 = new Spirit(13) {
				public void onMapUpdate(WorldMap newMap) {
					//
				}
			};
		
		WorldMap wm = new WorldMapBuilder(1,1,1)
			.placeEntity(0, 0, 0, 0, new Entity())
			.build();
		Nature n = new Nature(wm);
		
		try {
			n.possessEntity(0, 0, 0, 0, s1);
			n.possessEntity(0, 0, 0, 0, s2);			
			fail();
		} catch (Exception e) {
			assertEquals("Entity already possessed!", e.getMessage());
		}		
	}

	@Test
	public void testLocationEquality() {
		Nature.Location l1 = new Nature.Location(1, 2, 3, 4);
		Nature.Location l2 = new Nature.Location(1, 2, 3, 4);
		assertTrue(l1.hashCode() == l2.hashCode());
		assertEquals(l1, l2);
	}
	
	@Test
	public void testForsake() {
		Spirit spirit = new Spirit(12) {
				public void onMapUpdate(WorldMap newMap) {
					//
				}
			};

		BiMap bimap = HashBiMap.create();
		Nature.Location loc = new Nature.Location(1, 2, 3, 4);
		bimap.put(spirit, loc);
		
		loc = new Nature.Location(1, 2, 3, 4);
		assertTrue(bimap.inverse().get(loc) != null);
		
		Nature n = new Nature(new WorldMapBuilder(1, 1, 1).build(), bimap);
		n.forsakeEntity(1, 2, 3, 4);		
	}

		
	@Test
	public void testPossessAndForsake() {

		WorldMap wm = new WorldMapBuilder(1,1,1)
			.placeEntity(0, 0, 0, 0, new Entity())
			.build();
		Nature n = new Nature(wm);

		Spirit spirit = new Spirit(12) {
				public void onMapUpdate(WorldMap newMap) {
					//
				}
			};
		
		try {
			
			n.possessEntity(0, 0, 0, 0, spirit);
			n.forsakeEntity(0, 0, 0, 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}		
	}
	@Test
	public void testPossessAndDoubleForsake() {

		WorldMap wm = new WorldMapBuilder(1,1,1)
			.placeEntity(0, 0, 0, 0, new Entity())
			.build();
		Nature n = new Nature(wm);

		Spirit spirit = new Spirit(12) {
				public void onMapUpdate(WorldMap newMap) {
					//
				}
			};
		
		try {
			
			n.possessEntity(0, 0, 0, 0, spirit);
			n.forsakeEntity(0, 0, 0, 0);
			n.forsakeEntity(0, 0, 0, 0);
			fail();
		} catch (Exception e) {

		}		
	}



}
