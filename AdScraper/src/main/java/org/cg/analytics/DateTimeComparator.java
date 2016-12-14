package org.cg.analytics;

import java.util.Comparator;
import org.joda.time.DateTime;

public final class DateTimeComparator implements Comparator<DateTime> {
	@Override
	public int compare(DateTime r0, DateTime r1) {
		if (r0.isBefore(r1))
			return -1;
		if (r0.isAfter(r1))
			return 1;
		return 0;
	}
}
