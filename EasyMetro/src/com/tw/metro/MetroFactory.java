package com.tw.metro;

import android.content.Context;
import android.widget.LinearLayout;

import com.tw.metro.ui.IResultPathPresenter;
import com.tw.metro.ui.TextualResultPathPresenter;

public class MetroFactory implements IMetroFactory {

	final private Context mContext;
	
	public MetroFactory(Context context) {
		this.mContext = context;
	}
	
	@Override public IMapDataLoader createMapDataLoader(String path, String separater) {
		return new AssetMapDataLoader(mContext, path, separater);
	}
	
	@Override public IShortestPathStrategy createShortestPathStrategy(MetroGraph graph) {
		return new SingleShortestPathBfs(graph);
	}
	
	@Override public MetroGraph createMetroGraph() {
		return new MetroGraph(this);
	}
	
	@Override public IResultPathPresenter createResultPathPresenter(LinearLayout layout) {
		return new TextualResultPathPresenter(mContext, layout);
	}
	
}
