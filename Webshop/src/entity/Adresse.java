package entity;

public class Adresse {

	private String straße;
	private String hausnummer;
	private String postleitzahl;
	private String ort;
	private String zusatz;
	
	public Adresse(){		
		
	}
	
	public Adresse init(String piStraße, String piHausnummer, String piPostleitzahl, String piOrt, String piZusatz){		
		straße = piStraße != null ? piStraße : "";	
		hausnummer = piHausnummer != null ? piHausnummer : "";	
		postleitzahl = piPostleitzahl != null ? piPostleitzahl : "";
		ort = piOrt != null ? piOrt : "";
		zusatz = piZusatz != null ? piZusatz : "";
		
		return this;
	}
	

	public String toString(){
		return straße + " " + hausnummer + "|" + postleitzahl + " " + ort;
	}
	
	public String getStraße() {
		return straße;
	}

	public void setStraße(String straße) {
		this.straße = straße;
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
