package com.tw.metro;

import android.widget.LinearLayout;
import com.tw.metro.ui.IResultPathPresenter;
/**
 * Abstract factory implmentation to create required objects for metro system. 
 *
 */
public interface IMetroFactory {
	public IMapDataLoader createMapDataLoader(String path, String separater);
	
	public IShortestPathStrategy createShortestPathStrategy(MetroGraph graph);
	
	public MetroGraph createMetroGraph();
	
	// TODO: Abstract out the parameter LinearLayout in to some container class. 
	public IResultPathPresenter createResultPathPresenter(LinearLayout layout);
}