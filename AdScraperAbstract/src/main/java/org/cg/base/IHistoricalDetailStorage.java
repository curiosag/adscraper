package org.cg.base;

import org.cg.ads.advalues.ScrapedValues;

public interface IHistoricalDetailStorage {

	public abstract void store(ScrapedValues ad);

	public abstract int getMaxId();

	public abstract String setMaxId(int i);

	public abstract void flush();

}