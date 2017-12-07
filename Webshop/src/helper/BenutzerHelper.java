package helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import db.QueryManager;
import entity.Adresse;
import entity.Benutzer;
import entity.Rolle;
import enums.ENUM_ROLLE;

public class BenutzerHelper {
	private final QueryManager queryManager = QueryManager.getInstance();
	private final static BenutzerHelper instance = new BenutzerHelper();
	
	public BenutzerHelper(){
		
	}
	
	public static BenutzerHelper getInstance(){
		return instance;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert einen zufälligen 10-stelligen Code
	 * </pre>
	 * @return String
	 */	
	public String getNewResetCode(){
		String code = "";
		int range;
		
		for(int i = 0; i < 10;i++){
			range = (3 - 1) + 1; 
			int rnd = (int) ((Math.random() * range) + 1); 
			
			switch(rnd){
			case 1:
				range = (57 - 48) + 1;    
				int zahl = (int) ((Math.random() * range) + 48);
				code = code.concat(Character.toString((char) zahl));
				break;
			case 2:
				range = (122 - 97) + 1;    
				int kBuchstabe = (int) ((Math.random() * range) + 97);
				code = code.concat(Character.toString((char) kBuchstabe));
				break;
			case 3:
				range = (90 - 65) + 1;    
				int gBuchstabe = (int) ((Math.random() * range) + 65);
				code = code.concat(Character.toString((char) gBuchstabe));
				break;
			default:
				break;
			}
		}
		return code;
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode legt einen Benutzer mit der zugehörigen Adrese in der DB an.
	 * </pre>
	 *  @param req
	 *  @param adresse
	 */
	public String benutzerAnlegen(HttpServletRequest req, Adresse adresse){
		String fehlertext = null;		
		String emailadresse = req.getParameter("emailadresse");
		String vorname = req.getParameter("vorname");
		String nachname = req.getParameter("nachname");
		String passwort = req.getParameter("passwort");
		String passwortBestaetigt = req.getParameter("passwortBestaetigt");
		ENUM_ROLLE rolle = req.getParameter("rolle") == null ? ENUM_ROLLE.KUNDE : ENUM_ROLLE.getRolleByName(req.getParameter("rolle"));
	
		if(alleFelderGefuellt(emailadresse, vorname, nachname, passwort, passwortBestaetigt)){
			if(isEmailValid(emailadresse)){
				if(!isEmailUsed(emailadresse)){
					if(passwortIstGueltig(passwort)){
						if(passwoerterSindIdentisch(passwort, passwortBestaetigt)){

							// neues Benutzerobjekt anlegen
							Benutzer newBenutzer = new Benutzer();			
							MessageDigest hasher = null;
							String hashPasswort = null;
							
							try {
								hasher = MessageDigest.getInstance("MD5");
								hasher.update(passwort.getBytes());
								byte[] str = hasher.digest();
								hashPasswort = DatatypeConverter.printHexBinary(str).toUpperCase();
								
							} catch (NoSuchAlgorithmException e) {
								e.printStackTrace();
							}
							
							Rolle benutzerRolle = Rolle.initRolle(rolle);
							int bestaetigtFlag = rolle == ENUM_ROLLE.KUNDE ? 0 : 1;
							newBenutzer.init(emailadresse, hashPasswort, vorname, nachname, adresse, benutzerRolle, bestaetigtFlag, new Date(System.currentTimeMillis()));
							
							// Benutzerobjekt in der Datenbank anlegen
							if(queryManager.createBenutzer(newBenutzer)){							
								if(rolle != ENUM_ROLLE.KUNDE){
									if(queryManager.createAdresse(emailadresse, adresse)){
										return null;
									}else{
										fehlertext = "Es ist ein unerwarteter Fehler aufgetreten.";
									}
								}
							}else{
								fehlertext = "Es ist ein unerwarteter Fehler aufgetreten.";
							}
						}else{
							fehlertext = "Die Passw&ouml;rter sind nicht identisch.";
						}
					}else{
						fehlertext = "Das Passwort entspricht nicht den Richtlinien.";
					}
				}else{
					fehlertext = "Die E-Mail-Adresse ist bereits registriert.";
				}
			}else{
				fehlertext = "Die E-Mail-Adresse ist nicht g&uuml;ltig.";
			}
		}else{
			fehlertext = "Es wurden nicht alle Felder ausgef&uuml;llt.";
		}
		
		return fehlertext;	
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft ob das Passwort alle Passwortrichtlinien 
	 * einhält und gibt je nach dem true oder false zurück
	 * </pre>
	 * 
	 * @param piPasswort
	 * @return true or false
	 */
	public boolean passwortIstGueltig(String piPasswort) {
		String passwort = piPasswort;
		
		if(passwort != null && passwort.length() >= 6){
			return true;
		}
		
		return false;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft ob alle übergebenen Attribute mit Werten gefüllt wurden.
	 * </pre>
	 * 
	 * @param piEmail
	 * @param piVorname
	 * @param piNachname
	 * @param piPasswort
	 * @param piPasswortBestaetigt
	 * @return true or false
	 */
	private boolean alleFelderGefuellt(String piEmail, String piVorname, String piNachname, String piPasswort, String piPasswortBestaetigt){
		String email = piEmail;
		String vorname = piVorname;
		String nachname = piNachname;
		String passwort = piPasswort;
		String passwortBestaetigt = piPasswortBestaetigt;
		
		if(!email.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty() && !passwort.isEmpty() && !passwortBestaetigt.isEmpty()){
			return true;
		}
		
		return false;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft ob beide übergebenen Passwörter 
	 * identisch sind und gibt je nach dem true oder false zurück
	 * </pre>
	 * 
	 * @param piPasswort
	 * @param piPasswortBestaetigt
	 * @return true or false
	 */
	private boolean passwoerterSindIdentisch(String piPasswort, String piPasswortBestaetigt) {
		String passwort = piPasswort;
		String passwortBestaetigt = piPasswortBestaetigt;
		
		if(passwort != null && !passwort.isEmpty() && passwortBestaetigt != null && !passwortBestaetigt.isEmpty()){
			if(passwort.equals(passwortBestaetigt)){
				return true;
			}	
		}
		
		return false;
	}

	/**
	 *  <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft:
	 * 	1. ob Email-Adresse null ist,
	 *	2. ob Email-Adresse das Email-Pattern erfüllt
	 * </pre>
	 * 
	 * @param piEmail
	 * @return true or false
	 */
	private boolean isEmailValid(String piEmail){
		String email = piEmail;
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		if(email != null){
			Matcher matcher = pattern.matcher(email);
			
			if(matcher.find()){
				return true;		
			}
		}
		
		return false;
	}
	
	/**
	 *  <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft:
	 * 	1. ob Email-Adresse bereits in Verwendung ist
	 * </pre>
	 * 
	 * @param piEmail
	 * @return true or false
	 */
	private boolean isEmailUsed(String piEmail){
		String email = piEmail;
		if(queryManager.getBenutzerByEMailAdresse(email) != null){
			return true;
		}
		return false;
	}
}
