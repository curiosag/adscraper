package org.cg.scraping;

public class StringNullNormalizer implements StringNormalizer{
	private static StringNullNormalizer instance;

	public String normalize(String value){
		return value;
	};

	public static StringNullNormalizer getInstance(){
		if (instance ==null)
			instance = new StringNullNormalizer();
		return instance;
	}
}
