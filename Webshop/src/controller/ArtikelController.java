package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import db.QueryManager;
import entity.Artikel;
import enums.MELDUNG_ART;
import enums.RESPONSE_STATUS;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/artikelAnlegen")
public class ArtikelController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_ARTIKELNUMMER = 9999;
	private static final QueryManager queryManager = QueryManager.getInstance();

    public ArtikelController() {
        super();

    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		
		resp.addHeader("contentSite", "artikelAnlegenPanel");
		
		rd.forward(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rq = req.getRequestDispatcher("index.jsp");
		
		boolean result = false;
		String fehlertext = null;
		
		int nummer = NumberUtils.toInt(req.getParameter("nummer"), 0);
		String bezeichnung = req.getParameter("bezeichnung");
		String beschreibung = req.getParameter("beschreibung");
		double preis = NumberUtils.toDouble(req.getParameter("preis"), 0.00);
		int lagermenge = NumberUtils.toInt(req.getParameter("lagermenge"), 0);
		
		if((fehlertext = validateAttributes(bezeichnung, nummer, beschreibung, preis, lagermenge)) == null){
			
			Artikel newArtikel = new Artikel().init(bezeichnung, nummer, beschreibung, preis, lagermenge, 1);
			
			result = QueryManager.getInstance().createArtikel(newArtikel);
		}
				
		if(result){
			String hinweistext = "Der Artikel wurde erfolgreich angelegt.";
			resp.addHeader("Status", RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);
		}else{
			String fehlermeldung = fehlertext.toString();	
			resp.addHeader("Status", RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
		}
				
		resp.addHeader("contentSite", "artikelAnlegenPanel");
	
		rq.forward(req, resp);		
	}
    
    private String validateAttributes(String piBezeichnung, int piNummer, String piBeschreibung, double piPreis, int piLagermenge){	
    	String fehlertext = null;
    	
    	if(piBezeichnung != null && piNummer != 0 && piBeschreibung != null	&& piPreis != 0 && piLagermenge >= 0){
    		if(piNummer <= MAX_ARTIKELNUMMER){
    			if(queryManager.searchArtikelByNummer(piNummer) == null){
        			return fehlertext;
        		}else{
        			fehlertext = "Die Artikelnummer wird schon verwendet.";
        		}
    		}else{
    			fehlertext = "Die Artikelnummer liegt außerhalb des Nummernbereichs";
    		}
    	}else{
    		fehlertext = "Bitte überprüfe die Eingaben auf Vollständigkeit und Gültigkeit.";
    	}
    	return fehlertext;
    }

}
