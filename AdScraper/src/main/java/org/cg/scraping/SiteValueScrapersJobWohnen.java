package org.cg.scraping;

import org.cg.ads.advalues.ValueKind;
import org.gwtTests.base.Check;
import org.gwtTests.base.Const;

public final class SiteValueScrapersJobWohnen implements SiteValueScrapers  {

	public boolean canHandle(String url) {
		Check.notNull(url);
		return url.startsWith("http://www.jobwohnen.at");
	}
	
	public String masterListSelector() {
		return "[class=elist]:not(:contains(Diese Suche als Wohn-Agent speichern))";
	}

	public ValuesScraper extractorAdList() {
		ValuesScraper result = new ValuesScraper();
		result.add(ValueScraperJSoup.create(ValueKind.detailLink, "td:eq(2) a", "href"));
		result.add(ValueScraperJSoup.create(ValueKind.title, "td:eq(2) a"));
		result.add(ValueScraperJSoup.create(ValueKind.size, "td:eq(4)"));
		result.add(ValueScraperJSoup.create(ValueKind.prize, "td:eq(5)"));
		result.add(ValueScraperJSoup.create(ValueKind.transferMoney, "td:eq(6)"));
		
		return result;
	}

	public ValuesScraper extractorAdDetails() {
		ValuesScraper result = new ValuesScraper();
		
		result.add(ValueScraperJSoup.create(ValueKind.description, "td:containsOwn(Beschreibung) + td"));
		result.add(ValueScraperJSoup.create(ValueKind.deposit, "td:containsOwn(Kaution) + td"));
		result.add(ValueScraperJSoup.create(ValueKind.location, "td:containsOwn(Adresse) + td"));
		result.add(ValueScraperJSoup.create(ValueKind.contact, "[id=tblInserent]", Const.MULTIPLE_RESULT_ELEMENTS));
		
		return result;
	}

}
