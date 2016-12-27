package org.cg.ads.mock;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.adscraper.factory.IStorageFactory;
import org.cg.base.HistoryItem;
import org.cg.base.IHistoricalDetailStorage;
import org.cg.base.IHistoryStorage;
import org.cg.base.IKeyTypeValueStorage;
import org.cg.base.ISettingsStorage;

public class MockStorageFactory implements IStorageFactory {
	private static final Log LOG = LogFactory.getLog(MockStorageFactory.class);

	@Override
	public IHistoricalDetailStorage getHistoricalDetailStorage() {
		return new IHistoricalDetailStorage() {

			@Override
			public void store(ScrapedValues ad) {
				LOG.info("IHistoricalDetailStorage storing " + ad.valueOrDefault(ValueKind.url));
			}

			@Override
			public int getMaxId() {
				return 0;
			}

			@Override
			public String setMaxId(int i) {
				return null;
			}

			@Override
			public void flush() {
				LOG.info("flushing");
			}
		};
	}

	@Override
	public IHistoryStorage getHistoryStorage() {

		return new IHistoryStorage() {

			@Override
			public void store(String urlId, ScrapedValues ad) {
				LOG.info("IHistoryStorage storing " + ad.valueOrDefault(ValueKind.url));
			}

			@Override
			public List<HistoryItem> get(int count) {
				return new ArrayList<HistoryItem>();
			}
		};
	}

	@Override
	public IKeyTypeValueStorage createKeyTypeValueStorage() {
		return new IKeyTypeValueStorage(){

			@Override
			public IKeyTypeValueStorage of(String type, String key) {
				return this;
			}

			@Override
			public void clear() {
			}

			@Override
			public void clearAll() {
			}

			@Override
			public String get() {
				return null;
			}

			@Override
			public void save(String value) {
			}};
	}

	@Override
	public ISettingsStorage getSettingsStorage() {
		return new ISettingsStorage(){

			@Override
			public void set(String key, String type, String value) {
			}

			@Override
			public String get(String settingKey) throws Exception {
				return null;
			}

			@Override
			public List<SimpleImmutableEntry<String, String>> getSettingsByType(String settingType) {
				return null;
			}

			@Override
			public void del(String keyValue) {				
			}};
	}
}
