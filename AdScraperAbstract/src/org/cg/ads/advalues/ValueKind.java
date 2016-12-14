package org.cg.ads.advalues;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Optional;

public enum ValueKind {
	statusPredicted, url, detailLink, timestamp, title, description, size, rooms, phone, contact, prize, location, heating, limitationDuration, buildingType, overallState,
	agent, deposit, transferMoney, misc1, misc2, misc3;
	
	private static Map<String, ValueKind> valMap = new HashMap<String, ValueKind>(); 
	static {
		for ( ValueKind v : values()) 
			valMap.put(v.name(), v);
	}
	
	public static Optional<ValueKind> getValueOf(String kind) 
	{
		return Optional.fromNullable(valMap.get(kind));
	} 
}
