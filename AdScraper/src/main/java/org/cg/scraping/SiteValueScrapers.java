package org.cg.scraping;

public interface SiteValueScrapers {

	boolean canHandle(String url);

	boolean jsEnabled();

	String masterListSelector();

	ValuesScraper extractorAdList();

	ValuesScraper extractorAdDetails();

}
