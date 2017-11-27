package enums;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Das ENUM enth�lt alle verf�gbaren Zahlungsarten f�r den Webshop
 * </pre>
 *  @author Tim Hermbecker
 */
public enum ENUM_ZAHLUNGSART {

	RECHNUNG("Rechnung"), VORKASSE("Vorkasse");
	
	private final String name;
	
	ENUM_ZAHLUNGSART(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
