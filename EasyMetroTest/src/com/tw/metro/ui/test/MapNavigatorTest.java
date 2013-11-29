package com.tw.metro.ui.test;

import java.util.Stack;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.tw.metro.Link;
import com.tw.metro.Path;
import com.tw.metro.Station;
import com.tw.metro.ui.IResultPathPresenter;
import com.tw.metro.ui.MetroNavigator;
import com.tw.metro.ui.R;

public class MapNavigatorTest 
	extends ActivityInstrumentationTestCase2<MetroNavigator>
	implements IResultPathPresenter {

	public static final String EAST_END = "East end";
	public static final String CYPHER_LANE = "Cypher lane";
	public static final String SNAKE_PARK = "Snake park";
	public static final String JOKERS_STREET = "Jokers street";
	public static final String GOTHAM_STREET = "Gotham Street";
	public static final String BATMAN_STREET = "Batman Street";
	public static final String FOOT_STAND = "Foot stand";
	public static final String FOOTBALL_STADIUM = "Football stadium";
	public static final String CITY_CENTRE = "City centre";
	public static final String PETER_PARK = "Peter Park";
	public static final String MAXIMUS = "Maximus";
	public static final String ROCKY_STREET = "Rocky Street";
	public static final String BOXERS_STREET = "Boxers street";
	public static final String BOXING_AVENUE = "Boxing avenue";
	public static final String ORACLE_LANE = "Oracle lane";
	public static final String KEYMAKERS_LANE = "Keymakers lane";
	public static final String MATRIX_STAND = "Matrix stand";
	
	public static final String RED = "red";
	public static final String BLUE = "blue";
	public static final String GREEN = "green";
	public static final String BLACK = "black";
	public static final String YELLOW = "yellow";	
	
	MetroNavigator mActivity;
	
	AutoCompleteTextView mAutoEdtFrom;
	AutoCompleteTextView mAutoEdtTo;
	Button mBtnFetchRoute;
	
	Path path;
	int callbackId = 0;
	
	public MapNavigatorTest() {
		super(MetroNavigator.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mActivity = getActivity();
		
		mAutoEdtFrom = (AutoCompleteTextView) mActivity.findViewById(R.id.edt_from);
		mAutoEdtTo = (AutoCompleteTextView) mActivity.findViewById(R.id.edt_to);
		mBtnFetchRoute = (Button) mActivity.findViewById(R.id.btn_fetch_route);
		
		mActivity.setResultPathPresenter(this);
	}
	
	public void test_PreConditions() {
		assertEquals(mAutoEdtFrom.getText().length(), 0);
		assertEquals(mAutoEdtTo.getText().length(), 0);
	}
	
	@UiThreadTest
	public void test_btnClick1() {
		mAutoEdtFrom.setText(EAST_END);
		mAutoEdtTo.setText(MATRIX_STAND);
		
		mBtnFetchRoute.performClick();
				
		assertEquals(callbackId, 1);
		assertNotNull(path); 
		
		assertEquals(11, path.getDistance());
		assertEquals(11 * 5, path.getTime());
		assertEquals(11 + 1 * 1, path.getCost());
		
		Stack<Link> entries = path.getEntries();
		assertEquals(11, entries.size());
		
		Link entry1 = entries.pop();
		Link link1 = Link.newInstance(Station.get(EAST_END), Station.get(FOOT_STAND), BLUE);
		assertEquals(link1, entry1);
		
		Link entry2 = entries.pop();
		Link link2 = Link.newInstance(Station.get(FOOT_STAND), Station.get(FOOTBALL_STADIUM), BLUE);
		assertEquals(link2, entry2);
		
		Link entry3 = entries.pop();
		Link link3 = Link.newInstance(Station.get(FOOTBALL_STADIUM), Station.get(CITY_CENTRE), BLUE);
		assertEquals(link3, entry3);
		
		Link entry4 = entries.pop();
		Link link4 = Link.newInstance(Station.get(CITY_CENTRE), Station.get(PETER_PARK), BLUE);
		assertEquals(link4, entry4);
		
		Link entry5 = entries.pop();
		Link link5 = Link.newInstance(Station.get(PETER_PARK), Station.get(MAXIMUS), BLUE);
		assertEquals(link5, entry5);
		
		Link entry6 = entries.pop();
		Link link6 = Link.newInstance(Station.get(MAXIMUS), Station.get(ROCKY_STREET), BLUE);
		assertEquals(link6, entry6);
		
		Link entry7 = entries.pop();
		Link link7 = Link.newInstance(Station.get(ROCKY_STREET), Station.get(BOXERS_STREET), BLUE);
		assertEquals(link7, entry7);
		
		Link entry8 = entries.pop();
		Link link8 = Link.newInstance(Station.get(BOXERS_STREET), Station.get(BOXING_AVENUE), BLUE);
		assertEquals(link8, entry8);
		
		Link entry9 = entries.pop();
		Link link9 = Link.newInstance(Station.get(BOXING_AVENUE), Station.get(ORACLE_LANE), RED);
		assertEquals(link9, entry9);
		
		Link entry10 = entries.pop();
		Link link10 = Link.newInstance(Station.get(ORACLE_LANE), Station.get(KEYMAKERS_LANE), RED);
		assertEquals(link10, entry10);
		
		Link entry11 = entries.pop();
		Link link11 = Link.newInstance(Station.get(KEYMAKERS_LANE), Station.get(MATRIX_STAND), RED);
		assertEquals(link11, entry11);
		
	}
	
	@UiThreadTest
	public void test_btnClick2() {
		mAutoEdtFrom.setText(EAST_END);
		mAutoEdtTo.setText(JOKERS_STREET);
		
		mBtnFetchRoute.performClick();
				
		assertEquals(callbackId, 1);
		assertNotNull(path); 
		
		assertEquals(3, path.getDistance());
		assertEquals(3 * 5, path.getTime());
		assertEquals(3, path.getCost());
		
		Stack<Link> entries = path.getEntries();
		assertEquals(3, entries.size());
		
		Link entry1 = entries.pop();
		Link link1 = Link.newInstance(Station.get(EAST_END), Station.get(GOTHAM_STREET), BLACK);
		assertEquals(link1, entry1);
		
		Link entry2 = entries.pop();
		Link link2 = Link.newInstance(Station.get(GOTHAM_STREET), Station.get(BATMAN_STREET), BLACK);
		assertEquals(link2, entry2);
		
		Link entry3 = entries.pop();
		Link link3 = Link.newInstance(Station.get(BATMAN_STREET), Station.get(JOKERS_STREET), BLACK);
		assertEquals(link3, entry3);
	}

	@Override
	public void pathFound(Path path) {
		callbackId = 1;
		this.path = path;
		
	}

	@Override
	public void pathNotFound() {
		callbackId = 2;
	}

	@Override
	public void placeNotFound(String place) {
		callbackId = 3;
	}
	
	

}