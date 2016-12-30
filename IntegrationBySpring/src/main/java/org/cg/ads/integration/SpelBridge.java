package org.cg.ads.integration;

import java.util.Arrays;
import java.util.List;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.dispatch.Dispatch;
import org.cg.history.History;
import org.cg.scraping.SiteScraperFactory;
import org.springframework.messaging.Message;

public class SpelBridge {
	private final static ScrapedValues sentinel = createSentinel();
	private static final String SENTINEL = "sentinel";

	public List<ScrapedValues> scrapeMasterList(String url, String html) {
		if (SENTINEL.equals(url)) {
			return Arrays.asList(sentinel);
		} else
			return SiteScraperFactory.getMasterPageScraper(url).getMasterList(url, html);
	};

	private static ScrapedValues createSentinel() {
		ScrapedValues result = new ScrapedValues();
		result.add(ScrapedValue.create(ValueKind.url, SENTINEL));
		return result;
	}

	public ScrapedValues scrapeDetails(String url, ScrapedValues current, String html) {
		if (current != sentinel)
			SiteScraperFactory.getDetailPageScraper(url).addDetails(current, html);
		return current;
	}

	public boolean valid(ScrapedValues values) {
		return true;
	}

	public synchronized Message<?> historyStorage(final Message<ScrapedValues> message) {
		History.instance().add(message.getHeaders().get("urlId").toString(), message.getPayload());
		return message;
	}

	public void sendMail(final Message<ScrapedValues> message) {
		Dispatch.instance().deliver(message.getPayload());
	}

	public boolean containsAny(final Message<ScrapedValues> message, List<String> negTerms) {
		String text = (message.getPayload().get(ValueKind.description).valueOrDefault()
				+ message.getPayload().get(ValueKind.title).valueOrDefault()).toLowerCase();
		for (String s : negTerms)
			if (text.contains(s.toLowerCase()))
				return true;

		return false;
	}
	
}
