package org.cg.dispatch;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cg.ads.advalues.ValueKind;
import org.gwtTests.base.LangId;

public final class Language {

	private static Dictionary<String, String> translation = new Hashtable<String, String>();

	private Language(){
		
	}
	
	static {
		translation.put(ValueKind.title.name(), "Titel");
		translation.put(ValueKind.description.name(), "Beschreibung");
		translation.put(ValueKind.size.name(), "Größe");
		translation.put(ValueKind.rooms.name(), "Zimmer");
		translation.put(ValueKind.phone.name(), "Telefon");
		translation.put(ValueKind.contact.name(), "Kontakt");
		translation.put(ValueKind.prize.name(), "Preis");
		translation.put(ValueKind.location.name(), "Ort");
		translation.put(ValueKind.heating.name(), "Heizung");
		translation.put(ValueKind.limitationDuration.name() , "Befristung");
		translation.put(ValueKind.buildingType.name() , "Bautyp");
		translation.put(ValueKind.overallState.name() , "Zustand");
		translation.put(ValueKind.agent.name() , "Agent");
		translation.put(ValueKind.deposit.name() , "Kaution");
		translation.put(ValueKind.misc1.name() , "Zusatzinfo");
		translation.put(ValueKind.misc2.name() , "Zusatzinfo");
		translation.put(ValueKind.misc3.name() , "Zusatzinfo");
		translation.put(ValueKind.transferMoney.name() , "Ablöse");
		
	}

	public static final String translate(String source, LangId targetLanguage) {
		String translated = translation.get(source);
		return translated != null ? translated : source;
	}

}
