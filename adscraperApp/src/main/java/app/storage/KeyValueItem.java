package app.storage;

import org.springframework.data.annotation.Id;

public class KeyValueItem {
	@Id
	public String id;
	public String key;
	public String value;
	
	public KeyValueItem(){
		super();
	}
	
	public KeyValueItem(String key, String value){
		this();
		this.key = key;
		this.value = value;
	}

	
}
