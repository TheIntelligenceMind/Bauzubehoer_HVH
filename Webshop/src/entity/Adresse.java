package entity;


/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse bildet die Tabelle Adresse in der DB ab
 * </pre>
 *  @author Tim Hermbecker
 */
public class Adresse {

	private String strasse;
	private String hausnummer;
	private String postleitzahl;
	private String ort;
	private String zusatz;
	
	public Adresse(){		
		
	}
	
	public Adresse init(String piStrasse, String piHausnummer, String piPostleitzahl, String piOrt, String piZusatz){		
		strasse = piStrasse != null ? piStrasse : "";	
		hausnummer = piHausnummer != null ? piHausnummer : "";	
		postleitzahl = piPostleitzahl != null ? piPostleitzahl : "";
		ort = piOrt != null ? piOrt : "";
		zusatz = piZusatz != null ? piZusatz : "";
		
		return this;
	}
	

	public String toString(){
		return strasse + " " + hausnummer + "|" + postleitzahl + " " + ort;
	}
	
	public String getStrasse() {
		return strasse;
	}

	public void setStrasse(String strasse) {
		this.strasse = strasse;
	}

	public String getHausnummer() {
		return hausnummer;
	}

	public void setHausnummer(String hausnummer) {
		this.hausnummer = hausnummer;
	}

	public String getPostleitzahl() {
		return postleitzahl;
	}

	public void setPostleitzahl(String postleitzahl) {
		this.postleitzahl = postleitzahl;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public String getZusatz() {
		return zusatz;
	}

	public void setZusatz(String zusatz) {
		this.zusatz = zusatz;
	}
	
}
