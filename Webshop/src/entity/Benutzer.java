package entity;

import entity.Adresse;

public class Benutzer {

	String emailadresse;
	String passwort;
	String vorname;
	String nachname;
	Adresse lieferAdresse;	
	
	public Benutzer(){		
	
	}
	
	public Benutzer init(String piEmail, String piPasswort){		
		emailadresse = piEmail;
		passwort = piPasswort;
		vorname = "";
		nachname = "";
		lieferAdresse = null;
		
		return this;
	}
	
	public String getPasswort(){
		return passwort;
	}
}
