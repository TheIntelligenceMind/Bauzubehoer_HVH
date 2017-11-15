package enums;

public enum ENUM_ROLLENBEZEICHNUNG {

	KUNDE("Kunde"), MITARBEITER("Mitarbeiter"), ADMINISTRATOR("Administrator");
	
	private final String name;
	
	ENUM_ROLLENBEZEICHNUNG(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
