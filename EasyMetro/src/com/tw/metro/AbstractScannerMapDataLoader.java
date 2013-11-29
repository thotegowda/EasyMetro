package com.tw.metro;

import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Reads the the map information from the scanner
 * File format: 
 * <city name>
 * <distance> <time> <cost> <switch cost>
 * <line color 1>
 * <staitions list separated by ,>
 * <line color 2>
 * <stations list separated by ,>
 * ....
 */
public abstract class AbstractScannerMapDataLoader implements IMapDataLoader {

	private final String mPath;
	private final String mSeparator;
	
	public AbstractScannerMapDataLoader(String path, String separator) {
		mPath = path;
		mSeparator = separator;
	}
	
	@Override
	public void load(MapDataReceiver receiver) throws IOException {
		Scanner scanner = onCreateScanner(mPath);
		constructMap(scanner, mSeparator, receiver);
	}

	protected abstract Scanner onCreateScanner(String path) throws IOException; 
	
	private void constructMap(Scanner sc, String token, MapDataReceiver receiver) {
		String name = sc.nextLine().trim();
		
		receiver.setCityName(name);
		
		int distance = sc.nextInt();
		int time = sc.nextInt();
		int cost = sc.nextInt();
		int switchCost = sc.nextInt();
		sc.nextLine();
		
		receiver.setConfiguration(distance, time, cost, switchCost);
		
		while(sc.hasNext()){
			String colorName = sc.nextLine().trim();
			String stations = sc.nextLine().trim();
			
			Station from = null;
			StringTokenizer tokenizer = new StringTokenizer(stations, token);
			while (tokenizer.hasMoreTokens()) {
				Station to = Station.get(tokenizer.nextToken().trim());
				receiver.onAddLink(from, to, colorName);
				from = to;
			}
		}
		receiver.onComplete();
		sc.close();
	}
}
