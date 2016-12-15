package org.cg.util.enums;

import static org.junit.Assert.*;

import org.junit.Test;

public class EnumUtilTest {

	private enum tEnum{
		a, b, c
	}
	
	@Test
	public void test() {
		assertTrue(! EnumUtil.decode("na", tEnum.class).isPresent());
		assertEquals(EnumUtil.decode("a", tEnum.class).get(), tEnum.a);
	}

}
