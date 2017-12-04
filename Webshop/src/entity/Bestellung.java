package entity;

import java.util.Date;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse bildet die Tabelle Bestellung in der DB ab
 * </pre>
 *  @author Tim Hermbecker
 */
public class Bestellung {
	private String bestellnummer;
	private Date bestelldatum;
	private String status;
	private String zahlungsart;
	private Date voraussichtlichesLieferdatum;
	private double bestellwert;
	private double versandkosten;
	private Benutzer benutzer;
	private String geaendertBenutzer;
	
	public Bestellung(){
		
	}
	
	public Bestellung init(String piBestellnummer, Date piBestelldatum, String piStatus, 
			String piZahlungsart, Date piVoraussichtlichesLieferdatum, double piBestellwert, double piVersandkosten, Benutzer piBenutzer){
		
		bestellnummer = piBestellnummer;
		bestelldatum = piBestelldatum;
		status = piStatus;
		zahlungsart = piZahlungsart;
		voraussichtlichesLieferdatum = piVoraussichtlichesLieferdatum;
		bestellwert = piBestellwert;
		setVersandkosten(piVersandkosten);
		benutzer = piBenutzer;
		geaendertBenutzer = "";
		
		return this;
	}

	public String getBestellnummer() {
		return bestellnummer;
	}

	public void setBestellnummer(String bestellnummer) {
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

	public double getVersandkosten() {
		return versandkosten;
	}

	public void setVersandkosten(double versandkosten) {
		this.versandkosten = versandkosten;
	}
	
	public String getGeaendertBenutzer() {
		return geaendertBenutzer;
	}

	public void setGeaendertBenutzer(String geaendertBenutzer) {
		this.geaendertBenutzer = geaendertBenutzer;
	}
	
}