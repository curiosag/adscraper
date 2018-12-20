package org.cg.base;

import java.text.SimpleDateFormat;

public interface Const {

    int secsDeltaViennaTime = 2 * 60 * 60;

    boolean MULTIPLE_RESULT_ELEMENTS = true;
    int DEBUG_SAMPLES = 5;

    String SETTINGTYPE_URL = "url";
    String SETTINGTYPE_MAIL = "mail";
    String SETTINGTYPE_SWITCHES = "switch";
    String SETTINGTYPE_INTERN = "intern";
    String SETTINGTYPE_FILTER = "filter";
    String SETTINGTYPE_RULE = "rule";
    String SETTINGTYPE_DISPATCHRULE = "dispatchRule";

    String SETTING_DICTIONARY_CONTENT = "dictionary";
    String SETTING_PREDICTION_THETA = "theta";
    String SETTING_PREDICTION_KILLERTERMS = "killers";

    String SETTING_SWITCH_SUSPENDED = "suspended";

    String ENTITY_KIND_APPSETTING = "AppSetting";
    String ENTITY_KIND_HISTORYITEM = "HistoryItem";
    String ENTITY_KIND_STATISTICALDATA = "StatisticalData";

    String ENTITYPROP_URLID = "urlId";
    String ENTITYPROP_DATE = "date";
    String ENTITYPROP_WEEKDAY = "weekday";
    String ENTITYPROP_HOUR = "hour";
    String ENTITYPROP_URLGRAZED = "urlGrazed";
    String ENTITYPROP_STATDATA = "statisticalData";


    String SCRAPING_EVENTTYPE_INFO = "info";

    boolean ADD_STACK_TRACE = true;
    boolean STACK_TRACE = true;

    String LOGKEY = "adScraper";
    int HTTP_TIMEOUT = 7000;
    boolean CONCURRENT = true;
    boolean SUCCESS = true;


    SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
