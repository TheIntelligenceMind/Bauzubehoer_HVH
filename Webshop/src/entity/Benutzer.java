package entity;

import java.sql.Date;
import java.sql.Timestamp;

import entity.Adresse;

public class Benutzer {

	private String emailadresse;
	private String passwort;
	private String vorname;
	private String nachname;
	private Adresse lieferAdresse;	
	private int bestaetigt;
	private Date registriertDatum;
	
	public Benutzer(){		
	
	}
	
	public Benutzer init(String piEmail, String piPasswort, String piVorname, String piNachname, Adresse piLieferAdresse, int piBestaetigt, Date piRegistriertDatum){		
		emailadresse = piEmail != null ? piEmail : "";
		passwort = piPasswort != null ? piPasswort : "";	
		vorname = piVorname != null ? piVorname : "";
		nachname = piNachname != null ? piNachname : "";
		lieferAdresse = piLieferAdresse;
		bestaetigt = piBestaetigt;
		registriertDatum = piRegistriertDatum != null ? piRegistriertDatum : new Date(System.currentTimeMillis());
		
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

	public int getBestaetigt() {
		return bestaetigt;
	}

	public void setBestaetigt(int bestaetigt) {
		this.bestaetigt = bestaetigt;
	}

	public Date getRegistriertDatum() {
		return registriertDatum;
	}

	public void setRegistriertDatum(Date registriertDatum) {
		this.registriertDatum = registriertDatum;
	}
	
}
