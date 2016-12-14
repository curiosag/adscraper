package org.cg.util.time;

import static org.junit.Assert.*;

import org.gwtTests.base.Log;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class TimeUtilTest {

	DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	
	@Test
	public void test() {
		
		DateTime mon = f.parseDateTime("2015-08-03 23:13:26"); 
		DateTime wed = f.parseDateTime("2015-08-26 23:13:26"); 
		DateTime sun = f.parseDateTime("2015-07-12 23:13:26"); 

		for (int i = 0; i <= 6; i++) 
			Log.info(Integer.toString(TimeUtil.getDayOfWeek(mon.plusDays(i))));
		
		
		assertEquals(3, TimeUtil.getDayOfWeek(wed));
		assertEquals(1, TimeUtil.getDayOfWeek(mon));		
		assertEquals(7, TimeUtil.getDayOfWeek(sun));
		
		for (int i = 0; i <= 6; i++) 
			assertEquals(i + 1, TimeUtil.getDayOfWeek(mon.plusDays(i)));
				
	}

}
