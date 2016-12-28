package org.cg.ads.integration;

import org.springframework.integration.handler.AbstractReplyProducingMessageHandler;
import org.springframework.messaging.Message;

public class PingBackMassageHandler extends AbstractReplyProducingMessageHandler {

	@Override
	protected Object handleRequestMessage(Message<?> requestMessage) {
		return requestMessage;
	}

}
