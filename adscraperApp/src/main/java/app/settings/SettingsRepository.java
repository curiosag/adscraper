package app.settings;

import com.google.gson.Gson;
import org.cg.base.Check;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SettingsRepository {

    private static SettingsRepository _instance;
    private Settings settings;

    public synchronized void clear() {
        settings = null;
    }

    public Map<String, String> getMailProps() {
        if (settings == null || settings.mailprops == null) {
            throw new IllegalStateException("settings not initialized");
        }
        Map<String, String> result = new HashMap<>();
        settings.getMailprops().getProp().forEach(p -> result.put(p.getKey(), p.getVal()));
        return result;
    }

    public void set(InputStream in) {
        Check.notNull(in);
        settings = new Gson().fromJson(new InputStreamReader(in), Settings.class);
    }

    public Optional<Settings> get() {
        return Optional.ofNullable(settings);
    }

    public static SettingsRepository getInstance() {
        if (_instance == null) {
            _instance = new SettingsRepository();
        }
        return _instance;
    }
}
