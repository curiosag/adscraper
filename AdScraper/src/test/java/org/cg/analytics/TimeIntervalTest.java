package org.cg.analytics;

import static org.junit.Assert.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class TimeIntervalTest {

	private static DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	@Test
	public void testDay() {
		TimeIntervalDay interval = new TimeIntervalDay();
		
		DateTime dt = f.parseDateTime("2012-01-10 23:13:26");
		DateTime dtBeginOfDay = f.parseDateTime("2012-01-10 00:00:00");
		DateTime dtBeginOfNextDay = f.parseDateTime("2012-01-11 00:00:00");
		
		assertTrue("", interval.toInterval(dt).equals(dtBeginOfDay));
		assertTrue("", interval.nextInterval(dtBeginOfDay).equals(dtBeginOfNextDay));
		
	}

	@Test
	public void testWeek() {
		TimeIntervalWeek interval = new TimeIntervalWeek();
		
		DateTime saturday = f.parseDateTime("2015-08-08 23:13:26");
		DateTime dtBeginOfWeek = f.parseDateTime("2015-08-03 00:00:00"); // monday
		DateTime dtBeginOfNextWeek = f.parseDateTime("2015-08-10 00:00:00");
		DateTime dtBeginOfPreviousWeek = f.parseDateTime("2015-07-27 00:00:00");
		
		assertTrue("", interval.toInterval(dtBeginOfWeek).equals(dtBeginOfWeek)); 
		assertTrue("", interval.toInterval(saturday).equals(dtBeginOfWeek));
		
		assertFalse("", interval.nextInterval(saturday).equals(dtBeginOfNextWeek));
		assertTrue("", interval.nextInterval(dtBeginOfWeek).equals(dtBeginOfNextWeek));
		assertFalse("", interval.previousInterval(saturday).equals(dtBeginOfPreviousWeek));
		assertTrue("", interval.previousInterval(dtBeginOfWeek).equals(dtBeginOfPreviousWeek));
		
	}

}
