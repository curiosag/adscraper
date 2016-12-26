package app.server;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.cg.hub.Scraper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScanSchedulerClassic {

    private static final Logger log = LoggerFactory.getLogger(ScanSchedulerClassic.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //@Scheduled(fixedRate = 120000)
    public void reportCurrentTime() {
    	try {
    		new Scraper().execute();
    		log.info("Running scan at {}", dateFormat.format(new Date()));
		} catch (Exception e) {
			log.debug("Exception running scan", e);
		}
        
    }
}