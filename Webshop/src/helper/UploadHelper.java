package helper;


/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für die Validierung von Adressdaten zuständig. 
 * </pre>
 *  @author Tim Hermbecker
 */
public class UploadHelper {
	private static final UploadHelper instance = new UploadHelper();
	
	private UploadHelper(){
		
	}
	
	public static UploadHelper getInstance(){
		return instance;
	}
	
	public void bildHochladen(){
		
	}
	
}
