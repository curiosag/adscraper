package app.storage;

import org.springframework.data.annotation.Id;

public class KeyTypeValueItem {
	@Id
	public String id;
	public String key;
	public String type;
	public String value;
	
	public KeyTypeValueItem(){
		super();
	}
	
	public KeyTypeValueItem(String key, String type, String value){
		this();
		this.key = key;
		this.type = type;
		this.value = value;
	}

	
}
