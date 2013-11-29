package com.tw.metro;

import java.io.IOException;

public interface IMapDataLoader {
	/**
	 * Loads the required map information from source and notifies receiver with the APIs exposed. 
	 * @throws IOException when it fails to load/open the source specified
	 */
	public void load(MapDataReceiver receiver) throws IOException;
}
