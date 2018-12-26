package app;

import org.cg.processor.Processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AdScraperApplication {
    private static final Logger LOG = Logger.getLogger(AdScraperApplication.class.getSimpleName());

    private static final List<String> excludedTerms = Arrays.asList(
            "stunde", "hobbyr", "prober", "gemeinschaft", "woche", "sommer", "ferien", "pendler", "student",
            "tausch", "zwischenmiete", "suche", "mitbewohner", "vormerksch", "gemeinde");


    private final Map<String, String> urls = new HashMap<>();
    {
        urls.put("willhaben1020", "https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote?&PRICE_FROM=100&keyword=1020&PRICE_TO=500&areaId=900");
        urls.put("bazar1020","http://www.bazar.at/wien-leopoldstadt-wohnungen-miete-anzeigen,dir,1,cId,14,fc,107,loc,107,ref,3,ret,6,tp,0,at,1922");
    }

    public static void main(String[] args) {
        new AdScraperApplication().run();
    }

    public void run() {
        boolean run = true;
        while(run) {
            urls.forEach((key, value) -> new Processor().process(key, value, excludedTerms));
            try {
                Thread.sleep(1000 * 120);
            } catch (InterruptedException e) {
                run = false;
            }
        }
    }

}