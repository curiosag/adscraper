package org.cg.analytics;

import java.util.Map;

import org.cg.base.Check;

public class MapUtil {
	
	public static <T, U> void ExpandMap(Map<T, U> map, T key, U initialValue) {
		Check.notNull(map);
		Check.notNull(key);
		
		if (!map.containsKey(key))
			map.put(key, initialValue);
	}
	
}
