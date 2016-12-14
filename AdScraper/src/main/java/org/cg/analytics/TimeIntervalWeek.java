package org.cg.analytics;

import org.joda.time.DateTime;

class TimeIntervalWeek extends TimeIntervalDate_c {

	@Override
	public TimeIntervalType getIntervalType()
	{
		return TimeIntervalType.week;
	}
	
	@Override
	public DateTime toInterval(DateTime value) {
		return value.minusDays(value.getDayOfWeek() - 1).withTimeAtStartOfDay();
	}

	@Override
	public DateTime nextInterval(DateTime value) {
		return value.plusDays(7);

	}

	@Override
	public DateTime previousInterval(DateTime value) {
		return value.minusDays(7);
	}

}
