package entity;

public class Artikel {

	private String bezeichnung;
	private int artikelID;
	private String preis;
	private String beschreibung;
	private int lagermenge;
	
	public Artikel(){
		
	}
	
	public Artikel init(String piBezeichnung, int piArtikelID, String piBeschreibung, String piPreis, int piLagermenge){		
		bezeichnung = piBezeichnung;
		artikelID = piArtikelID;
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

	public String getPreis() {
		return preis;
	}

	public void setPreis(String preis) {
		this.preis = preis;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public int getArtikelID() {
		return artikelID;
	}

	public void setArtikelID(int artikelID) {
		this.artikelID = artikelID;
	}

	public int getLagermenge() {
		return lagermenge;
	}

	public void setLagermenge(int lagermenge) {
		this.lagermenge = lagermenge;
	}
	
}
