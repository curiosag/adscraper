package org.cg.scraping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.*;
import org.cg.util.http.HttpResult;
import org.cg.util.http.HttpUtil;

import com.google.common.base.Predicate;

public final class SiteScraperJSoup implements SiteScraper, IMasterPageScraper, IDetailPageScraper {

	private SiteValueScrapers extractions;

	private String masterListSelector() {
		return extractions.masterListSelector();
	};

	private ValuesScraper extractorAdList() {
		return extractions.extractorAdList();
	};

	private ValuesScraper extractorAdDetails() {
		return extractions.extractorAdDetails();
	};

	SiteScraperJSoup(SiteValueScrapers extractions) {
		Check.notNull(extractions);
		this.extractions = extractions;
	}

	public boolean canHandle(String url) {
		return extractions.canHandle(url);
	}

	public final Collection<ScrapedValues> get(String url, Predicate<ScrapedValues> canProcessDetail) {
		return get(url, canProcessDetail, Integer.MAX_VALUE);
	}

	public final Collection<ScrapedValues> get(String url, Predicate<ScrapedValues> canProcessDetail,
			int numberSamples) {
		Check.notNull(url);
		Check.notNull(canProcessDetail);

		Collection<ScrapedValues> result;
		Document mainDoc = HttpUtil.getJsoupDoc(url);

		if (mainDoc != null) {
			List<ScrapedValues> masterListValues = scrapeMasterList(url, numberSamples,
					mainDoc.select(masterListSelector()));

			result = scrapeDetails(filter(masterListValues, canProcessDetail));

		} else
			result = new ArrayList<ScrapedValues>();
		return result;
	}

	private Collection<ScrapedValues> filter(List<ScrapedValues> original, Predicate<ScrapedValues> predicate) {
		List<ScrapedValues> result = new LinkedList<ScrapedValues>();
		for (ScrapedValues v : original)
			if (predicate.apply(v))
				result.add(v);

		return result;
	}

	private boolean addDetailLink(String url, Element e, ScrapedValues scrapedValues) {
		ScrapedValue detailLink = scrapedValues.get(ValueKind.detailLink);
		if (!detailLink.isPresent()) {
			Log.warning("No detail link retrieved for fragment \n" + e.html());
			return false;
		}

		scrapedValues.add(getDetailUrl(url, detailLink.valueOrDefault()));
		return true;
	}

	private List<ScrapedValues> scrapeMasterList(String url, int numberSamples, Elements elements) {
		List<ScrapedValues> result = new ArrayList<ScrapedValues>();

		for (Element e : elements) {

			if (result.size() == numberSamples)
				break;

			ScrapedValues currentValues = extractorAdList().apply(e, new ScrapedValues());

			if (addDetailLink(url, e, currentValues))
				result.add(currentValues);
			else
				continue;
		}

		return result;
	}

	private Collection<ScrapedValues> scrapeDetails(Collection<ScrapedValues> ads) {

		Collection<ScrapedValues> result = new LinkedList<ScrapedValues>();

		for (HttpResult<ScrapedValues> httpResult : HttpUtil.getDocsAsync(ads))
			if (httpResult.success()) {
				ScrapedValues scrapedValues = httpResult.input().thingWithUrl();
				result.add(extractorAdDetails().apply(httpResult.document(), scrapedValues));
			}

		return result;
	}

	@SuppressWarnings("unused")
	private Collection<ScrapedValues> scrapeWrappedDetails(Collection<ScrapedValues> ads) {

		Collection<ScrapedValues> result = new LinkedList<ScrapedValues>();
		Collection<Wrap<ScrapedValues, String>> urlsToFetch = new LinkedList<Wrap<ScrapedValues, String>>();

		for (ScrapedValues ad : ads)
			urlsToFetch.add(Wrap.of(ad, ad.url()));

		for (Wrap<Wrap<ScrapedValues, String>, Document> httpResult : HttpUtil.wrapDocAsync(urlsToFetch))
			result.add(extractorAdDetails().apply(httpResult.wrap(), httpResult.wrapped().wrapped()));

		return result;
	}

	private ScrapedValue getDetailUrl(String url, String urlSuffix) {

		if (!urlSuffix.startsWith("/"))
			urlSuffix = "/" + urlSuffix;

		return ScrapedValue.create(ValueKind.url, HttpUtil.baseUrl(url) + urlSuffix);
	}

	@Override
	public void addDetails(ScrapedValues current, String html) {
		extractorAdDetails().apply(Jsoup.parse(html, "UTF-8"), current);

	}

	@Override
	public List<ScrapedValues> getMasterList(String url, String html) {
		Document doc = Jsoup.parse(html, "UTF-8");

		List<ScrapedValues> result = new ArrayList<ScrapedValues>();

		for (Element e : doc.select(masterListSelector())){
			ScrapedValues currentValues = extractorAdList().apply(e, new ScrapedValues());
			addDetailLink(url, e, currentValues);
			result.add(currentValues);
		}
		
		return result;

	}

}
