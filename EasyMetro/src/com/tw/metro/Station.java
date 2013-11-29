package com.tw.metro;

import java.util.HashMap;
import java.util.Map;

public class Station {
	final String name;
	
	private Station(String name) {
		this.name = name.toLowerCase();
	}

	public String getName() {
		return name;
	}

	public static Station get(String name) {
		return StationFactory.getStation(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Station)) {
			return false;
		}
		
		if (obj == this) {
			return false;
		}
		
		return ((Station) obj).getName().equalsIgnoreCase(name);
	}

	@Override public int hashCode() {
		return name.hashCode();
	}

	@Override public String toString() {
		return name;
	}
	
	private static class StationFactory {
		
		private static Map<String, Station> mStnUniverse = new HashMap<String, Station>();
		
		public static Station getStation(String name) {
			Station station = mStnUniverse.get(name);
			if (station == null) {
				station = new Station(name);
				mStnUniverse.put(name, station);
			}
			return station;
		}
	}
}

