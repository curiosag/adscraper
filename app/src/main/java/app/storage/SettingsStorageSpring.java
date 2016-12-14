package app.storage;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;

import org.gwtTests.base.ISettingsStorage;

public class SettingsStorageSpring implements ISettingsStorage {

	KeyTypeValueItemRepository repo;
	
	public SettingsStorageSpring(KeyTypeValueItemRepository repo) {
		this.repo = repo;
	}

	public void set(String key, String type, String value) {
		repo.save(new KeyTypeValueItem(key, type, value));
	}

	private KeyTypeValueItem getItem(String settingKey) throws Exception {
		List<KeyTypeValueItem> result = repo.findByKey(settingKey);
		if (result.size() == 0 || result.size() > 1)
			throw new Exception(
					String.format("%d result items found for setting %s, should be 1", result.size(), settingKey));
		return result.get(0);
	}

	
	public String get(String settingKey) throws Exception {
		return getItem(settingKey).getValue();
	}

	public List<SimpleImmutableEntry<String, String>> getSettingsByType(String settingType) {
		List<KeyTypeValueItem> items = repo.findByType(settingType);
		ArrayList<SimpleImmutableEntry<String, String>> result = new ArrayList<SimpleImmutableEntry<String, String>>();
		for (KeyTypeValueItem item : items) 
			result.add(new SimpleImmutableEntry<String, String>(item.getKey(), item.getValue()));
		
		return result;
	}

	public void del(String keyValue) {
		try {
			repo.delete(getItem(keyValue));
		} catch (Exception e) { // ok if nothing is there anyway
		}
	}

}
