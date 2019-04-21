package org.cg.scraping;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;
import org.cg.util.http.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SiteScraperJSoup implements SiteScraper, IMasterPageScraper, IDetailPageScraper {

    private HttpUtil httpUtil = new HttpUtil();
    private SiteValueScrapers extractions;

    private String masterListSelector() {
        return extractions.masterListSelector();
    }

    private ValuesScraper extractorAdList() {
        return extractions.extractorAdList();
    }

    private ValuesScraper extractorAdDetails() {
        return extractions.extractorAdDetails();
    }

    public SiteScraperJSoup(SiteValueScrapers extractions) {
        Check.notNull(extractions);
        this.extractions = extractions;
    }

    public boolean canHandle(String url) {
        return extractions.canHandle(url);
    }

    public final Collection<ScrapedValues> get(String url, Predicate<ScrapedValues> canProcessDetail) {
        Check.notNull(url);
        Check.notNull(canProcessDetail);

        Document mainDoc = httpUtil.getJsoupDoc(url, extractions.jsEnabled());

        if (mainDoc != null) {
            List<ScrapedValues> masterListValues = scrapeMasterList(url, mainDoc.select(masterListSelector()));
            return scrapeDetails(filter(masterListValues, canProcessDetail));
        }
        return Collections.emptyList();
    }

    private Collection<ScrapedValues> filter(List<ScrapedValues> original, Predicate<ScrapedValues> predicate) {
        return original.stream().filter(predicate).collect(Collectors.toList());
    }

    private Optional<ScrapedValues> addDetailLink(String url, ScrapedValues scrapedValues) {
        ScrapedValue detailLink = scrapedValues.get(ValueKind.detailLink);
        if (!detailLink.isPresent()) {
            return Optional.empty();
        }

        scrapedValues.add(getDetailUrl(url, detailLink.valueOrDefault()));
        return Optional.of(scrapedValues);
    }

    private List<ScrapedValues> scrapeMasterList(String url, Elements elements) {
        return elements.stream()
                .map(e -> extractorAdList().apply(e))
                .map(v -> v.set(ValueKind.timestamp, (new Date()).toString()))
                .map(v -> addDetailLink(url, v))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .skip(extractions.skip())
                .collect(Collectors.toList());
    }

    private Collection<ScrapedValues> scrapeDetails(Collection<ScrapedValues> ads) {
        return httpUtil.getDocs(ads, extractions.jsEnabled()).stream()
                .filter(httpResult -> httpResult.success() && httpResult.document() != null)
                .map(httpResult -> extractorAdDetails().apply(httpResult.document(), httpResult.input().thingWithUrl()))
                .collect(Collectors.toList());
    }

    private ScrapedValue getDetailUrl(String url, String urlSuffix) {
        return ScrapedValue.create(ValueKind.url, HttpUtil.baseUrl(url) + padSlash(urlSuffix));
    }

    @Override
    public void addDetails(ScrapedValues current, String html) {
        extractorAdDetails().apply(Jsoup.parse(html, "UTF-8"), current);
    }

    @Override
    public List<ScrapedValues> getMasterList(String url, String html) {
        return Jsoup.parse(html, "UTF-8").select(masterListSelector()).stream()
                .map(e -> extractorAdList().apply(e))
                .map(v -> addDetailLink(url, v))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
