package org.cg.base;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ISettings {

	Optional<String> get(String settingName);

	List<String> getKeysByType(String keyType);

	List<AbstractMap.SimpleImmutableEntry<String, String>> getSettingsByType(String keyType);

	String set(String keyValuePair);

	String set(String key, String type, String value);

	void del(String settingName);

	Optional<String> getMappedItem(String key, Map<String, String> map, String mapDescription);

	Map<String, String> createMappedSettings(String keyType);

}