package org.gwtTests.base;

public interface IKeyTypeValueStorage {

	IKeyTypeValueStorage of(String type, String key);
	
	void clear();
	
	void clearAll();
	
	String get();
	
	void save(String value);
	
}
