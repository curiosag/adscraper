package org.cg.adscraper.factory;

import org.cg.common.check.Check;

public class StorageFactory {

	private static IStorageFactory dep;

	public static void setUp(IStorageFactory dependency) {
		dep = dependency;
	}

	public static IStorageFactory get() {
		Check.notNull(dep);
		return dep;
	}
	
	public static boolean isSet(){
		return dep != null;
	}
}
