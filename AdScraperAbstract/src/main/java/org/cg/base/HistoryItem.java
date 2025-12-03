package org.cg.base;

import java.util.Date;

public class HistoryItem {

	public final String urlGrazed;
	public final Date date;
	public final String urlId;
	
	public HistoryItem(String urlGrazed, Date date, String urlId) {
		this.urlGrazed = urlGrazed;
		this.date = date;
		this.urlId = urlId;
	}
	
}
