package app.server;

import org.cg.dispatch.IMailDelivery;

public abstract class MailDeliveryFactory {
	
	/**
	 * by factory method injection
	 * http://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch04s03.html#beans-factory-method-injection
	 * @return
	 */
	abstract IMailDelivery createMailDelivery();
	
}
