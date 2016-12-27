package org.cg.ads.integration;

public class AdDataCollector {
	public final ScrapingBaseUrl baseUrl;
	private String html;
	
	
	public AdDataCollector(ScrapingBaseUrl baseUrl) {
		super();
		this.baseUrl = baseUrl;
	}


	public String getHtml() {
		return html;
	}


	public void setHtml(String html) {
		this.html = html;
	}
	
}
