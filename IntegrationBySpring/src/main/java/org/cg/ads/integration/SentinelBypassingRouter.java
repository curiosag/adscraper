package org.cg.ads.integration;

import org.cg.base.Check;
import org.springframework.messaging.Message;

public class SentinelBypassingRouter {

	private String targetChannel;

	public SentinelBypassingRouter(String targetChannel) {
		super();
		this.targetChannel = targetChannel;
	}

	public String route(Message<?> message) {
		String result = targetChannel;
		if (message.getPayload().toString().equals("sentinel")) {
			Check.isTrue(message.getHeaders().containsKey("replyChannel"));
			result = message.getHeaders().get("replyChannel").toString();

		}
		System.out.println("routing httpstuffs to " + result);
		return result;
	}
}
