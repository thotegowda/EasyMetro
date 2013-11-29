package com.tw.metro.ui.test;

import com.tw.metro.Link;
import com.tw.metro.Station;

import junit.framework.TestCase;

public class LinkTest extends TestCase {

	public void test_equals() {
		Link a1 = Link.newInstance(Station.get("A"), Station.get("B"), 
				"white");
		Link a2 = Link.newInstance(Station.get("A"), Station.get("B"), 
				"white");
		Link a3 = Link.newInstance(Station.get("A"), Station.get("B"), 
				"Black");
		Link a4 = Link.newInstance(Station.get("B"), Station.get("A"), 
				"white");
		
		assertTrue(a1.equals(a2));
		assertFalse(a1.equals(a3));
		assertFalse(a1.equals(a4));
		assertFalse(a2.equals(a3));
		assertFalse(a2.equals(a4));
	}
	
	public void test_hashCode() {
		Link a1 = Link.newInstance(Station.get("A"), Station.get("B"), 
				"white");
		Link a2 = Link.newInstance(Station.get("A"), Station.get("B"), 
				"white");
		Link a3 = Link.newInstance(Station.get("A"), Station.get("B"), 
				"Black");
		Link a4 = Link.newInstance(Station.get("B"), Station.get("A"), 
				"white");
		
		assertTrue(a1.hashCode() == a1.hashCode());
		assertTrue(a1.hashCode() == a2.hashCode());
		assertFalse(a1.hashCode() == a3.hashCode());
		assertFalse(a1.hashCode() == a4.hashCode());
		assertFalse(a2.hashCode() == a3.hashCode());
		assertFalse(a2.hashCode() == a4.hashCode());
	}
}
