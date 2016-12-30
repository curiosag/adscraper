package org.cg.ads.integration;

import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.dispatch.Dispatch;
import org.cg.history.History;
import org.cg.scraping.SiteScraperFactory;
import org.springframework.messaging.Message;

public class SpelBridge {

	public List<ScrapedValues> scrapeMasterList(String url, String html) {
		return SiteScraperFactory.getMasterPageScraper(url).getMasterList(url, html);
	};

	public ScrapedValues scrapeDetails(String url, ScrapedValues current, String html) {
		SiteScraperFactory.getDetailPageScraper(url).addDetails(current, html);
		return current;
	}

	public synchronized Message<?> historyStorage(final Message<ScrapedValues> message) {
		History.instance().add(message.getHeaders().get("urlId").toString(), message.getPayload());
		return message;
	}

	public synchronized Message<?> flushResources(final Message<ScrapedValues> message) {
		History.instance().flush(message.getHeaders().get("urlId").toString());
		return message;
	}
	
	public synchronized Message<ScrapedValues> sendMail(final Message<ScrapedValues> message) {
		Dispatch.instance().deliver(message.getPayload());
		return message;
	}

	public boolean containsAny(final Message<ScrapedValues> message, List<String> negTerms) {
		String text = "";
		if (message.getPayload().has(ValueKind.description))
			text = text + message.getPayload().get(ValueKind.description).valueOrDefault();

		if (message.getPayload().has(ValueKind.title))
			text = text + message.getPayload().get(ValueKind.title).valueOrDefault();

		text = text.toLowerCase();

		for (String s : negTerms)
			if (text.contains(s.toLowerCase()))
				return true;

		return false;
	}

}
