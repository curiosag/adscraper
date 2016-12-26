package app.storage;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeyTypeValueItemRepository extends MongoRepository<KeyTypeValueItem, String>  {
	public List<KeyTypeValueItem> findByKey(String key);
	public List<KeyTypeValueItem> findByType(String type);
	public KeyTypeValueItem findByKeyAndType(String key, String type);
}