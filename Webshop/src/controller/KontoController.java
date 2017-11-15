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

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/meinKonto")
public class KontoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final QueryManager queryManager = QueryManager.getInstance();
	private Benutzer benutzer = null;

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req,resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		resp.setContentType("text/html"); 
		
		// Berechtigung für die Seite prüfen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtKonto() != 1){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
		
		
		String method = req.getParameter("method");
		
		if(method != null){
			switch(method){
			case "anzeigen":
				meinKontoAnzeigen(req, resp);
				break;
			case "benutzerSpeichern":
				if(speicherBenutzer(req)){
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
				break;
			case "adresseSpeichern":
				if(speicherAdresse(req)){
					benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());
					
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
				break;
			case "loeschen":
				if(kontoLoeschen(req)){
					rd = req.getRequestDispatcher("/abmelden");
					
					String hinweistext = "Das Benutzerkonto wurde erfolgreich gel&ouml;scht.";
					resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
					resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);	
				}else{
					req.setAttribute("benutzer", benutzer);
					resp.addHeader("contentSite", "meinKontoPanel");
					
					String fehlermeldung = "Das Benutzerkonto konnte nicht gel&ouml;scht werden.";	
					resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
					resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);	
				}
				break;
			default:
				meinKontoAnzeigen(req, resp);
				break;
			}
		}else{
			meinKontoAnzeigen(req, resp);
		}
				
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
    private boolean kontoLoeschen(HttpServletRequest req){
    	String emailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	return queryManager.deleteBenutzer(emailadresse);
    }
    
    private boolean speicherAdresse(HttpServletRequest req){
    	boolean result = false;
    	String strasse = req.getParameter("strasse");
    	String hausnummer = req.getParameter("hausnummer");
    	String plz = req.getParameter("postleitzahl");
    	String ort = req.getParameter("ort");

    	Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());
    	
    	if(benutzer != null && benutzer.getLieferAdresse() == null){
    		Adresse new_adresse = new Adresse().init(strasse, hausnummer, plz, ort, "");
    		benutzer.setLieferAdresse(new_adresse);
    		result = queryManager.createAdresse(benutzer.getEmailadresse(), new_adresse);
    		
    		if(result){
    			result = queryManager.modifyBenutzer(benutzer);
    		}

    	}else if(benutzer != null && benutzer.getLieferAdresse() != null){
    		if(strasse != null && !strasse.isEmpty() && hausnummer != null && !hausnummer.isEmpty() && plz != null && !plz.isEmpty() && ort != null && !ort.isEmpty()){
        		Adresse update_adresse = benutzer.getLieferAdresse();
        		
        		update_adresse.setStrasse(strasse);
        		update_adresse.setHausnummer(hausnummer);
        		update_adresse.setPostleitzahl(plz);
        		update_adresse.setOrt(ort);
        		
        		result = queryManager.modifyAdresse(benutzer.getEmailadresse(), update_adresse);
        	}
    	}
    	
    	return result;
    }
    
    
    private boolean speicherBenutzer(HttpServletRequest req){
    	boolean result = false;
    	String vorname = req.getParameter("vorname");
    	String nachname = req.getParameter("nachname");
    	
    	if(vorname != null && !vorname.isEmpty() && nachname != null && !nachname.isEmpty()){
    		Benutzer update_benutzer = queryManager.getBenutzerByEMailAdresse(req.getSession().getAttribute("emailadresse").toString());
    		
    		if(update_benutzer != null){
    			update_benutzer.setVorname(vorname);
    			update_benutzer.setNachname(nachname);

        		result = queryManager.modifyBenutzer(update_benutzer);
    		}
    	}
    	
    	if(result){
    		return true;
    	}else{
    		return false;
    	}	
    }
    
    private void updateSessionDetails(HttpSession session, Benutzer benutzer){
    	session.setAttribute("vorname", benutzer.getVorname());
    	session.setAttribute("nachname", benutzer.getNachname());
    }

}
