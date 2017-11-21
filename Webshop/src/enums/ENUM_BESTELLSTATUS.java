package enums;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Das ENUM enthält alle möglichen Bestellstatus
 * vom Aufgeben einer Bestellung bis zur abschliessenden Auslieferung des Produktes
 * </pre>
 *  @author Tim Hermbecker
 */
public enum ENUM_BESTELLSTATUS {

	NEU("Neu"), IN_BEARBEITUNG("In Bearbeitung"), VERSANDT("Versandt"), ABGESCHLOSSEN("Abgeschlossen");
	
	private final String name;
	
	ENUM_BESTELLSTATUS(String name){
		this.name= name;
	}
	
	@Override
	public String toString(){
		return name;
	}

}
