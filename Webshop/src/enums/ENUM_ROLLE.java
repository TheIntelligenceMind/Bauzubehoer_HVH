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
	
	@Override
	public String toString(){
		return name;
	}
}
