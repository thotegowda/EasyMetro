package com.tw.metro;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;

public class AssetMapDataLoader extends AbstractScannerMapDataLoader {

	final Context mContext;
	
	public AssetMapDataLoader(Context context, String path, String token) {
		super(path, token);
		mContext = context;
	}

	@Override
	protected Scanner onCreateScanner(String path) throws IOException {
		AssetManager manager = mContext.getAssets();
		InputStream is = manager.open(path);
		return new Scanner(new BufferedInputStream(is));
	}
	
	
}
