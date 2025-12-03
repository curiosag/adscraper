package org.cg.scraping;

public interface SiteValueScrapers {

	boolean canHandle(String url);

	default int skip(){
		return 0;
	}

	boolean jsEnabled();

	String masterListSelector();

	ValuesScraper extractorAdList();

	ValuesScraper extractorAdDetails();

}
