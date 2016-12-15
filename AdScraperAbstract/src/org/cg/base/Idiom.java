package org.cg.base;

public final class Idiom {

	public static boolean no(boolean what) {
		return ! what;
	}
	
	public static boolean no(Object o) {
		return o == null;
	}
}
