package org.cg.ads.advalues;

import java.text.DecimalFormat;
import org.gwtTests.base.Log;

import com.google.common.base.Optional;

public class InterpretedValue {

	private final ScrapedValue raw;

	private InterpretedValue() {
		raw = null;
	}

	private InterpretedValue(ScrapedValue v) {
		raw = v;
	}

	public Optional<Double> asNumber() {
		Optional<String> sVal = asString();
		if (sVal.isPresent())
			return getNumber(sVal.get());
		else
			return Optional.absent();
	}

	public double asNumberOrDefault() {
		Optional<Double> num = asNumber();
		return num.isPresent() ? num.get() : 0;
	}

	public Optional<String> asString() {
		if (raw.isPresent())
			return Optional.of(normString(raw.valueOrDefault()));
		else
			return Optional.absent();
	}

	public String asStringOrDefault() {
		return asString().or("");
	}

	public static InterpretedValue create(ScrapedValue v) {
		return new InterpretedValue(v);
	}

	public static String normString(String s) {
		return s.replace("'", "\"");
	}

	private final static DecimalFormat nf = new DecimalFormat("###.##");

	public static Optional<Double> getNumber(String scrapedValue) {
		if (scrapedValue == null)
			return Optional.absent();

		scrapedValue = scrapedValue.trim();
		if (scrapedValue.endsWith(","))
			scrapedValue = scrapedValue.substring(0, scrapedValue.length() - 1);

		try {
			return Optional.of(Double.valueOf(nf.parse(scrapedValue)
					.doubleValue()));
		} catch (Exception e) {
			Log.info("Error decoding number from string: " + scrapedValue.toString());
			return Optional.absent();
		}

	}

}
