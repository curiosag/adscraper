package org.cg.scraping;

import org.cg.ads.advalues.ScrapedValues;

public interface IDetailPageScraper {
	public void addDetails(ScrapedValues current, String html);
}
