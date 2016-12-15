package org.cg.util.enums;

import java.util.HashMap;

import org.cg.common.check.Check;

import com.google.common.base.Optional;

public class EnumUtil {

	public static <T extends Enum<T>> Optional<T> decode(String s, Class<T> enumClass) {
		Check.notEmpty(s);
		Check.notNull(enumClass);
		
		HashMap<String, T> renderingTypes = new HashMap<String, T>();

		for (T t : enumClass.getEnumConstants())
			renderingTypes.put(t.name(), t);

		if (!renderingTypes.containsKey(s))
			return Optional.absent();
		else
			return Optional.of(renderingTypes.get(s));
	}
}
