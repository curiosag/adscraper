package org.cg.ads;

import org.springframework.integration.annotation.Gateway;

public interface SystemEntryGateway {
    @Gateway
    public void trigger(String runId);
}
