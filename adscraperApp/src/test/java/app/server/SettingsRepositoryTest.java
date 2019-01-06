package app.server;

import app.settings.SettingsRepository;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class SettingsRepositoryTest {

    @Test
    public void set() {
        InputStream data = this.getClass().getClassLoader().getResourceAsStream("settings.json");
        SettingsRepository.getInstance().set(data);
        assertTrue(SettingsRepository.getInstance().get().isPresent());
    }
}