package org.cg.util.debug;

import java.util.Date;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;

public class DebugUtilities {

	public static ScrapedValues getTestAd() {
	
		ScrapedValues result = new ScrapedValues();
		result.add(ScrapedValue.create(ValueKind.title, "title"));
		result.add(ScrapedValue.create(ValueKind.prize, "1234,-"));
		result.add(ScrapedValue.create(ValueKind.timestamp, (new Date()).toString()));
		result.add(ScrapedValue.create(ValueKind.url, "http://www.nowhere.com"));
		result.add(ScrapedValue.create(ValueKind.detailLink, "http://www.nowhere.com/detail"));
		result.add(ScrapedValue.create(ValueKind.description, "niiiiiice flaht!"));
		result.add(ScrapedValue.create(ValueKind.phone, "0699-123456"));
		result.add(ScrapedValue.create(ValueKind.size, "1 m2"));
		result.add(ScrapedValue.create(ValueKind.rooms, "1024"));
		result.add(ScrapedValue.create(ValueKind.contact, "contact person"));
		result.add(ScrapedValue.create(ValueKind.location, "somewhere"));
		result.add(ScrapedValue.create(ValueKind.heating, "hot or cold"));
		result.add(ScrapedValue.create(ValueKind.limitationDuration, "befristed oder nicht"));
		result.add(ScrapedValue.create(ValueKind.agent, "agency"));
		result.add(ScrapedValue.create(ValueKind.deposit, "vieeel kaution"));
		return result;
	
	}

}
