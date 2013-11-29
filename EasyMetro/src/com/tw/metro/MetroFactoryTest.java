package com.tw.metro;

import com.tw.metro.ui.IResultPathPresenter;

import android.widget.LinearLayout;

public class MetroFactoryTest extends MetroFactory {

	public MetroFactoryTest() {
		super(null);
		 // This is fine, because we override methods which use context below to give 
		 // different implementation
	}
	
	@Override public IMapDataLoader createMapDataLoader(String path, String separater) {
		return new FileMapDataLoader(path, separater);
	}
	
	@Override public IResultPathPresenter createResultPathPresenter(LinearLayout layout) {
		throw new UnsupportedOperationException();
	}
}
