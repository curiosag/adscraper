package org.cg.scraping;

import org.cg.ads.advalues.ValueKind;
import org.cg.base.Check;

public final class SiteValueScrapersWillhaben implements SiteValueScrapers {

    public boolean canHandle(String url) {
        Check.notNull(url);
        return url.startsWith("https://www.willhaben.at");
    }

    public String masterListSelector() {
        return ".content-section";
    }

    @Override
    public boolean jsEnabled() {
        return false;
    }

    @Override
    public int skip(){
        return 1;
    }

    public ValuesScraper extractorAdList() {
        ValuesScraper result = new ValuesScraper();
        result.add(ValueScraperJSoup.create(ValueKind.detailLink, "a", "href"));
        result.add(ValueScraperJSoup.create(ValueKind.title, "[itemprop=name]"));
        result.add(ValueScraperJSoup.create(ValueKind.size, ".desc-left"));
        result.add(ValueScraperJSoup.create(ValueKind.location, ".address-lg"));
        return result;
    }

    public ValuesScraper extractorAdDetails() {
        ValuesScraper result = new ValuesScraper();

        result.add(ValueScraperRegex.create(ValueKind.prize, "\"price\":\"([^\".]*)\"", 1));
         result.add(ValueScraperRegex.create(ValueKind.phone, "\\{\"name\":\"contactphone\",\"value\":\"([0-9\\/-])\"\\},", 1));
        result.add(ValueScraperRegex.create(ValueKind.deposit, "\\{\"name\":\"additionalcostdeposit\",\"value\":\"€*\\s*([^}.]*)\"\\},", 1));
        result.add(ValueScraperJSoup.create(ValueKind.description, "[itemprop=description]"));

        return result;
    }

}
