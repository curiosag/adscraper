package org.cg.hub;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.base.*;
import org.cg.common.threading.Parallel;
import org.cg.common.threading.TaskHandlerExt;
import org.cg.common.util.StringUtil;
import org.cg.dispatch.Dispatch;
import org.cg.history.History;
import org.cg.scraping.SiteScraper;
import org.cg.scraping.SiteScraperFactory;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;

public final class Scraper {
	private static final String listDelimiter = ",";
	private static final String excludedTermsSubstandard = "0049-,@gmail,stunde,hobbyr,prober,gemeinschaft,woche,sommer,ferien,pendler,student,tausch,zwischen,suche,mitbewohner,vormerksch,gemeinde";

	private class ExecuteScrapingCommand {
		private Scraper scraper;
		private String urlId;
		private String url;
		private String[] excludedTerms;

		public ExecuteScrapingCommand(Scraper scraper, String urlId, String url, String[] excludedTerms) {
			this.scraper = scraper;
			this.urlId = urlId;
			this.url = url;
			this.excludedTerms = excludedTerms;
		}

		public Void execute() {

			try {
				scraper.scrape(urlId, url, excludedTerms);
			} catch (Exception e) {
				Log.logException(e, Const.ADD_STACK_TRACE);
			}
			return null;
		}

	}

	private static final boolean containsAny(String text, String[] terms) {
		for (String s : terms)
			if (text.contains(s))
				return true;
		return false;
	}

	public static final boolean validate(ScrapedValues ad, String[] excludedTerms) {

		if (!ad.get(ValueKind.prize).isPresent())
			ScrapingEventProvider.get().add(Const.SCRAPING_EVENTTYPE_INFO, "prize missing",
					ad.valueOrDefault(ValueKind.title));

		return ad.get(ValueKind.prize).isPresent() && ad.get(ValueKind.description).isPresent()
				&& !containsAny(
						(ad.valueOrDefault(ValueKind.title) + ad.valueOrDefault(ValueKind.description)).toLowerCase(),
						excludedTerms);
	}

	public final void execute() {

		if (Settings.instance().get(Const.SETTING_SWITCH_SUSPENDED).or("").length() > 0) {
			Log.info("Appsetting *suspended* set. Aborting execution.");
			return;
		}

		String[] excludedTerms = Settings.instance().get("neg").or(excludedTermsSubstandard).split(listDelimiter);
		Log.info("using excluded terms: " + Arrays.toString(excludedTerms));

		List<ExecuteScrapingCommand> siteScrapings = new ArrayList<ExecuteScrapingCommand>();

		for (SimpleImmutableEntry<String, String> setting : Settings.instance().getSettingsByType(Const.SETTINGTYPE_URL))
			siteScrapings.add(new ExecuteScrapingCommand(this, setting.getKey(), setting.getValue(), excludedTerms));

		execute(Const.CONCURRENT, siteScrapings);
	}

	private void execute(boolean concurrent, List<ExecuteScrapingCommand> commands) {
		if (concurrent)
			// @formatter:off
			TaskHandlerExt.from(new Parallel.ForEach<ExecuteScrapingCommand, Void>(commands)
					.apply(new Parallel.F<ExecuteScrapingCommand, Void>() {
						public Void apply(ExecuteScrapingCommand e) {
							try {
								return e.execute();
							} catch (Exception ex) {
								Log.severe(StringUtil.getStackTrace(ex));
								return null;
							}
						}
					})).awaitCompletion();
		// @formatter:on
		else
			for (ExecuteScrapingCommand command : commands)
				command.execute();
	}

	private Predicate<ScrapedValues> canProcessDetailPredicate(final History history, final String urlId) {
		return new Predicate<ScrapedValues>() {
			@Override
			public boolean apply(ScrapedValues scrapedValues) {
				return !history.find(urlId, scrapedValues.valueOrDefault(ValueKind.url));
			}
		};
	}

	private void scrape(String urlId, String url, String[] excludedTerms) {
		Check.notEmpty(url);

		SiteScraper scraper = getScraper(url);
		if (scraper == null)
			return;

		Log.info(String.format("scraping urlId %s url %s", urlId, url));

		History history = History.instance();
		boolean initialRun = history.size(urlId) == 0;

		Collection<ScrapedValues> newAds = scraper.get(url, canProcessDetailPredicate(history, urlId),
				Switches.numberSamples);
		List<ScrapedValues> validAds = new LinkedList<ScrapedValues>();
		Log.info(String.format("found %s new ads for %s", Integer.toString(newAds.size()), urlId));

		for (ScrapedValues ad : newAds) {
			history.add(urlId, ad);
			ad.set(ValueKind.timestamp, (new Date()).toString());
			Log.debug(ad.toString());
			if (validate(ad, excludedTerms)) {
				validAds.add(ad);
				history.addDetails(ad);
			} else
				Log.info("excluded: " + ad.valueOrDefault(ValueKind.url));
		}
		history.flush(urlId);

		notify(urlId, initialRun, validAds);
	}

	private void notify(String urlId, boolean initialRun, List<ScrapedValues> newAds) {
		if (initialRun)
			Log.info(String.format("initial execution for urlId %s, no recipients will be notified", urlId));
		else
			Dispatch.instance().deliver(newAds);
	}

	private static SiteScraper getScraper(String url) {
		Optional<SiteScraper> optExtraction = SiteScraperFactory.get(url);

		if (!optExtraction.isPresent()) {
			Log.warning("No handler found for url " + url);
			return null;
		}

		return optExtraction.get();
	}

}
