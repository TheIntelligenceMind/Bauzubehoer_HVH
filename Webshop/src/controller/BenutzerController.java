package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import db.QueryManager;
import entity.Adresse;
import entity.Benutzer;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;
import helper.AdressenHelper;
import helper.BenutzerHelper;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Benutzer zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 *  @author Tim Hermbecker
 */
@WebServlet("/benutzer")
public class BenutzerController extends HttpServlet {
	private final static long serialVersionUID = 1L;
	private final static QueryManager queryManager = QueryManager.getInstance();
	private final static BenutzerHelper benutzerHelper = BenutzerHelper.getInstance();
	private final static AdressenHelper adressenHelper = AdressenHelper.getInstance();
	private String dispatchSite = "index.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher(dispatchSite);
		resp.setContentType("text/html"); 	
		
		// prüfen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){
			rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
		}
		
		// Berechtigung für die Seite prüfen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtBenutzerstammdaten() != 1){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
    		
		String method = req.getParameter("method");
		
		if(method == null){
			method = "";
		}
		
		switch(method){
		case "benutzerstammdatenAnzeigen":			
			benutzerstammdatenAnzeigen(req, resp);		
			break;
		case "benutzerAnlegenAnzeigen":
			benutzerAnlegenAnzeigen(req, resp);
			break;
		case "benutzerAnlegen":
			benutzerAnlegen(req, resp);
			break;
		case "benutzerBearbeitenAnzeigen":
			benutzerBearbeitenAnzeigen(req, resp);
			break;
		case "benutzerBearbeiten":
			benutzerBearbeiten(req, resp);
			break;
		case "benutzerLoeschen":
			benutzerLoeschen(req, resp);
		default:
			resp.addHeader("contentSite", "benutzerstammdatenPanel");
			break;		
		}

		rd = req.getRequestDispatcher(dispatchSite);
		rd.forward(req, resp);		
	}
 
    private void benutzerstammdatenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
    	List<Benutzer> benutzerliste = null;
		
    	benutzerliste = queryManager.selectAllMitarbeiter();

		req.setAttribute("benutzerstammdatenListe", benutzerliste);		
		resp.addHeader("contentSite", "benutzerstammdatenPanel");   	
    }

    private void benutzerAnlegenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
		resp.addHeader("contentSite", "benutzerAnlegenPanel");   	
    }
    
    private void benutzerAnlegen(HttpServletRequest req, HttpServletResponse resp){
    	String strasse = String.valueOf(req.getAttribute("strasse"));
    	String hausnummer = String.valueOf(req.getAttribute("hausnummer"));
    	String postleitzahl = String.valueOf(req.getAttribute("postleitzahl"));
    	String ort = String.valueOf(req.getAttribute("ort"));
    	String zusatz = String.valueOf(req.getAttribute("zusatz"));
    	
    	Adresse new_Adresse = new Adresse().init(strasse, hausnummer, postleitzahl, ort, zusatz);
    	
    	if(adressenHelper.validateAdresse(new_Adresse)){
    		String fehlermeldung = benutzerHelper.benutzerAnlegen(req, new_Adresse);
    		
    		if(fehlermeldung == null){
    			String hinweismeldung = "Das Benutzerkonto wurde erfolgreich angelegt.";
    			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
    			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweismeldung);	
    			resp.addHeader("contentSite", "benutzerstammdatenPanel"); 
    		}else{
    			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
        		resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
        		resp.addHeader("contentSite", "benutzerAnlegenPanel"); 
    		}
    		
    	}else{
    		String fehlermeldung = "Die Adresse ist nicht g&uuml;ltig.";
    		resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
    		resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);	
    		resp.addHeader("contentSite", "benutzerAnlegenPanel"); 
    	}	
    }
    
    private void benutzerBearbeitenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
    	String emailadresse = req.getParameter("emailadresse");
    	Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
    	
    	req.setAttribute("benutzer", benutzer);
    	resp.addHeader("contentSite", "benutzerBearbeitenPanel");   	
    }
    
    private void benutzerBearbeiten(HttpServletRequest req, HttpServletResponse resp){
		
    	
    	
    	resp.addHeader("contentSite", "benutzerBearbeitenAnzeigen");   	
    }
    
    private void benutzerLoeschen(HttpServletRequest req, HttpServletResponse resp){
    	boolean result = false;
    	String emailadresse = String.valueOf(req.getAttribute("emailadresse")); 	
    	
    	result = queryManager.deleteBenutzer(emailadresse);
    	
    	if(result){
			String hinweismeldung = "Das Benutzerkonto wurde erfolgreich gel&ouml;scht.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweismeldung);	
		}else{		
			String fehlermeldung = "Das Benutzerkonto konnte nicht gel&ouml;scht werden.";	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);	
			
			resp.addHeader("contentSite", "benutzerstammdatenPanel");
		}
    }

}
