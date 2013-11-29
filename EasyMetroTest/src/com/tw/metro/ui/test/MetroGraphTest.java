package com.tw.metro.ui.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import junit.framework.TestCase;

import com.tw.metro.IMapDataLoader;
import com.tw.metro.IMetroFactory;
import com.tw.metro.IResultPathReceiver;
import com.tw.metro.Link;
import com.tw.metro.MetroFactoryTest;
import com.tw.metro.MetroGraph;
import com.tw.metro.Path;
import com.tw.metro.Station;

public class MetroGraphTest extends TestCase implements IResultPathReceiver {

	MetroGraph thiz = null;
	IMetroFactory  factory = null;
	
	int callbackId = 0;
	String fileName = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		factory = new MetroFactoryTest();
		thiz = factory.createMetroGraph();
	}
	
	@Override protected void tearDown() throws Exception {
		super.tearDown();
		
//		File file = new File(fileName);
//		if (file.exists()) {
//			file.delete();
//		}
	}
	
	public void test_preConditions() {
		assertTrue(thiz.getStationCount() == 0);
		assertFalse(thiz.isLoadingComplete());
	}
	
	public void test_loadMap() throws IOException {
		
		thiz.loadMap(getLoader());
		
		assertTrue("TestingLocation".equals(thiz.getCityName()));
		assertTrue(thiz.getStationCount() == 12);
		assertTrue(thiz.isLoadingComplete());
	}
	
	public void test_addLink() throws IOException {
		thiz.loadMap(getLoader());
		int baseCount = thiz.getStationCount();
		
		thiz.addLink("E", "F", "red");
		assertTrue("stationCount:" + thiz.getStationCount(), 
				thiz.getStationCount() == baseCount+1);
		
		thiz.addLink("J", "K", "green");
		assertTrue(thiz.getStationCount() == baseCount+2);
		
		thiz.addLink("E", "S", "blue");
		assertTrue(thiz.getStationCount() == baseCount+3);
		
		thiz.addLink("M", "N", "white");
		assertTrue(thiz.getStationCount() == baseCount+5 );
	}
	
	public void test_getLinks() throws IOException {
		thiz.loadMap(getLoader());
		
		Station A = Station.get("A");
		Station B = Station.get("B");
		Station C = Station.get("C");
		Station G = Station.get("G");
		Station J = Station.get("J");
		Station I = Station.get("I");
		Station P = Station.get("P");
		
		Link A2B = Link.newInstance(A, B, "red");
		Link A2G = Link.newInstance(A, G, "green");
		Link B2A = Link.newInstance(B, A, "red");
		Link B2C = Link.newInstance(B, C, "red");
		Link J2I = Link.newInstance(J, I, "green");
		Link J2P = Link.newInstance(J, P, "blue");
		
		Set<Link> links = thiz.getLinks(A);
		assertTrue(links.size() == 2);
		assertTrue(links.contains(A2B));
		assertTrue(links.contains(A2G));
		
		links = thiz.getLinks(B);
		assertTrue(links.size() == 2);
		assertTrue(links.contains(B2A));
		assertTrue(links.contains(B2C));
		
		links = thiz.getLinks(J);
		assertTrue(links.size() == 2);
		assertTrue(links.contains(J2I));
		assertTrue(links.contains(J2P));
		
	}

	public void test_getStationName() throws IOException {
		
		thiz.loadMap(getLoader());
		
		String[] names = thiz.getStationNames();
		assertTrue("names count : " + names.length, names.length == 12);
	}
	
	public void test_findShortestPath() throws IOException {
		
		thiz.loadMap(getLoader());
		
		thiz.addLink("M", "N", "white");
		
		thiz.findShortestPath("A", "E", this);
		assertTrue(callbackId == 1);
		
		thiz.findShortestPath("A", "Z", this);
		assertTrue(callbackId == 3);
		
		thiz.findShortestPath("X", "A", this);
		assertTrue(callbackId == 3);
		
		thiz.findShortestPath("A", "M", this);
		assertTrue(callbackId == 2);
		
		// Details of this method are tested in AugmentedBfsTest
	}
	
	private IMapDataLoader getLoader() throws IOException {
		fileName = createTestInputFile("test");
		return factory.createMapDataLoader(fileName, ",");
	}
	
	private String createTestInputFile(String fileName) throws IOException {
		File file = File.createTempFile(fileName, "in");
		FileWriter writer = new FileWriter(file);
		String contents = "TestingLocation \n " +
				" 2 4 6 8 \n" +
				"red \n" +
				"A, B, C, D, E \n" +
				"green \n" +
				"A, G, H, I, J \n" +
				"blue \n" +
				"J, P, Q, R, E \n";
		writer.write(contents);
		
		writer.flush();
		writer.close();
		
		return file.getPath();
	}

	@Override
	public void onPathFound(Path path) {
		callbackId = 1;
	}

	@Override
	public void onPathNotFound() {
		callbackId = 2;
	}

	@Override
	public void onPlaceNotFound(String place) {
		callbackId = 3;
	}
}
