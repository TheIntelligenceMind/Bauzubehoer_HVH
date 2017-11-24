package entity;


/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse wird zum Speichern 
 * von Kontaktformular-Nachrichten verwendet
 * </pre>
 *  @author Tim Hermbecker
 */
public class Nachricht {

	private String absenderAdresse;
	private String betreff;
	private String inhalt;
	
	public Nachricht(){		
		
	}
	
	public Nachricht init(String piAbsenderAdresse, String piBetreff, String piInhalt){		
		absenderAdresse  = piAbsenderAdresse != null ? piAbsenderAdresse : "";	
		betreff = piBetreff != null ? piBetreff : "";	
		inhalt = piInhalt != null ? piInhalt : "";
		
		return this;
	}

	public String getBetreff() {
		return betreff;
	}

	public void setBetreff(String betreff) {
		this.betreff = betreff;
	}

	public String getAbsenderAdresse() {
		return absenderAdresse;
	}

	public void setAbsenderAdresse(String absenderAdresse) {
		this.absenderAdresse = absenderAdresse;
	}

	public String getInhalt() {
		return inhalt;
	}

	public void setInhalt(String inhalt) {
		this.inhalt = inhalt;
	}
	

	
	
}
