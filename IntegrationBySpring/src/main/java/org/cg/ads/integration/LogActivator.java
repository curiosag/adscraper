package org.cg.ads.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.messaging.Message;

public class LogActivator {
	private static final Log LOG = LogFactory.getLog(LogActivator.class);

	public synchronized Message<?> logMessage(final Message<?> message) {
		
		LOG.info(String.format("message in tid: %d", Thread.currentThread().getId()));
		message.getHeaders().forEach((x, y) -> LOG.info(String.format("   h: %s %s", x, y.toString())));
		LOG.info(String.format("   p: %s", message.getPayload().toString()));
		
		return message;
	}
	
	public Message<?> logPayload(final Message<?> message) {
		LOG.info(message.getPayload().toString());
		return message;
	}


	
}