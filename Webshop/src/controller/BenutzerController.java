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
import entity.Artikel;
import entity.Benutzer;
import entity.WarenkorbArtikel;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Benutzer zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 *  @author Tim Hermbecker
 */
@WebServlet("/benutzer")
public class BenutzerController extends HttpServlet {
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
		default:
			resp.addHeader("contentSite", "benutzerstammdatenPanel");
			break;		
		}

		rd.forward(req, resp);		
	}
 
    private void benutzerstammdatenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
    	List<Benutzer> benutzerliste = null;
		
    	//benutzerliste = queryManager.selectAllMaBenutzer();

		req.setAttribute("benutzerstammdatenListe", benutzerliste);
				
		resp.addHeader("contentSite", "benutzerstammdatenPanel");   	
    }

    private void benutzerAnlegenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
		resp.addHeader("contentSite", "benutzerAnlegenPanel");   	
    }
    
    private void benutzerAnlegen(HttpServletRequest req, HttpServletResponse resp){
		resp.addHeader("contentSite", "benutzerBearbeitenAnzeigen");   	
    }
    
    private void benutzerBearbeitenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
		
    	
    	
    	
    	resp.addHeader("contentSite", "benutzerstammdatenPanel");   	
    }
    
    private void benutzerBearbeiten(HttpServletRequest req, HttpServletResponse resp){
		
    	
    	
    	resp.addHeader("contentSite", "benutzerBearbeitenAnzeigen");   	
    }
}
