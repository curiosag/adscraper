package org.cg.ads;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScanScheduler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    SystemEntryGateway entry;
    
    // use the esoteric gatewayProxyFactory
    public ScanScheduler (SystemEntryGateway entry){
    	this.entry = entry;
    }
    
    @Scheduled(fixedRate = 20000)
    public String reportCurrentTime() {
    	String id = "Running scan at " + dateFormat.format(new Date());
    	entry.trigger(id);
    	return id;
 
    }
}