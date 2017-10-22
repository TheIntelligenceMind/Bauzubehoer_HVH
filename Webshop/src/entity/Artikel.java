package entity;

public class Artikel {

	private String bezeichnung;
	private int nummer;
	private double preis;
	private String beschreibung;
	private int lagermenge;
	
	public Artikel(){
		
	}
	
	public Artikel init(String piBezeichnung, int piNummer, String piBeschreibung, double piPreis, int piLagermenge){		
		bezeichnung = piBezeichnung;
		nummer = piNummer;
		beschreibung = piBeschreibung;
		preis = piPreis;
		lagermenge = piLagermenge;
		
		return this;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}


	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public double getPreis() {
		return preis;
	}

	public void setPreis(double preis) {
		this.preis = preis;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public int getNummer() {
		return nummer;
	}

	public void setNummer(int nummer) {
		this.nummer = nummer;
	}

	public int getLagermenge() {
		return lagermenge;
	}

	public void setLagermenge(int lagermenge) {
		this.lagermenge = lagermenge;
	}
	
}
