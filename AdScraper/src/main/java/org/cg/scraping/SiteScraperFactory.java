package org.cg.scraping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.cg.base.Check;

public final class SiteScraperFactory {

	private static List<SiteScraper> scrapers = new ArrayList<SiteScraper>();

	private static void addScraper(SiteValueScrapers usingExtractions) {
		scrapers.add(new SiteScraperJSoup(usingExtractions));
	}

	static {
		addScraper(new SiteValueScrapersBazar());
		addScraper(new SiteValueScrapersJobWohnen());
		addScraper(new SiteValueScrapersWillhaben());
		addScraper(new SiteValueScrapersImmobilienscout24());
	}

	public static final Optional<SiteScraper> get(String url) {
		Check.notNull(url);

		for (SiteScraper scraper : scrapers)
			if (scraper.canHandle(url))
				return Optional.of(scraper);

		return Optional.empty();
	}

}
