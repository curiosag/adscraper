package org.cg.history;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.base.Check;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public final class History {

	private static History instance;
	private Dictionary<String, HistoryRingBuffer> urlBuffers = new Hashtable<String, HistoryRingBuffer>();

	private History() {
		super();
	}

	public final static synchronized History instance() {
		if (instance == null) {
			instance = new History();
		}
		return instance;
	}

	private HistoryRingBuffer urlBuffer(String urlId) {
		Check.notEmpty(urlId);

		HistoryRingBuffer result = urlBuffers.get(urlId);

		if (result != null)
			return result;
		else {
			urlBuffers.put(urlId, HistoryRingBuffer.create(urlId));
			return urlBuffer(urlId);
		}
	}

	public final void add(String urlId, List<ScrapedValues> ads) {
		Check.notEmpty(urlId);
		Check.notNull(ads);
		
		HistoryRingBuffer u = urlBuffer(urlId);
		u.store(ads);

	}

	public final void add(String urlId, ScrapedValues ad) {
		Check.notEmpty(urlId);
		Check.notNull(ad);
		
		urlBuffer(urlId).store(ad);
	}

	public final boolean find(String urlId, String url) {
		Check.notEmpty(urlId);
		Check.notEmpty(url);
		
		return urlBuffer(urlId).find(url);
	};

	public final int size(String urlId) {
		Check.notEmpty(urlId);
		
		return urlBuffer(urlId).size();
	}

	public final void reset() {
		urlBuffers = new Hashtable<>();
	}
	

}
