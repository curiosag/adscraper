package app;

import org.cg.base.Check;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Settings {

    private static Settings _instance;
    private org.cg.ads.app.settings.Settings settings;

    public synchronized void clear() {
        settings = null;
    }

    public Map<String, String> getMailProps() {
        if (settings == null) {
            throw new IllegalStateException("settings not initialized");
        }
        Map<String, String> result = new HashMap<>();
        settings.getMailprops().getProp().forEach(p -> result.put(p.getKey(), p.getVal()));
        return result;
    }

    public void set(InputStream in) throws JAXBException {
        Check.notNull(in);
        JAXBContext jaxbContext = JAXBContext.newInstance(org.cg.ads.app.settings.Settings.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        settings = (org.cg.ads.app.settings.Settings) jaxbUnmarshaller.unmarshal(in);
    }

    public Optional<org.cg.ads.app.settings.Settings> get() {
        return Optional.ofNullable(settings);
    }

    public static Settings getInstance() {
        if (_instance == null) {
            _instance = new Settings();
        }
        return _instance;
    }
}
