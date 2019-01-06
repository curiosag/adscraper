package app.dispatch;

import app.settings.SettingsRepository;

import java.util.Map;
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

    public static MailSessionProperties current() {
        if (! SettingsRepository.getInstance().get().isPresent())
        {
            throw new IllegalStateException("settings not initialized");
        }
        Map<String, String> props = SettingsRepository.getInstance().getMailProps();
        return new MailSessionProperties(props.get("username"),
                props.get("pwd"), props.get("host"), props.get("port"), props.get("security"));
    }
}
