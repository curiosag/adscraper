package org.cg.analytics;

import org.joda.time.DateTime;
import java.util.List;

public class TimeSeriesRow {
	DateTime date;
	List<Integer> values;

	public TimeSeriesRow(DateTime date, List<Integer> values) {
		this.date = date;
		this.values = values;
	}

	public DateTime GetDateTime() {
		return date;
	}

	public List<Integer> GetValues() {
		return values;
	}
}
