package enums;

public enum ENUM_MELDUNG_ART {

	HINWEISMELDUNG("hinweismeldung"), FEHLERMELDUNG("fehlermeldung");
	
	private final String name;
	
	ENUM_MELDUNG_ART(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
