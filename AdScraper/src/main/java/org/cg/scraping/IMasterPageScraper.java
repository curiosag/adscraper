package org.cg.scraping;

import java.util.List;

import org.cg.ads.advalues.ScrapedValues;

public interface IMasterPageScraper {
	public List<ScrapedValues> getMasterList(String url, String html);
}
