package com.tw.metro;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;


/*
 * This is modified BFS algorithm to find the shortest link between
 * two station in the given metro system of the city.
 * 
 * This algorithm finds the first shortest path and returns it through 
 * the PathDataReceiver object. 
 * 
 * The distance/cost/switch cost are same for each stations. This can be 
 * inputed from a text file. 
 * 
 */
public class SingleShortestPathBfs implements IShortestPathStrategy {

	public static class PathEntry {
		public final int linksCount;
		public final boolean visited;
		public final Link link;

		private PathEntry(Station from, Station to, String color,
				boolean visited, int linksCount) {
			this.linksCount = linksCount;
			this.visited = visited;
			this.link = Link.newInstance(from, to, color);
		}
		
		private PathEntry(Link link, boolean visited, int linksCount) {
			this.link = link;
			this.visited = visited;
			this.linksCount = linksCount;
		}
		
		public static PathEntry newInstance(Link link, boolean visited, int linksCount) {
			return new PathEntry(link, visited, linksCount);
		}
	}
	
	private int mDistanceBwStations = 1;
	private int mTimeBwStations = 1;
	private int mCostBwStations = 1;
	private int mLineSwitchCost = 1;
	
	final MetroGraph mGraph;
		
	public SingleShortestPathBfs(MetroGraph graph) {
		mGraph = graph;
	}
	
	@Override
	public void setConfigurations(int linkDistance, int linkTime, 
			int linkCost, int stationSwitchCost) {
		mDistanceBwStations = linkDistance;
		mTimeBwStations = linkTime;
		mCostBwStations = linkCost;
		mLineSwitchCost = stationSwitchCost;
	}

	@Override
	public int findShortestPath(Station start, Station end, IResultPathReceiver receiver) {		
		final MetroGraph graph = mGraph;
		
		// check for presence of stations in the graph
		if (graph.getLinks(start) == null) {
			receiver.onPlaceNotFound(start.toString());
			return -1;
		} else if (graph.getLinks(end) == null) {
			receiver.onPlaceNotFound(end.toString());
			return -1;
		} else if (start.equals(end)){
			receiver.onPathNotFound();
			return -1;
		}
		
		Map<Station, PathEntry> stations = new HashMap<Station, PathEntry>();
		Queue<Station> Q = new LinkedList<Station>();
		
		Q.add(start);

		while (!Q.isEmpty()) {
			Station prev = Q.remove();
			PathEntry prevEntry = stations.get(prev);
			for (Link t : graph.getLinks(prev)) {
				Station current = t.getEnd();

				if (current.equals(end)) {
					int linkCount = 1;
					if (prevEntry != null) {
						linkCount = prevEntry.linksCount + 1;
					}
					stations.put(current, PathEntry.newInstance(t, true, linkCount));
					notifyPath(start, current, stations, receiver);
					return linkCount;
				}
				
				PathEntry entry = stations.get(current);
				if (entry == null || entry.visited == false) {
					
					int linkCount = 1;
					if (prevEntry != null) {
						linkCount = prevEntry.linksCount + 1;
					}
					Q.add(current);
					stations.put(current, PathEntry.newInstance(t, true, linkCount));
				}
			}
		}
		receiver.onPathNotFound();
		return -1;
	}

	private void notifyPath(Station start, Station end, 
			Map<Station, PathEntry> stations, IResultPathReceiver receiver) {
		Stack<Link> path = new Stack<Link>();
		PathEntry curEntry = stations.get(end);
		
		int linkCount = curEntry.linksCount;
		int distance = linkCount * mDistanceBwStations;
		int cost = linkCount * mCostBwStations;
		int time = linkCount * mTimeBwStations; 

		path.push(curEntry.link);

		while (!curEntry.link.getStart().equals(start)) {
			Station prev = curEntry.link.getStart();
			PathEntry prevEntry = stations.get(prev);

			path.push(prevEntry.link);

			// check for the junction
			if (!curEntry.link.getColor().equals(prevEntry.link.getColor())) {
				cost += mLineSwitchCost;
			}

			end = prev;
			curEntry = prevEntry;
		}
		receiver.onPathFound(
				Path.newInstance(distance, time, cost, path));
	}
}