package org.cg.scraping;

import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;

public final class SiteValueScrapersWillhaben implements SiteValueScrapers {

	public boolean canHandle(String url) {
		Check.notNull(url);
		return url.startsWith("https://www.willhaben.at");
	}
	
	public String masterListSelector() {
		return ".content-section";
	}

	public ValuesScraper extractorAdList() {
		ValuesScraper result = new ValuesScraper();
		result.add(ValueScraperJSoup.create(ValueKind.detailLink, "a", "href"));
		result.add(ValueScraperJSoup.create(ValueKind.title, "[itemprop=name]"));
		
		result.add(ValueScraperJSoup.create(ValueKind.prize, ".pull-right"));
		result.add(ValueScraperJSoup.create(ValueKind.size, ".desc-left"));
		result.add(ValueScraperJSoup.create(ValueKind.location, ".address-lg"));
		result.add(ValueScraperJSoup.create(ValueKind.description, ".description"));
		  
		return result;
	}

	public ValuesScraper extractorAdDetails() {
		ValuesScraper result = new ValuesScraper();
		
		
		result.add(ValueScraperJSoup.create(ValueKind.phone, ".dl-horizontal :matchesOwn(Telefon) + dd", true));
		result.add(ValueScraperJSoup.create(ValueKind.agent, ".dl-horizontal :matchesOwn(Firma) + dd"));
		result.add(ValueScraperJSoup.create(ValueKind.deposit, ":matchesOwn(Kaution) + span"));
		
		return result;
	}

}
