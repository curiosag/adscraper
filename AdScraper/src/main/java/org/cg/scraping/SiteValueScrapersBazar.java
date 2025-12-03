package org.cg.scraping;

import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;

public final class SiteValueScrapersBazar implements SiteValueScrapers {

	public boolean canHandle(String url) {
		Check.notNull(url);

		return url.startsWith("http://www.bazar.at");
	}


	public String masterListSelector() {
		return "[class^=result i]";
	}

	@Override
	public boolean jsEnabled() {
		return false;
	}

	public ValuesScraper extractorAdList() {
		ValuesScraper result = new ValuesScraper();
		result.add(ValueScraperJSoup.create(ValueKind.detailLink, "[class=title] [href]", "href"));
		result.add(ValueScraperJSoup.create(ValueKind.prize, "[class=result-list-item-link-price productPrice]")
				.setNormalizer(prizeNormalizer));
		result.add(ValueScraperJSoup.create(ValueKind.timestamp, "[class=timeago]", "title"));
		result.add(ValueScraperJSoup.create(ValueKind.location, "[class=district]"));
		return result;
	}

	public ValuesScraper extractorAdDetails() {
		ValuesScraper result = new ValuesScraper();
		result.add(ValueScraperJSoup.create(ValueKind.title, "[itemprop=name]"));
		result.add(ValueScraperJSoup.create(ValueKind.phone, "[class=adSidebarContentBox phoneNumberSmall] p"));
		result.add(ValueScraperJSoup.create(ValueKind.description, "[class=description]"));
		result.add(ValueScraperJSoup.create(ValueKind.size,
				"[class=\"ad-detail-attr-element fir Space\"] [class=adDescription]"));
		result.add(ValueScraperJSoup.create(ValueKind.rooms, "[class=room] [class=adDescription]"));
		return result;
	}

	private final StringNormalizer prizeNormalizer = new StringNormalizer() {
		public String normalize(String value) {
			return normalizePrize(value);
		}
	};

	public static String normalizePrize(String value){
		if (value == null)
			return null;
		else {
			// "123.456,78" -> 123456.78
			// 123,- -> 123
			return value.trim().replace(",-", "").replace(".", "").replace(',', '.');
		}
	}
	
}
