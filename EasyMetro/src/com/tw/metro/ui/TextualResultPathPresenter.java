package com.tw.metro.ui;

import java.util.Stack;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Html;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tw.metro.Link;
import com.tw.metro.Path;
import com.tw.metro.Station;

public class TextualResultPathPresenter implements IResultPathPresenter {
	
	final Resources mRes;
	final Context mContext;
	final LinearLayout mContainerLayout;
	
	public TextualResultPathPresenter(Context context, LinearLayout containerLayout) {
		mContext = context;
		mContainerLayout = containerLayout;
		
		mRes = context.getResources();
	}
	
	@Override public void pathFound(Path path) {
		//mContainerLayout.removeAllViews(); 
		populateViews(path);
	}

	@Override public void pathNotFound() {
		mContainerLayout.removeAllViews(); 
		
		String notFound = mRes.getString(R.string.str_path_not_found);
		addTextView(mContainerLayout, notFound);
	}
	
	@Override public void placeNotFound(String place) {
		mContainerLayout.removeAllViews(); 
		
		String notFound = mRes.getString(R.string.str_place_not_found);
		notFound = String.format(notFound, place);
		
		addTextView(mContainerLayout, notFound);
	}

	private void populateViews(Path path) {
		final LinearLayout layout = (LinearLayout) mContainerLayout;
		displaySummary(layout, path.getDistance(), 
				path.getTime(), path.getCost());
		DisplayDirections(layout, path.getEntries() );
	}
	
	private void displaySummary(LinearLayout layout,
			int distance, int time, int cost) {
		CharSequence summary = null;
		if (distance == -1) {
			summary = mRes.getString(R.string.str_path_not_found);
			return;
		}  else {
			String formattedString = mRes.getString(R.string.str_summary_entry);
			summary = Html.fromHtml(
					String.format(formattedString, distance, time, cost));
		}
		addTextView(layout, summary);
		addTextView(layout, " "); // just an empty layout
	}
	
	private void DisplayDirections(LinearLayout layout, Stack<Link> path) {
		boolean firstEntry = true;
		Station startPoint = null;
		String previousLineColor = null;
		Link entry = null;
		int stepNo = 1;
		while (!path.isEmpty()) {
			entry = path.pop();
			if (firstEntry && entry.getStart() != null) {
				startPoint = entry.getStart();
				previousLineColor = entry.getColor();
				firstEntry = false;
			} else if (!previousLineColor.equals(entry.getColor())) {
				String formatString = 
						mRes.getString(R.string.str_path_entry);
				CharSequence htmlStr = Html.fromHtml(String.format(
						formatString, stepNo, previousLineColor, 
						startPoint, entry.getStart()));				
				addTextView(layout, htmlStr);
				
				startPoint = entry.getStart();
				previousLineColor = entry.getColor();
				stepNo++;
			}
		}
	
		String formatString = mRes.getString(R.string.str_last_entry);
		CharSequence htmlStr = Html.fromHtml(String.format(formatString, 
				stepNo, previousLineColor, startPoint, entry.getEnd()));
		addTextView(layout, htmlStr);
	}
	
	private void addTextView(ViewGroup parent, CharSequence text) {
		TextView view = createTextView();
		view.setText(text);
		parent.addView(view);
	}
	
	private TextView createTextView() {
		TextView textView = new TextView(mContext);
		textView.setMaxLines(2);
		LinearLayout.LayoutParams params = 
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		params.weight = 1;
		params.setMargins(0, 5, 0, 5);
		textView.setLayoutParams(params);
		textView.setMinHeight(50);
		textView.setBackgroundColor(Color.argb(33, 22, 44, 66));
		return textView;
	}
	
}
