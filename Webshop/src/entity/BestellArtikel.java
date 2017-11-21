package entity;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse bildet die Tabelle Bestellartikel in der DB ab
 * </pre>
 *  @author Tim Hermbecker
 */
public class BestellArtikel {
	
	private int position;
	private int menge;
	private Artikel artikel;
	private Bestellung bestellung;
	
	public BestellArtikel(){
		
	}
	
	public BestellArtikel init(int piPosition, int piMenge, Artikel piArtikel, Bestellung piBestellung){
		position = piPosition;
		menge = piMenge;
		artikel = piArtikel;
		bestellung = piBestellung;
		
		return this;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getMenge() {
		return menge;
	}

	public void setMenge(int menge) {
		this.menge = menge;
	}

	public Artikel getArtikel() {
		return artikel;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
	}

	public Bestellung getBestellung() {
		return bestellung;
	}

	public void setBestellung(Bestellung bestellung) {
		this.bestellung = bestellung;
	}
	
	
}
