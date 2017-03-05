package app.storage;

import org.cg.adscraper.factory.IStorageFactory;
import org.cg.base.IHistoricalDetailStorage;
import org.cg.base.IHistoryStorage;
import org.cg.base.IKeyTypeValueStorage;
import org.cg.base.IKeyValueStorage;
import org.cg.base.ISettingsStorage;

public abstract class SpringStorageFactory implements IStorageFactory {

	private HistoryStorageSpring historyStorageSpring;
	private HistoricalDetailStorageSpring historicalDetailStorageSpring;
	private SettingsStorageSpring settingsStorageSpring;

	public SpringStorageFactory(HistoryStorageSpring historyStorageSpring,
			HistoricalDetailStorageSpring historicalDetailStorageSpring,
			SettingsStorageSpring settingsStorageSpring) {
		this.historicalDetailStorageSpring = historicalDetailStorageSpring;
		this.historyStorageSpring = historyStorageSpring;
		this.settingsStorageSpring = settingsStorageSpring;
	}

	@Override
	public IHistoricalDetailStorage getHistoricalDetailStorage() {
		return historicalDetailStorageSpring;
	}

	@Override
	public IHistoryStorage getHistoryStorage() {
		return historyStorageSpring;
	}

	@Override
	public abstract IKeyTypeValueStorage createKeyTypeValueStorage();
	// a lookup method injection, since a new instance is needed per call, see commonConfig.xml
	// http://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch04s03.html#beans-factory-method-injection

	@Override
	public abstract IKeyValueStorage createKeyValueStorage();
	
	@Override
	public ISettingsStorage getSettingsStorage() {
		return settingsStorageSpring;
	}

}
