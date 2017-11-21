package enums;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Das ENUM enthält alle verfügbaren Sichten im Benutzerpanel
 * </pre>
 *  @author Tim Hermbecker
 */
public enum ENUM_SICHT {

	WARENKORB("Warenkorb"), BESTELLUNGEN("meine Bestellungen"), KONTO("mein Konto"), ARTIKELSTAMMDATEN("Artikelstammdaten"), BENUTZERSTAMMDATEN("Benutzerstammdaten");
	
	private final String name;
	
	ENUM_SICHT(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}
}
