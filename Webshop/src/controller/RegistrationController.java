package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.QueryManager;
import entity.Benutzer;
import enums.ENUM_RESPONSE_STATUS;
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

	private final QueryManager queryManager = QueryManager.getInstance();
	private final BenutzerHelper benutzerHelper = BenutzerHelper.getInstance();
	
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
	
	
}