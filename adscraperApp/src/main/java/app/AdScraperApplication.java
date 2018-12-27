package app;

import app.dispatch.Dispatch;
import app.server.RudiHttpServer;
import org.cg.ads.app.settings.Term;
import org.cg.ads.app.settings.Url;
import org.cg.history.History;
import org.cg.processor.Processor;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AdScraperApplication {
    private static final Logger LOG = Logger.getLogger(AdScraperApplication.class.getSimpleName());
    private String LOCAL_SETTINGS = null;

    public static void main(String[] args) {
        new AdScraperApplication().run();
    }

    public void run() {
        settings();
        rudi();
        scan();
    }

    private void scan() {
        boolean run = true;
        while (run) {
            if (Settings.getInstance().get().isPresent()) {
                org.cg.ads.app.settings.Settings settings = Settings.getInstance().get().get();
                List<String> filter = settings.getFilter().getTerm().stream().map(Term::getVal).collect(Collectors.toList());
                settings.getUrls().getUrl().forEach(url -> new Processor().process(url.getId(), url.getVal(), filter, ad -> Dispatch.instance().deliver(ad)));
            }
            try {
                if (Thread.interrupted())
                {
                    return;
                }
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                run = false;
            }
        }
    }

    private void settings() {
        if (LOCAL_SETTINGS != null)
        {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("settings.xml");
            try {
                Settings.getInstance().set(in);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void rudi() {
        RudiHttpServer rudi = new RudiHttpServer(1781);

        rudi.setGet(() -> {
            if (!Settings.getInstance().get().isPresent()) {
                return "nope";
            } else {
                Optional<Integer> urlids = Settings.getInstance().get().get()
                        .getUrls().getUrl().stream()
                        .map(Url::getId)
                        .map(id -> History.instance().size(id))
                        .reduce(Integer::sum);

                return "read: " + urlids.orElse(0).toString();
            }
        });

        rudi.setPut(in -> {
            try {
                Settings.getInstance().set(in);
                return true;
            } catch (JAXBException e) {
                e.printStackTrace();
                return false;
            }
        });

        rudi.setDel(() -> {
            Settings.getInstance().clear();
        });

        rudi.start();
    }


}