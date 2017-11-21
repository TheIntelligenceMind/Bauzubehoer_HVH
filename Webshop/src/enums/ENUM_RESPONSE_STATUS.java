package enums;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Das ENUM enthält alle möglichen Response Status
 * </pre>
 *  @author Tim Hermbecker
 */
public enum ENUM_RESPONSE_STATUS {

	HINWEIS("hinweis"), FEHLER("fehler"), WARNUNG("warnung"), OK("ok");
	
	private final String name;
	
	ENUM_RESPONSE_STATUS(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
