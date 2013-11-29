package com.tw.metro.ui.test;

import com.tw.metro.Station;

import junit.framework.TestCase;

public class StationTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void test_equals() {
		Station a1 = Station.get("A");
		Station a2 = Station.get("A");
		Station b1 = Station.get("B");
		
		assertTrue(a1.equals(a2));
		assertEquals(a1, a2);
		assertTrue(a1 == a2);
		assertNotSame(a1, b1);
		
	}
	
	public void test_hashCode() {
		Station a1 = Station.get("A");
		Station a2 = Station.get("A");
		Station b1 = Station.get("B");
		
		assertEquals(a1.hashCode(), a2.hashCode());
		assertNotSame(a1.hashCode(), b1.hashCode());
	}
	
}
