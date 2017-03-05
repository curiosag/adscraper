package org.cg.history;

import java.util.*;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.adscraper.factory.StorageFactory;
import org.cg.base.Check;
import org.cg.base.IKeyTypeValueStorage;
import org.cg.base.IKeyValueStorage;

public final class HistoryRingBuffer {

	private static final String URL_DELIMITER = "\n";
	private static final int MAX_HISTORY_ITEMS = 90;
	private final String entityKind = "HistoryBulkItem";
	private List<String> cache;

	private IKeyValueStorage storage;

	public static HistoryRingBuffer create(String urlId) {
		Check.notEmpty(urlId);

		return new HistoryRingBuffer(urlId);
	}

	private HistoryRingBuffer() {
		super();
	}

	private HistoryRingBuffer(String urlId) {
		this();
		storage = StorageFactory.get().createKeyValueStorage().of(entityKind + '_' + urlId);
		cache = load(MAX_HISTORY_ITEMS);
	}

	public final void delete() {
		storage.clear();
		cache.clear();
	}

	public final void flush() {
		StringBuilder sb = new StringBuilder();

		int itemsAdded = 0;
		for (String s : cache) {
			if (itemsAdded > MAX_HISTORY_ITEMS)
				break;
			sb.append(s).append(URL_DELIMITER);
			itemsAdded++;
		}
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);

		String urlCsv = sb.toString();

		storage.save(urlCsv);
	}

	public final void store(List<ScrapedValues> ads) {
		Check.notNull(ads);

		for (ScrapedValues values : ads)
			store(values);
	}

	public final void store(ScrapedValues ad) {
		Check.notNull(ad);

		cache.add(0, ad.get(ValueKind.url).valueOrDefault());
	}

	private List<String> load(int limit) {
		List<String> history = new ArrayList<String>();
		String historyCsv = storage.get();

		if (historyCsv != null) {
			String[] items = historyCsv.split(URL_DELIMITER);

			int maxItem = Math.min(limit, items.length);
			for (int i = 0; i < maxItem; i++)
				history.add(items[i]);
		}

		return history;
	}

	public final int clip(int num) {

		int hi = Math.min(num, cache.size());
		for (int i = 0; i < hi; i++)
			cache.remove(0);

		return hi;
	}

	public final boolean find(String url) {
		Check.notEmpty(url);

		return cache.indexOf(url) >= 0;
	}

	public final int size() {
		return cache.size();
	}

}
