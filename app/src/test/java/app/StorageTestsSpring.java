package app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.Assert;

import org.cg.adscraper.factory.*;
import org.cg.adscraper.test.storage.StorageTests;
import org.cg.base.IKeyTypeValueStorage;

@SuppressWarnings("deprecation")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/app-config-test.xml")
public class StorageTestsSpring {

	StorageTests test = new StorageTests();
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
	public void testHistoryBulkStorage(){
		test.testHistoryBulkStorage();
	}

	@Test
	public void testSettings(){
		test.testSettings();
	}
	
	@Test
	public void testSettingsByType(){
		test.testSettingsByType();
	}
	
	@Test
	public void testBigValue(){
		test.testSettingsByType();
		IKeyTypeValueStorage fat = StorageFactory.get().createKeyTypeValueStorage().of("fatUrls", "fatUrlsKey");
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10000; i++) 
			sb.append('a');
		String fatString = sb.toString();
		
		fat.save(fatString);
		Assert.assertEquals(fatString, fat.get());
	}
	
}
