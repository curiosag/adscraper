package org.cg.analytics;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class TimeIntervalDate_c extends TimeInterval<DateTime, DateTime> {
	
	private static DateTimeFormatter dtFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	
  /**
   *	a < b => 1
   *	a > b => -1
   *	a = b => 0 	
   */
	
	private int compare(DateTime a, DateTime b) {
		if (b.isAfter(a))
			return 1;
		if (b.isBefore(a))
			return -1;
		return 0;
	}
	
	@Override
	public boolean isBefore(DateTime a, DateTime b)
	{
		return compare(a, b) > 0;
	}
	
	@Override
	public boolean isAfter(DateTime a, DateTime b)
	{
		return compare(a, b) < 0;
	}
	
	@Override
	public String render(DateTime a)
	{
		return a.toString(dtFormat);
	}

}
