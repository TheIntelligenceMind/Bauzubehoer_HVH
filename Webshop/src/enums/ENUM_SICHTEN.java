package enums;

public enum ENUM_SICHTEN {

	WARENKORB("Warenkorb"), BESTELLUNGEN("meine Bestellungen"), KONTO("mein Konto"), ARTIKELSTAMMDATEN("Artikelstammdaten");
	
	private final String name;
	
	ENUM_SICHTEN(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
