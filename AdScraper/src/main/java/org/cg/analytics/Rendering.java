package org.cg.analytics;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.cg.analytics.WeekDayDistribution.aggItem;
import org.cg.base.Check;
import org.cg.common.util.StringUtil;
import org.joda.time.DateTime;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class Rendering {

	private static String digraphJSonTemplate = "{\"cols\":[%s],\"rows\":[%s]}";
	private static List<TimeIntervalType> dateXAxis = Lists.newArrayList(TimeIntervalType.day, TimeIntervalType.week);
	
	private static String formatDateColumn(RenderingType t, TimeInterval<DateTime, DateTime> interval, DateTime d) {
		if (t == RenderingType.dygraphJson && dateXAxis.contains(interval.getIntervalType()))
			return Long.toString(d.getMillis());
		else
			return interval.render(d);
	}

	private static List<String> toList(RenderingType t, TimeInterval<DateTime, DateTime> interval,
			Entry<DateTime, List<Integer>> e) {
		List<String> result = Lists.newArrayList(formatDateColumn(t, interval, e.getKey()));
		for (Integer i : e.getValue())
			result.add(i.toString());

		return result;
	}

	private static String renderRow(RenderingType t, String s) {
		switch (t) {
		case csv:
			return s;
		case dygraphCsv:
			return "\"" + s + " \\n\"";
		case dygraphJson:
			return "[" + s + "]";
		default:
			Check.isTrue(false);
			return "";
		}
	}

	private static String getRowSeparator(RenderingType t) {
		switch (t) {
		case csv:
			return "\n";
		case dygraphCsv:
			return "+\n";
		case dygraphJson:
			return ",\n";
		default:
			Check.isTrue(false);
			return "";
		}
	}

	private static String getEnvelope(RenderingType t, List<String> columnNames, List<String> rows) {
		String csv = Joiner.on(getRowSeparator(t)).skipNulls().join(rows);
		switch (t) {
		case dygraphJson:
			return String.format(digraphJSonTemplate, getJsonColumnNames(columnNames), csv);
		default:
			return csv;
		}
	}

	private static String getJsonColumnNames(List<String> columnNames) {
		List<String> quoted = new LinkedList<String>();
		for (String s : columnNames)
			quoted.add("\"" + s + "\"");
		return StringUtil.toCsv(quoted, ",");
	}

	public static String get(RenderingType t, TimeSeries s) {
		List<String> rowsFormatted = new LinkedList<String>();

		List<String> columnNames = Lists.newArrayList("Date");
		columnNames.addAll(s.GetColumnNames());

		if (t != RenderingType.dygraphJson)
			rowsFormatted.add(renderRow(t, StringUtil.toCsv(columnNames, ",")));

		for (Entry<DateTime, List<Integer>> r : s.GetSeries())
			rowsFormatted.add(renderRow(t, StringUtil.toCsv(toList(t, s.getInterval(), r), ",")));

		return getEnvelope(t, columnNames, rowsFormatted);
	}
	
    private static String round(float d) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }
    
    private static String round(double d) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }
	
	public static String get(RenderingType t, WeekDayDistribution d) {
		Check.isTrue(t == RenderingType.dygraphJson);
		
		List<String> rows = new LinkedList<String>();
	
		for (aggItem ai : d.days())
		{
			float avg = ai.avg();
			double stddev = ai.stdDeviation();
			String lo = round(avg - stddev);
			String mid = round(avg);
			String hi = round(avg + stddev);
			rows.add("[" + Integer.toString(ai.day()) + ", [" + lo + "," + mid  + "," + hi + "]]");
		}	
		return getEnvelope(t, Lists.newArrayList("Day", "Avg"), rows);
	}
	
	public static String urlToDimension(String url) {
		int dot1 = url.indexOf(".");
		int dot2 = url.indexOf(".", dot1 + 1);

		if (dot1 >= 0 && dot2 >= 0)
			return url.substring(dot1 + 1, dot2);
		else
			return url;
	}

}
