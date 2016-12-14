package org.cg.scraping;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ValueKind;
import org.jsoup.nodes.Element;

public interface ValueScraper {

	public ValueKind kind();
	public ValueScraper setNormalizer(StringNormalizer normalizer);
	public ScrapedValue scrape(Element e);
	
}
