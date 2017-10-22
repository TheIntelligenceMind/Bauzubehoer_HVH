package enums;

public enum MELDUNG_ART {

	HINWEISMELDUNG("hinweismeldung"), FEHLERMELDUNG("fehlermeldung");
	
	private final String name;
	
	MELDUNG_ART(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
