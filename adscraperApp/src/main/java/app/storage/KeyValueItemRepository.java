package app.storage;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeyValueItemRepository extends MongoRepository<KeyValueItem, String>  {
	public List<KeyValueItem> findByKey(String key);
}