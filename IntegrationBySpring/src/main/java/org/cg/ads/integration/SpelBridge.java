package org.cg.ads.integration;

import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.history.History;
import org.cg.scraping.SiteScraperFactory;
import org.springframework.messaging.Message;

public class SpelBridge {
	public static List<ScrapedValues> scrapeMasterList(String url, String html){
		return SiteScraperFactory.getMasterPageScraper(url).getMasterList(url, html);
	};
	
	public ScrapedValues scrapeDetails(String url, ScrapedValues current, String html){
		SiteScraperFactory.getDetailPageScraper(url).addDetails(current, html);
		return current;
	}
	
	public boolean valid(ScrapedValues values){
		return true;
	}
	
	public synchronized Message<?> historyStorage(final Message<ScrapedValues> message) {
		History.instance().add(message.getHeaders().get("urlId").toString(), message.getPayload());
		return message;
	}
	
}
