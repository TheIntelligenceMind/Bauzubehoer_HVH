package view;

/**
 * <h3>Beschreibung:</h3>
 * <pre>
 * Diese Klasse stellt verschiedenste Methoden zum Anzeigen/Ver�ndern/L�schen von Daten
 * in der Oberfl�che bereit und kann von �berall in der Anwendung aufgerufen werden.
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
