package entity;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse bildet die Tabelle Warenkorbartikel in der DB ab
 * </pre>
 *  @author Tim Hermbecker
 */
public class WarenkorbArtikel {

	private Artikel artikel;
	private int position;
	private int menge;
	
	public WarenkorbArtikel(){
		
	}
	
	public WarenkorbArtikel init(Artikel piArtikel, int piPosition, int piMenge){		
		artikel = piArtikel;
		position = piPosition;
		menge = piMenge;
		
		return this;
	}

	public Artikel getArtikel() {
		return artikel;
	}

	public void setArtikel(Artikel artikel) {
		this.artikel = artikel;
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

}
