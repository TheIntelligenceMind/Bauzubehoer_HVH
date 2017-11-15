package enums;

public enum ENUM_ARTIKELKATEGORIE {

	BEKLEIDUNG("Bekleidung");
	
	private final String name;
	
	ENUM_ARTIKELKATEGORIE(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
