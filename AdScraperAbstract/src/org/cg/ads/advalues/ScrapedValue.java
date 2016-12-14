package org.cg.ads.advalues;

import org.gwtTests.base.Check;

import com.google.common.base.Optional;

public final class ScrapedValue {

	private ValueKind id;
	private Optional<String> rawValue;
	private final InterpretedValue interpreted;

	private ScrapedValue(ValueKind id, Optional<String> optional) {
		Check.notNull(id);
		Check.notNull(optional);

		this.id = id;
		this.rawValue = optional;
		interpreted = InterpretedValue.create(this);
	}

	public InterpretedValue interpret() 
	{
		return interpreted;
	}
	
	public final ValueKind elementId() {
		return id;
	};

	public final String valueOrDefault() {
		return rawValue.or("");
	}

	public final boolean isPresent() {
		return rawValue.isPresent();
	}
	
	public void set(String value)
	{
		rawValue = Optional.fromNullable(value);
	}

	public static final ScrapedValue create(ValueKind id, String value) {
		Optional<String> optional;
		
		if (value == null || value.trim().length() == 0)
			optional = Optional.absent();
		else
			optional = Optional.of(value);
		
		return new ScrapedValue(id, optional);
	}

}
