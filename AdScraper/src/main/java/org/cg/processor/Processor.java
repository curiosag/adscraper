package org.cg.processor;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;
import org.cg.base.Log;
import org.cg.history.History;
import org.cg.scraping.SiteScraper;
import org.cg.scraping.SiteScraperFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class Processor {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    Consumer<ScrapedValues> dispatcher;

    public void process(String urlId, String url, List<String> excludedTerms, Consumer<ScrapedValues> dispatcher) {
        Check.notEmpty(url);
        this.dispatcher = dispatcher;

        Optional<SiteScraper> scraper = getScraper(url);
        if (!scraper.isPresent())
            return;

        Log.info(String.format("scraping urlId %s url %s", urlId, url));

        History history = History.instance();
        boolean dispatch = history.size(urlId) > 0;

        Collection<ScrapedValues> ads = scraper.get().get(url, i -> !history.find(urlId, i.url()));
        Log.info(String.format("found %s new ads for %s", Integer.toString(ads.size()), urlId));

        ads.forEach(ad -> {
            Log.debug(ad.toString());
            history.add(urlId, ad);
            validate(ad, excludedTerms);
        });

        ads.stream()
                .filter(ad -> FALSE.equals(ad.get(ValueKind.valid).valueOrDefault()))
                .forEach(ad -> Log.info("excluded: " + ad.valueOrDefault(ValueKind.url)));

        List<ScrapedValues> valid = ads.stream()
                .filter(ad -> TRUE.equals(ad.get(ValueKind.valid).valueOrDefault()))
                .collect(Collectors.toList());

        dispatch(urlId, dispatch, valid);
    }

    private void dispatch(String urlId, boolean dispatch, List<ScrapedValues> valid) {
        if (dispatch) {
            valid.forEach(dispatcher::accept);
        } else {
            Log.info(String.format("initial execution for urlId %s, no recipients will be notified", urlId));
        }
    }

    private static Optional<SiteScraper> getScraper(String url) {
        Optional<SiteScraper> result = SiteScraperFactory.get(url);

        if (!result.isPresent()) {
            Log.warning("No handler found for url " + url);
        }

        return result;
    }

    private static boolean containsAny(String text, List<String> terms) {
        for (String s : terms)
            if (text.contains(s))
                return true;
        return false;
    }

    private static void validate(ScrapedValues ad, List<String> excludedTerms) {
        ad.set(ValueKind.valid,
                ad.get(ValueKind.prize).isPresent() && ad.get(ValueKind.description).isPresent()
                        && !containsAny((ad.valueOrDefault(ValueKind.title) + ad.valueOrDefault(ValueKind.description)).toLowerCase(),
                        excludedTerms) ? TRUE : FALSE
        );

    }

}
