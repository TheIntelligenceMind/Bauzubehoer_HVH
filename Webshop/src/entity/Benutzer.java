package entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import entity.Adresse;

public class Benutzer {

	String emailadresse;
	String passwort;
	String vorname;
	String nachname;
	Adresse lieferAdresse;	
	
	public Benutzer(){		
	
	}
	
	public Benutzer init(String piEmail, String piPasswort, String piVorname, String piNachname){		
		emailadresse = piEmail != null ? piEmail : "";	
		passwort = piPasswort != null ? piPasswort : "";	
		vorname = piVorname != null ? piVorname : "";;
		nachname = piNachname != null ? piNachname : "";;
		lieferAdresse = null;
		
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
