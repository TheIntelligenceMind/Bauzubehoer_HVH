package enums;

public enum RESPONSE_STATUS {

	HINWEIS("hinweis"), FEHLER("fehler"), WARNUNG("warnung"), OK("ok");
	
	private final String name;
	
	RESPONSE_STATUS(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
