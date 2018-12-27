package app.dispatch;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.base.Check;
import org.cg.base.Log;

import java.util.Arrays;
import java.util.List;

public class Dispatch {

    private final IMailDelivery delivery;

    public Dispatch(IMailDelivery delivery) {
        this.delivery = delivery;
    }

    private IMailDelivery getDelivery() {
        Check.notNull(delivery);
        return delivery;
    }

    private final List<String> targets = Arrays.asList("curiosa.globunznik@current.com");

    public void deliver(List<ScrapedValues> ads) {
        Log.info(String.format("about to deliver %d ads considering defined rules", ads.size()));
        for (ScrapedValues ad : ads)
            deliver(ad);
    }

    public void deliver(ScrapedValues ad) {
        for (String target : targets)
            getDelivery().sendMail(ad, target);
    }

    private static Dispatch instance;

    public static Dispatch instance() {
        if (instance == null)
            instance = new Dispatch(new MailDelivery(MailSessionProperties.current()));

        return instance;
    }
}
