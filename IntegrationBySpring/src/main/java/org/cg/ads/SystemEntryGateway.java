package org.cg.ads;

import org.cg.ads.integration.ScrapingBaseUrl;
import org.springframework.integration.annotation.Gateway;

import java.util.List;

public interface SystemEntryGateway {
    @Gateway
    public void trigger(List<ScrapingBaseUrl> urls);
}
