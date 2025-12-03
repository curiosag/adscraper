package org.cg.scraping;

import java.util.ArrayList;
import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;
import org.jsoup.nodes.Element;

public final class ValuesScraper {

    private List<ValueScraper> valueScrapers = new ArrayList<ValueScraper>();

    public final void add(ValueScraper extractor) {
        Check.notNull(extractor);
        Check.isTrue(indexOf(extractor.kind()) < 0);

        valueScrapers.add(extractor);
    }

    private int indexOf(ValueKind kind) {
        int result = 0;
        for (ValueScraper s : valueScrapers) {

            if (s.kind() == kind)
                return result;
            result++;
        }
        return -1;
    }

    public ScrapedValues apply(Element source) {
        return apply(source, new ScrapedValues());
    }

    public ScrapedValues apply(Element source, ScrapedValues target) {
        Check.notNull(target);

        for (ValueScraper e : valueScrapers)
            target.add(e.scrape(source));

        return target;
    }

}
