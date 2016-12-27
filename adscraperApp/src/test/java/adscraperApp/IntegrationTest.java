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

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/app-config-test.xml")

public class IntegrationTest {
	
	@Autowired
	SystemEntryGateway entry;
	
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
	
	@Test
	public void test(){
		
	  	List<ScrapingBaseUrl> items = new ArrayList<ScrapingBaseUrl>();
	  	items.add(new ScrapingBaseUrl("1", "http://localhost"));
	  	items.add(new ScrapingBaseUrl("2", "http://sniffbazar.appspot.com/statData"));
	  	items.add(new ScrapingBaseUrl("3", "http://localhost"));
	  	items.add(new ScrapingBaseUrl("4", "http://sniffbazar.appspot.com/statData"));
	  	
    	entry.trigger(items);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}
