package helper;

import entity.Adresse;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist f�r die Validierung von Adressdaten zust�ndig. 
 * </pre>
 *  @author Tim Hermbecker
 */
public class AdressenHelper {
	private static final AdressenHelper instance = new AdressenHelper();
	
	private AdressenHelper(){
		
	}
	
	public static AdressenHelper getInstance(){
		return instance;
	}

	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode �berpr�ft alle Attribute des Adressobjekts auf Validit�t.
	 * </pre>
	 *  @param piAdresse
	 */
	public boolean validateAdresse(Adresse piAdresse){
		Adresse adresse = piAdresse;
		
		// Hausnummer pr�fen
		if(adresse.getHausnummer() != null && !adresse.getHausnummer().isEmpty()){
			
		}
		
		// Strasse pr�fen
		if(adresse.getStrasse() != null && !adresse.getStrasse().isEmpty()){
			
		}
		
		// Ort pr�fen
		if(adresse.getOrt() != null && !adresse.getOrt().isEmpty()){
			String ortRegex = "[a-zA-Z ]*";
			
			if(!adresse.getOrt().matches(ortRegex)){
				return false;
			}
		}
			
		// Postleitzahl pr�fen
		if(adresse.getPostleitzahl() != null && !adresse.getPostleitzahl().isEmpty()){
			String plzRegex = "[0-9]{5}";
			
			if(!adresse.getPostleitzahl().matches(plzRegex)){
				return false;
			}	
		}
			
		return true;
	}
	
}
