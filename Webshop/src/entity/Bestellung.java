package entity;

import java.util.Date;

public class Bestellung {
	private int bestellnummer;
	private Date bestelldatum;
	private String status;
	private String zahlungsart;
	private Date voraussichtlichesLieferdatum;
	private double bestellwert;
	private Benutzer benutzer; 
	
	public Bestellung(){
		
	}
	
	public Bestellung init(int piBestellnummer, Date piBestelldatum, String piStatus, 
			String piZahlungsart, Date piVoraussichtlichesLieferdatum, double piBestellwert, Benutzer piBenutzer){
		
		bestellnummer = piBestellnummer;
		bestelldatum = piBestelldatum;
		status = piStatus;
		zahlungsart = piZahlungsart;
		voraussichtlichesLieferdatum = piVoraussichtlichesLieferdatum;
		bestellwert = piBestellwert;
		benutzer = piBenutzer;
		
		return this;
	}

	public int getBestellnummer() {
		return bestellnummer;
	}

	public void setBestellnummer(int bestellnummer) {
		this.bestellnummer = bestellnummer;
	}

	public Date getBestelldatum() {
		return bestelldatum;
	}

	public void setBestelldatum(Date bestelldatum) {
		this.bestelldatum = bestelldatum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getZahlungsart() {
		return zahlungsart;
	}

	public void setZahlungsart(String zahlungsart) {
		this.zahlungsart = zahlungsart;
	}

	public Date getVoraussichtlichesLieferdatum() {
		return voraussichtlichesLieferdatum;
	}

	public void setVoraussichtlichesLieferdatum(Date voraussichtlichesLieferdatum) {
		this.voraussichtlichesLieferdatum = voraussichtlichesLieferdatum;
	}

	public double getBestellwert() {
		return bestellwert;
	}

	public void setBestellwert(double bestellwert) {
		this.bestellwert = bestellwert;
	}

	public Benutzer getBenutzer() {
		return benutzer;
	}

	public void setBenutzer(Benutzer benutzer) {
		this.benutzer = benutzer;
	}
	
}