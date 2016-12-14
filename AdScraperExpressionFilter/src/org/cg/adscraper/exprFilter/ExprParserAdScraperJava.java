package org.cg.adscraper.exprFilter;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.filterlist.FilterList;

public class ExprParserAdScraperJava {

	private final ScrapedValues v;
	private final FilterList f;
	
	public ExprParserAdScraperJava(ScrapedValues v ,FilterList f ) {
		this.v = v;
		this.f = f;
	}
	
	public ResultAdScraper eval(String rule ){
		return new ExprParserAdScraper(v, f).eval(rule);
	} 
	
}
