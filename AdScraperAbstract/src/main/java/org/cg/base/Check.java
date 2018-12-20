package org.cg.base;

import com.google.common.base.Preconditions;

public class Check {

	public static void notNull(Object o) {
		Preconditions.checkNotNull(o);
	}

	public static void notEmpty(String s) {
		notNull(s);

		Preconditions.checkState(!s.isEmpty());
	}

	public static void isTrue(boolean b) {
		Preconditions.checkState(b);
	}

	public static void isFalse(boolean b) {
		isTrue(!b);
	}

}
