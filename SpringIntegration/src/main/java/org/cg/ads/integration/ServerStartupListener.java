package org.cg.ads.integration;
 
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;

public class ServerStartupListener implements ApplicationListener<ApplicationReadyEvent>, MessageSelector {

	boolean ready = false;
	
	@Override
	public boolean accept(Message<?> arg0) {
		return ready;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent e) {
		ready = true;
		
	}
}