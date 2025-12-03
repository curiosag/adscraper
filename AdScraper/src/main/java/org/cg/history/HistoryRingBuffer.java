package org.cg.history;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;

import java.util.ArrayList;
import java.util.List;

public final class HistoryRingBuffer {

	private static final int MAX_HISTORY_ITEMS = 250;
    private final String urlId;
    private List<String> cache = new ArrayList<>();

	public static HistoryRingBuffer create(String urlId) {
		Check.notEmpty(urlId);

		return new HistoryRingBuffer(urlId);
	}

	private HistoryRingBuffer(String urlId) {
	    this.urlId = urlId;
	}

	public final void store(List<ScrapedValues> ads) {
		Check.notNull(ads);

		for (ScrapedValues values : ads)
			store(values);
	}

	public final void store(ScrapedValues ad) {
		Check.notNull(ad);

		cache.add(0, ad.get(ValueKind.url).valueOrDefault());
		if (cache.size() > MAX_HISTORY_ITEMS){
		    cache.remove(cache.size() -1);
        }
	}

	public final boolean find(String url) {
		Check.notEmpty(url);

		return cache.indexOf(url) >= 0;
	}

	public final int size() {
		return cache.size();
	}

	public void clip(int num){
		for(int i = 0; i < num && ! cache.isEmpty(); i ++){
			cache.remove(i);
		}
	}
}
