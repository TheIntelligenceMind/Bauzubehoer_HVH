package enums;

public enum DB_TABELLE {

	ARTIKEL("artikel"), BENUTZER("benutzer"), WARENKORB("warenkorb"), BESTELLUNG("bestellung"), ADRESSE("adresse"), ROLLE("rolle");
	
	private final String name;
	
	DB_TABELLE(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
