package app;


import org.cg.dispatch.MailDelivery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration("/app-config-test.xml")
public class MailTest {

    @Autowired
    org.cg.base.MailSessionProperties props;

    @Before
    public void setUp() {
    }

    @Test
    public void setSend() {
    	new MailDelivery(props).testMail();
    }

}