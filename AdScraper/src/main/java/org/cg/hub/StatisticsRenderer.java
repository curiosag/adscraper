package org.cg.hub;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.cg.adscraper.factory.StorageFactory;
import org.cg.analytics.DataPoint;
import org.cg.analytics.Rendering;
import org.cg.analytics.RenderingType;
import org.cg.analytics.TimeIntervalType;
import org.cg.analytics.TimeSeries;
import org.cg.analytics.WeekDayDistribution;
import org.cg.base.HistoryItem;
import org.cg.common.util.StringUtil;
import org.cg.hub.Settings;
import org.cg.util.enums.EnumUtil;
import org.joda.time.DateTime;

import java.util.AbstractMap.SimpleImmutableEntry;

import com.google.common.base.Optional;

public class StatisticsRenderer {

	private final String adStatJSonTemplate = "{\"byDay\":%s,\"byHourOfDay\":%s,\"byWeek\":%s,\"byDayOfWeek\":%s,\"scanUrls\":%s}";

	private String getScanUrls() {
		String digraphJSonTemplate = "{\"urlids\":[%s],\"urls\":[%s]}";
		List<String> urlids = new LinkedList<String>();
		List<String> urls = new LinkedList<String>();

		for (SimpleImmutableEntry<String, String> e : Settings.instance().getSettingsByType(org.cg.base.Const.SETTINGTYPE_URL)) {
			urlids.add(StringUtil.quote(e.getKey()));
			urls.add(StringUtil.quote(e.getValue()));
		}
		return String.format(digraphJSonTemplate, StringUtil.ToCsv(urlids, ","), StringUtil.ToCsv(urls, ","));
	}

	private String getAdStatPageData() {

		RenderingType jSon = RenderingType.dygraphJson;
		List<DataPoint> points =AsDataPoints();
		TimeSeries daily = new TimeSeries(TimeIntervalType.day, points).addSumPerInterval();

		String byDay = Rendering.get(jSon, daily);
		String byHour = Rendering.get(jSon, new TimeSeries(TimeIntervalType.hourOfDay, points).addSumPerInterval());
		String byWeek = Rendering.get(jSon, new TimeSeries(TimeIntervalType.week, points).addSumPerInterval());
		String byDayOfWeek = Rendering.get(jSon, new WeekDayDistribution(daily));

		String json = String.format(adStatJSonTemplate, byDay, byHour, byWeek, byDayOfWeek, getScanUrls());
		return json;

	}
	
	public List<DataPoint> AsDataPoints() {
		LinkedList<DataPoint> result = new LinkedList<DataPoint>();
		
		List<HistoryItem> hist = StorageFactory.get().getHistoryStorage().get(Integer.MAX_VALUE);
		
		for (HistoryItem h : hist) {
			String dimension = Rendering.urlToDimension(h.urlGrazed);
			result.add(new DataPoint(new DateTime(h.date).plusSeconds(org.cg.base.Const.secsDeltaViennaTime), dimension));
		}
		
		return result;
	}

	public String createStatistics(String dataFormat, String interval) {
		Optional<RenderingType> renderingType = EnumUtil.decode(dataFormat, RenderingType.class);
		Optional<TimeIntervalType> intervalType = EnumUtil.decode(interval, TimeIntervalType.class);

		if (!renderingType.isPresent())
			return "Unknown data format: " + dataFormat;
		if (!intervalType.isPresent())
			return "Unknown time interval: " + interval;

		return Rendering.get(renderingType.get(), new TimeSeries(intervalType.get())
				.addAll(AsDataPoints()).addSumPerInterval());
	}

}
