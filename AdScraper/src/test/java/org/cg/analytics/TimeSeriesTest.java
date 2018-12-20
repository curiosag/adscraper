package org.cg.analytics;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.cg.analytics.WeekDayDistribution.aggItem;
import org.cg.base.Const;
import org.cg.common.io.FileUtil;
import org.cg.common.util.CollectionUtil;
import org.cg.util.list.ListUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.opencsv.CSVReader;

@SuppressWarnings("boxing")
public class TimeSeriesTest {

	DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	final String dim1 = "dim1";
	final String dim2 = "dim2";

	private void add(List<DataPoint> data, DateTime date, String dimension) {
		data.add(new DataPoint(date, dimension));
	}

	private void add(TimeSeries data, DateTime date, String dimension) {
		data.add(new DataPoint(date, dimension));
	}

	private TimeSeries createTs() {
		return new TimeSeries(TimeIntervalType.day);
	}

	//@Test
	public void testWeekDayDistribution() {
		DateTime mon1 = f.parseDateTime("2015-08-03 23:13:26");
		DateTime mon2 = f.parseDateTime("2015-08-10 23:13:26");
		DateTime wed = f.parseDateTime("2015-08-26 23:13:26");
		
		TimeSeries s = createTs();
		add(s, mon1, dim1);
		add(s, mon1, dim1);
		add(s, mon1, dim2);
		add(s, mon1, dim2);
		
		add(s, mon2, dim2);
		add(s, mon2, dim2);
		
		add(s, wed, dim2);
		
		s.addSumPerInterval();
		
		WeekDayDistribution wd = new WeekDayDistribution(s);
		List<aggItem> agg = CollectionUtil.toList(wd.days());
		assertEquals(7, agg.size());
		assertEquals(1, agg.get(0).day(), 0.000001);
		assertEquals(3, agg.get(2).day(), 0.000001);
		assertEquals(3, agg.get(0).avg(), 0.000001);
		assertEquals(1, agg.get(1).avg(), 0.000001);
	}

	@Test
	public void testBigData() {

		try {
			try (CSVReader reader = new CSVReader(new FileReader(FileUtil.pwd() + "/TestData/adHistory.csv"))) {
				TimeSeries s = new TimeSeries(TimeIntervalType.day);
				String[] fields;
				while ((fields = reader.readNext()) != null && fields.length == 4) {
					s.add(new DataPoint(new DateTime(Const.dateFmt.parse(fields[1])), Rendering
							.urlToDimension(fields[2])));
				}
				s.addSumPerInterval();
				FileUtil.writeToFile(Rendering.get(RenderingType.dygraphJson, new WeekDayDistribution(s)), FileUtil.pwd()
						+ "/TestData/hgen.csv");
			}
		} catch (IOException | ParseException e) {
			fail(e.getMessage());
		}
	}

	//@Test
	public void testDayOfWeek() {

		DateTime mon = f.parseDateTime("2015-08-03 23:13:26");
		DateTime wed = f.parseDateTime("2015-08-26 23:13:26");
		DateTime sun = f.parseDateTime("2015-07-12 23:13:26");

		TimeSeries ts = new TimeSeries(TimeIntervalType.dayOfWeek);
		add(ts, mon, dim1);
		add(ts, wed, dim1);
		add(ts, sun, dim1);

		for (int i = 0; i < 7; i++)
			assertTrue(ts.contains(mon.plusDays(i)));

		assertEquals(7, ts.Size());

	}

	//@Test
	public void testDay() {

		DateTime day_2 = f.parseDateTime("1999-12-30 23:13:26");

		DateTime day1 = f.parseDateTime("2000-01-01 23:13:26");
		DateTime day1BeginOfDay = f.parseDateTime("2000-01-01 00:00:00");
		DateTime day2 = f.parseDateTime("2000-01-02 23:13:26");
		DateTime day4 = f.parseDateTime("2000-01-04 23:13:26");
		DateTime day6 = f.parseDateTime("2000-01-06 23:13:26");

		List<DataPoint> data = new LinkedList<DataPoint>();
		TimeSeries ts = createTs().addAll(data);

		// empty series
		assertEquals(ts.GetSeries().iterator().hasNext(), false);
		assertEquals(ts.GetColumnNames().iterator().hasNext(), false);
		assertEquals(ts.Size(), 0);

		// 2000-01-01	dim1	1
		add(data, day1, dim1);
		ts.addAll(data);
		assertTrue(ts.contains(day1));
		assertTrue(ts.contains(day1BeginOfDay));
		assertTrue(ListUtil.equal(ts.GetColumnNames(), Lists.newArrayList(dim1)));

		// 2000-01-01	dim1	2
		ts.addAll(data);
		assertTrue(ListUtil.equal(ts.valuesFor(day1).get(), Lists.newArrayList(2)));
		assertTrue(ListUtil.equal(ts.GetColumnNames(), Lists.newArrayList(dim1)));

		//					dim1	
		// 2000-01-01		2
		// 2000-01-02		1

		ts.add(new DataPoint(day2, dim1));
		assertTrue(ts.valuesFor(day2).isPresent());
		assertTrue(ListUtil.equal(ts.valuesFor(day2).get(), Lists.newArrayList(1)));

		//				dim1	
		// 2000-01-01	  2		
		// 2000-01-02     1
		// 2000-01-03     0
		// 2000-01-04     1

		ts.add(new DataPoint(day4, dim1));
		DateTime gapDay = day4.minusDays(1);
		assertTrue(ts.valuesFor(gapDay).isPresent());
		assertTrue(ListUtil.equal(ts.valuesFor(gapDay).get(), Lists.newArrayList(0)));

		assertTrue(ts.valuesFor(day4).isPresent());
		assertTrue(ListUtil.equal(ts.valuesFor(day4).get(), Lists.newArrayList(1)));

		//				dim1	dim2	
		// 2000-01-01	  2		0
		// 2000-01-02     1		1
		// 2000-01-03     0		1
		// 2000-01-04     1		0
		// 2000-01-05     0		0
		// 2000-01-06     0		2

		ts.add(new DataPoint(day2, dim2));
		assertTrue(ListUtil.equal(ts.GetColumnNames(), Lists.newArrayList(dim1, dim2)));

		assertTrue(ListUtil.equal(ts.valuesFor(day1).get(), Lists.newArrayList(2, 0)));
		assertTrue(ListUtil.equal(ts.valuesFor(day2).get(), Lists.newArrayList(1, 1)));
		assertTrue(ListUtil.equal(ts.valuesFor(gapDay).get(), Lists.newArrayList(0, 1)));
		assertTrue(ListUtil.equal(ts.valuesFor(day4).get(), Lists.newArrayList(1, 0)));
		assertFalse(ts.valuesFor(day4.plusDays(1)).isPresent());

		ts.add(new DataPoint(day6, dim2));
		ts.add(new DataPoint(day6, dim2));
		assertTrue(ListUtil.equal(ts.valuesFor(day6.minusDays(1)).get(), Lists.newArrayList(0, 0)));
		assertTrue(ListUtil.equal(ts.valuesFor(day6).get(), Lists.newArrayList(0, 2)));

		//				dim1	dim2
		// 1999-12-30	  0		1
		// 1999-12-31	  0		0
		// 2000-01-01	  2		0
		// 2000-01-02     1		1		etc...

		ts.add(new DataPoint(day_2, dim2));
		assertTrue(ListUtil.equal(ts.valuesFor(day_2).get(), Lists.newArrayList(0, 1)));
		assertTrue(ListUtil.equal(ts.valuesFor(day_2.plusDays(1)).get(), Lists.newArrayList(0, 0)));

	}

}
