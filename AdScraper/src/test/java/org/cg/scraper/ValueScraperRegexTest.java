package org.cg.scraper;

import static org.junit.Assert.*;

import java.util.Collection;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.scraping.SiteScraper;
import org.cg.scraping.SiteScraperFactory;
import org.junit.Test;

import com.google.common.base.Predicate;

public class ScraperTest {

	//private static final String baz = "http://www.bazar.at/wien-brigittenau-motorraeder-mopeds-quads-anzeigen,dir,1,cId,8,fc,125,loc,125,o,1,tp,0";
	private static final String will = "https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote?areaId=900&sort=0&periode=0&PRICE_TO=500&page=3&rows=30&view=";

	Predicate<ScrapedValues> canAlways = new Predicate<ScrapedValues>() {

		@Override
		public boolean apply(ScrapedValues input) {
			return true;
		} 
	};
	
	@Test
	public void test() {
		String url = will;
		SiteScraper scraper = SiteScraperFactory.get(url).orNull();
		assertNotNull(scraper);
		Collection<ScrapedValues> scraped = scraper.get(url, canAlways);
		System.out.println("scraped " + String.valueOf(scraped.size()) + " from " + url);
		assertTrue(scraped.size() > 0);
		for (ScrapedValues values : scraped) {
			System.out.println(values.toString());
		}
		
	}

}
