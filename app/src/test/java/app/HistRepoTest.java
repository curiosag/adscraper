package app;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import app.data.UtilTest;
import app.storage.HistRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/app-config-test.xml")
public class HistRepoTest {

    @Autowired
    HistRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void setsIdOnSave() {

    	repository.save(UtilTest.createItems(5));
    	Assert.assertEquals(5, repository.count());
        
    }

}