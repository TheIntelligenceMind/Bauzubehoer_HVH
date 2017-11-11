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
import entity.Benutzer;
import entity.WarenkorbArtikel;
import enums.RESPONSE_STATUS;

/**
 * Servlet implementation class WarenkorbController
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
	    		resp.addHeader("contentSite", "warenkorbPanel");	
	    		break;
	    	case "artikelInDenWarenkorb": 		
	    		if(artikelHinzufuegen(req)){
	    			updateWarenkorb(req);
	    			
	    			resp.addHeader("status", RESPONSE_STATUS.HINWEIS.toString());
    				resp.addHeader("hinweismeldung", "Der Artikel wurde dem Warenkorb hinzugef&uuml;gt.");
	    		}else{
	    			resp.addHeader("status", RESPONSE_STATUS.FEHLER.toString());
    				resp.addHeader("fehlermeldung", "Der Artikel konnte nicht hinzugef&uuml;gt werden.");	
	    		}
	    		
	    		rd = req.getRequestDispatcher("/suchen");
	    		break;
	    	case "artikelAusWarenkorbLoeschen":
	    		if(req.getParameter("row") != null){
	    			int row = Integer.valueOf(req.getParameter("row") );
	    			boolean hasDeleted = false;  
	    			
	    			hasDeleted = queryManager.deleteArtikelFromWarenkorb(row, ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());
	
	    			if(hasDeleted){
	    				updateWarenkorb(req);
	    				
	    				resp.addHeader("status", RESPONSE_STATUS.HINWEIS.toString());
	    				resp.addHeader("hinweismeldung", "Der Artikel wurde aus dem Warenkorb entfernt.");
	    			}else{
	    				resp.addHeader("status", RESPONSE_STATUS.FEHLER.toString());
	    				resp.addHeader("fehlermeldung", "Es ist ein Problem beim L&ouml;schen aufgetreten.");	
	    			}
	    		}
	  	    		
	    		resp.addHeader("contentSite", "warenkorbPanel");		
	    		break;    		
	    	case "artikelMengeVeraendern":
	    		if(artikelMengeVeraendern(req)){
	    			updateWarenkorb(req);
	    			
    				resp.addHeader("status", RESPONSE_STATUS.HINWEIS.toString());
    				resp.addHeader("hinweismeldung", "Die Menge des Artikels wurde ge&auml;ndert.");
    			}else{
    				resp.addHeader("status", RESPONSE_STATUS.FEHLER.toString());
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
    
    private void updateWarenkorb(HttpServletRequest req){
    	String benutzerEmailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }

    private boolean artikelHinzufuegen(HttpServletRequest req){  	
    	int artikelnummer = 0;
    	boolean added = false;
    	String emailadresseBenutzer = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	try{
    		artikelnummer = Integer.valueOf(req.getParameter("artikelnummer"));
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    	
    	added = queryManager.addArtikelToWarenkorb(emailadresseBenutzer, artikelnummer);
     	
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
