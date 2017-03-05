package org.cg.base;

public interface IKeyValueStorage {

	IKeyValueStorage of(String key);
	
	void clear();
	
	void clearAll();
	
	String get();
	
	void save(String value);
	
}
