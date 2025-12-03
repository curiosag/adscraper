package org.cg.ads.advalues;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestScrapedValues {

	@Test
	public void testFailGet() {

		ScrapedValues v = new ScrapedValues();

		try {
			v.get(ValueKind.agent);
		} catch (Exception e) {
			return;
		}
		fail("exception expected");
	}

	@Test
	public void testFailAddDuplicate() {
		ScrapedValues v = new ScrapedValues();

		try {
			v.add(ScrapedValue.create(ValueKind.agent, "agent"));
			v.add(ScrapedValue.create(ValueKind.agent, "agent"));
		} catch (IllegalStateException e) {
			return;
		}
		fail("exception expected for insertion of duplicate element");
	}

	@Test
	public void testValues() {
		ScrapedValues v = new ScrapedValues();

		assertTrue(v.isEmpty());

		v.add(ScrapedValue.create(ValueKind.agent, "agent"));
		v.add(ScrapedValue.create(ValueKind.prize, "prize"));
		
		assertTrue(!v.isEmpty());

		assertEquals("agent", v.get(ValueKind.agent).valueOrDefault());

	}

}
