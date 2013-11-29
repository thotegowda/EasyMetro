package com.tw.metro;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FileMapDataLoader extends AbstractScannerMapDataLoader {

	public FileMapDataLoader(String path, String token) {
		super(path, token);
	}

	@Override
	protected Scanner onCreateScanner(String path) throws IOException {
		return new Scanner(new FileReader(path));
	}
	
}
