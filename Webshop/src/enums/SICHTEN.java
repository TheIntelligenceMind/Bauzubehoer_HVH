package enums;

public enum SICHTEN {

	WARENKORB("Warenkorb"), BESTELLUNGEN("meine Bestellungen"), KONTO("mein Konto"), ARTIKELSTAMMDATEN("Artikelstammdaten");
	
	private final String name;
	
	SICHTEN(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
