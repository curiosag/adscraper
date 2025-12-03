package org.cg.base;

public class Wrap<T, V> {

	final T wrapped;
	final V wrap;

	private Wrap(T wrapped, V wrap) {
		this.wrapped = wrapped;
		this.wrap = wrap;
	}

	public static <T, V> Wrap<T, V> of(T wrapped, V wrap) {
		return new Wrap<T, V>(wrapped, wrap);
	}

	public T wrapped() {
		return wrapped;
	}

	public V wrap() {
		return wrap;
	}
}
