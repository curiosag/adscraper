package org.cg.ads.filterlist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtTests.base.Check;

import com.google.common.base.Optional;

public class FilterList {
	
	private final Map<String, List<String>> filters = new HashMap<String, List<String>>(); 
	
	public void add(String filterName, List<String> elements)
	{
		Check.isFalse(filters.containsKey(filterName));
		filters.put(filterName, elements);
	}
	
	public Optional<List<String>> get(String filterName)
	{
		return Optional.fromNullable(filters.get(filterName));
	}
}
