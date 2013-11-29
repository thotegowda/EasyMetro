package com.tw.metro;

public interface MapDataReceiver {
	
	public void setCityName(String name);
	
	/**
	 * According requirement all the links will have equal distance/cost and
	 * all Stations will have equal switch cost
	 */
	public void setConfiguration(
			int linkDistance, int linkTime, int linkCost, int stationSwitchCost);

	public void onAddLink(Station from, Station to, String color);
	
	public void onComplete();
}
