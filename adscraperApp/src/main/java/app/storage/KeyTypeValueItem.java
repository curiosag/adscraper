package app.storage;

import javax.persistence.Id;

public class KeyTypeValueItem {
	@Id
	public String type;
	public String id;
	public String key;
	public String value;
		
	public KeyTypeValueItem(){
		super();
	}
	
	public KeyTypeValueItem(String key, String type, String value){
		super();
		this.key = key;
		this.type = type;
		this.value = value;
	}

}
