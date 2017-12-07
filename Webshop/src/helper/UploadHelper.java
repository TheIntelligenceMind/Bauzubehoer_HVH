package helper;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.Part;


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
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode validiert ein ausgewähltes Bild und lädt es hoch.
	 * </pre>
	 * @param part
	 * @return bytes
	 */
	public byte[] bildHochladen(Part part){
		byte[] bytes = null;
		try {
			InputStream fileContent = part.getInputStream();
			bytes = new byte[fileContent.available()];
			fileContent.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bytes;
	}
	
}
