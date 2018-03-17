package org.smmo.test;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import org.smmo.common.*;
import org.smmo.common.actions.*;
import org.smmo.common.util.*;

import java.util.Map;
import java.util.HashMap;

public class ContextReducerTest {
	HashMap<UniqueEntity, Vec4i> cache;
	UniqueEntity e1;
	WorldMap wm;
	Context context;

	int iStart;
	int jStart;
	int kStart;
	
	int N_ROWS = 9;
	int N_COLS = 7;
	int N_LAYS = 5;
	
	@Before
	public void setup() {
		cache = new HashMap();
		wm = new WorldMapBuilder(41, 41, 41).build();
		context = new Context(wm, cache);
	}
	
	@Test
	public void intervalContained() {
		int iStart = 10;
		int jStart = 10;
		int kStart = 10;
	}
	
	@Test
	public void intervalLowCorner() {
		int iStart = 0;
		int jStart = 0;
		int kStart = 0;
	}
	
	@Test
	public void intervalHighCorner() {
		int iStart = 39;
		int jStart = 39;
		int kStart = 39;
	}
	
	@Test
	public void testOutsideLow() {
		int iStart = -1;
		int jStart = -1;
		int kStart = -1;
	}
	
	@Test
	public void testOutsideHigh() {
		int iStart = 44;
		int jStart = 44;
		int kStart = 44;
	}

	@After
	public void reduceContextAndCheckBounds() {
		Interval3i interval = new Interval3i(iStart, iStart + N_ROWS,
											 jStart, jStart + N_COLS,
											 kStart, kStart + N_LAYS);
		
		context = ContextReducer.reduceToInterval(interval, context);
		wm = context.getWorldMap();		
		assertEquals(wm.getRows(), N_ROWS);
		assertEquals(wm.getColumns(), N_COLS);
		assertEquals(wm.getLayers(), N_LAYS);
	}
}
