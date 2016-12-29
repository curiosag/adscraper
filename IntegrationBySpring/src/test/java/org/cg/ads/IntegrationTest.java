package org.cg.ads;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.integration.handler.DelayHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cg.ads.SystemEntryGateway;
import org.cg.ads.integration.LogActivator;
import org.cg.ads.integration.ScrapingBaseUrl;
import org.cg.ads.mock.MockStorageFactory;
import org.cg.adscraper.factory.StorageFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("classpath*:scanner-config.xml")

public class IntegrationTest {

	@Autowired
	SystemEntryGateway entry;


	
	@Before
	public void setUp() {
		StorageFactory.setUp(new MockStorageFactory());
		LogFactory.getFactory().setAttribute("level", "DEBUG");
		
		LogFactory.getLog(AbstractMessageHandler.class);
	}

	@After
	public void tearDown() {

	}

	 @Test
	public void test() {
		final List<ScrapingBaseUrl> items = new ArrayList<ScrapingBaseUrl>();
		// items.add(new ScrapingBaseUrl("urlErr", "http://www.nowhere.nix"));

		// IntStream.range(0, 1).forEach(x -> items.add(new
		// ScrapingBaseUrl(String.format("url%s", x),
		// "http://www.bazar.at/klagenfurt-zimmer-wgs-anzeigen,dir,1,cId,16,fc,48,loc,48,tp,0")));

		items.add(new ScrapingBaseUrl("sentinel", "sentinel"));

		// items.add(new
		// ScrapingBaseUrl("motorradlUrl","http://www.bazar.at/wien-brigittenau-motorraeder-mopeds-quads-anzeigen,dir,1,cId,8,fc,125,loc,125,o,1,tp,0"));

		entry.trigger(items);

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
