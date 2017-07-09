package adscraperApp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import org.cg.ads.SystemEntryGateway;
import org.cg.ads.integration.ScrapingBaseUrl;
import org.cg.adscraper.factory.IStorageFactory;
import org.cg.adscraper.factory.StorageFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/app-config-test.xml")

public class IntegrationTest {
	
	@Autowired
	SystemEntryGateway entry;
	
	
	@Autowired
	IStorageFactory factory;
	
	@Before
	public void setUp() {
		StorageFactory.setUp(factory);
		StorageFactory.get().createKeyTypeValueStorage().clearAll();
	}

	@After
	public void tearDown() {
	}
	
	@Test
	public void test(){
		
	  	List<ScrapingBaseUrl> items = new ArrayList<ScrapingBaseUrl>();

//	  	items.add(new ScrapingBaseUrl("motorradlUrl",
	  	//				"http://www.bazar.at/wien-brigittenau-motorraeder-mopeds-quads-anzeigen,dir,1,cId,8,fc,125,loc,125,o,1,tp,0", false, new ArrayList<String>()));

	  	items.add(new ScrapingBaseUrl("willhaben",
				"https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote?PROPERTY_TYPE=3&areaId=900&sort=0&rows=30&PRICE_TO=500&periode=0", false, new ArrayList<String>()));

	  	
	  	
    	entry.trigger(items);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}
