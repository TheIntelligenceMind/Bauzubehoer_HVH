package entity;

public class Artikel {

	private String bezeichnung;
	private int nummer;
	private double preis;
	private String beschreibung;
	private int lagermenge;
	private String kategorie_1;
	private String kategorie_2;
	private int aktiv;
	
	public Artikel(){
		
	}
	
	public Artikel init(String piBezeichnung, int piNummer, String piBeschreibung, double piPreis, int piLagermenge, 
			String piKategorie_1, String piKategorie_2, int piAktiv){		
		bezeichnung = piBezeichnung;
		nummer = piNummer;
		beschreibung = piBeschreibung;
		preis = piPreis;
		lagermenge = piLagermenge;
		kategorie_1 = piKategorie_1;
		kategorie_2 = piKategorie_2;
		setAktiv(piAktiv);
		
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
	

	public String getKategorie_1() {
		return kategorie_1;
	}

	public void setKategorie_1(String kategorie_1) {
		this.kategorie_1 = kategorie_1;
	}

	public String getKategorie_2() {
		return kategorie_2;
	}

	public void setKategorie_2(String kategorie_2) {
		this.kategorie_2 = kategorie_2;
	}

	public int getAktiv() {
		return aktiv;
	}

	public void setAktiv(int aktiv) {
		this.aktiv = aktiv;
	}
	
}
