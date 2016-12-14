package app.storage;

import java.lang.reflect.Field;
import java.util.Date;

import org.cg.ads.advalues.ScrapedValues;
import org.cg.ads.advalues.ValueKind;
import org.springframework.data.annotation.Id;

public class HistoryDetailItem {

	@Id
	private String id;

	private Date timestamp;

	private int keyId;

	private String url;

	private String location;

	private String prize;

	private String size;

	private String title;

	private String description;

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

	public String getUrl() {
		return url;
	}

	public String getLocation() {
		return location;
	}

	public String getPrize() {
		return prize;
	}

	public String getSize() {
		return size;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public int getKeyId() {
		return keyId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

}