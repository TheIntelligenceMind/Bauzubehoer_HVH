package enums;

public enum ENUM_ARTIKELKATEGORIE {

	BEKLEIDUNG("Bekleidung"),
		KOPF("Helme, Mützen, Kappen"),
		JACKEN("Jacken"),
		HOSEN("Hosen"),
		SCHUHE("Schuhe"),
		ZUBEHOER("Zubehör");
	
	
	private final String name;
	
	ENUM_ARTIKELKATEGORIE(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
