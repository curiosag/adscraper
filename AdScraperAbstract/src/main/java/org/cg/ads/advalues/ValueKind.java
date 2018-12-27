package org.cg.ads.advalues;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum ValueKind {

	url, detailLink, timestamp, title, description, size, rooms, phone, contact, prize, location, heating, limitationDuration,
	agent, deposit, valid;
	
	private static Map<String, ValueKind> valMap = new HashMap<>();
	static {
		for ( ValueKind v : values()) 
			valMap.put(v.name(), v);
	}
	
	public static Optional<ValueKind> getValueOf(String kind)
	{
		return Optional.ofNullable(valMap.get(kind));
	} 
}
