package app.storage;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
public class MongoTestConfiguration extends MongoConfiguration {
	
    @Override
    protected String getDatabaseName() {
        return "test";
    }

}