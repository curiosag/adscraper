package org.cg.scraping;

import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;

public final class SiteValueScrapersImmobilienscout24 implements SiteValueScrapers {

    public boolean canHandle(String url) {
        Check.notNull(url);
        return url.startsWith("https://www.immobilienscout24.at");
    }

    public String masterListSelector() {
        return "h2 [href~=^expose.*]";
    }

    @Override
    public boolean jsEnabled() {
        return true;
    }

    public ValuesScraper extractorAdList() {
        ValuesScraper result = new ValuesScraper();
        result.add(ValueScraperJSoup.create(ValueKind.detailLink, "a", "href"));
        return result;
    }

    public ValuesScraper extractorAdDetails() {
        ValuesScraper result = new ValuesScraper();

        result.add(ValueScraperJSoup.create(ValueKind.title, "#at-expose-title"));
        result.add(ValueScraperJSoup.create(ValueKind.size, "[data-ng-bind~=hit.area.totalArea]"));
        result.add(ValueScraperJSoup.create(ValueKind.location, "[data-ng-bind-html~=expose.localization.address.street]"));
        result.add(ValueScraperJSoup.create(ValueKind.prize, "[data-ng-bind~=primaryPrice]"));
        result.add(ValueScraperJSoup.create(ValueKind.description, "[data-ng-bind-html~=expose.description.descriptionNote]"));

        return result;
    }

}
