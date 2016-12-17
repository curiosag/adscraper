package org.cg.dispatch;

import java.util.List;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;
import org.cg.base.Const;
import org.cg.base.LangId;
import org.cg.base.Log;
import org.cg.base.MailSessionProperties;
import org.cg.hub.Settings;
import org.cg.util.debug.DebugUtilities;
import org.cg.util.http.HttpUtil;

import com.google.common.collect.Lists;


public final class MailDelivery {

	private final boolean asHtml = true;
	private final String sender;
	private final SendMail sendMail;
	
	public MailDelivery(MailSessionProperties properties) {
		sendMail = new SendMail(properties);
		this.sender = properties.sender;
	}
	
	private String bodyMailFormatted(ScrapedValues ad) {
		Check.notNull(ad);

		List<ValueKind> mandatoyElements = Lists.newArrayList(ValueKind.title, ValueKind.prize, ValueKind.size,
				ValueKind.rooms, ValueKind.phone, ValueKind.contact, ValueKind.description);

		StringBuilder s = new StringBuilder();
		s.append("<table>");

		s.append("<a href = " + ad.valueOrDefault(ValueKind.url) + ">" + ad.valueOrDefault(ValueKind.url) + " </a><br>");

		for (ScrapedValue v : ad.get())
			if (mandatoyElements.indexOf(v.elementId()) > -1)
				s.append(String.format("<b>%s:</b>%s<br>", Language.translate(v.elementId().name(), LangId.german),
						ad.valueOrDefault(v.elementId())));

		s.append("</table>");
		s.append("<br><br>");

		for (ScrapedValue v : ad.get())
			if (mandatoyElements.indexOf(v.elementId()) == -1)
				s.append(String.format("<b>%s:</b>%s<br>", Language.translate(v.elementId().name(), LangId.german),
						ad.valueOrDefault(v.elementId())));

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

	public final String testMail() {
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

	public final void sendMail(ScrapedValues ad) {
		Check.notNull(ad);

		List<String> mailIds = Settings.instance().getKeysByType(Const.SETTINGTYPE_MAIL);

		if (mailIds.size() == 0)
			Log.info("no mail recipients defined");

		for (String mailId : mailIds)
			sendMail(ad, Settings.instance().get(mailId).get());

	}

	public String sendMail(ScrapedValues ad, String mailRecipient) {

		String from = HttpUtil.baseUrl(ad.get(ValueKind.url).valueOrDefault()).replace("http://www.", "");
		String result = null;
		try {
			Log.info("mail to " + mailRecipient + "ad von " + from + " " + headerFormatted(ad));
			sendMail.send(sender, mailRecipient, "ad von " + from, headerFormatted(ad), bodyMailFormatted(ad), asHtml);
		} catch (Exception e) {
			result = e.getMessage() + "\n" + result;
			Log.logException(e, !Const.ADD_STACK_TRACE);
		}
		return result;
	}

}
