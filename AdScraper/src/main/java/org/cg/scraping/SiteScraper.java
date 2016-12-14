package org.cg.scraping;

import java.util.Collection;

import org.cg.ads.advalues.ScrapedValues;

import com.google.common.base.Predicate;

public interface SiteScraper {
	public boolean canHandle(String url);

	public Collection<ScrapedValues> get(String url, Predicate<ScrapedValues> filterAds);

	public Collection<ScrapedValues> get(String url, Predicate<ScrapedValues> filterAds, int numberSamples);
}
