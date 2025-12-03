package org.cg.base;

import java.util.List;

import org.cg.ads.advalues.ScrapedValues;

public interface IHistoryStorage {

	void store(String urlId, ScrapedValues ad);

	List<HistoryItem> get(int count);

}