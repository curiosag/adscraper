package org.cg.ads.advalues;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestScrapedValue {

	@Test
	public void testScrapedValue() {

		String nullString = null;
		testNullEmpty(nullString);
		testNullEmpty("");
		testNullEmpty("  ");
				
		checkNonNullEmptyCondition(ScrapedValue.create(ValueKind.agent, "a"), "a");
		
	}

	private void testNullEmpty(String v) {
		ScrapedValue sv1 = ScrapedValue.create(ValueKind.agent, v);
		checkNullEmptyCondition(sv1);
	}

	private void checkNullEmptyCondition(ScrapedValue sv1) {
		assertFalse(sv1.isPresent());
		assertTrue(sv1.valueOrDefault() != null && sv1.valueOrDefault() == "");
	}

	private void checkNonNullEmptyCondition(ScrapedValue sv1, String refValue) {
		assertTrue(sv1.isPresent());
		assertTrue(sv1.valueOrDefault() != null && sv1.valueOrDefault() == refValue);
	}

	
}
