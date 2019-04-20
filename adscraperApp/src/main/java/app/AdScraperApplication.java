package app;

import app.dispatch.Dispatch;
import app.dispatch.MailDelivery;
import app.server.HttpHeader;
import app.server.RudiHttpServer;
import app.settings.Settings;
import app.settings.SettingsRepository;
import app.settings.Term;
import app.settings.Url;
import com.google.gson.Gson;
import org.cg.common.util.Exc;
import org.cg.history.History;
import org.cg.processor.Processor;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AdScraperApplication {
    private static final Logger LOG = Logger.getLogger(AdScraperApplication.class.getSimpleName());
    private String LOCAL_SETTINGS = "settings.json";

    public static void main(String[] args) {
        new AdScraperApplication().run();
    }

    public void run() {
        try {
            settings();
            rudi();
            scan();
        } catch (Exception e) {
            LOG.severe("Adscraper died from " + e.getMessage() + "\r\n" + Exc.getStackTrace(e));
        }
    }

    private void scan() {
        boolean run = true;
        while (run) {
            if (SettingsRepository.getInstance().get().isPresent()) {
                Settings settings = SettingsRepository.getInstance().get().get();
                List<String> filter = settings.getFilter().getTerm().stream().map(Term::getVal).collect(Collectors.toList());
                settings.getUrls().getUrl().forEach(url -> new Processor().process(url.getId(), url.getVal(), filter, ad -> Dispatch.instance().deliver(ad)));
            }
            try {
                if (Thread.interrupted()) {
                    return;
                }
                Thread.sleep(1000 * 120);
            } catch (InterruptedException e) {
                run = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    private void settings() {
        if (LOCAL_SETTINGS != null) {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(LOCAL_SETTINGS);
            SettingsRepository.getInstance().set(in);
        }
    }

    private void rudi() {
        RudiHttpServer rudi = new RudiHttpServer(8080);

        rudi.setHdl(this::hdl);

        rudi.setPut(in -> {
            SettingsRepository.getInstance().set(in);
            return true;
        });

        rudi.start();
    }

    private String hdl(HttpHeader header) {
        switch (header.method) {
            case "GET": {
                if (header.path.size() > 0) {
                    switch (header.path.iterator().next()) {
                        case "stat":
                            if (!SettingsRepository.getInstance().get().isPresent()) {
                                return "nope";
                            } else {
                                Optional<Integer> urlids = SettingsRepository.getInstance().get().get()
                                        .getUrls().getUrl().stream()
                                        .map(Url::getId)
                                        .map(id -> History.instance().size(id))
                                        .reduce(Integer::sum);

                                return "read: " + urlids.orElse(0).toString();
                            }
                        case "help":
                            return getHelp();
                        case "m":
                            Dispatch.instance().testmail();
                            return "sent";
                        case "settings":
                            return SettingsRepository.getInstance().get().map(s -> new Gson().toJson(s)).orElse("null");
                        default:
                            return "nooooh...";
                    }
                }

            }
            case "DELETE": {
                if (header.path.size() > 0) {
                    switch (header.path.iterator().next()) {
                        case "settings":
                            SettingsRepository.getInstance().clear();
                            return "yo";
                        case "clip":
                            History.instance().getBuffers().forEach(b -> b.clip(1));
                            return "clipped";
                        default:
                            return "eh...";
                    }
                }
            }
            default:
                return null;
        }

    }

    private String getHelp() {
        StringBuilder result = new StringBuilder();
        result.append("GET help ... help\n");
        result.append("GET stat\n");
        result.append("GET settings\n");
        result.append("GET m ... testmail\n");
        result.append("DELETE settings\n");
        result.append("DELETE clip\n");
        return result.toString();
    }


}