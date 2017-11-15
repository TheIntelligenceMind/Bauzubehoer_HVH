package enums;

public enum ENUM_DB_TABELLE {

	ARTIKEL("artikel"), BENUTZER("benutzer"), WARENKORB("warenkorb"), BESTELLUNG("bestellung"), BESTELLARTIKEL("bestellartikel"),
	ADRESSE("adresse"), ROLLE("rolle");
	
	private final String name;
	
	ENUM_DB_TABELLE(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
