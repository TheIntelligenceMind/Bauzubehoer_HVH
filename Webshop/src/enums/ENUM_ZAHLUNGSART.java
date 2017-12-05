package enums;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Das ENUM enthält alle verfügbaren Zahlungsarten für den Webshop
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
	
	public static List<ENUM_ZAHLUNGSART> getZahlungsartListe(){
		List<ENUM_ZAHLUNGSART> zahlungsartliste = new ArrayList<ENUM_ZAHLUNGSART>();
		
		zahlungsartliste.add(ENUM_ZAHLUNGSART.VORKASSE);
		zahlungsartliste.add(ENUM_ZAHLUNGSART.RECHNUNG);
		
		return zahlungsartliste;
	}

}
