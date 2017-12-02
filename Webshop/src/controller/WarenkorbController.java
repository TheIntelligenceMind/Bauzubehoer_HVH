package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import db.QueryManager;
import entity.Benutzer;
import entity.WarenkorbArtikel;
import enums.ENUM_RESPONSE_STATUS;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Warenkorb zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 * @author Tim Hermbecker
 */
@WebServlet("/warenkorb")
public class WarenkorbController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final QueryManager queryManager = QueryManager.getInstance();

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req, resp);
    }

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
    	RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
    	resp.setContentType("text/html");
    	
    	// prüfen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){
			rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
		}
    	
    	// Berechtigung für die Seite prüfen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtWarenkorb() != 1){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
    	
    	
		String method = req.getParameter("method");
		
		if(method == null){
			method = "";
		}
    	
    	switch(method){
	    	case "warenkorbAnzeigen":
	    		updateWarenkorb(req);
	    		
	    		resp.addHeader("contentSite", "warenkorbPanel");	
	    		break;
	    	case "artikelInDenWarenkorb": 		
	    		artikelInDenWarenkorb(req, resp);
	    		
	    		rd = req.getRequestDispatcher("/suchen");
	    		break;
	    	case "artikelAusWarenkorbLoeschen":
	    		artikelAusWarenkorbLoeschen(req, resp);		
	    		break;    		
	    	case "artikelMengeVeraendern":
	    		if(artikelMengeVeraendern(req)){
	    			updateWarenkorb(req);
	    			
    				resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
    				resp.addHeader("hinweismeldung", "Die Menge des Artikels wurde ge&auml;ndert.");
    			}else{
    				resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
    				resp.addHeader("fehlermeldung", "Die Menge des Artikels konnte leider nicht ge&auml;ndert werden.");	
    			}
	    		
	    		resp.addHeader("contentSite", "warenkorbPanel");
	    		break;
	    	default:
	    		resp.addHeader("contentSite", "warenkorbPanel");	
	    		break;   	
    	}		
		
		rd.forward(req, resp);
	}

	private void artikelAusWarenkorbLoeschen(HttpServletRequest req, HttpServletResponse resp) {
		if(req.getParameter("row") != null){
			int row = Integer.valueOf(req.getParameter("row") );
			boolean hasDeleted = false;  
			
			hasDeleted = queryManager.deleteArtikelFromWarenkorb(row, ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());

			if(hasDeleted){
				updateWarenkorb(req);
				
				resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
				resp.addHeader("hinweismeldung", "Der Artikel wurde aus dem Warenkorb entfernt.");
			}else{
				resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
				resp.addHeader("fehlermeldung", "Es ist ein Problem beim L&ouml;schen aufgetreten.");	
			}
		}
			
		resp.addHeader("contentSite", "warenkorbPanel");
	}

	private void artikelInDenWarenkorb(HttpServletRequest req, HttpServletResponse resp) {
		if(artikelHinzufuegen(req)){
			updateWarenkorb(req);
			
			resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader("hinweismeldung", "Der Artikel wurde dem Warenkorb hinzugef&uuml;gt.");
		}else{
			resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader("fehlermeldung", "Der Artikel konnte nicht hinzugef&uuml;gt werden.");	
		}
	}
    
    private void updateWarenkorb(HttpServletRequest req){
    	String benutzerEmailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }

    private boolean artikelHinzufuegen(HttpServletRequest req){  	
    	int artikelnummer = 0;
    	int menge = NumberUtils.toInt(req.getParameter("artikelmenge"), 1);
    	boolean added = false;
    	String emailadresseBenutzer = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	try{
    		artikelnummer = Integer.valueOf(req.getParameter("artikelnummer"));
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	
    	added = queryManager.addArtikelToWarenkorb(emailadresseBenutzer, artikelnummer, menge);
     	
    	return added;
    }
    
    private boolean artikelMengeVeraendern(HttpServletRequest req){
    	boolean changed = false;
    	int menge;
    	int artikelnummer;
    	String emailadresseBenutzer = null;
    	
    	try{
    		menge = Integer.valueOf(req.getParameter("menge"));
    		artikelnummer = Integer.valueOf(req.getParameter("artikelnummer"));
    		emailadresseBenutzer = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
  	
    	changed = queryManager.modifyWarenkorbArtikelMenge(menge, artikelnummer, emailadresseBenutzer);
    	
    	return changed;
    }
}
