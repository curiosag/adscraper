package app.storage;

import java.util.logging.Logger;

import org.cg.base.IKeyTypeValueStorage;

public class KeyTypeValueStorageSpring implements IKeyTypeValueStorage {

	String type;
	String key;

	KeyTypeValueItemRepository repo;

	public KeyTypeValueStorageSpring(KeyTypeValueItemRepository repo) {
		Logger.getGlobal().info(String.format("creating bulk type: %s key: %s object: %s", key, type, toString()));
		this.repo = repo;
	}

	public IKeyTypeValueStorage of(String type, String key) {
		this.type = type;
		this.key = key;
		return this;
	}

	public void clearAll() {
		repo.deleteAll();
	}

	public void clear() {
		KeyTypeValueItem item = getItem();
		if (item != null)
			repo.delete(item);
	}

	private KeyTypeValueItem getItem() {
		return repo.findByKeyAndType(key, type);
	}

	public String get() {
		KeyTypeValueItem item = getItem();
		if (item != null)
			return item.value;
		else
			return null;
	}

	public void save(String value) {
		Logger.getGlobal().info(String.format("storing bulk type: %s key: %s object: %s", key, type, toString()));
		KeyTypeValueItem item = getItem();
		if (item != null)
			item.value = value;
		else
			item = new KeyTypeValueItem(key, type, value);
		repo.save(item);
	}

}
