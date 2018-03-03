package simple_mmo;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class WorldMapTest {
	
	private static class TestEntity extends Entity implements Serializable {
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
		WorldMap wm = new WorldMap.Builder(10, 20, 30).build();
		assertEquals(wm.getRows(), 10);
		assertEquals(wm.getColumns(), 20);
		assertEquals(wm.getDepth(), 30);
    }

	@Test
	public void testPlaceEntity() {
		WorldMap wm = new WorldMap.Builder(1, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		assertEquals(wm.getEntities(0, 0, 0).get(0).toString(), "hej");
	}

	@Test
	public void testClone() {
		WorldMap wm1 = new WorldMap.Builder(1, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = new WorldMap.Builder(wm1).build();
		assertEquals(wm2.getEntities(0, 0, 0).get(0).toString(), "hej");
	}

	@Test
	public void testMoveEntity() {
		WorldMap wm = new WorldMap.Builder(2, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		wm = new WorldMap.Builder(wm)
			.moveEntity(0, 0, 0, 0,
						1, 0, 0, 0)
			.build();
		
		assertEquals(wm.getEntities(0, 0, 0).size(), 0);
		assertEquals(wm.getEntities(1, 0, 0).get(0).toString(), "hej");		
	}
	
	@Test
	public void testRemoveEntity() {
		WorldMap wm = new WorldMap.Builder(2, 1, 1)
			.placeEntity(0, 0, 0, 0, new TestEntity("hej"))
			.build();

		wm = new WorldMap.Builder(wm)
			.removeEntity(0, 0, 0, 0)
			.build();
		
		assertEquals(wm.getEntities(0, 0, 0).size(), 0);
	}

	@Test
	public void testGetSubmapBottomRow() {

		WorldMap wm1 = new WorldMap.Builder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = wm1.getSubmap(1, 0, 0, 1, 2, 1);		
		assertEquals(wm2.getRows(), 1);
		assertEquals(wm2.getColumns(), 2);
		assertEquals(wm2.getDepth(), 1);
		assertEquals(wm2.getEntities(0, 1, 0).get(0).toString(), "hej");
	}
	
	@Test
	public void testGetSubmapEntityOrder() {

		WorldMap wm1 = new WorldMap.Builder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("0"))
			.placeEntity(1, 1, 0, 1, new TestEntity("1"))
			.build();

		WorldMap wm2 = wm1.getSubmap(1, 0, 0, 1, 2, 1);		
		assertEquals(wm2.getEntities(0, 1, 0).get(0).toString(), "0");
		assertEquals(wm2.getEntities(0, 1, 0).get(1).toString(), "1");
	}

	@Test
	public void testShiftLeft() {

		WorldMap wm1 = new WorldMap.Builder(2, 2, 1)
			.placeEntity(0, 1, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = wm1.shift(0, -1, 0);
		assertEquals(wm2.getEntities(0, 0, 0).get(0).toString(), "hej");
		assertEquals(wm2.getEntities(0, 1, 0).size(), 0);
		assertEquals(wm2.getEntities(1, 0, 0).size(), 0);		
		assertEquals(wm2.getEntities(1, 1, 0).size(), 0);				
	}
	
	@Test
	public void testShiftLeftUp() {

		WorldMap wm1 = new WorldMap.Builder(2, 2, 1)
			.placeEntity(1, 1, 0, 0, new TestEntity("hej"))
			.build();

		WorldMap wm2 = wm1.shift(-1, -1, 0);
		assertEquals(wm2.getEntities(0, 0, 0).get(0).toString(), "hej");
		assertEquals(wm2.getEntities(1, 1, 0).size(), 0);
		assertEquals(wm2.getEntities(0, 1, 0).size(), 0);
		assertEquals(wm2.getEntities(1, 0, 0).size(), 0);						
	}

	@Test
	public void testSerializeEntity() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(new TestEntity("hej"));
		} catch (Exception e) {
			fail();
		}		
	}
	
	@Test
	public void testDeserializeEntity() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(new TestEntity("hej"));

			byte [] bytes = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(bais);
			TestEntity entity = (TestEntity) in.readObject();
			assertTrue(entity.toString().equals("hej"));			
		} catch (Exception e) {
			fail();
		}		
	}

	@Test
	public void testSerializeWorldMap() {
		try {
			WorldMap wm = new WorldMap.Builder(10, 20, 30).build();
			
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
			WorldMap wm1 = new WorldMap.Builder(10, 20, 30).build();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(wm1);
			
			byte [] bytes = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(bais);
			WorldMap wm2 = (WorldMap) in.readObject();
			
			assertEquals(wm1.getRows(), wm2.getRows());
			assertEquals(wm1.getColumns(), wm2.getColumns());
			assertEquals(wm1.getDepth(), wm2.getDepth());			
		} catch (Exception e) {
			fail();
		}		
	}

	
}

