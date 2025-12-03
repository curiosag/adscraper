package org.cg.scraper;

import org.cg.ads.advalues.ScrapedValue;
import org.cg.ads.advalues.ValueKind;
import org.cg.scraping.ValueScraperRegex;
import org.junit.Assert;
import org.junit.Test;

public class ValueScraperRegexTest {

	private static final String value_ = "{\"name\":\"preference\",\"value\":\"Barrierefrei_Einbaukueche_Fahrstuhl_Garage_Teilmoebliert_Moebliert\"},\n" +
			"{\"name\":\"additionalcostdeposit\",\"value\":\"5000\"}]}},\n"+"" +
			"{\"renderSlot\":\"adition_Leaderboard\",\"contentUnitId\":3082122,\n"+
			"\"profiles\":{\"nameValuePairs\":[{\"name\":\"price\",\"value\":\"463\"},\n"+
			"{\"name\":\"region\",\"value\":\"Wien_Wien_1220_Wien_22_Bezirk_Donaustadt\"},{\"name\":\"device\",\"value\":\"desktop\"},{\"name\":\"pagetype\",\"value\":\"Anzeige\"},{\"name\":\"heading\",\"value\":\"Kleinapartments_voll_moebliert\"},{\"name\":\"address\",\"value\":\"Dueckegasse_11\"},{\"name\":\"willhabencode\",\"value\":\"173544222\"},{\"name\":\"partnerid\",\"value\":\"pk_neuesleben\"},{\"name\":\"orgaddressstreet\",\"value\":\"Troststrasse_108\"},{\"name\":\"orgaddresspostcode\",\"value\":\"1100\"},{\"name\":\"rooms\",\"value\":\"1\"},{\"name\":\"area\",\"value\":\"23\"},{\"name\":\"propertytype\",\"value\":\"Garconniere\"},{\"name\":\"orgid\",\"value\":\"29861677\"},{\"name\":\"contactcompanyname\",\"value\":\"Neues_Leben\"},{\"name\":\"contactname\",\"value\":\"Helga_Grafl\"},{\"name\":\"contactphone\",\"value\":\"01604263545\"},{\"name\":\"energyhwb\",\"value\":\"61\"},{\"name\":\"preference\",\"value\":\"Barrierefrei_Einbaukueche_Fahrstuhl_Garage_Teilmoebliert_Moebliert\"},{\"name\":\"additionalcostdeposit\",\"value\":\"5000\"}]}},{\"renderSlot\":\"adition_Skyscraper\",\"contentUnitId\":3082123,\n"+
			"\"profiles\":{\"nameValuePairs\":[{\"name\":\"price\",\"value\":\"463\"},\n"+
			"{\"name\":\"region\",\"value\":\"Wien_Wien_1220_Wien_22_Bezirk_Donaustadt\"}";


	private static final String value = "\"profiles\":{\"nameValuePairs\":[{\"name\":\"price\",\"value\":\"463\"},\n";
	//nameValuePairs":\[\{"name":"price","value":"([0-9]*)"\}

	@Test
	public void test() {
		String regex = "\"nameValuePairs\":\\[\\{\"name\":\"price\",\"value\":\"([^}.]*)\"\\},"; //willhaben prize pattern
		ValueScraperRegex s = new ValueScraperRegex(ValueKind.prize, regex, 1);

		ScrapedValue result = s.scrape(value_);
		Assert.assertTrue(result.isPresent());
		Assert.assertTrue(result.valueOrDefault().equals("463"));
	}

}
