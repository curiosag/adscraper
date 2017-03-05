package org.cg.adscraper.factory;

import org.cg.base.IHistoricalDetailStorage;
import org.cg.base.IHistoryStorage;
import org.cg.base.IKeyTypeValueStorage;
import org.cg.base.IKeyValueStorage;
import org.cg.base.ISettingsStorage;

public interface IStorageFactory {
	public IHistoricalDetailStorage getHistoricalDetailStorage();
	public IHistoryStorage getHistoryStorage();
	public IKeyTypeValueStorage createKeyTypeValueStorage();
	public IKeyValueStorage createKeyValueStorage();
	public ISettingsStorage getSettingsStorage();
}
