package org.cg.analytics;

import org.joda.time.DateTime;

class TimeIntervalDay extends TimeIntervalDate_c {
	
	@Override
	public TimeIntervalType getIntervalType()
	{
		return TimeIntervalType.day;
	}
	
	@Override
	public DateTime toInterval(DateTime value) {
		return value.withTimeAtStartOfDay();
	}

	@Override
	public DateTime nextInterval(DateTime value) {
		return value.plusDays(1);
		
	}

	@Override
	public DateTime previousInterval(DateTime value) {
		return value.minusDays(1);
	}
	
}
