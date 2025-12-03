package app.dispatch;

import java.io.UnsupportedEncodingException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;
import org.cg.base.Check;

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

	private final Session session;

	public SendMail(MailSessionProperties properties) {
		session = auth(properties);
	}
	
	private Session auth(final MailSessionProperties properties){
		
		Session result = Session.getInstance(properties.get(),
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.username, properties.password);
			}
		  });
		result.setDebugOut(System.out);
		return result;
	}
	
	public synchronized void send(String adminEmail, String recipient,
			String from, String subject, String content, boolean asHtml)
			throws UnsupportedEncodingException, MessagingException {

		Check.notEmpty(adminEmail);
		Check.notEmpty(recipient);
		Check.notEmpty(from);
		Check.notEmpty(subject);
		
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
