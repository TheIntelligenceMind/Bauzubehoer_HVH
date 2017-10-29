package entity;

import entity.Adresse;

public class Benutzer {

	private String emailadresse;
	private String passwort;
	private String vorname;
	private String nachname;
	private Adresse lieferAdresse;	
	
	public Benutzer(){		
	
	}
	
	public Benutzer init(String piEmail, String piPasswort, String piVorname, String piNachname, Adresse piLieferAdresse){		
		emailadresse = piEmail != null ? piEmail : "";
		passwort = piPasswort != null ? piPasswort : "";	
		vorname = piVorname != null ? piVorname : "";
		nachname = piNachname != null ? piNachname : "";
		lieferAdresse = piLieferAdresse != null ? piLieferAdresse : null;
		
		return this;
	}
	
	public String getPasswort(){
		return passwort;
	}
	
	public String getEmailadresse() {
		return emailadresse;
	}

	public void setEmailadresse(String emailadresse) {
		this.emailadresse = emailadresse;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public Adresse getLieferAdresse() {
		return lieferAdresse;
	}

	public void setLieferAdresse(Adresse lieferAdresse) {
		this.lieferAdresse = lieferAdresse;
	}
	
}
