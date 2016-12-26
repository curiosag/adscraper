package app.storage;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryDetailItemRepository extends MongoRepository<HistoryDetailItem, String>  {
}