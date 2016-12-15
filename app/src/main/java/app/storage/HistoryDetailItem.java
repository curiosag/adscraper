package app.storage;

import java.lang.reflect.Field;
import java.util.Date;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.springframework.data.annotation.Id;

public class HistoryDetailItem {

	@Id
	public String id;

	public Date timestamp;

	public int keyId;

	public String url;

	public String location;

	public String prize;

	public String size;

	public String title;

	public String description;

	public String getId() {
		return id;
	}

	public HistoryDetailItem() {
		super();
	}

	public HistoryDetailItem(int keyId, ScrapedValues ad) {
		this(keyId, ad.valueOrDefault(ValueKind.url), ad.valueOrDefault(ValueKind.location),
				ad.valueOrDefault(ValueKind.prize), ad.valueOrDefault(ValueKind.size),
				ad.valueOrDefault(ValueKind.title), ad.valueOrDefault(ValueKind.description));
	}

	public HistoryDetailItem(int keyId, String url, String location, String prize, String size, String title,
			String description) {
		this();
		this.timestamp = new Date();
		this.keyId = keyId;
		this.url = url;
		this.location = location;
		this.prize = prize;
		this.size = size;
		this.title = title;
		this.description = description;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof HistoryDetailItem && fieldsEqual((HistoryDetailItem) o);
	}

	private boolean strEq(String s1, String s2) {
		return (s1 == null && s1 == null) || (s1 != null && s1.equals(s2)) || (s2 != null && s2.equals(s1));
	}

	private boolean fieldsEqual(HistoryDetailItem o) {
		try {
			for (Field f : HistoryDetailItem.class.getDeclaredFields()) {
				if ((f.getType().equals(String.class) && !strEq((String) f.get(this), (String) f.get(o)))
						|| (f.getType().equals(Integer.TYPE) && f.getInt(this) != f.getInt(o)))
					return false;
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}