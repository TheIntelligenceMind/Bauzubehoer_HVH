package entity;

public class Adresse {

	private String stra�e;
	private String hausnummer;
	private String postleitzahl;
	private String ort;
	private String zusatz;
	
	public Adresse(){		
		
	}
	
	public Adresse init(String piStra�e, String piHausnummer, String piPostleitzahl, String piOrt, String piZusatz){		
		stra�e = piStra�e != null ? piStra�e : "";	
		hausnummer = piHausnummer != null ? piHausnummer : "";	
		postleitzahl = piPostleitzahl != null ? piPostleitzahl : "";
		ort = piOrt != null ? piOrt : "";
		zusatz = piZusatz != null ? piZusatz : "";
		
		return this;
	}
	

	public String toString(){
		return stra�e + " " + hausnummer + "|" + postleitzahl + " " + ort;
	}
	
	public String getStra�e() {
		return stra�e;
	}

	public void setStra�e(String stra�e) {
		this.stra�e = stra�e;
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
