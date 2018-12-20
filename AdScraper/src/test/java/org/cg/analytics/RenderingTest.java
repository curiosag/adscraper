package org.cg.analytics;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class RenderingTest {

	@Test
	public void test() {
		
		final String dim1 = "dim1";
		DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime day1 = f.parseDateTime("2000-01-01 23:13:26");
		f.parseDateTime("2000-01-02 23:13:26");
		
		// 2000-01-01	dim1	1
		
		
		TimeSeries ts = new TimeSeries(TimeIntervalType.day);
		ts.add(new DataPoint(day1, dim1));
		String a = Rendering.get(RenderingType.dygraphCsv, ts);
		assertTrue(a.length() > 0);
		
	}

}
