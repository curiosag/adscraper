package org.cg.hub;

import org.cg.base.IScrapingEvent;

public class ScrapingEventProvider {

	private final static IScrapingEvent event = new IScrapingEvent(){

		@Override
		public void add(String type, String message, String url) {
			
		}};
	
	public static IScrapingEvent get(){
		return event;
	}
}
