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
import org.cg.dispatch.MailDelivery;
import org.cg.history.History;
import org.cg.hub.Settings;
import org.springframework.context.ApplicationContext;

import app.storage.SpringStorageFactory;

public class ScanRunner {
	private static final Log LOG = LogFactory.getLog(ScanRunner.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	SystemEntryGateway entry;
	ApplicationContext ctx;

	private List<ScrapingBaseUrl> items = new ArrayList<ScrapingBaseUrl>();
	
	// use the esoteric gatewayProxyFactory
	public ScanRunner(SystemEntryGateway entry, ApplicationContext applicationContext) {
		this.entry = entry;
		this.ctx = applicationContext;
	}

	public void run() {
		StorageFactory.setUp(ctx.getBean(SpringStorageFactory.class));
		Dispatch.setUp(ctx.getBean(MailDelivery.class)); // it's a prototye. a new maildelivery is needed each time
		
		if (Settings.instance().get(Const.SETTING_SWITCH_SUSPENDED).or("").length() > 0) {
			LOG.info("***suspended*** set, no scan");
			return;
		}
		
		List<String> negTerms = Arrays.asList(Settings.instance().get("neg").or("").split(","));
		  
		LOG.info("excluding ads containing any of: " + negTerms.stream().reduce("", (x, y) -> x + ", " + y));
		

		LOG.info("Running scan at " + dateFormat.format(new Date()));

		items.clear();
		Settings.instance().getSettingsByType(Const.SETTINGTYPE_URL).forEach(u -> items
				.add(new ScrapingBaseUrl(u.getKey(), u.getValue(), History.instance().size(u.getKey()) == 0, negTerms)));

		entry.trigger(items);
	}

}