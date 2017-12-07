package helper;

import entity.Adresse;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für die Validierung von Adressdaten zuständig. 
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
	 * <h3>Beschreibung:</h3> Die Methode überprüft alle Attribute des Adressobjekts auf Validität.
	 * </pre>
	 *  @param piAdresse
	 */
	public boolean validateAdresse(Adresse piAdresse){
		Adresse adresse = piAdresse;
		
		// Hausnummer prüfen
		if(adresse.getHausnummer() != null && !adresse.getHausnummer().isEmpty()){
			
		}
		
		// Strasse prüfen
		if(adresse.getStrasse() != null && !adresse.getStrasse().isEmpty()){
			
		}
		
		// Ort prüfen
		if(adresse.getOrt() != null && !adresse.getOrt().isEmpty()){
			String ortRegex = "[a-zA-Z ]*";
			
			if(!adresse.getOrt().matches(ortRegex)){
				return false;
			}
		}
			
		// Postleitzahl prüfen
		if(adresse.getPostleitzahl() != null && !adresse.getPostleitzahl().isEmpty()){
			String plzRegex = "[0-9]{5}";
			
			if(!adresse.getPostleitzahl().matches(plzRegex)){
				return false;
			}	
		}
			
		return true;
	}
	
}
