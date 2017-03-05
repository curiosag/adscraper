package app.storage;

public class KeyTypeValueItem extends KeyValueItem {
	public String type;

	public KeyTypeValueItem(){
		super();
	}
	
	public KeyTypeValueItem(String key, String type, String value){
		super(key, value);
		this.type = type;		
	}

	
}
