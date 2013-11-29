package com.tw.metro;


public interface IShortestPathStrategy {
	
	/**
	 * Use to set the configuration information to calculate the shortest path
	 * 
	 * @param linkDistance - distance between two stations
	 * @param linkTime - time to travel between two stations
	 * @param linkCost - cost between two stations
	 * @param stationSwitchCost - extra cost for switching line at junction
	 */
	public void setConfigurations(
			int linkDistance, int linkTime, int linkCost, int stationSwitchCost);
	/**
	 * Finds the shortest path and notifies the receiver with path, if there are any. Otherwise
	 * the error cases
	 * 
	 * @return - Returns number of links that needs to be traveled to reach the destination
	 */
	public int findShortestPath(Station from, Station to, 
			IResultPathReceiver receiver);
}
