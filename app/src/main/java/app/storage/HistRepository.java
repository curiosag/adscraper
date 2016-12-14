package app.storage;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistRepository extends MongoRepository<HistoryDetailItem, String>  {
	public HistoryDetailItem findByUrl(String url);	
}