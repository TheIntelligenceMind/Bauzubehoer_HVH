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
import entity.WarenkorbArtikel;
import enums.RESPONSE_STATUS;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/warenkorb")
public class WarenkorbController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final QueryManager queryManager = QueryManager.getInstance();
	
    public WarenkorbController() {
        super();

    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req, resp);
    }

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
    	RequestDispatcher rq = req.getRequestDispatcher("index.jsp");; 
    	resp.setContentType("text/html");
    		  	
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
	    		
	    		rq = req.getRequestDispatcher("/suchen");
	    		break;
	    	case "artikelAusWarenkorbLoeschen":
	    		if(req.getParameter("row") != null){
	    			int row = Integer.valueOf(req.getParameter("row") );
	    			boolean hasDeleted = false;  
	    			
	    			hasDeleted = queryManager.deleteArtikelFromWarenkorb(row, req.getSession().getAttribute("emailadresse").toString());
	
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
		
		
		
		rq.forward(req, resp);
	}
    
    private void updateWarenkorb(HttpServletRequest req){
    	String benutzerEmailadresse = req.getSession().getAttribute("emailadresse").toString();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }

    private boolean artikelHinzufuegen(HttpServletRequest req){  	
    	int artikelnummer = 0;
    	boolean added = false;
    	String emailadresseBenutzer = String.valueOf(req.getSession().getAttribute("emailadresse"));
    	
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
    		emailadresseBenutzer = String.valueOf(req.getSession().getAttribute("emailadresse"));
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
  	
    	changed = queryManager.modifyWarenkorbArtikelMenge(menge, artikelnummer, emailadresseBenutzer);
    	
    	return changed;
    }
}
