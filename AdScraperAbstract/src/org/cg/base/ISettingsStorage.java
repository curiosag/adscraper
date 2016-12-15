package org.cg.base;

import java.util.AbstractMap;
import java.util.List;

public interface ISettingsStorage {

	void set(String key, String type, String value);

	String get(String settingKey) throws Exception;

	List<AbstractMap.SimpleImmutableEntry<String, String>> getSettingsByType(String settingType);

	void del(String keyValue);

}