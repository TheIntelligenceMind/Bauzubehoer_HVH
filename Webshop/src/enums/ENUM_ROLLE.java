package enums;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Das ENUM enthält alle verfügbaren Rollen
 * </pre>
 *  @author Tim Hermbecker
 */
public enum ENUM_ROLLE {

	KUNDE("Kunde"), MITARBEITER("Mitarbeiter"), ADMINISTRATOR("Administrator");
	
	private final String name;
	
	ENUM_ROLLE(String name){
		this.name= name;
	}
	
	public static ENUM_ROLLE getRolleByName(String name){
		switch(name){
		case "Kunde":
			return ENUM_ROLLE.KUNDE;
		case "Mitarbeiter":
			return ENUM_ROLLE.MITARBEITER;
		case "Administrator":
			return ENUM_ROLLE.ADMINISTRATOR;
		default:
			return null;
		}
	}
	
	@Override
	public String toString(){
		return name;
	}
}
