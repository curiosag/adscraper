package org.cg.scraping;

import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;
import org.cg.base.Const;

public final class SiteValueScrapersWillhaben implements SiteValueScrapers {

	public boolean canHandle(String url) {
		Check.notNull(url);
		return url.startsWith("http://www.willhaben.at");
	}
	
	public String masterListSelector() {
		return "[itemprop=url]";
	}

	public ValuesScraper extractorAdList() {
		ValuesScraper result = new ValuesScraper();
		result.add(ValueScraperJSoup.create(ValueKind.detailLink, "a", "href"));
		return result;
	}

	public ValuesScraper extractorAdDetails() {
		ValuesScraper result = new ValuesScraper();
		
		result.add(ValueScraperJSoup.create(ValueKind.title, "[name=description]", "content"));
		result.add(ValueScraperJSoup.create(ValueKind.description, "[itemprop=description]", Const.MULTIPLE_RESULT_ELEMENTS));
		result.add(ValueScraperJSoup.create(ValueKind.phone, "dl :containsOwn(Telefon) + dd"));
		result.add(ValueScraperJSoup.create(ValueKind.size, "div :containsOwn(Wohnfläche) + div"));
		result.add(ValueScraperJSoup.create(ValueKind.rooms, "div :containsOwn(Zimmer) + div"));
		result.add(ValueScraperJSoup.create(ValueKind.limitationDuration, "div :containsOwn(Befristung) + div"));
		result.add(ValueScraperJSoup.create(ValueKind.buildingType, "div :containsOwn(Bautyp) + div"));
		result.add(ValueScraperJSoup.create(ValueKind.heating, "div :containsOwn(Heizung) + div"));
		result.add(ValueScraperJSoup.create(ValueKind.overallState, "div :containsOwn(Zustand) + div"));
		result.add(ValueScraperJSoup.create(ValueKind.agent, "[class=col-xs-6 contact-desc] :containsOwn(Firma) + dd"));
		result.add(ValueScraperJSoup.create(ValueKind.contact, "[class=col-xs-6 contact-desc] :containsOwn(Kontaktperson) + dd"));
		result.add(ValueScraperJSoup.create(ValueKind.location, "[itemprop=Address]"));
		result.add(ValueScraperJSoup.create(ValueKind.prize, "[class=price-box] [id=priceBox-price]"));
		result.add(ValueScraperJSoup.create(ValueKind.deposit, "[class=additional-prices clearfix] :matches(Kaution) + span"));
		result.add(ValueScraperJSoup.create(ValueKind.misc1, "[class=additional-prices clearfix] :matches(Zusatzinformation) + span"));
		
		return result;
	}

}
