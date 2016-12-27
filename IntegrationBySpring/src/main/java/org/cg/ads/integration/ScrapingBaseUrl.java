package org.cg.ads.integration;

public class ScrapingBaseUrl {
	public final String id;
	public final String url;
	
	public ScrapingBaseUrl(String id, String url) {
		super();
		this.url = url;
		this.id = id;
	}

	@Override
	public String toString(){
		return String.format("%s %s", id, url);
	}
	
}
