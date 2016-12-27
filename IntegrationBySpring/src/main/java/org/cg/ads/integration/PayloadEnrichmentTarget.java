package org.cg.ads.integration;

public class PayloadEnrichmentTarget {
	public final Object base;
	private String added;
	
	
	public PayloadEnrichmentTarget(Object base) {
		super();
		this.base = base;
	}


	public String getAdded() {
		return added;
	}


	public void setAdded(String added) {
		this.added = added;
	}
	
}
