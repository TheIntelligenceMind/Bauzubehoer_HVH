package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.QueryManager;
import entity.Adresse;
import entity.Benutzer;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;
import helper.AdressenHelper;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/meinKonto")
public class KontoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final QueryManager queryManager = QueryManager.getInstance();
	private static final AdressenHelper adressenHelper = AdressenHelper.getInstance();
	private Benutzer benutzer = null;
	private String dispatchSite = "index.jsp";

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req,resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		resp.setContentType("text/html"); 
		RequestDispatcher rd = null;
		
		// pr�fen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){
			rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
		}
		
		// Berechtigung f�r die Seite pr�fen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtKonto() != 1){
    		dispatchSite = "/suchen";	
    		rd = req.getRequestDispatcher(dispatchSite);
    		rd.forward(req, resp);
    		return;
    	}
		
		
		String method = req.getParameter("method");
		
		if(method == null){
			method = "";
		}
			
		switch(method){
			case "anzeigen":
				
				meinKontoAnzeigen(req, resp);
				
				break;
			case "benutzerSpeichern":
				
				speicherBenutzer(req, resp);
				
				break;
			case "adresseSpeichern":
				
				speicherAdresse(req, resp);
				
				break;
			case "loeschen":
				
				kontoLoeschen(req, resp);

				break;
			default:
				
				meinKontoAnzeigen(req, resp);
				
				break;
		}

			
		rd = req.getRequestDispatcher(dispatchSite);
		rd.forward(req, resp);	
	}
    
    private void meinKontoAnzeigen(HttpServletRequest req, HttpServletResponse resp){
    	benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());
		
		if(benutzer != null){
			req.setAttribute("benutzer", benutzer);
		}else{
			benutzer = new Benutzer().init("", "", "", "", null, null, 0, null);
		}
		req.setAttribute("benutzer", benutzer);
		resp.addHeader("contentSite", "meinKontoPanel");
    }
    
    private void kontoLoeschen(HttpServletRequest req, HttpServletResponse resp){
    	boolean result = false;
    	String emailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	
    	result = queryManager.deleteBenutzer(emailadresse);
    	
    	if(result){
    		dispatchSite = "/abmelden";
			
			String hinweistext = "Das Benutzerkonto wurde erfolgreich gel&ouml;scht.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);	
		}else{		
			String fehlermeldung = "Das Benutzerkonto konnte nicht gel&ouml;scht werden.";	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);	
			
			req.setAttribute("benutzer", benutzer);
			resp.addHeader("contentSite", "meinKontoPanel");
		}
    }
    
    private void speicherAdresse(HttpServletRequest req, HttpServletResponse resp){ 	
    	boolean result = false;
    	Adresse new_adresse = new Adresse().init(
    			req.getParameter("strasse")
    			, req.getParameter("hausnummer")
    			, req.getParameter("postleitzahl")
    			, req.getParameter("ort")
    			, "");

    	Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());
    			
		if(benutzer.getLieferAdresse() == null){
    		// Adresse auf G�ltigkeit pr�fen
    		if(adressenHelper.validateAdresse(new_adresse)){

        		result = queryManager.createAdresse(benutzer.getEmailadresse(), new_adresse);
        		
        		// nur wenn die Adreses erfolgreich angelegt wurde soll das Benutzerobjekt mit dem neuen Adressobjekt verkn�pft werden
        		if(result){
        			benutzer.setLieferAdresse(new_adresse);
        			result = queryManager.modifyBenutzer(benutzer);
        		}
    		}
    	}else{
    		if(adressenHelper.validateAdresse(new_adresse)){
        		Adresse update_adresse = new_adresse;
        		
        		if(update_adresse.getStrasse() == null){
        			update_adresse.setStrasse("");
        		}
        		if(update_adresse.getHausnummer() == null){
        			update_adresse.setHausnummer("");
        		}  		
        		if(update_adresse.getPostleitzahl() == null){
        			update_adresse.setPostleitzahl("");
        		}   		
        		if(update_adresse.getOrt() == null){
        			update_adresse.setOrt("");
        		}
        		
        		result = queryManager.modifyAdresse(benutzer.getEmailadresse(), update_adresse);
        		
        		if(result){
        			benutzer.setLieferAdresse(update_adresse);
        		}
        	}
    	}
    	 	
    	if(result){
			String hinweistext = "Die Benutzeradresse wurde erfolgreich gespeichert.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);			
		}else{
			String fehlermeldung = "ung&uuml;ltige &Auml;nderungen";	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
		}
    	
		req.setAttribute("benutzer", benutzer);
		resp.addHeader("contentSite", "meinKontoPanel");
    }
    
    
    private void speicherBenutzer(HttpServletRequest req, HttpServletResponse resp){
    	boolean result = false;
    	String vorname = req.getParameter("vorname");
    	String nachname = req.getParameter("nachname");
    	
    	if(vorname != null && !vorname.isEmpty() && nachname != null && !nachname.isEmpty()){
    		Benutzer update_benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());
    		
    		if(update_benutzer != null){
    			update_benutzer.setVorname(vorname);
    			update_benutzer.setNachname(nachname);

        		result = queryManager.modifyBenutzer(update_benutzer);
    		}
    	}
    	
    	if(result){
			benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());

			updateSessionDetails(req.getSession(), benutzer);

			String hinweistext = "Die Benutzerdaten wurden erfolgreich gespeichert.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);
				
		}else{
			String fehlermeldung = "ung&uuml;ltige &Auml;nderungen";	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
		}	
    	
		req.setAttribute("benutzer", benutzer);
		resp.addHeader("contentSite", "meinKontoPanel");
    }
    
    private void updateSessionDetails(HttpSession session, Benutzer benutzer){
    	session.setAttribute("vorname", benutzer.getVorname());
    	session.setAttribute("nachname", benutzer.getNachname());
    }

}
