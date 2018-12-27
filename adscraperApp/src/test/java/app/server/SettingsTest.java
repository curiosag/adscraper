package app.server;

import app.Settings;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class SettingsTest {

    @Test
    public void set() {
        InputStream data = this.getClass().getClassLoader().getResourceAsStream("settings.xml");
        try {
            Settings.getInstance().set(data);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Settings.getInstance().get().isPresent());
    }
}