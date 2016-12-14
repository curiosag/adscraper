package org.cg.dispatch;

import static org.junit.Assert.*;

import org.cg.ads.advalues.ValueKind;
import org.gwtTests.base.LangId;
import org.junit.Test;

public class LanguageTests {

	@Test
	public void testTranslate() {
		String nonTranslatable = "}}that's supposed to be non translatable {{";
		assertTrue(Language.translate(nonTranslatable, LangId.german).equals(nonTranslatable));
		assertTrue(Language.translate(ValueKind.phone.name(), LangId.german).equals("Telefon"));
	}

}
