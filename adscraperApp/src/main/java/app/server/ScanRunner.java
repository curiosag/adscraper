package app.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cg.ads.SystemEntryGateway;
import org.cg.ads.integration.ScrapingBaseUrl;
import org.cg.adscraper.factory.StorageFactory;
import org.cg.base.Const;
import org.cg.dispatch.Dispatch;
import org.cg.history.History;
import org.cg.hub.Settings;
import app.storage.SpringStorageFactory;

public class ScanRunner {
	private static final Log LOG = LogFactory.getLog(ScanRunner.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	SystemEntryGateway entry;
	MailDeliveryFactory mailDeliveryFactory;

	private List<ScrapingBaseUrl> items = new ArrayList<ScrapingBaseUrl>();

	// use the esoteric gatewayProxyFactory
	public ScanRunner(SystemEntryGateway entry, SpringStorageFactory storageFactory,
			MailDeliveryFactory mailDeliveryFactory) {
		this.entry = entry;
		StorageFactory.setUp(storageFactory);
		this.mailDeliveryFactory = mailDeliveryFactory;
	}

	public synchronized void run() {

		Dispatch.setUp(mailDeliveryFactory.createMailDelivery());
		LOG.info("*** scaning at " + dateFormat.format(new Date()) + " ***************************************");
		if (Settings.instance().get(Const.SETTING_SWITCH_SUSPENDED).orElse("").length() > 0) {
			LOG.info("***suspended*** set, no scan");
			return;
		}

		List<String> negTerms = Arrays.asList(
				Settings.instance().get("neg").map(x -> x == null ? null : x.split(",")).orElse(new String[0]));

		LOG.info("excluding ads containing any of: " + negTerms.stream().reduce("", (x, y) -> x + ", " + y));

		items.clear();
		Settings.instance().getSettingsByType(Const.SETTINGTYPE_URL).forEach(u -> items.add(
				new ScrapingBaseUrl(u.getKey(), u.getValue(), History.instance().size(u.getKey()) == 0, negTerms)));

		LOG.info(String.format("Running scan for %d base urls", items.size()));

		entry.trigger(items);
	}

}