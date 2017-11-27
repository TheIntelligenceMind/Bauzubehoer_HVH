package entity;


/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse bildet die Tabelle Artikel in der DB ab
 * </pre>
 *  @author Tim Hermbecker
 */
public class Artikel {

	private String bezeichnung;
	private int nummer;
	private double preis;
	private String beschreibung;
	private int lagermenge;
	private int meldebestand;
	private String kategorie_1;
	private String kategorie_2;
	private byte[] bild;
	private int aktiv;
	
	public Artikel(){
		
	}
	
	public Artikel init(String piBezeichnung, int piNummer, String piBeschreibung, double piPreis, int piLagermenge, 
			int piMeldebestand, byte[] piBild, String piKategorie_1, String piKategorie_2, int piAktiv){		
		bezeichnung = piBezeichnung;
		nummer = piNummer;
		beschreibung = piBeschreibung;
		preis = piPreis;
		lagermenge = piLagermenge;
		meldebestand = piMeldebestand;
		bild = piBild;
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
	
	public int getMeldebestand() {
		return meldebestand;
	}

	public void setMeldebestand(int meldebestand) {
		this.meldebestand = meldebestand;
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

	public String getBildasString(){
		return org.apache.commons.codec.binary.Base64.encodeBase64String(bild);
	}
	
	public byte[] getBild() {
		return bild;
	}

	public void setBild(byte[] bild) {
		this.bild = bild;
	}
	
}
