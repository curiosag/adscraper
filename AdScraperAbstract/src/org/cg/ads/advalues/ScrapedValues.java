package org.cg.ads.advalues;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.gwtTests.base.Check;
import org.gwtTests.base.Log;

public final class ScrapedValues implements WithUrl<ScrapedValues> {

	private final Dictionary<ValueKind, ScrapedValue> values = new Hashtable<ValueKind, ScrapedValue>();
	private final InterpretedValues interpreted;
	
	public ScrapedValues() {
		interpreted = InterpretedValues.create(this);
	}

	public InterpretedValues interpret()
	{
		return interpreted;
	}
	
	public final boolean isEmpty() {
		return values.isEmpty();
	}

	public final boolean has(ValueKind id) {
		Check.notNull(id);

		return values.get(id) != null;
	}

	public final ScrapedValue get(ValueKind id) {
		Check.notNull(id);

		Check.isTrue(has(id));
		return values.get(id);
	}

	public final String valueOrDefault(ValueKind id) {
		Check.notNull(id);

		return get(id).valueOrDefault();
	}
	
	public final void add(ScrapedValue value) {
		Check.isFalse(has(value.elementId()));
		values.put(value.elementId(), value);
	}

	public final void set(ValueKind id, String value) {
		if (!has(id)) {
			Log.warning(String.format("setting unexpected ValueKind %s to %s", id.name(), value));
			add(ScrapedValue.create(id, value));
		} else
			get(id).set(value);
	}

	public final Iterable<ScrapedValue> get() {
		return java.util.Collections.list(values.elements());
	}

	private List<String> asStrings() {
		List<String> result = new ArrayList<String>();
		for (ScrapedValue v : get()) {
			String value;
			if (v.isPresent())
				value = v.valueOrDefault();
			else
				value = "<absent>";

			result.add(v.elementId().name() + ": " + value);
		}

		return result;
	}

	
	
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		for (String s : asStrings())
			sb.append(s + "\n");
		return sb.toString();
	}

	@Override
	public ScrapedValues thingWithUrl() {
		return this;
	}

	@Override
	public String url() {
		Check.isTrue(has(ValueKind.url));
		return get(ValueKind.url).valueOrDefault();
	}

	
	
}
