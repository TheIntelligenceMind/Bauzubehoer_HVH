package enums;

public enum ENUM_ARTIKELKATEGORIE {

	BEKLEIDUNG("Bekleidung"),
		KOPF("Helme, M�tzen, Kappen"),
		JACKEN("Jacken"),
		HOSEN("Hosen"),
		SCHUHE("Schuhe"),
		ZUBEHOER("Zubeh�r");
	
	
	private final String name;
	
	ENUM_ARTIKELKATEGORIE(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
