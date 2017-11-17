package enums;

import java.util.ArrayList;
import java.util.List;

public enum ENUM_ARTIKELKATEGORIE {

	NULL(""),
	BEKLEIDUNG("Bekleidung"),
		KOPF("Helme, Mützen, Kappen"),
		JACKEN("Jacken"),
		HOSEN("Hosen"),
		SCHUHE("Schuhe"),
		ZUBEHOER("Zubehör"),
	WERKZEUG("Werkzeug"),
		ABRISS("Abriss");
	
	
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
		list.add(JACKEN);
		list.add(HOSEN);
		list.add(SCHUHE);
		list.add(ZUBEHOER);
		list.add(ABRISS);

		return list;
	}
	
	public static List<ENUM_ARTIKELKATEGORIE> getArtikelkategorien2List(ENUM_ARTIKELKATEGORIE kategorie1){
		List<ENUM_ARTIKELKATEGORIE> list = new ArrayList<ENUM_ARTIKELKATEGORIE>();
		
		switch (kategorie1) {
			case BEKLEIDUNG:
				list.add(KOPF);
				list.add(JACKEN);
				list.add(HOSEN);
				list.add(SCHUHE);
				list.add(ZUBEHOER);
				break;
			case WERKZEUG:
				list.add(ABRISS);
				break;
			default:
				break;
		}

		return list;
	}
}
