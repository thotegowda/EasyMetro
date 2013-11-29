package com.tw.metro.ui;

import com.tw.metro.Path;

public interface IResultPathPresenter {
	public void pathFound(Path path);
	
	public void pathNotFound();
	
	public void placeNotFound(String place);
}