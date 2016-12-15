package app.storage;

import org.cg.base.IKeyTypeValueStorage;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyTypeValueStorageSpring implements IKeyTypeValueStorage {

	String type;
	String key;

	KeyTypeValueItemRepository repo;

	public KeyTypeValueStorageSpring(KeyTypeValueItemRepository repo) {
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
		KeyTypeValueItem item = getItem();
		if (item != null)
			item.value = value;
		else
			item = new KeyTypeValueItem(key, type, value);
		repo.save(item);
	}

}
