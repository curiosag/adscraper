package org.cg.dispatch;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.gwtTests.base.Check;

public final class SendMail {

	private static String[] toReplace = { "ß", "ö", "ä", "ü", "Ö", "Ä", "Ü" };
	private static String[] replacement = { "ss", "oe", "ae", "ue", "OE", "AE",	"UE" };

	private static String normalizeString(String s) {
		Check.notNull(s);
		
		String result = s;
		for (int i = 0; i < toReplace.length; i++)
			result = result.replaceAll(toReplace[i], replacement[i]);
		return result;
	}

	public final static synchronized void send(String adminEmail, String recipient,
			String from, String subject, String content, boolean asHtml)
			throws UnsupportedEncodingException, AddressException,
			MessagingException {

		Check.notEmpty(adminEmail);
		Check.notEmpty(recipient);
		Check.notEmpty(from);
		Check.notEmpty(subject);
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(adminEmail, from));
		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
				recipient));

		msg.setSubject(normalizeString(subject));

		if (asHtml) {
			Multipart mp = new MimeMultipart();
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(normalizeString(content), "text/html");
			mp.addBodyPart(htmlPart);
			msg.setContent(mp);
		} else
			msg.setText(normalizeString(content));

		Transport.send(msg);
	}

}
