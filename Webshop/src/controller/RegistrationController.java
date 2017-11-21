package controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import db.QueryManager;
import entity.Benutzer;
import entity.Rolle;
import enums.ENUM_RESPONSE_STATUS;
import enums.ENUM_ROLLE;
import helper.MailHelper;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Registration zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 * @author Tim Hermbecker
 */
@WebServlet("/registrieren")
public class RegistrationController extends HttpServlet{
	private static final long serialVersionUID = 8289722651997847704L;

	QueryManager queryManager = QueryManager.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		resp.setContentType("text/html");  				
		String fehlertext = null;
		
		// Berechtigung für die Seite prüfen
    	if(req.getSession().getAttribute("benutzer") != null){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
		
		String method = req.getParameter("method");

		if(method == null){
			method = "";
		}
		
		//falls ein alter Status besteht wird dieser gelöscht.
		if(resp.getHeader("status") != null){
			resp.setHeader("status", "");
		}
		
		switch(method){
			case "registrierenMitarbeiter":
				
				break;
			case "registrierenKunde":
				fehlertext = registerKunde(req);

				// aktueller Status wird gesetzt
				if(fehlertext != null){
					resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
					resp.addHeader("fehlermeldung", fehlertext);	
				}else{
					Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(req.getParameter("emailadresse"));
					
					MailHelper.getInstance().sendBestaetigungsmail(benutzer);
							
					resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
					resp.addHeader("hinweismeldung", "Erfolgreich registriert. Bitte prüfen Sie Ihre Mails.");
				}		
				break;
			case "confirm":	
				fehlertext = validateConfirmation(req);
				
				// aktueller Status wird gesetzt
				if(fehlertext != null){
					resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
					resp.addHeader("fehlermeldung", fehlertext);	
				}else{
					resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
					resp.addHeader("hinweismeldung", "Das Benutzerkonto wurde erfolgreich aktiviert.");
				}
				
				break;
			default:
				break;
		}

					
		resp.addHeader("contentSite", "registrierungPanel");
		resp.addHeader("result", String.valueOf(fehlertext));
		
		rd.forward(req, resp);
	}	
	
	private String validateConfirmation(HttpServletRequest req){
		String fehlertext = null;
		String emailadresse = req.getParameter("ea");
		Benutzer benutzer = null;
		
		benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
		
		if(benutzer != null){
			if(benutzer.getBestaetigt() == 0){
				benutzer.setBestaetigt(1);
				if(queryManager.modifyBenutzer(benutzer)){
					return null;
				}else{
					fehlertext = "Es ist ein unerwarteter Fehler bei der Aktivierung aufgetreten.";
				}
			}else{
				fehlertext = "Das Benutzerkonto ist bereits aktiviert.";
			}
		}else{
			fehlertext = "Es ist ein unerwarteter Fehler bei der Aktivierung aufgetreten.";
		}
		
		req.setAttribute("benutzer", benutzer);
		
		return fehlertext;
	}
	
	private String registerKunde(HttpServletRequest req){
		String fehlertext = null;		
		String email = req.getParameter("emailadresse");
		String vorname = req.getParameter("vorname");
		String nachname = req.getParameter("nachname");
		String passwort = req.getParameter("passwort");
		String passwortBestaetigt = req.getParameter("passwortBestaetigt");
	
		if(alleFelderGefuellt(email, vorname, nachname, passwort, passwortBestaetigt)){
			if(isEmailValid(email)){
				if(!isEmailUsed(email)){
					if(passwortIstGueltig(passwort)){
						if(passwoerterSindIdentisch(passwort, passwortBestaetigt)){
							QueryManager queryManager = QueryManager.getInstance();
							
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
							
							Rolle kundenrolle = new Rolle().init(ENUM_ROLLE.KUNDE.toString(), 1, 1, 1, 0);
							newBenutzer.init(email, hashPasswort, vorname, nachname, null, kundenrolle, 0, new Date(System.currentTimeMillis()));
							
							// Benutzerobjekt in der Datenbank anlegen
							if(!queryManager.createBenutzer(newBenutzer)){
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