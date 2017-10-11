package view;

/**
 * <h3>Beschreibung:</h3>
 * <pre>
 * Diese Klasse stellt verschiedenste Methoden zum Anzeigen/Verändern/Löschen von Daten
 * in der Oberfläche bereit und kann von überall in der Anwendung aufgerufen werden.
 * </pre>
 * @author Tim Hermbecker
 *
 */
public class ViewManager {
	private static final ViewManager instance = new ViewManager();
	
	public static ViewManager getInstance(){
		return instance;
	}
}
