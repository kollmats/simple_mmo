package org.smmo.test;

import org.junit.Test;
import static org.junit.Assert.*;

import org.smmo.common.Entity;

public class EntityTest {

	@Test
	public void testEqualsTrue() {
		Entity e = new Entity();
		assertEquals(e, e);
	}

	@Test
	public void testEqualsFalse() {
		Entity e1 = new Entity();
		Entity e2 = new Entity();		
		assertNotEquals(e1, e2);
	}

	@Test
	public void hashCodeEqual() {
		Entity e = new Entity();
		assertEquals(e.hashCode(), e.hashCode());
	}
	
	@Test
	public void hashCodeNotEqual() {
		Entity e1 = new Entity();
		Entity e2 = new Entity();		
		assertNotEquals(e1.hashCode(), e2.hashCode());
	}
	
	
}
