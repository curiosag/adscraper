package org.cg.hub;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.ads.filterlist.FilterList;
//import org.cg.adscraper.exprFilter.ExprParserAdScraper;
//import org.cg.adscraper.exprFilter.ResultAdScraper;
import org.cg.adscraper.factory.StorageFactory;
import org.cg.base.Check;
import org.cg.base.Const;
import org.cg.base.ISettings;
import org.cg.base.ISettingsStorage;
import org.cg.base.Log;
import org.cg.common.util.CollectionUtil;
import org.cg.util.debug.DebugUtilities;

public final class Settings implements ISettings {

	static Settings _instance = null;
	private final ISettingsStorage settingsStorage;

	public Settings() {
		settingsStorage = StorageFactory.get().getSettingsStorage();
	}

	public final static synchronized Settings instance() {
		if (_instance == null) {
			_instance = new Settings();
		}
		return _instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#get(java.lang.String)
	 */
	@Override
	public final Optional<String> get(String settingName) {
		Check.notEmpty(settingName);

		String settingValue = "";
		try {
			settingValue = settingsStorage.get(settingName);
		} catch (Exception e) { 
			Log.info("Setting '" + settingName + "' not set: " + e.getMessage());
			return Optional.empty();
		}
		return Optional.of(settingValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#getKeysByType(java.lang.String)
	 */
	@Override
	public final List<String> getKeysByType(String keyType) {
		Check.notEmpty(keyType);

		List<String> result = new LinkedList<String>();
		List<SimpleImmutableEntry<String, String>> settings = settingsStorage.getSettingsByType(keyType);
		if (settings != null)
			for (SimpleImmutableEntry<String, String> setting : settings)
				result.add(setting.getKey());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#getSettingsByType(java.lang.String)
	 */
	@Override
	public final List<AbstractMap.SimpleImmutableEntry<String, String>> getSettingsByType(String keyType) {
		Check.notEmpty(keyType);

		return settingsStorage.getSettingsByType(keyType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#set(java.lang.String)
	 */
	@Override
	public final String set(String keyValuePair) {
		if (keyValuePair == null || keyValuePair.length() == 0)
			return "empty string received";

		int idxSeparator = keyValuePair.indexOf("=");
		if (idxSeparator < 0)
			return "no valid key-value pair. Separator '=' not found";

		String keytuple = keyValuePair.substring(0, idxSeparator);
		String val = keyValuePair.substring(idxSeparator + 1);

		String[] keyparts = keytuple.split(":");
		if (keyparts.length < 2)
			return "no valid key-value pair. Key qualifier ':' not found";

		String key = keyparts[1];
		String type = keyparts[0];

		return set(key, type, val);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#set(java.lang.String, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public final String set(String key, String type, String value) {
		if (type.equals(Const.SETTINGTYPE_RULE)) {
			String errorMsg = checkRule(key, type, value);
			if (errorMsg != null)
				return errorMsg;
		}

		settingsStorage.set(key, type, value);
		return String.format("Set key %s to value %s, type %s", key, value, type);
	}

	private String checkRule(String key, String type, String rule) {
		ScrapedValues values = DebugUtilities.getTestAd();
		values.set(ValueKind.prize, "1");
		values.set(ValueKind.size, "1");
		values.set(ValueKind.statusPredicted, "1");
		values.set(ValueKind.rooms, "1");
		values.set(ValueKind.phone, "phone");
		values.set(ValueKind.description, "bb aa bb cc dd");

		/*
		 * ResultAdScraper evalResult = (new ExprParserAdScraper(values,
		 * getFilterList())).eval(rule);
		 * 
		 * if (evalResult.msg.length() > 0) return
		 * String.format("Error evaluating dispatch rule '%s' \n", key) +
		 * evalResult.msg;
		 */
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#del(java.lang.String)
	 */
	@Override
	public final void del(String settingName) {
		Check.notEmpty(settingName);

		settingsStorage.del(settingName);
	}

	public final FilterList getFilterList() {
		return populateFilters(new FilterList());
	}

	private FilterList populateFilters(FilterList filters) {
		Map<String, String> filtersDefs = createMappedSettings(Const.SETTINGTYPE_FILTER); // filter
																							// id
																							// ->
																							// filter
																							// terms
		for (Entry<String, String> f : filtersDefs.entrySet())
			filters.add(f.getKey(), CollectionUtil.toList(f.getValue().split(",")));
		return filters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#getMappedItem(java.lang.String, java.util.Map,
	 * java.lang.String)
	 */
	@Override
	public Optional<String> getMappedItem(String key, Map<String, String> map, String mapDescription) {
		Optional<String> result = Optional.ofNullable(map.get(key));
		if (!result.isPresent())
			Log.info(String.format("Settings %s: key not found: %s", key, mapDescription));

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cg.hub.ISettings#createMappedSettings(java.lang.String)
	 */
	@Override
	public Map<String, String> createMappedSettings(String keyType) {
		Map<String, String> result = new HashMap<String, String>();

		for (String s : getKeysByType(keyType))
			result.put(s, get(s).get());

		return result;
	}

}
