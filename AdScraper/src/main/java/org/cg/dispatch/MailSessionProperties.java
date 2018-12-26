package org.cg.dispatch;

import java.util.Properties;

public class MailSessionProperties {
    final String username;
    final String password;
    final String host;
    final String port;
    final String security;
    boolean debug;

    private MailSessionProperties(String username, String password, String host, String port,
                                  String security) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.security = security;
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

    public static MailSessionProperties gmail() {
        return new MailSessionProperties("curiosa.globunznik@gmail.com",
                "pfrzniklala", "smtp.gmail.com", "587", "tls");
    }
}
