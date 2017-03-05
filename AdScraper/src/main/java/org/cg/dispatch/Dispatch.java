package org.cg.dispatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.cg.ads.filterlist.FilterList;
import org.cg.adscraper.exprFilter.ExprParserAdScraper;
import org.cg.adscraper.exprFilter.ResultAdScraper;
import org.cg.base.Check;
import org.cg.base.Const;
import org.cg.base.Log;
import org.cg.hub.Settings;


public class Dispatch {

	private static IMailDelivery delivery = null;

	public static void setUp(IMailDelivery d) {
		delivery = d;
	}

	private IMailDelivery getDelivery() {
		Check.notNull(delivery);
		return delivery;
	}

	private class Target {
		public final String email;
		public final Map<String, String> rules = new HashMap<String, String>();

		public Target(String email) {
			this.email = email;
		}

		public void addRule(String id, String rule) {
			rules.put(id, rule);
		}

	}

	private final List<Target> targets = new LinkedList<Target>();
	private final FilterList filters = Settings.instance().getFilterList();
	private final String cInvalidRule = "invalid rule";

	public Dispatch() {
		targets.addAll(createTargets());
	}

	@SuppressWarnings("boxing")
	public void deliver(List<ScrapedValues> ads) {
		Log.info(String.format("about to deliver %d ads considering defined rules", ads.size()));
		for (ScrapedValues ad : ads)
			deliver(ad);
	}

	public void deliver(ScrapedValues ad) {
		for (Target target : targets)
			if (passesRules(ad, target.rules))
				getDelivery().sendMail(ad, target.email);
	}

	public boolean passesRules(ScrapedValues v, Map<String, String> rules) {
		for (Entry<String, String> r : rules.entrySet())
			if (!passesRule(v, r.getKey(), r.getValue()))
				return false;
		return true;
	}

	private boolean passesRule(ScrapedValues v, String ruleId, String rule) {
		if (rule.equals(cInvalidRule)) {
			Log.warning(String.format("invalid rule id '%s'encountered. Check dispatch setup.", ruleId));
			return false;
		}
		Log.info(String.format("about to try to evaluate rule '%s'", rule));
		ResultAdScraper evalResult = (new ExprParserAdScraper(v, filters)).eval(rule);
		Log.info(String.format("rule evaluated", ruleId));

		if (evalResult.msg.length() > 0){
			Log.warning(String.format("Error evaluating dispatch rule '%s' for %s\n", rule, getUrl(v)) + evalResult.msg);
			return true;
		}

		return evalResult.status.intValue() == 1;
	}

	private Object getUrl(ScrapedValues v) {
		return v.get(ValueKind.url).valueOrDefault();
	}

	private List<Target> createTargets() {
		List<Target> result = new LinkedList<Target>();

		Map<String, String> mailIds = Settings.instance().createMappedSettings(Const.SETTINGTYPE_MAIL); // mailid
																										// ->
																										// emailAddress
		Map<String, String> rules = Settings.instance().createMappedSettings(Const.SETTINGTYPE_RULE); // ruleId
																										// ->
																										// rule
		Map<String, String> dispatches = Settings.instance().createMappedSettings(Const.SETTINGTYPE_DISPATCHRULE); // dispatchId
																													// ->
																													// dispatch
																													// definition

		for (Entry<String, String> d : dispatches.entrySet()) {
			String[] ruleElements = d.getValue().split(";");

			if (ruleElements.length == 0)
				Log.info(String.format("Settings empty for key %s", d.getKey()));
			else {
				Optional<String> mailAddress = Settings.instance().getMappedItem(ruleElements[0], mailIds,
						Const.SETTINGTYPE_MAIL);
				if (mailAddress.isPresent()) {
					Target target = new Target(mailAddress.get());
					result.add(target);
					for (int i = 1; i < ruleElements.length; i++) {
						String ruleId = ruleElements[i];
						Optional<String> rule = Settings.instance().getMappedItem(ruleId, rules,
								Const.SETTINGTYPE_RULE);
						if (rule.isPresent())
							target.addRule(ruleId, rule.get());
						else
							target.addRule(ruleId, cInvalidRule);
					}
				}
			}
		}

		return result;
	}

	private static Dispatch instance;

	public static Dispatch instance() {
		if (instance == null)
			instance = new Dispatch();

		return instance;
	}
}
