package com.tw.metro;

public class Link {
	final private Station mStart;
	final private Station mEnd;
	final private String mColor;

	private Link(Station from, Station to, String color) {
		this.mColor = color;
		this.mStart = from;
		this.mEnd = to;
	}

	public static Link newInstance(Station from, Station to, String color) {
		return new Link(from, to, color);
	}

	public Station getStart() {
		return mStart;
	}
	
	public Station getEnd() {
		return mEnd;
	}
	
	public String getColor() {
		return mColor;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Link)) {
			return false;
		}
		
		if (this == o) {
			return true;
		}
		
		Link link = (Link)o;
		return link.mStart.equals(mStart) &&
				link.mEnd.equals(mEnd) &&
				link.mColor.equalsIgnoreCase(mColor);
	}
	
	@Override
	public int hashCode() {
		return mStart.hashCode() * 1 + mEnd.hashCode() * 2 + mColor.hashCode();
	}
	
	@Override
	public String toString() {
		return "[" + mStart + ":" + mEnd + ":" + mColor + "]";
	}

}
