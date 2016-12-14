package app.storage;

import java.util.Date;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.springframework.data.annotation.Id;

public class ScrapedValuesMongo {

	@Id
	public String id;
	public Date timestamp;
	public String url, prize, size, title, description;

	public ScrapedValuesMongo() {
		super();
	}

	public ScrapedValuesMongo(ScrapedValues ad) {
		super();
		this.timestamp = new Date();
		this.url = ad.valueOrDefault(ValueKind.url);
		this.prize = ad.valueOrDefault(ValueKind.prize);
		this.size = ad.valueOrDefault(ValueKind.size);
		this.title = ad.valueOrDefault(ValueKind.title);
		this.description = ad.valueOrDefault(ValueKind.description);
	}

}
