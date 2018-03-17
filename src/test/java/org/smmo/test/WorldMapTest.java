package org.smmo.test;

import org.smmo.common.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class WorldMapTest {
	
	public static class TestEntity implements Entity {
		private final String text;

		public TestEntity(String text) {
			this.text = text;
		}

		public String toString() {
			return text;
		}
	}
	
    @Test
	public void testDimensions() {
		WorldMap wm = new WorldMapBuilder(10, 20, 30).build();
		assertEquals(wm.getRows(), 10);
		assertEquals(wm.getColumns(), 20);
		assertEquals(wm.getLayers(), 30);
    }

	@Test
	public void testPlaceEntity() {
		WorldMap wm = new WorldMapBuilder(1, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		assertEquals(wm.getEntities(0, 0, 0).get(0).toString(), "hej");
	}

	@Test
	public void testClone() {
		WorldMap wm1 = new WorldMapBuilder(1, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = new WorldMapBuilder(wm1).build();
		assertEquals(wm2.getEntities(0, 0, 0).get(0).toString(), "hej");
	}

	@Test
	public void testMoveEntity() {
		WorldMap wm = new WorldMapBuilder(2, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		wm = new WorldMapBuilder(wm)
			.moveEntity(0, 0, 0, 0,
						1, 0, 0, 0)
			.build();
		
		assertEquals(wm.getEntities(0, 0, 0).size(), 0);
		assertEquals(wm.getEntities(1, 0, 0).get(0).toString(), "hej");		
	}
	
	@Test
	public void testRemoveEntity() {
		WorldMap wm = new WorldMapBuilder(2, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		wm = new WorldMapBuilder(wm)
			.removeEntity(0, 0, 0, 0)
			.build();
		
		assertEquals(wm.getEntities(0, 0, 0).size(), 0);
	}

	@Test
	public void testGetSubmapBottomRow() {

		WorldMap wm1 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = wm1.getSubmap(1, 0, 0, 1, 2, 1);		
		assertEquals(wm2.getRows(), 1);
		assertEquals(wm2.getColumns(), 2);
		assertEquals(wm2.getLayers(), 1);
		assertEquals(wm2.getEntities(0, 1, 0).get(0).toString(), "hej");
	}
	
	@Test
	public void testGetSubmapEntityOrder() {

		WorldMap wm1 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("0"))
			.placeEntity(1, 1, 0, 1, new TestEntity("1"))
			.build();

		WorldMap wm2 = wm1.getSubmap(1, 0, 0, 1, 2, 1);		
		assertEquals(wm2.getEntities(0, 1, 0).get(0).toString(), "0");
		assertEquals(wm2.getEntities(0, 1, 0).get(1).toString(), "1");
	}

	@Test
	public void testShiftLeft() {

		WorldMap wm1 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(0, 1, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = new WorldMapBuilder(wm1)
			.shift(0, -1, 0)
			.build();
		
		assertEquals(wm2.getEntities(0, 0, 0).get(0).toString(), "hej");
		assertEquals(wm2.getEntities(0, 1, 0).size(), 0);
		assertEquals(wm2.getEntities(1, 0, 0).size(), 0);		
		assertEquals(wm2.getEntities(1, 1, 0).size(), 0);				
	}
	
	@Test
	public void testShiftLeftUp() {

		WorldMap wm1 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = new WorldMapBuilder(wm1)
			.shift(-1, -1, 0)
			.build();

		assertEquals(wm2.getEntities(0, 0, 0).get(0).toString(), "hej");
		assertEquals(wm2.getEntities(1, 1, 0).size(), 0);
		assertEquals(wm2.getEntities(0, 1, 0).size(), 0);
		assertEquals(wm2.getEntities(1, 0, 0).size(), 0);						
	}

	@Test
	public void testAppendRow() {
		WorldMap wm1 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("hej"))
			.build();
		
		WorldMap wm2 = new WorldMapBuilder(1, 2, 1)
			.placeEntity(0, 1, 0, 0, new TestEntity("hejdå"))
			.build();

		try {
			WorldMap wm3 = new WorldMapBuilder(wm1)
				.append(wm2, 0)
				.build();

			assertEquals(wm3.getRows(), 3);
			assertEquals(wm3.getColumns(), 2);
			assertEquals(wm3.getLayers(), 1);
		
			assertEquals(wm3.getEntities(1, 1, 0).size(), 1);
			assertEquals(wm3.getEntities(2, 0, 0).size(), 0);								
			assertEquals(wm3.getEntities(2, 1, 0).size(), 1);
		} catch (Exception e) {
			fail();
		}
	}


	@Test
	public void testAppendRows() {
		WorldMap wm1 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("hej"))
			.build();
		
		WorldMap wm2 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(0, 1, 0, 0, new TestEntity("hejdå"))
			.build();

		try {
			WorldMap wm3 = new WorldMapBuilder(wm1)
				.append(wm2, 0)
				.build();

			assertEquals(wm3.getRows(), 4);
			assertEquals(wm3.getColumns(), 2);
			assertEquals(wm3.getLayers(), 1);
		
			assertEquals(wm3.getEntities(1, 1, 0).size(), 1);
			assertEquals(wm3.getEntities(2, 0, 0).size(), 0);								
			assertEquals(wm3.getEntities(2, 1, 0).size(), 1);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testPrependRows() {
		WorldMap wm1 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("hej"))
			.build();
		
		WorldMap wm2 = new WorldMapBuilder(2, 2, 1)
			.placeEntity(0, 1, 0, 0, new TestEntity("hejdå"))
			.build();

		try {
			WorldMap wm3 = new WorldMapBuilder(wm2)
				.prepend(wm1, 0)
				.build();

			assertEquals(wm3.getRows(), 4);
			assertEquals(wm3.getColumns(), 2);
			assertEquals(wm3.getLayers(), 1);
		
			assertEquals(wm3.getEntities(1, 1, 0).size(), 1);
			assertEquals(wm3.getEntities(2, 0, 0).size(), 0);								
			assertEquals(wm3.getEntities(2, 1, 0).size(), 1);
		} catch (Exception e) {
			fail();
		}
	}
	

	
	@Test
	public void testAppendColumn() {
		WorldMap wm1 = new WorldMapBuilder(1, 1, 1)
			.build();
		
		WorldMap wm2 = new WorldMapBuilder(1, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hejdå"))
			.build();

		try {
			WorldMap wm3 = new WorldMapBuilder(wm1)
				.append(wm2, 1)
				.build();

			assertEquals(wm3.getRows(), 1);
			assertEquals(wm3.getColumns(), 2);
			assertEquals(wm3.getLayers(), 1);
		
			assertEquals(wm3.getEntities(0, 0, 0).size(), 0);
			assertEquals(wm3.getEntities(0, 1, 0).size(), 1);
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testAppendLayer() {
		WorldMap wm1 = new WorldMapBuilder(1, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hejdå"))
			.build();
		
		WorldMap wm2 = new WorldMapBuilder(1, 1, 1)
			.build();

		try {
			WorldMap wm3 = new WorldMapBuilder(wm1)
				.append(wm2, 2)
				.build();

			assertEquals(wm3.getRows(), 1);
			assertEquals(wm3.getColumns(), 1);
			assertEquals(wm3.getLayers(), 2);
		
			assertEquals(wm3.getEntities(0, 0, 0).size(), 1);
			assertEquals(wm3.getEntities(0, 0, 1).size(), 0);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testSerializeWorldMap() {
		try {
			WorldMap wm = new WorldMapBuilder(10, 20, 30).build();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(wm);
			
		} catch (Exception e) {
			fail();
		}		
	}
	
	@Test
	public void testDeserializeWorldMap() {
		try {
			WorldMap wm1 = new WorldMapBuilder(10, 20, 30).build();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(wm1);
			
			byte [] bytes = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(bais);
			WorldMap wm2 = (WorldMap) in.readObject();
			
			assertEquals(wm1.getRows(), wm2.getRows());
			assertEquals(wm1.getColumns(), wm2.getColumns());
			assertEquals(wm1.getLayers(), wm2.getLayers());			
		} catch (Exception e) {
			fail();
		}		
	}

	
}

