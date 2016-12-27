package org.cg.ads.demo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = {"classpath:demo.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSyncErrorHandlingDemo {
    
    @Autowired
    private OrderService service;
    
    @Test
    public void testCorrectOrder() {
        String confirmation = service.sendOrder("a correct order");
        Assert.assertNotNull(confirmation);
        Assert.assertEquals("confirmed", confirmation);
    }
    
    @Test
    public void testSyncErrorHandling() {
        String confirmation = null;
        try {
            confirmation = service.sendOrder("an invalid order");
            Assert.fail("Should throw a MessageHandlingException");
        } catch (MessageHandlingException e) {
            Assert.assertNull(confirmation);
        }
    }
}
