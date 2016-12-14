package org.cg.util.list;

import java.util.List;

public class ListUtil {
	
	public static <T> boolean equal(List<T> v1, List<T> v2) {
		return ! v1.retainAll(v2);
	}
}
