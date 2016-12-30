package org.cg.ads.integration;

import java.util.List;

public class ScrapingBaseUrl {
	public final String id;
	public final String url;
	public final boolean isInitialRun;
	public final List<String> negTerms;
	
	public ScrapingBaseUrl(String id, String url, boolean isInitialRun, List<String> negTerms) {
		super();
		this.url = url;
		this.id = id;
		this.isInitialRun = isInitialRun;
		this.negTerms = negTerms;
	}

	@Override
	public String toString(){
		return String.format("%s %s", id, url);
	}
	
}
