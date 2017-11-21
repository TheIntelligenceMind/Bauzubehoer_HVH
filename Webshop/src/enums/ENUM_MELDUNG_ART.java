package enums;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Das ENUM enthält alle möglichen Meldungsarten in der Oberfläche
 * </pre>
 *  @author Tim Hermbecker
 */
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
