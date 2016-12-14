package org.cg.analytics;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

class TimeIntervalHourOfDay extends TimeIntervalDate_c {

	private DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	private DateTime baseDate = f.parseDateTime("2012-01-01 00:00:00");
	
	@Override
	public TimeIntervalType getIntervalType()
	{
		return TimeIntervalType.hourOfDay;
	}
	
	@Override
	public DateTime toInterval(DateTime value) {
		return  baseDate.plusHours(value.getHourOfDay());
	}

	@Override
	public DateTime nextInterval(DateTime value) {
		return value.plusHours(1);
	}

	@Override
	public DateTime previousInterval(DateTime value) {
		return value.minusHours(1);
	}

	@Override
	public String render(DateTime value) {
		return Integer.toString(value.getHourOfDay());
	}

}
