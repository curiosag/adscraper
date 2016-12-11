package app;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import app.HistRepository;
import app.data.UtilTest;



@RunWith(SpringRunner.class)
@SpringBootTest
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