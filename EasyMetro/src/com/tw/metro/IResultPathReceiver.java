package com.tw.metro;

public interface IResultPathReceiver {

	public void onPathFound(Path result);

	public void onPathNotFound();
	
	public void onPlaceNotFound(String place);
}
