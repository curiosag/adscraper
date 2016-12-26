package org.cg.ads;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(locations = { "classpath:demo.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestProgrammaticSendAndReceive {

	@Test
	public void testProgrammaticSendAndReceive() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("demo.xml");
		try {

			MessageChannel channel = (MessageChannel) context.getBean("requestChannel");

			SubscribableChannel subscribableChannel = (SubscribableChannel) context.getBean("exit");

			Assert.assertTrue(subscribableChannel.subscribe(new MessageHandler() {

				@Override
				public void handleMessage(Message<?> arg0) throws MessagingException {
					String result = arg0.getPayload().toString();
					Assert.assertNotNull(result);
					Assert.assertEquals("confirmed", result);
				}
			}));

			// should actually return false says javadoc
			Assert.assertTrue(subscribableChannel.subscribe(new MessageHandler() {

				@Override
				public void handleMessage(Message<?> arg0) throws MessagingException {
					// only first subscriber will get notified
					throw new RuntimeException("shouldn't get raised at all");

				}
			}));

			channel.send(new GenericMessage<String>("programmatic message"));

		} finally {
			context.close();
		}
	}

	/*
	 * add advices programatically
	 * http://www.copperykeenclaws.com/configuring-spring-integration-channels-
	 * without-xml/
	 * 
	 * @Override public void
	 * postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
	 * throws BeansException { // retrieve the gateway bean definition by its
	 * name BeanDefinition gatewayBeanDefinition = beanFactory
	 * .getBeanDefinition("myOutboundgateway");
	 * 
	 * if (gatewayBeanDefinition != null) {
	 * 
	 * while (gatewayBeanDefinition.getOriginatingBeanDefinition() != null) {
	 * gatewayBeanDefinition =
	 * gatewayBeanDefinition.getOriginatingBeanDefinition(); }
	 * 
	 * PropertyValue handler =
	 * gatewayBeanDefinition.getPropertyValues().getPropertyValue("handler");
	 * 
	 * if (handler != null) {
	 * 
	 * String handlerBeanName = ((RuntimeBeanReference)
	 * handler.getValue()).getBeanName();
	 * 
	 * BeanDefinition handlerBeanDefinition =
	 * beanFactory.getBeanDefinition(handlerBeanName); while
	 * (handlerBeanDefinition.getOriginatingBeanDefinition() != null) {
	 * handlerBeanDefinition =
	 * handlerBeanDefinition.getOriginatingBeanDefinition(); }
	 * 
	 * ManagedList adviceChain = new ManagedList(); // myAdvice is the advice
	 * that extend AbstractRequestHandlerAdvice as recommended and registered as
	 * a bean adviceChain.add(new RuntimeBeanReference("myAdvice")); // this
	 * code simply sets the adviceChain, but other situations might require
	 * adding an adviser to an already existing list.
	 * handlerBeanDefinition.getPropertyValues().add("adviceChain",
	 * adviceChain); }
	 * 
	 * } }
	 */

}
