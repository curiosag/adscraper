package org.cg.ads.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;

public class PingBackMassageHandler extends AbstractReplyProducingMessageHandler {

	private int delay = 0;

	private static final Log LOG = LogFactory.getLog(PingBackMassageHandler.class);
	
	public PingBackMassageHandler() {
		super();
	}

	public PingBackMassageHandler(int delay) {
		this();
		this.delay = delay;
	}

	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		if (delay > 0)
			try {
				LOG.debug(String.format("delaying message by %d", delay));
				Thread.sleep(delay);
			} catch (Exception e) {
			}
		return requestMessage;
	}

}
