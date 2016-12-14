package org.cg.adscraper.factory;

import org.gwtTests.base.IKeyTypeValueStorage;
import org.gwtTests.base.IHistoricalDetailStorage;
import org.gwtTests.base.IHistoryStorage;
import org.gwtTests.base.ISettingsStorage;

public interface IStorageFactory {
	public IHistoricalDetailStorage getHistoricalDetailStorage();
	public IHistoryStorage getHistoryStorage();
	public IKeyTypeValueStorage createKeyTypeValueStorage();
	public ISettingsStorage getSettingsStorage();
}
