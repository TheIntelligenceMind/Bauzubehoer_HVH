package enums;

import java.util.ArrayList;
import java.util.List;

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
	
	public static List<ENUM_BESTELLSTATUS> getBestellstatusListe(){
		List<ENUM_BESTELLSTATUS> bestellstatusliste = new ArrayList<ENUM_BESTELLSTATUS>();
		
		bestellstatusliste.add(ENUM_BESTELLSTATUS.NEU);
		bestellstatusliste.add(ENUM_BESTELLSTATUS.IN_BEARBEITUNG);
		bestellstatusliste.add(ENUM_BESTELLSTATUS.VERSANDT);
		bestellstatusliste.add(ENUM_BESTELLSTATUS.ABGESCHLOSSEN);

		return bestellstatusliste;
	}

}
