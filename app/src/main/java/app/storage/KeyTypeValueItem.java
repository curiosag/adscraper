package app.storage;

import org.springframework.data.annotation.Id;

public class KeyTypeValueItem {
	@Id
	private String id;
	private String key;
	private String type;
	private String value;
	
	public KeyTypeValueItem(){
		super();
	}
	
	public KeyTypeValueItem(String key, String type, String value){
		this();
		this.setKey(key);
		this.setType(type);
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
