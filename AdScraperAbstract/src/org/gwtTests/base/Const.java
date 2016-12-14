package org.gwtTests.base;

import java.text.SimpleDateFormat;

public final class Const {

	public static final int secsDeltaViennaTime = 2 * 60 * 60;
	
	public static final	boolean MULTIPLE_RESULT_ELEMENTS = true;
	public static final int DEBUG_SAMPLES = 5;
	
	public static final String SETTINGTYPE_URL = "url";
	public static final String SETTINGTYPE_MAIL = "mail";
	public static final String SETTINGTYPE_SWITCHES = "switch";
	public static final String SETTINGTYPE_INTERN = "intern";
	public static final String SETTINGTYPE_FILTER = "filter";
	public static final String SETTINGTYPE_RULE = "rule";
	public static final String SETTINGTYPE_DISPATCHRULE = "dispatchRule";

	public static final String SETTING_DICTIONARY_CONTENT = "dictionary";
	public static final String SETTING_PREDICTION_THETA = "theta";
	public static final String SETTING_PREDICTION_KILLERTERMS = "killers";

	public static final String SETTING_SWITCH_SUSPENDED = "suspended";
	
	public static final String ENTITY_KIND_APPSETTING = "AppSetting";
	public static final String ENTITY_KIND_HISTORYITEM = "HistoryItem";
	public static final String ENTITY_KIND_STATISTICALDATA= "StatisticalData";
	
	public static String ENTITYPROP_URLID = "urlId";
	public static String ENTITYPROP_DATE = "date";
	public static String ENTITYPROP_WEEKDAY = "weekday";
	public static String ENTITYPROP_HOUR = "hour";
	public static String ENTITYPROP_URLGRAZED = "urlGrazed";	
	public static String ENTITYPROP_STATDATA = "statisticalData";			
	
	
	public static final String SCRAPING_EVENTTYPE_INFO = "info";
	
	public static final boolean ADD_STACK_TRACE = true;
	public static final boolean STACK_TRACE = true;
	
	public static final String LOGKEY= "adScraper";
	public static final int HTTP_TIMEOUT = 7000;
	public static final boolean CONCURRENT = true;
	public static final boolean SUCCESS = true;

	
	public static SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
}
