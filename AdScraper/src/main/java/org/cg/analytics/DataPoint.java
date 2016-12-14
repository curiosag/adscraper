package org.cg.analytics;

import org.joda.time.DateTime;

public class DataPoint {
	private DateTime date;
	private String dimension;

	public DataPoint(DateTime date, String dimension) {
		this.date = date;
		this.dimension = dimension;
	}

	public DateTime GetDateTime() {
		return date;
	}

	public String GetDimension() {
		return dimension;
	}
}
