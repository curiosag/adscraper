package adscraperApp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import app.server.ScanRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/app-config.xml")

public class ScanRunnerTest {
	
	@Autowired
	ScanRunner scanRunner;
	
	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
	
	@Test
	public void test(){
		scanRunner.run();
	}

	
}
