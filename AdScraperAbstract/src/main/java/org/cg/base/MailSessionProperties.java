package org.cg.base;

import java.util.Properties;

public class MailSessionProperties {
	public final String sender, username, password, host, port, protocol, security;
	private boolean debug;
	
	public MailSessionProperties(String sender, String username, String password, String host, String port,
			String protocol, String security) {
		this.sender = sender;
		this.username = username;
		this.password = password;
		this.host = host;
		this.port = port;
		this.protocol = protocol;
		this.security = security;
		Check.isTrue(protocol.equals("smtp"));
	}

	
	
	public Properties get() {
		Properties result = new Properties();
		boolean isTls = "tls".equals(security);
		boolean isSsl = "ssl".equals(security);
		result.put("mail.smtp.auth", String.valueOf(isTls || isSsl));
		result.put("mail.smtp.starttls.enable", String.valueOf(isTls));

		if (isTls)
			result.put("mail.smtp.startssl.enable", String.valueOf(isSsl));

		if (isSsl) {
			result.put("mail.smtp.socketFactory.port", "465");
			result.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}

		result.put("mail.smtp.host", host);
		result.put("mail.smtp.port", port);
		result.put("mail.smtp.username", username);
		result.put("mail.smtp.password", password);
		return result;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
