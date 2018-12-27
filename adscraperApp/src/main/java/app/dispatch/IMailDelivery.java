package app.dispatch;

import org.cg.ads.advalues.ScrapedValues;

public interface IMailDelivery {

	String sendMail(ScrapedValues ad, String recipient);

}