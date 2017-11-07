package entity;

public class Rolle {

	private String bezeichnung;
	private int sichtWarenkorb;
	private int sichtBestellungen;
	private int sichtKonto;
	private int sichtArtikelstammdaten;
	
	public Rolle(){		
		
	}
	
	public Rolle init(String piBezeichnung, int piSichtWarenkorb, int piSichtBestellungen, int piSichtKonto, int piSichtArtikelstammdaten){		
		bezeichnung = piBezeichnung != null ? piBezeichnung : "";	
		sichtWarenkorb = piSichtWarenkorb;	
		sichtBestellungen = piSichtBestellungen;
		sichtKonto = piSichtKonto;
		sichtArtikelstammdaten = piSichtArtikelstammdaten;
		
		return this;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public int getSichtWarenkorb() {
		return sichtWarenkorb;
	}

	public void setSichtWarenkorb(int sichtWarenkorb) {
		this.sichtWarenkorb = sichtWarenkorb;
	}

	public int getSichtBestellungen() {
		return sichtBestellungen;
	}

	public void setSichtBestellungen(int sichtBestellungen) {
		this.sichtBestellungen = sichtBestellungen;
	}

	public int getSichtKonto() {
		return sichtKonto;
	}

	public void setSichtKonto(int sichtKonto) {
		this.sichtKonto = sichtKonto;
	}

	public int getSichtArtikelstammdaten() {
		return sichtArtikelstammdaten;
	}

	public void setSichtArtikelstammdaten(int sichtArtikelstammdaten) {
		this.sichtArtikelstammdaten = sichtArtikelstammdaten;
	}
}