package db;

import entity.Adresse;
import entity.Benutzer;
import entity.Bestellung;

/**
 * <h3>Beschreibung:</h3>
 * <pre>
 * Die Klasse stellt Methoden zum Anlegen/Verändern/Löschen von Daten in der Datenbank zur Verfügung.
 * </pre>
 * 
 * @author Tim Hermbecker
 */
public class QueryManager {
	private final static QueryManager instance = new QueryManager();
	
	public static QueryManager getInstance(){
		return instance;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert zu einer EmailAdresse einen Benutzer
	 * falls kein Benutzer gefunden wird ist der Returnwert null
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @return Benutzer
	 */
	public Benutzer getBenutzerByEMailAdresse(String piEMailAdresse){
		Benutzer benutzer = null;
		
		
		return benutzer;
	}	
	
	public boolean createAdresse(Adresse piAdresse){
		
		
		
		return true;
	}
	
	public boolean modifyAdresse(Adresse piAdresse){
		
		
		
		return true;
	}
	
	public boolean deleteAdresse(int id){
		
		
		return true;
	}
	
	
	
	public boolean createBenutzer(Benutzer piBenutzer){
		
		
		
		return true;
	}
	
	public boolean modifyBenutzer(Benutzer piBenutzer){
		
		
		
		return true;
	}
	
	public boolean deleteBenutzer(int id){
		
		
		return true;
	}
	
	public boolean createBestellung(Bestellung piBestellung){
		
		
		return true;
	}
	
	
}
