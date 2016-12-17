package app.storage;

import org.cg.adscraper.factory.IStorageFactory;
import org.cg.base.IHistoricalDetailStorage;
import org.cg.base.IHistoryStorage;
import org.cg.base.IKeyTypeValueStorage;
import org.cg.base.ISettingsStorage;

public class SpringStorageFactory implements IStorageFactory {

	private HistoryStorageSpring historyStorageSpring;
	private HistoricalDetailStorageSpring historicalDetailStorageSpring;
	private KeyTypeValueStorageSpring keyTypeValueStorageSpring;
	private SettingsStorageSpring settingsStorageSpring;

	public SpringStorageFactory(HistoryStorageSpring historyStorageSpring,
			HistoricalDetailStorageSpring historicalDetailStorageSpring,
			KeyTypeValueStorageSpring keyTypeValueStorageSpring, SettingsStorageSpring settingsStorageSpring) {
		this.historicalDetailStorageSpring = historicalDetailStorageSpring;
		this.historyStorageSpring = historyStorageSpring;
		this.keyTypeValueStorageSpring = keyTypeValueStorageSpring;
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
	public IKeyTypeValueStorage createKeyTypeValueStorage() {
		return keyTypeValueStorageSpring;
	}

	@Override
	public ISettingsStorage getSettingsStorage() {
		return settingsStorageSpring;
	}

}
