package app.dispatch;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;
import org.cg.base.Const;
import org.cg.base.Log;
import org.cg.util.debug.DebugUtilities;
import org.cg.util.http.HttpUtil;

import java.util.Arrays;
import java.util.List;

public final class MailDelivery implements IMailDelivery {

	private final String sender = "curiosa.globunznik@current.com";
	private SendMail sendMail;
	
	public MailDelivery(MailSessionProperties properties) {
		this.sendMail = new SendMail(properties);
	}
	
	
	private String bodyMailFormatted(ScrapedValues ad) {
		Check.notNull(ad);

		List<ValueKind> mandatoyElements = Arrays.asList(ValueKind.title, ValueKind.prize, ValueKind.size,
				ValueKind.rooms, ValueKind.phone, ValueKind.contact, ValueKind.description);

		StringBuilder s = new StringBuilder();
		s.append("<table>");

		s.append("<a href = " + ad.valueOrDefault(ValueKind.url) + ">" + ad.valueOrDefault(ValueKind.url) + " </a><br>");

		for (ScrapedValue v : ad.get())
			if (mandatoyElements.indexOf(v.elementId()) > -1)
				s.append(String.format("<b>%s:</b>%s<br>", v.elementId().name(), ad.valueOrDefault(v.elementId())));

		s.append("</table>");
		s.append("<br><br>");

		for (ScrapedValue v : ad.get())
			if (mandatoyElements.indexOf(v.elementId()) == -1)
				s.append(String.format("<b>%s:</b>%s<br>", v.elementId().name(), ad.valueOrDefault(v.elementId())));

		s.append("<br><br>");

		return s.toString();
	}

	private String headerFormatted(ScrapedValues ad) {
		Check.notNull(ad);

		return String.format("%s EUR/%s m2 ", ad.valueOrDefault(ValueKind.prize), ad.valueOrDefault(ValueKind.size))
				+ ad.valueOrDefault(ValueKind.title);
	}

	private String bodySmsFormatted(ScrapedValues ad) {
		Check.notNull(ad);

		return String.format("Tel:%s %s", ad.valueOrDefault(ValueKind.phone), ad.valueOrDefault(ValueKind.description));
	}

	public String testMail() {
		Log.info(String.format("testMail. sender: %s, mailRecipient: %s, from: %s", sender, sender, "vom Grausewitz"));
		sendMail(DebugUtilities.getTestAd(), sender);
		return "sent";
	}

	public final String testFormat() {
		ScrapedValues testAd = DebugUtilities.getTestAd();
		return String
				.format("<b>HEADER</b><br><br>%s<br><b><br>BODY SMS FORMATTED</b><br><br>%s<br><br><b>BODY MAIL FORMATTED</b><br>%ss",
						headerFormatted(testAd), bodySmsFormatted(testAd), bodyMailFormatted(testAd));
	}
	
	@Override
	public String sendMail(ScrapedValues ad, String mailRecipient) {

		String from = HttpUtil.baseUrl(ad.get(ValueKind.url).valueOrDefault()).replace("http://www.", "");
		String result = null;
		try {
			Log.info("mail to " + mailRecipient + ", ad von " + from + " " + headerFormatted(ad));
			sendMail.send(sender, mailRecipient, "ad von " + from, headerFormatted(ad), bodyMailFormatted(ad), true);
		} catch (Exception e) {
			result = e.getMessage() + "\n" + result;
			Log.logException(e, !Const.ADD_STACK_TRACE);
		}
		return result;
	}

}
