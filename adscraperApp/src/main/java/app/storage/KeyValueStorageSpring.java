package app.storage;

import java.util.List;

import org.cg.base.IKeyValueStorage;

public class KeyValueStorageSpring implements IKeyValueStorage {

	String key;

	KeyValueItemRepository repo;

	public KeyValueStorageSpring(KeyValueItemRepository repo) {
		this.repo = repo;
	}

	public IKeyValueStorage of(String key) {	
		this.key = key;
		return this;
	}

	public void clearAll() {
		repo.deleteAll();
	}

	public void clear() {
		KeyValueItem item = getItem();
		if (item != null)
			repo.delete(item);
	}

	private KeyValueItem getItem() {
		List<KeyValueItem> result = repo.findByKey(key);
		if (result == null || result.size() == 0)
			return null;
		
		if (result.size() > 1)			
			throw new RuntimeException("found more than one key value item for key " + key);
		
		return result.get(0);
	}

	public String get() {
		KeyValueItem item = getItem();
		if (item != null)
			return item.value;
		else
			return null;
	}

	public void save(String value) {
		KeyValueItem item = getItem();
		if (item != null)
			item.value = value;
		else
			item = new KeyValueItem(key, value);
		repo.save(item);
	}

}
