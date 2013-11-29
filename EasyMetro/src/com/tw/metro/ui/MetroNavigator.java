package com.tw.metro.ui;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.metro.IResultPathReceiver;
import com.tw.metro.MetroFactory;
import com.tw.metro.MetroGraph;
import com.tw.metro.Path;

public class MetroNavigator 
	extends Activity 
	implements OnClickListener, IResultPathReceiver {
	
	private static final String TAG = MetroNavigator.class.getCanonicalName();

	public static String INPUT_FILE = "mars.in";
	public static String STATION_SEPARATOR = ",";

	private TextView mTxtDirection;
	private AutoCompleteTextView mAutoEdtFrom;
	private AutoCompleteTextView mAutoEdtTo;
	private TextView mTxtFrom;
	private TextView mTxtTo;
	private Button mBtnFetchRoute;
	private LinearLayout mPathResultLayout;
	
	private MetroGraph mMetroSystem;
	private IResultPathPresenter mResultPathPresenter;
	
	MetroFactory mMetroFactory = null;
	
	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_metro_navigate);
		
		configureView();
		
		try {
			mMetroFactory = new MetroFactory(this); 
			configureModules(mMetroFactory);
			
			// Load here for time being, if it gets large, move it thread. 
			loadMapData();
			
			populateUiSystem();
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.d(TAG, "Exception received while Cofiguring modules ");
			
			onMapLoadFailed();
		}
	}
	
	private void configureView() {
		mTxtDirection = (TextView) findViewById(R.id.txt_header);
		
		mTxtFrom = (TextView) findViewById(R.id.txt_from);
		mTxtTo = (TextView) findViewById(R.id.txt_to);
		
		mAutoEdtFrom = (AutoCompleteTextView) findViewById(R.id.edt_from);
		mAutoEdtTo = (AutoCompleteTextView) findViewById(R.id.edt_to);
		
		mBtnFetchRoute = (Button) findViewById(R.id.btn_fetch_route);
		mBtnFetchRoute.setOnClickListener(this);
		
		mPathResultLayout = (LinearLayout) findViewById(R.id.content_area);
	}

	private void configureModules(MetroFactory factory) throws IOException {
		mMetroSystem = factory.createMetroGraph();
		setResultPathPresenter(factory.createResultPathPresenter(mPathResultLayout));
	}
	
	// public ?  because of unit testing. 
	public void setResultPathPresenter(IResultPathPresenter presenter) {
		mResultPathPresenter = presenter;
	}
	
	private void loadMapData() throws IOException {
		mMetroSystem.loadMap(
				mMetroFactory.createMapDataLoader(INPUT_FILE, STATION_SEPARATOR));
	}
	
	private void populateUiSystem() {
		String[] stations = mMetroSystem.getStationNames();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, stations);
		mAutoEdtFrom.setAdapter(adapter);
		mAutoEdtTo.setAdapter(adapter);
	}
	
	@Override public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_fetch_route:
			hideIme();
			fetchRoutes();
			break;			
		}
	}
	
	private void fetchRoutes() {
		String from = mAutoEdtFrom.getText().toString().trim();
		String to = mAutoEdtTo.getText().toString().trim();
		
		if (!from.isEmpty() && !to.isEmpty()) {
			fetchShortestRoute(from, to);
		}
		
		// Else do nothing. 
	}
	
	private void fetchShortestRoute(String from, String to) {		
		assert(from != null && from.length() > 0);
		assert(to != null && to.length() > 0);
		
		Log.d(TAG, "fetchShortestRoute from: " + from + " to: " + to);
		mMetroSystem.findShortestPath(from, to, this);
	}
	
	@Override public void onPathFound(Path path) {
		mResultPathPresenter.pathFound(path);
	}
	
	@Override public void onPathNotFound() {
		mResultPathPresenter.pathNotFound();
	}
	
	@Override public void onPlaceNotFound(String place) {
		mResultPathPresenter.placeNotFound(place);
	}
	
	private void onMapLoadFailed() {
		mTxtDirection.setText(R.string.str_map_load_failed);
		mBtnFetchRoute.setVisibility(View.INVISIBLE);
		mAutoEdtFrom.setVisibility(View.INVISIBLE);
		mAutoEdtTo.setVisibility(View.INVISIBLE);
		mTxtFrom.setVisibility(View.INVISIBLE);
		mTxtTo.setVisibility(View.INVISIBLE);
	}
	
	private void hideIme() {
		InputMethodManager im = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(mAutoEdtTo.getWindowToken(), 0);
	}
	
	
	
	
}
