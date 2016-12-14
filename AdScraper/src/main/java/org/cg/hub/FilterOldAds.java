package org.cg.hub;

import java.util.ArrayList;
import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.history.History;
import org.gwtTests.base.Check;

import com.google.common.base.Function;

public class FilterOldAds implements Function<List<ScrapedValues>, List<ScrapedValues>> {
	
	String urlId;
	History history;
	
	public FilterOldAds(History history, String urlId) {
		Check.notNull(history);
		Check.notEmpty(urlId);

		this.urlId = urlId;
		this.history = history;
	}

	@Override
	public List<ScrapedValues> apply(List<ScrapedValues> in) {
		ArrayList<ScrapedValues> result = new ArrayList<ScrapedValues>();
		
		for (ScrapedValues scrapedValues : in) 
			if (!history.find(urlId, scrapedValues.valueOrDefault(ValueKind.url)))
				result.add(scrapedValues);
		
		return result;
	}

	@Override
	public boolean equals(Object object) {
		return false;
	}

}
