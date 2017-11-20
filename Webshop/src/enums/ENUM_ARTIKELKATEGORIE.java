package enums;

import java.util.ArrayList;
import java.util.List;

public enum ENUM_ARTIKELKATEGORIE {

	NULL(""),
	BEKLEIDUNG("Bekleidung"),
		KOPF("Helme, M&uuml;tzen, Kappen"),
		OBERKOERPER("Jacken"),
		BEINE("Hosen"),
		FUESSE("Schuhe"),
		// SONSTIGES
	SICHERHEIT("Sicherheit"),
		ABSPERRUNG("Absperrung"),
		// SONSTIGES
	WERKZEUG("Werkzeug"),
		MASCHINEN("Maschinen"),
		HANDWERKZEUG("Handwerkzeug"),
		// SONSTIGES
		
		SONSTIGES("Sonstiges");
	
	
	private final String name;
	
	ENUM_ARTIKELKATEGORIE(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}

	public static List<ENUM_ARTIKELKATEGORIE> getArtikelkategorien1List(){
		List<ENUM_ARTIKELKATEGORIE> list = new ArrayList<ENUM_ARTIKELKATEGORIE>();
		
		list.add(BEKLEIDUNG);
		list.add(SICHERHEIT);
		list.add(WERKZEUG);
		
		return list;
	}
	
	public static String[] getArtikelkategorien1Array(){
		String[] array = {BEKLEIDUNG.toString(), WERKZEUG.toString()};

		return array;
	}
	
	public static List<ENUM_ARTIKELKATEGORIE> getArtikelkategorien2List(){
		List<ENUM_ARTIKELKATEGORIE> list = new ArrayList<ENUM_ARTIKELKATEGORIE>();

		list.add(KOPF);
		list.add(OBERKOERPER);
		list.add(BEINE);
		list.add(FUESSE);
		list.add(ABSPERRUNG);
		list.add(MASCHINEN);
		list.add(HANDWERKZEUG);
		list.add(SONSTIGES);

		return list;
	}
	
	public static List<ENUM_ARTIKELKATEGORIE> getArtikelkategorien2List(ENUM_ARTIKELKATEGORIE kategorie1){
		List<ENUM_ARTIKELKATEGORIE> list = new ArrayList<ENUM_ARTIKELKATEGORIE>();
		
		switch (kategorie1) {
			case BEKLEIDUNG:
				list.add(KOPF);
				list.add(OBERKOERPER);
				list.add(BEINE);
				list.add(FUESSE);
				list.add(SONSTIGES);
				break;
			case SICHERHEIT:
				list.add(ABSPERRUNG);
				list.add(SONSTIGES);
				break;
			case WERKZEUG:
				list.add(MASCHINEN);
				list.add(HANDWERKZEUG);
				list.add(SONSTIGES);
				break;
			default:
				break;
		}
		return list;
	}
}
