package org.cg.analytics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cg.base.Check;
import org.cg.util.time.TimeUtil;
import org.joda.time.DateTime;

public class WeekDayDistribution {

	public class aggItem {
		private int day;
		List<Integer> values = new ArrayList<Integer>();

		public aggItem(int day)
		{
			this.day = day;
		}
		
		public int day()
		{
			return this.day;
		}
		
		public void addValue(Integer i) {
			values.add(i);
		}
		
		public float avg()
		{
			int sum = 0;
			for (Integer i : values) 
				sum = sum + i.intValue();
			return sum / values.size();
		}
		
		public double stdDeviation()
		{
			float avg = avg();
			double sumDev = 0;
			for (Integer i : values) 
				sumDev = sumDev + Math.pow((i.intValue() - avg), 2);
			return Math.sqrt(sumDev / values.size());
		}
		
	}

	TimeSeries daily;
	TreeMap<Integer, aggItem> days = new TreeMap<Integer, aggItem>();

	int idxSum;
	
	public WeekDayDistribution() {
	}

	public WeekDayDistribution(TimeSeries s) {
		this.daily = s;
		idxSum = s.GetColumnNames().indexOf(TimeSeries.colNameTotal);
		
		Check.isTrue(idxSum > 0);
		Check.isTrue(s.getInterval().getIntervalType() == TimeIntervalType.day);
		
		aggregate(s);
	}
	
	public Collection<aggItem> days()
	{
		return days.values();
	}

	private void extendDays(DateTime t, Integer count) {
		Integer day = Integer.valueOf(TimeUtil.getDayOfWeek(t));
		if (!days.containsKey(day))
			days.put(day, new aggItem(day.intValue()));
		days.get(day).addValue(count);
	}

	private void aggregate(TimeSeries s) {
		for (Entry<DateTime, List<Integer>> e : s.GetSeries())
			extendDays(e.getKey(), e.getValue().get(idxSum));
	}
	
	

}
