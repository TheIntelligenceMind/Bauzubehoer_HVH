package controller;

import java.io.IOException;

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

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Benutzerkonto zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 * @author Tim Hermbecker
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
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		
		// prüfen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){
			rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
		}
		
		// Berechtigung für die Seite prüfen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtKonto() != 1){
    		rd = req.getRequestDispatcher("/suchen");
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
    		setDispatchSite("/abmelden");
			
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
    			, req.getParameter("zusatz"));

    	Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());
    			
		if(benutzer.getLieferAdresse() == null){
    		// Adresse auf Gültigkeit prüfen
    		if(adressenHelper.validateAdresse(new_adresse)){

        		result = queryManager.createAdresse(benutzer.getEmailadresse(), new_adresse);
        		
        		// nur wenn die Adreses erfolgreich angelegt wurde soll das Benutzerobjekt mit dem neuen Adressobjekt verknüpft werden
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
        		if(update_adresse.getZusatz() == null){
        			update_adresse.setZusatz("");
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
    	String emailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	Benutzer update_benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
    	
    	if(vorname != null && !vorname.isEmpty() && nachname != null && !nachname.isEmpty()){
    			
    		if(update_benutzer != null){
    			update_benutzer.setVorname(vorname);
    			update_benutzer.setNachname(nachname);

        		result = queryManager.modifyBenutzer(update_benutzer);
    		}
    	}
    	
    	if(result){
			req.getSession().setAttribute("benutzer", update_benutzer);

			String hinweistext = "Die Benutzerdaten wurden erfolgreich gespeichert.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);
				
		}else{
			String fehlermeldung = "ung&uuml;ltige &Auml;nderungen";	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
		}	
    	
		req.setAttribute("benutzer", update_benutzer);
		resp.addHeader("contentSite", "meinKontoPanel");
    } 
    
    private void setDispatchSite(String site){
    	this.dispatchSite = site;
    }

}
