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
	
	public String benutzerAnlegen(HttpServletRequest req, Adresse adresse){
		String fehlertext = null;		
		String email = req.getParameter("emailadresse");
		String vorname = req.getParameter("vorname");
		String nachname = req.getParameter("nachname");
		String passwort = req.getParameter("passwort");
		String passwortBestaetigt = req.getParameter("passwortBestaetigt");
		ENUM_ROLLE rolle = req.getParameter("rolle") == null ? ENUM_ROLLE.KUNDE : (ENUM_ROLLE) req.getAttribute("rolle");
	
		if(alleFelderGefuellt(email, vorname, nachname, passwort, passwortBestaetigt)){
			if(isEmailValid(email)){
				if(!isEmailUsed(email)){
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
							
							Rolle benutzerRolle = rolleAnlegen(rolle);
							int aktivFlag = rolle == ENUM_ROLLE.KUNDE ? 0 : 1;
							newBenutzer.init(email, hashPasswort, vorname, nachname, adresse, benutzerRolle, aktivFlag, new Date(System.currentTimeMillis()));
							
							// Benutzerobjekt in der Datenbank anlegen
							if(queryManager.createBenutzer(newBenutzer)){
								return null;
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

	private Rolle rolleAnlegen(ENUM_ROLLE rolle) {
		switch(rolle){
		case KUNDE:
			return new Rolle().init(rolle.toString(), 1, 1, 1, 0, 0);
		case MITARBEITER:
			return new Rolle().init(rolle.toString(), 0, 0, 1, 1, 0);
		case ADMINISTRATOR:
			return new Rolle().init(rolle.toString(), 1, 1, 1, 1, 1);
		default:
			return null;
		}	
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
	private boolean passwortIstGueltig(String piPasswort) {
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
