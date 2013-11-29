package com.tw.metro.ui.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.tw.metro.FileMapDataLoader;
import com.tw.metro.IMapDataLoader;
import com.tw.metro.MapDataReceiver;
import com.tw.metro.Station;

public class FileMapDataLoaderTest extends TestCase implements MapDataReceiver {

	IMapDataLoader thiz = null;
	String fileName = null;
	String cityName = null;
	int distance;
	int time;
	int cost;
	int switchCost;
	int addLinkCount = 0;
	boolean onComplete = false;
	
	List<Station> fromList = new LinkedList<Station>();
	List<Station> toList = new LinkedList<Station>();
	List<String> colorList = new LinkedList<String>();
 	
	@Override
	protected void setUp() throws Exception {	
		super.setUp();
		
		thiz = getLoader();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		File file = new File(fileName);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public void testLoad() throws IOException {
		
		thiz.load(this);
		
		assertTrue("TestingLocation".equals(cityName));
		
		assertTrue(distance == 2);
		assertTrue(time == 4);
		assertTrue(cost == 6);
		assertTrue(switchCost == 8);
		
		assertTrue("addLinkCount:" + addLinkCount, addLinkCount == 12); // first and last empty nodes
		assertTrue("fromList size:" + fromList.size(), fromList.size() == 11);
		assertTrue("toList size:" + toList.size(),  toList.size() == 11);
		assertTrue("colorList size: " + colorList.size(), colorList.size() == 3); 
		
		assertTrue(fromList.contains(Station.get("A")));
		assertTrue(fromList.contains(Station.get("D")));
		assertTrue(fromList.contains(Station.get("J")));
		assertFalse(fromList.contains(Station.get("M")));
		assertFalse(fromList.contains(Station.get("E")));
		
		assertTrue(toList.contains(Station.get("B")));
		assertTrue(toList.contains(Station.get("E")));
		assertTrue(toList.contains(Station.get("J")));
		assertFalse(toList.contains(Station.get("M")));
		assertFalse(toList.contains(Station.get("A")));
		
		assertTrue(colorList.contains("red"));
		assertTrue(colorList.contains("green"));
		assertTrue(colorList.contains("blue"));
		assertFalse(colorList.contains("black"));
		
		assertTrue(onComplete);
	}
	
	public  IMapDataLoader getLoader() throws IOException {
		fileName = createTestFile("test1");
		 return new FileMapDataLoader(fileName, ",");
	}
	
	public String createTestFile(String fileName) throws IOException {
		File file = File.createTempFile(fileName, "in");
		
		FileWriter writer = new FileWriter(file);
		String contents = "TestingLocation\n " +
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
	public void setCityName(String name) {
		cityName = name;
	}

	@Override
	public void setConfiguration(int linkDistance, int linkTime, int linkCost,
			int stationSwitchCost) {
		distance = linkDistance;
		time = linkTime;
		cost = linkCost;
		switchCost = stationSwitchCost;
		
	}

	@Override
	public void onAddLink(Station from, Station to, String color) {
		
		if (from == null)  {
			return;
		}
			
		addLinkCount++;
		
		if (from != null && !fromList.contains(from)) {
			fromList.add(from);
		}
		
		if (to != null && !toList.contains(to)) {
			toList.add(to);
		}
		
		if (color != null && !colorList.contains(color)) {
			colorList.add(color);
		}
	}

	@Override
	public void onComplete() {
		onComplete = true;
		
	}
}
