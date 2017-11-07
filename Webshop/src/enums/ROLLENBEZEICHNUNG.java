package enums;

public enum ROLLENBEZEICHNUNG {

	KUNDE("Kunde"), MITARBEITER("Mitarbeiter"), ADMINISTRATOR("Administrator");
	
	private final String name;
	
	ROLLENBEZEICHNUNG(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
