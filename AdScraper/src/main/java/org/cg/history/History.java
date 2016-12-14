package org.cg.history;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.adscraper.factory.StorageFactory;
import org.gwtTests.base.Check;
import org.gwtTests.base.IHistoricalDetailStorage;
import org.gwtTests.base.IHistoryStorage;

public final class History {

	private static History instance;
	private Dictionary<String, HistoryRingBuffer> urlBuffers = new Hashtable<String, HistoryRingBuffer>();

	private final IHistoryStorage historyStorage;
	private final IHistoricalDetailStorage historicalDetailStorage;
	
	
	private History() {
		super();
		historyStorage = StorageFactory.get().getHistoryStorage();
		historicalDetailStorage = StorageFactory.get().getHistoricalDetailStorage(); 
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

		for (ScrapedValues ad : ads)
			historyStorage.store(urlId, ad);
	}

	public final void add(String urlId, ScrapedValues ad) {
		Check.notEmpty(urlId);
		Check.notNull(ad);
		
		urlBuffer(urlId).store(ad);
		historyStorage.store(urlId, ad);
		
	}

	public void addDetails(ScrapedValues ad) {
		historicalDetailStorage.store(ad);
	}
	
	public final void flush(String urlId) {
		Check.notEmpty(urlId);
		Check.isTrue(findBuffer(urlId));
		
		urlBuffer(urlId).flush();
		
		historicalDetailStorage.flush();	
	}

	public final boolean find(String urlId, String url) {
		Check.notEmpty(urlId);
		Check.notEmpty(url);
		
		return urlBuffer(urlId).find(url);
	};

	public final String clip(String urlId, int num) {
		Check.notEmpty(urlId);
		
		String result = "clipped " + Integer.toString(urlBuffer(urlId).clip(num));
		urlBuffer(urlId).flush();
		return result;
	}

	public final int size(String urlId) {
		Check.notEmpty(urlId);
		
		return urlBuffer(urlId).size();
	}

	public final void reset() {
		urlBuffers = new Hashtable<String, HistoryRingBuffer>();
	}
	
	private boolean findBuffer(String urlId){
		return urlBuffers.get(urlId) != null;
	}
	
}
