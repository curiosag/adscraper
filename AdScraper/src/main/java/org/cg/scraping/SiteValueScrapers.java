package org.cg.scraping;

public interface SiteValueScrapers {

	boolean canHandle(String url);
	
	String masterListSelector();

	ValuesScraper extractorAdList();

	ValuesScraper extractorAdDetails();

}
