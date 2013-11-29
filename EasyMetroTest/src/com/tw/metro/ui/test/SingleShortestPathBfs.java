package com.tw.metro.ui.test;

import java.util.Stack;

import junit.framework.TestCase;

import com.tw.metro.IMetroFactory;
import com.tw.metro.IResultPathReceiver;
import com.tw.metro.IShortestPathStrategy;
import com.tw.metro.Link;
import com.tw.metro.MetroFactoryTest;
import com.tw.metro.MetroGraph;
import com.tw.metro.Station;

public class SingleShortestPathBfs extends TestCase
	implements IResultPathReceiver {

	IShortestPathStrategy thiz;
	IMetroFactory  factory;
	MetroGraph graph;
	
	String placeNotFoundStr = null;
	int callbackId = 0;
	int distance = 0;
	int cost = 0;
	int time = 0;
	Stack<Link> path = null;
	
	@Override
	protected void setUp() throws Exception {	
		super.setUp();
		
		factory = new MetroFactoryTest();
		graph = factory.createMetroGraph();
		thiz = graph.getStrategy();
	}
	
	public void testPreConditions() {		
		assertNotNull(thiz);
		assertTrue(placeNotFoundStr == null);
		assertTrue(callbackId == 0);
		assertTrue(distance == 0);
		assertTrue(cost == 0);
		assertTrue(time == 0);
		assertNull(path);
	}
	
	public void test_findShortestPathForLine() {
		// A - B - C - D - E - F [black]
		resetCallbackIdentifiers();
		
		Station A = Station.get("A");
		Station B = Station.get("B");
		Station F = Station.get("F");
		Station G = Station.get("G");
		Station I = Station.get("I");
		
		thiz.findShortestPath(A, B, this);
		
		assertTrue(callbackId == 3);
		assertTrue("placeNotFoundStr: " + placeNotFoundStr, 
				placeNotFoundStr.equalsIgnoreCase("A"));
				
		// with just two stations A - B
		graph.addLink("A", "B", "Black");
		
		resetCallbackIdentifiers();
		int returnValue = thiz.findShortestPath(A, B, this);
		assertTrue("rv is " + returnValue, returnValue == 1);
		assertTrue("callbackId is : " + callbackId, callbackId == 1);
		assertTrue("distance:" + distance, distance == 1);
		assertTrue("cost: " + cost, cost == 1);
		assertTrue("time: " + time, time == 1);
		assertTrue("path.size: " + path.size(), path.size() == 1);		

		// disconnected graph A - B & C - D - E - F
		graph.addLink("C", "D", "Black");
		graph.addLink("D", "E", "Black");
		graph.addLink("E", "F", "Black");
		
		resetCallbackIdentifiers();
		returnValue = thiz.findShortestPath(A, F, this); 
		assertTrue(returnValue == -1);
		assertTrue(callbackId == 2);
		assertTrue(distance == 0);
		assertTrue(cost == 0);
		assertTrue(time == 0);
		assertNull(path);		
		
		// Connect graph A - B - C - D - E - F
		graph.addLink("B", "C", "Black");
		resetCallbackIdentifiers();
		returnValue = thiz.findShortestPath(A, F, this); 
		assertTrue("rv is " + returnValue, returnValue == 5);
		assertTrue("callbackId is : " + callbackId, callbackId == 1);
		assertTrue("distance:" + distance, distance == 5);
		assertTrue("cost: " + cost, cost == 5);
		assertTrue("time: " + time, time == 5);
		assertTrue("path.size: " + path.size(), path.size() == 5);	
		
		
		// Connect graph with different time, distance and switch cost
		// A - B - C - D - E - F
		graph.setConfiguration(2, 10, 10, 10);
		resetCallbackIdentifiers();
		returnValue = thiz.findShortestPath(A, F, this); 
		assertTrue(returnValue == 5);
		assertTrue(callbackId == 1);
		assertTrue(distance == 10);
		assertTrue(cost == 50);
		assertTrue(time == 50);
		assertEquals(path.size(), 5);	
		
		// Stations which are not present
		resetCallbackIdentifiers();
		returnValue = thiz.findShortestPath(A, G, this); 
		assertTrue(returnValue == -1);
		assertTrue(callbackId == 3);
		assertTrue(placeNotFoundStr.equalsIgnoreCase("G"));
		
		resetCallbackIdentifiers();
		returnValue = thiz.findShortestPath(I, F, this); 
		assertTrue(returnValue == -1);
		assertTrue(callbackId == 3);
		assertTrue(placeNotFoundStr.equalsIgnoreCase("I"));
		
		resetCallbackIdentifiers();
		returnValue = thiz.findShortestPath(I, G, this); 
		assertTrue(returnValue == -1);
		assertTrue(callbackId == 3);
		assertTrue(placeNotFoundStr.equalsIgnoreCase("I"));
	}

	public void test_findShortestPathForGraph() {
		// Graph - is a triangle
		// A - B - C - D - E  [red]
		// A - G - H - I - J  [green]
		// J - P - Q - R - E [blue] 
		
		Station A = Station.get("A");
		Station B = Station.get("B");
		Station C = Station.get("C");
		Station E = Station.get("E");
		Station I = Station.get("I");
		Station D = Station.get("D");
		Station R = Station.get("R");
		Station Z = Station.get("Z");
		
		graph.setConfiguration(2, 5, 10, 20);
		
		// red line
		graph.addLink("A", "B", "red");
		assertTrue(graph.getStationCount() == 2);
		graph.addLink("B", "C", "red");
		assertTrue(graph.getStationCount() == 3);
		graph.addLink("C", "D", "red");
		assertTrue(graph.getStationCount() == 4);
		graph.addLink("D", "E", "red");
		assertTrue(graph.getStationCount() == 5);
		
		// green line
		graph.addLink("A", "G", "green");
		assertTrue(graph.getStationCount() == 6);
		graph.addLink("G", "H", "green");
		assertTrue(graph.getStationCount() == 7);
		graph.addLink("H", "I", "green");
		assertTrue(graph.getStationCount() == 8);
		graph.addLink("I", "J", "green");
		assertTrue(graph.getStationCount() == 9);
		
		// blue line
		graph.addLink("J", "P", "blue");
		assertTrue(graph.getStationCount() == 10);
		graph.addLink("P", "Q", "blue");
		assertTrue(graph.getStationCount() == 11);
		graph.addLink("Q", "R", "blue");
		assertTrue(graph.getStationCount() == 12);
		graph.addLink("R", "E", "blue");
		assertTrue(graph.getStationCount() == 12);
		
		resetCallbackIdentifiers();
		int rv = thiz.findShortestPath(A, E, this);
		assertTrue("rv is " + rv, rv == 4);
		assertTrue("callbackId is " + callbackId, callbackId == 1);
		assertTrue("distance is " + distance, distance == 4 * 2);
		assertTrue("time is " + time, time == 4 * 5);
		assertTrue("cost is " + cost, cost == 4 * 10);
		assertEquals("different path size", path.size(), 4);	
		
		Link entry1 = path.pop();
		Link link1 = Link.newInstance(A, B, "red");
		assertEquals(entry1, link1);
	
		Link entry2 = path.pop();
		Link link2 = Link.newInstance(B, C, "red");
		assertEquals(entry2, link2);
		
		Link entry3 = path.pop();
		Link link3 = Link.newInstance(C, D, "red");
		assertEquals(entry3, link3);
		
		Link entry4 = path.pop();
		Link link4 = Link.newInstance(D, E, "red");
		assertEquals(entry4, link4);
		
		resetCallbackIdentifiers();
		rv = thiz.findShortestPath(A, R, this);
		assertTrue(rv == 5);
		assertTrue(callbackId == 1);
		assertTrue(distance == 5 * 2);	
		assertTrue(time == 5 * 5);
		assertTrue(cost == (5 * 10 + 1 * 20));
		assertEquals(path.size(), 5);	
		
		entry1 = path.pop();
		link1 = Link.newInstance(A, B, "red");
		assertEquals(entry1, link1);
	
		entry2 = path.pop();
		link2 = Link.newInstance(B, C, "red");
		assertEquals(entry2, link2);
		
		entry3 = path.pop();
		link3 = Link.newInstance(C, D, "red");
		assertEquals(entry3, link3);
		
		entry4 = path.pop();
		link4 = Link.newInstance(D, E, "red");
		assertEquals(entry4, link4);
		
		Link entry5 = path.pop();
		Link link5 = Link.newInstance(E, R, "blue");
		assertEquals(entry5, link5);
		
		resetCallbackIdentifiers();
		rv = thiz.findShortestPath(I, D, this);
		assertTrue(rv == 6);
		assertTrue(callbackId == 1);
		assertTrue(distance == 6 * 2);
		assertTrue(time == 6 * 5);
		assertTrue(cost == 6 * 10 + 2 * 20 || cost == 6 * 10 + 1 * 20);  // shortest path, not lowest cost
		assertEquals(path.size(), 6);	
		
		rv = thiz.findShortestPath(A, Z, this); 
		assertEquals(rv, -1);
		assertEquals(callbackId, 3);
		assertTrue(placeNotFoundStr.equalsIgnoreCase("Z"));
	}
	
	private void resetCallbackIdentifiers() {
		callbackId = 0;
		placeNotFoundStr = null;
		path = null;
		distance = cost = time = 0;
	}	
	
	@Override public void onPathFound(com.tw.metro.Path path) {
		callbackId = 1;
		this.distance = path.getDistance();
		this.cost = path.getCost();
		this.time = path.getTime();
		this.path = path.getEntries();
	}

	@Override public void onPathNotFound() {
		callbackId = 2;
	}

	@Override public void onPlaceNotFound(String place) {
		callbackId = 3;
		placeNotFoundStr = place; 
	}
}
