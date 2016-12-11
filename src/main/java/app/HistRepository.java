package app;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.data.HistoryDetailItem;

public interface HistRepository extends MongoRepository<HistoryDetailItem, String>  {
	public HistoryDetailItem findByUrl(String url);	
}