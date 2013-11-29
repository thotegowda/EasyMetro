package com.tw.metro.ui.test;

import java.util.Stack;

import com.tw.metro.Link;
import com.tw.metro.Path;
import com.tw.metro.Station;

import junit.framework.TestCase;

public class PathTest extends TestCase {

	@Override protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void test_creation() {
		Stack<Link> entries = new Stack<Link>();
		Link link1 = Link.newInstance(Station.get("A"), Station.get("B"), "black");
		Link link2 = Link.newInstance(Station.get("B"), Station.get("C"), "black");

		entries.add(link1);
		entries.add(link2);
		
		Path path = Path.newInstance(2, 4, 6, entries);
		
		assertEquals(path.getDistance(), 2);
		assertEquals(path.getTime(), 4);
		assertEquals(path.getCost(), 6);
		assertEquals(path.getEntries().size(), 2);
	}
	
	public void test_equals() {
		Stack<Link> entries = new Stack<Link>();
		Link link1 = Link.newInstance(Station.get("A"), Station.get("B"), "black");
		Link link2 = Link.newInstance(Station.get("B"), Station.get("C"), "black");
		
		entries.add(link1);
		entries.add(link2);
		
		Path path1 = Path.newInstance(2, 4, 6, entries);
		Path path2 = Path.newInstance(3, 5, 7, entries);
		Path path3 = Path.newInstance(2, 4, 6, entries);
		
		assertTrue(!path1.equals(path2));
		assertTrue(path1.equals(path3));
	}
	
	public void test_hashCode() {
		Stack<Link> entries = new Stack<Link>();
		Link link1 = Link.newInstance(Station.get("A"), Station.get("B"), "black");
		Link link2 = Link.newInstance(Station.get("B"), Station.get("C"), "black");
		
		entries.add(link1);
		entries.add(link2);
		
		Path path1 = Path.newInstance(2, 4, 6, entries);
		Path path2 = Path.newInstance(3, 5, 7, entries);
		Path path3 = Path.newInstance(2, 4, 6, entries);
		 
		assertNotSame(path1.hashCode(), path2.hashCode());
		assertEquals(path1.hashCode(), path3.hashCode());
	}
	
}
