package controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import db.QueryManager;
import entity.Benutzer;
import enums.ENUM_RESPONSE_STATUS;
import enums.ENUM_ROLLE;
import helper.BenutzerHelper;
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

	private static final QueryManager queryManager = QueryManager.getInstance();
	private static final BenutzerHelper benutzerHelper = BenutzerHelper.getInstance();
	private static final MailHelper mailHelper = MailHelper.getInstance();
	
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
		case "pwResetEmailAnzeigen":			
			resp.addHeader("contentSite", "passwortResetEmailPanel");
			break;
		case "pwResetEmail":			
			resetEmailVerschicken(req, resp);
			resp.addHeader("contentSite", "passwortResetEmailPanel");
			break;
		case "pwResetAnzeigen":
			if(resetCodeValidieren(req, resp)){
				resp.addHeader("contentSite", "passwortResetPanel");
			}else{
				rd = req.getRequestDispatcher("/suchen");
			}
			break;
		case "pwReset":			
			if(passwortReset(req, resp)){
				rd = req.getRequestDispatcher("/suchen");
			}else{
				resp.addHeader("contentSite", "passwortResetPanel");
			}
			break;
		case "registrierenKundeAnzeigen":
			resp.addHeader("contentSite", "registrierungPanel");
			break;
		case "registrierenKunde":
			fehlertext = benutzerHelper.benutzerAnlegen(req, null);

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
			resp.addHeader("contentSite", "registrierungPanel");
			resp.addHeader("result", String.valueOf(fehlertext));
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
			resp.addHeader("contentSite", "registrierungPanel");
			resp.addHeader("result", String.valueOf(fehlertext));
			break;
		default:
			break;
		}	
		
		rd.forward(req, resp);
	}	
	
	private boolean resetCodeValidieren(HttpServletRequest req, HttpServletResponse resp){
		String resetCode = req.getParameter("resetCode");
		String emailadresse = req.getParameter("bMail");
		
		if(resetCode != null && emailadresse != null){
			String originalResetCode = queryManager.getResetCode(emailadresse);
			
			if(originalResetCode != null && !originalResetCode.isEmpty() && originalResetCode.equals(resetCode)){
				Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
				queryManager.setResetCode(benutzer, null);
				return true;				
			}else{
				resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
				resp.addHeader("fehlermeldung", "Der Link ist ung&uuml;ltig.");	
			}	
		}
		return false;
	}
	
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft die E-Mail-Adresse und den dahinter liegenden Benutzeraccount auf seine Rolle.
	 * Wenn die Rolle gleich Administrator oder Mitarbeiter ist wird keine E-Mail verschickt.
	 * Ist die E-Mail-Adresse gültig und von einem Kundenkonto wird die E-Mail verschickt.
	 * </pre> 
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse 
	 */	
	private void resetEmailVerschicken(HttpServletRequest req, HttpServletResponse resp){
		String emailadresse = req.getParameter("emailadresse");

		if(emailadresse != null && !emailadresse.isEmpty()){
			Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
			
			if(benutzer.getRolle().getBezeichnung().equals(ENUM_ROLLE.MITARBEITER.toString()) || benutzer.getRolle().getBezeichnung().equals(ENUM_ROLLE.ADMINISTRATOR.toString())){
				resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
				resp.addHeader("hinweismeldung", "Die E-Mail wurde erfolgreich verschickt. In k&uuml;rze sollten Sie eine E-Mail von uns erhalten.");	
				return;
			}
			
			// neuen ResetCode erzeugen
			String resetCode = benutzerHelper.getNewResetCode();
			
			// E-Mail an die E-Mail-Adresse verschicken
			boolean result = mailHelper.sendResetEmail(emailadresse, resetCode);
			
			// Wenn das senden der E-Mail nicht erfolgreich war wird eine Fehlermeldung ausgegeben
			if(!result){
				resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
				resp.addHeader("fehlermeldung", "Die Mail konnte nicht verschickt werden. Bitte versuchen Sie es nochmal zu einem sp&auml;teren Zeitpunkt.");
				return;
			}
		
			if(benutzer != null){
				queryManager.setResetCode(benutzer, resetCode);
			}	
			
			resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader("hinweismeldung", "Die E-Mail wurde erfolgreich verschickt. In K&uuml;rze sollten Sie eine E-Mail von uns erhalten.");	
			
		}else{
			resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader("fehlermeldung", "Bitte geben Sie Ihre E-Mail-Adresse ein.");	
		}
	}
	
	private boolean passwortReset(HttpServletRequest req, HttpServletResponse resp){
		String passwort = req.getParameter("passwort");
		String passwortBestaetigt = req.getParameter("passwortBestaetigt");
		String emailadesse = req.getParameter("emailadresse");
		
		if(emailadesse.isEmpty()){
			resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader("fehlermeldung", "Es ist ein unerwarteter Fehler aufgetreten.");
			return false;
		}
		
		if(passwort != null && !passwort.isEmpty() && passwortBestaetigt != null && !passwortBestaetigt.isEmpty()){
			if(benutzerHelper.passwortIstGueltig(passwort)){
				if(passwort.equals(passwortBestaetigt)){
					
					Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadesse);
					
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
					
					benutzer.setPasswort(hashPasswort);
					
					if(queryManager.modifyBenutzer(benutzer)){
						resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
						resp.addHeader("hinweismeldung", "Das Passwort wurde erfolgreich zur&uuml;ckgesetzt.");
						return true;
					}else{
						resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
						resp.addHeader("fehlermeldung", "Es ist ein unerwarteter Fehler aufgetreten.");
					}
					
				}else{
					resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
					resp.addHeader("fehlermeldung", "Die Passw&ouml;rter sind nicht identisch.");
				}
			}else{
				resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
				resp.addHeader("fehlermeldung", "Bitte beachten Sie die Passwortrichtlinien.");
			}
		}else{
			resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader("fehlermeldung", "Bitte alle Felder ausf&uuml;llen.");
		}
		req.setAttribute("bMail", emailadesse);
		
		return false;
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
	
	
}