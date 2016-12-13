package app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@EnableMongoRepositories
public class MongoConfiguration extends AbstractMongoConfiguration {
// http://stackoverflow.com/questions/23515295/spring-boot-and-how-to-configure-connection-details-to-mongodb
	
    @Override
    protected String getDatabaseName() {
        return "test";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
    	String envUri = System.getenv().get("OPENSHIFT_MONGODB_DB_URL");

		MongoClient client;
		if (envUri != null)
			client = new MongoClient(new MongoClientURI(envUri));
		else
			client = new MongoClient();
		return client;
    }

}