package com.tw.metro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MetroGraph implements MapDataReceiver {

	Map<Station, Set<Link>> mMetroLinks = new HashMap<Station, Set<Link>>();
	ArrayList<String> mStationNames = new ArrayList<String>(); // helper for autocomplete textview. 
	
	final IMetroFactory mFactory;
	final IShortestPathStrategy mStrategy;	
	
	private String mCityName;	
	private boolean mMapLoadingComplete = false;	

	public MetroGraph(IMetroFactory factory)  {
		mFactory = factory;
		this.mStrategy = factory.createShortestPathStrategy(this);
	}
	
	public MetroGraph(String cityName, IMetroFactory factory)  {
		mCityName = cityName;
		mFactory = factory;
		this.mStrategy = factory.createShortestPathStrategy(this);
	}

	public void loadMap(IMapDataLoader dataProvider) throws IOException {
		dataProvider.load(this);
	}
	
	// TODO:Don't expose data type, bind it within an object. 
	public Set<Link> getLinks(Station station) {
		return mMetroLinks.get(station);
	}
	
	public int getStationCount() {
		return mMetroLinks.size();
	}
	
	public String getCityName() {
		return mCityName;
	}
		
	public boolean isLoadingComplete() {
		return mMapLoadingComplete;
	}
	
	public void addLink(String from, String to, String color) {
		addLink(Station.get(from),
				Station.get(to), color);
	}
		
	public void addLink(Station from, Station to, String color) {
		// means first node - Need this information for starting line.
		// but for now ignore
		if (from == null) {
			return;
		}

		Link toTrack = Link.newInstance(from, to, color);
		Link fromTrack = Link.newInstance(to, from, color);

		// undirected graph => two entries.
		Set<Link> tracks = mMetroLinks.get(from);
		if (tracks == null) {
			tracks = new HashSet<Link>(); 
			mMetroLinks.put(from, tracks);
		}
		tracks.add(toTrack);

		tracks = mMetroLinks.get(to);
		if (tracks == null) {
			tracks = new HashSet<Link>(); 
			mMetroLinks.put(to, tracks);
		}
		tracks.add(fromTrack);
		
		mStationNames.add(from.toString());
	}
	
	/**
	 * finds the shortest path between any two stations, in terms of distance
	 */
	public int findShortestPath(String from, String to, IResultPathReceiver pathReceiver) {
		return findShortestPath(Station.get(from), 
				 Station.get(to), pathReceiver);
	}
	
	public int findShortestPath(Station from, Station to, IResultPathReceiver pathReceiver) {
		System.out.println("shortestPath(" + from + " -> " + to + ")");
		
		return mStrategy.findShortestPath(from, to, pathReceiver);
	}

	/**
	 * Array of all Station names for UI system to use
	 */
	public String[] getStationNames() {
		String[] names = new String[mStationNames.size()];
		mStationNames.toArray(names);
		return names;
	}
	
	public IShortestPathStrategy getStrategy() {
		return mStrategy;
	}
	
	@Override public void setCityName(String name) {
		mCityName = name;
	}
		
	@Override public void setConfiguration(int linkDistance, int linkTime, 
			int linkCost, int stationSwitchCost) {
		mStrategy.setConfigurations(
				linkDistance, linkTime, linkCost, stationSwitchCost);
	}

	@Override public void onAddLink(Station from, Station to, String color) {
		addLink(from, to, color);
	}
	
	@Override public void onComplete() {
		mMapLoadingComplete = true;
	}

	@Override public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(" Metro System of the City: " + mCityName + "\n");
		for (Entry<Station, Set<Link>> p : mMetroLinks.entrySet()) {
			Station stn = p.getKey();
			builder.append(stn.getName() + "->");

			Set<Link> tracks = p.getValue();
			for (Link track : tracks) {
				builder.append("[" + track.getColor() + ":" + track.getEnd() + "], ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
}
