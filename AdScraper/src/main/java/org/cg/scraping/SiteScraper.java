package org.cg.scraping;

import java.util.Collection;
import java.util.function.Predicate;

import org.cg.ads.advalues.ScrapedValues;

public interface SiteScraper {
	boolean canHandle(String url);

	Collection<ScrapedValues> get(String url, Predicate<ScrapedValues> filterAds);

	default String padSlash(String urlSuffix) {
		if (!urlSuffix.startsWith("/"))
			urlSuffix = "/" + urlSuffix;
		return urlSuffix;
	}
}
