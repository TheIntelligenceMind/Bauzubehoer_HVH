package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Adresse;
import entity.Benutzer;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;
/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/meineBestellungen")
public class BestellungenController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String dispatchSite = "index.jsp";

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher(dispatchSite);
		resp.setContentType("text/html"); 
		
		// Berechtigung f�r die Seite pr�fen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtBestellungen() != 1){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
			
		String method = req.getParameter("method");
		
		if(method == null){
			method = "";
		}
			
		switch(method){
			case "bestellungenAnzeigen":				
				resp.addHeader("contentSite", "meineBestellungenPanel");
				break;
			case "bestellungErfassenS1Anzeigen":
				
				break;
			case "bestellungErfassenS2Anzeigen":
				
				break;
			case "bestellungErfassenS3Anzeigen":
						
				break;
			case "bestellungErfassenS1Bestaetigt":
				bestellungS1Validieren(req, resp);
				break;
			case "bestellungErfassenS2Bestaetigt":
				bestellungS2Validieren(req, resp);
				break;
			case "bestellungErfassenS3Bestaetigt":
				bestellungS3Validieren(req, resp);			
				break;
			default:
				resp.addHeader("contentSite", "meineBestellungenPanel");
				break;
		}	
		
		rd = req.getRequestDispatcher(dispatchSite);
		rd.forward(req, resp);	
	}
    
    private boolean bestellungS1Validieren(HttpServletRequest req, HttpServletResponse resp){
    	Adresse lieferAdresse = new Adresse().init(req.getParameter("strasse"), req.getParameter("hausnummer"), req.getParameter("postleitzahl"), req.getParameter("ort"), "");
    	
    	req.getSession().setAttribute("bestellvorgang_lieferadresse", lieferAdresse);
    	
    	if(!lieferAdresse.getStrasse().isEmpty() && !lieferAdresse.getHausnummer().isEmpty() && !lieferAdresse.getPostleitzahl().isEmpty() && !lieferAdresse.getOrt().isEmpty()){
    		req.setAttribute("method", "bestellungErfassenS2Anzeigen");
    		dispatchSite = "/meineBestellungen";	
    	}else{
    		resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), "Die Lieferadresse ist nicht vollst�ndig.");
			
			req.setAttribute("method", "bestellungErfassenS1Anzeigen");
    		dispatchSite = "/meineBestellungen";
    	}
   	
    	return false;
    }
    
	private boolean bestellungS2Validieren(HttpServletRequest req, HttpServletResponse resp){
		return false; 	
	}
	
	private boolean bestellungS3Validieren(HttpServletRequest req, HttpServletResponse resp){
		return false;
	}
}
