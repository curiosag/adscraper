package org.cg.ads.advalues;

import java.util.Optional;

public class InterpretedValues {
	private final ScrapedValues v;

	private InterpretedValues() {
		v = null;
	}

	private InterpretedValues(ScrapedValues v) {
		this.v = v;
	}

	public Optional<String> asString(ValueKind id) {
		if (v.has(id))
			return v.get(id).interpret().asString();
		else
			return Optional.empty();
	}

	public Optional<Double> asDouble(ValueKind id) {
		if (v.has(id))
			return v.get(id).interpret().asNumber();
		else
			return Optional.empty();
	}

	public Optional<Integer> asInteger(ValueKind id) {
		Optional<Double> dblVal = asDouble(id);
		if (dblVal.isPresent())
			return Optional.of(Integer.valueOf((int) Math.round(dblVal.get())));

		return Optional.empty();
	}

	public static InterpretedValues create(ScrapedValues v) {
		return new InterpretedValues(v);
	}
}
