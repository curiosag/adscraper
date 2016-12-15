package org.cg.analytics;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.cg.base.Check;
import org.cg.base.Log;
import org.cg.common.util.CollectionUtil;

import com.google.common.base.Optional;

public final class TimeSeries {

	public static final String colNameTotal = "total";

	private TimeInterval<DateTime, DateTime> interval;
	private TreeMap<String, Integer> colNames = new TreeMap<String, Integer>(); // columnName -> columnIndex
	private TreeMap<DateTime, List<Integer>> dataRows = new TreeMap<DateTime, List<Integer>>(new DateTimeComparator());
	private HashMap<TimeIntervalType, Class<?>> timeIntervalStrategies = new HashMap<TimeIntervalType, Class<?>>();

	private TimeIntervalType intervalType;

	private TimeSeries() {
		timeIntervalStrategies.put(TimeIntervalType.day, TimeIntervalDay.class);
		timeIntervalStrategies.put(TimeIntervalType.week, TimeIntervalWeek.class);
		timeIntervalStrategies.put(TimeIntervalType.hourOfDay, TimeIntervalHourOfDay.class);
		timeIntervalStrategies.put(TimeIntervalType.dayOfWeek, TimeIntervalDayOfWeek.class);
	}

	public TimeSeries(TimeIntervalType interval) {
		this();
		this.intervalType = interval;
		this.interval = getTimeInterval(interval);
	}

	public TimeSeries(TimeIntervalType interval, List<DataPoint> dataPoints) {
		this(interval);
		addAll(dataPoints);
	}

	public TimeSeries setDense(boolean value) {
		return this;
	}

	private void addColumn(String name) {
		colNames.put(name, Integer.valueOf(colNames.size()));
		for (Entry<DateTime, List<Integer>> entry : dataRows.entrySet())
			entry.getValue().add(Integer.valueOf(0));
	}

	public TimeInterval<DateTime, DateTime> getInterval() {
		return interval;
	}

	public int columnCount() {
		return colNames.size();
	}

	private void considerColumn(String name) {
		if (!colNames.containsKey(name))
			addColumn(name);
	}

	private Integer columnIndex(String name) {
		Integer idx = colNames.get(name);
		Check.isTrue(idx != null);

		return idx;
	}

	private void logTime(String msg, DateTime t) {
		Log.info(msg + " " + t.toString());
	}

	private void extendSeriesTo(DateTime t) {
		DateTime i = interval.toInterval(t);

		if (dataRows.isEmpty())
			dataRows.put(i, createInitValues());

		else if (!contains(t)) { //contains t, not i! contains re-maps to interval again, which is not idempotent

			while (interval.isBefore(dataRows.lastKey(), i))
				dataRows.put(interval.nextInterval(dataRows.lastKey()), createInitValues());

			while (interval.isAfter(dataRows.firstKey(), i))
				dataRows.put(interval.previousInterval(dataRows.firstKey()), createInitValues());
		}
	}

	@SuppressWarnings("unused")
	private void logPastExtension(DateTime t) {
		logTime("extending series for date ", t);
		logTime("first key is ", dataRows.firstKey());
		logTime("adding past interval ", interval.previousInterval(dataRows.firstKey()));
	}

	@SuppressWarnings("unused")
	private void logFutureExtension(DateTime t) {
		logTime("extending series for date ", t);
		logTime("last key is ", dataRows.lastKey());
		logTime("adding future interval ", interval.nextInterval(dataRows.lastKey()));
	}

	private List<Integer> createInitValues() {
		List<Integer> result = new ArrayList<Integer>(columnCount());

		for (int i = 0; i < columnCount(); i++)
			result.add(Integer.valueOf(0));

		return result;
	}

	public void add(DataPoint d) {
		Check.notNull(d);

		String colName = d.GetDimension();
		considerColumn(colName);

		extendSeriesTo(d.GetDateTime());
		DateTime currDate = interval.toInterval(d.GetDateTime());

		List<Integer> currValues = dataRows.get(currDate);
		Check.notNull(currValues);

		int colIndex = columnIndex(colName).intValue();
		Integer newCount = Integer.valueOf(currValues.get(colIndex).intValue() + 1);
		currValues.set(colIndex, newCount);
	}

	public TimeSeries addAll(List<DataPoint> dataPoints) {
		Check.notNull(dataPoints);

		for (DataPoint dataPoint : dataPoints)
			add(dataPoint);

		return this;
	}

	public List<String> GetColumnNames() {
		TreeMap<Integer, String> namesByIndex = new TreeMap<Integer, String>();

		for (Entry<String, Integer> e : colNames.entrySet())
			namesByIndex.put(e.getValue(), e.getKey());

		return CollectionUtil.toList(namesByIndex.values());
	}

	public Iterable<Entry<DateTime, List<Integer>>> GetSeries() {
		return dataRows.entrySet();
	}

	public boolean contains(DateTime t) {
		DateTime i = interval.toInterval(t);
		return dataRows.containsKey(i);
	}

	public int Size() {
		return dataRows.size();
	}

	public Optional<List<Integer>> valuesFor(DateTime t) {
		DateTime i = interval.toInterval(t);
		if (!dataRows.containsKey(i))
			return Optional.absent();
		else
			return Optional.of(dataRows.get(i));
	}

	public Integer getSum(List<Integer> numbers) {
		int sum = 0;
		for (Integer i : numbers)
			sum = sum + i.intValue();
		return Integer.valueOf(sum);
	}

	public TimeSeries addSumPerInterval() {
		addColumn(colNameTotal);

		for (Entry<DateTime, List<Integer>> e : dataRows.entrySet()) {
			List<Integer> vals = e.getValue();
			vals.set(vals.size() - 1, getSum(vals));
		}

		return this;
	}

	private TimeInterval<DateTime, DateTime> getTimeInterval(TimeIntervalType t) {
		switch (t) {
		case day:
			return new TimeIntervalDay();
		case week:
			return new TimeIntervalWeek();
		case dayOfWeek:
			return new TimeIntervalDayOfWeek();
		case hourOfDay:
			return new TimeIntervalHourOfDay();

		default:
			Check.isTrue(false);
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private TimeInterval<DateTime, DateTime> getTimeInterval_(TimeIntervalType t) {
		Check.isTrue(timeIntervalStrategies.containsKey(t));
		Class<?> c = timeIntervalStrategies.get(t);
		
		try {
			// problematic way to make this dynamic
			return (TimeInterval<DateTime, DateTime>) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Failed to create timeInterval for type " + t.name());
		}
	}

	public TimeIntervalType getIntervalType() {
		return intervalType;
	}

}