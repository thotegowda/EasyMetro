package com.tw.metro;

import java.util.Stack;

public class Path {
	private final int mDistance;
	private final int mTime;
	private final int mCost;
	private final Stack<Link> mPath;

	private Path(int distance, int time, int cost, Stack<Link> path) {
		this.mDistance = distance;
		this.mTime = time;
		this.mCost = cost;
		this.mPath = path;
	}

	public static Path newInstance(int distance, int time, int cost,
			Stack<Link> path) {
		return new Path(distance, time, cost, path);
	}

	public int getDistance() {
		return mDistance;
	}

	public int getTime() {
		return mTime;
	}

	public int getCost() {
		return mCost;
	}

	public Stack<Link> getEntries() {
		return mPath;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Path)) {
			return false;
		}

		if (this == o) {
			return true;
		}

		Path p = (Path) o;
		return this.mDistance == p.getDistance() && this.mCost == p.getCost()
				&& this.mTime == p.getTime() && mPath.equals(p.getEntries());
	}

	@Override
	public int hashCode() {
		return mDistance * 1 + mTime * 2 + mCost * 3 + mPath.hashCode();
	}
}
