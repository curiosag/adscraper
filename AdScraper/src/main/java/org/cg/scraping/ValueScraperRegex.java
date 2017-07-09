package org.cg.scraping;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ValueKind;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Caused by ssmertnig on 7/9/17.
 */
public class ValueScraperRegex implements ValueScraper  {

    private ValueKind kind;
    private Pattern regex;
    private int capturingGroup;
    private StringNormalizer normalizer = StringNullNormalizer.getInstance();

    public ValueScraperRegex(ValueKind kind, String regex, int capturingGroup) {
        this.kind = kind;
        this.regex = Pattern.compile(regex, Pattern.MULTILINE);
        this.capturingGroup = capturingGroup;
    }

    @Override
    public ValueKind kind() {
        return kind;
    }

    @Override
    public ValueScraper setNormalizer(StringNormalizer normalizer) {
        this.normalizer = normalizer;
        return this;
    }

    @Override
    public ScrapedValue scrape(Element e) {

        return scrape(e.toString());
    }

    public ScrapedValue scrape(String s) {

        Matcher match = regex.matcher(s);
        String result = null;
        if (match.find() && match.groupCount() >= capturingGroup)
         result = match.group(capturingGroup).toString();

        return  ScrapedValue.create(kind, normalizer.normalize(result));
    }

    public static ValueScraperRegex create(ValueKind kind, String regex, int capturingGroup){
        return new ValueScraperRegex(kind, regex, capturingGroup);
    }
}
