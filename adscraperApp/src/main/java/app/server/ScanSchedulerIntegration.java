package app.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.cg.ads.SystemEntryGateway;
import org.cg.ads.integration.ScrapingBaseUrl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScanSchedulerIntegration {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	SystemEntryGateway entry;

	// use the esoteric gatewayProxyFactory
	public ScanSchedulerIntegration(SystemEntryGateway entry) {
		this.entry = entry;
	}

	@Scheduled(fixedRate = 20000)
	public String reportCurrentTime() {
		String id = "Running scan at " + dateFormat.format(new Date());
		List<ScrapingBaseUrl> items = new ArrayList<ScrapingBaseUrl>();
		if (true)
			throw new RuntimeException("TOD");
		entry.trigger(items);
		return id;
	}
}