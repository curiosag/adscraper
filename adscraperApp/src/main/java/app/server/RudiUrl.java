package app.server;

import java.util.Optional;

public class RudiUrl {
    private final String path;
    private final String params;

    public RudiUrl(String path, String params) {
        this.path = path;
        this.params = params;
    }

    public String getPath() {
        return path;
    }

    public Optional<String> getParams() {
        return Optional.ofNullable(params);
    }
}
