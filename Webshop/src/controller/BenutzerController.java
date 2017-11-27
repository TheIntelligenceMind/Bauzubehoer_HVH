package controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import db.QueryManager;
import entity.Adresse;
import entity.Benutzer;
import entity.Rolle;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;
import enums.ENUM_ROLLE;
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
		case "benutzerSpeichern":
			benutzerSpeichern(req, resp);
			break;
		case "adresseSpeichern":
			adresseSpeichern(req, resp);
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
		req.setAttribute("benutzerstammdatenListe", getBenutzerstammdatenListe(req, resp));		
		resp.addHeader("contentSite", "benutzerstammdatenPanel");   	
    }
    
    private List<Benutzer> getBenutzerstammdatenListe(HttpServletRequest req, HttpServletResponse resp){
    	List<Benutzer> benutzerliste = null;
		
    	benutzerliste = queryManager.selectAllMitarbeiter();

    	// eigenes Benutzerobjekt nicht in den Stammdaten anzeigen
    	String eigeneEmailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();	
    	benutzerliste = benutzerliste.stream().filter(b -> !b.getEmailadresse().equals(eigeneEmailadresse)).collect(Collectors.toList());
 
    	return benutzerliste;
    }

    private void benutzerAnlegenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
    	
    	
		resp.addHeader("contentSite", "benutzerAnlegenPanel");   	
    }
    
    private void benutzerAnlegen(HttpServletRequest req, HttpServletResponse resp){
    	String emailadresse = req.getParameter("emailadresse");
    	String vorname = req.getParameter("vorname");
    	String nachname = req.getParameter("nachname");
    	String strasse = req.getParameter("strasse");
    	String hausnummer = req.getParameter("hausnummer");
    	String postleitzahl =req.getParameter("postleitzahl");
    	String ort = req.getParameter("ort");
    	String zusatz = req.getParameter("zusatz");
    	Benutzer neuerBenutzer = new Benutzer();
    	
    	Adresse new_Adresse = new Adresse().init(strasse, hausnummer, postleitzahl, ort, zusatz);
    	
    	neuerBenutzer.setLieferAdresse(new_Adresse);
    	neuerBenutzer.setEmailadresse(emailadresse);
    	neuerBenutzer.setVorname(vorname);
    	neuerBenutzer.setNachname(nachname);
    	
    	if(adressenHelper.validateAdresse(new_Adresse)){
    		String fehlermeldung = benutzerHelper.benutzerAnlegen(req, new_Adresse);	
    		
    		if(fehlermeldung == null){
    			String hinweismeldung = "Das Benutzerkonto wurde erfolgreich angelegt.";
    			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
    			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweismeldung);	
    		}else{
    			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
        		resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
        		req.setAttribute("neuerBenutzer", neuerBenutzer);
    		}
    		
    	}else{
    		String fehlermeldung = "Die Adresse ist nicht g&uuml;ltig.";
    		resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
    		resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
    		req.setAttribute("neuerBenutzer", neuerBenutzer);
    	}	
    	
    	resp.addHeader("contentSite", "benutzerAnlegenPanel"); 
    }
    
    private void benutzerBearbeitenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
    	String emailadresse = req.getParameter("emailadresse");
    	Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
    	
    	req.setAttribute("benutzer", benutzer);
    	resp.addHeader("contentSite", "benutzerBearbeitenPanel");   	
    }
    
    private void adresseSpeichern(HttpServletRequest req, HttpServletResponse resp){ 	
    	boolean result = false;
    	String emailadresse = req.getParameter("emailadresse");
    	Adresse new_adresse = new Adresse().init(
    			req.getParameter("strasse")
    			, req.getParameter("hausnummer")
    			, req.getParameter("postleitzahl")
    			, req.getParameter("ort")
    			, req.getParameter("zusatz"));

    	Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
    			
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
		resp.addHeader("contentSite", "benutzerBearbeitenPanel");
    }
    
    
    private void benutzerSpeichern(HttpServletRequest req, HttpServletResponse resp){
    	boolean result = false;
    	Benutzer benutzer = null;
    	String emailadresse = req.getParameter("emailadresse");
    	String vorname = req.getParameter("vorname");
    	String nachname = req.getParameter("nachname");
    	String rolle = req.getParameter("rolle");
    	
    	if(vorname != null && !vorname.isEmpty() && nachname != null && !nachname.isEmpty()){
    		Benutzer update_benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);
    		
    		if(update_benutzer != null){
    			update_benutzer.setVorname(vorname);
    			update_benutzer.setNachname(nachname);
    			update_benutzer.setRolle(Rolle.initRolle(ENUM_ROLLE.getRolleByName(rolle)));

        		result = queryManager.modifyBenutzer(update_benutzer);
    		}
    	}
    	
    	if(result){
			benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);

			String hinweistext = "Die Benutzerdaten wurden erfolgreich gespeichert.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);
				
		}else{
			String fehlermeldung = "ung&uuml;ltige &Auml;nderungen";	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
		}	
    	
		req.setAttribute("benutzer", benutzer);
		resp.addHeader("contentSite", "benutzerBearbeitenPanel");
    }
    
    private void benutzerLoeschen(HttpServletRequest req, HttpServletResponse resp){
    	boolean result = false;
    	String emailadresse = req.getParameter("emailadresse"); 	
    	
    	result = queryManager.deleteBenutzer(emailadresse);
    	
    	if(result){
			String hinweismeldung = "Das Benutzerkonto wurde erfolgreich gel&ouml;scht.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweismeldung);	
			req.setAttribute("benutzerstammdatenListe", getBenutzerstammdatenListe(req, resp));
		}else{		
			String fehlermeldung = "Das Benutzerkonto konnte nicht gel&ouml;scht werden.";	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);	
			
			resp.addHeader("contentSite", "benutzerstammdatenPanel");
		}
    }

}
