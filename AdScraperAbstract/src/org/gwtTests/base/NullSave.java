package org.gwtTests.base;

public class NullSave {
	
	public static String get(String s) {
		if (s != null)
			return s;
		else
			return "";
	}
}
