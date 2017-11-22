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
 * <h3>Beschreibung:</h3> Die Klasse ist f�r den Themenbereich Artikel zust�ndig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 *  @author Tim Hermbecker
 */
@WebServlet("/artikel")
public class ArtikelController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_ARTIKELNUMMER = 9999;
	private static final QueryManager queryManager = QueryManager.getInstance();

	private Artikel artikel = null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		resp.setContentType("text/html"); 	
		
		// pr�fen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){
			rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
		}
		
		// Berechtigung f�r die Seite pr�fen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtArtikelstammdaten() != 1){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
    		
		String method = req.getParameter("method");
		
		if(method == null){
			method = "";
		}
		
		switch(method){
		case "artikelstammdatenAnzeigen":
			artikelstammdatenAnzeigen(req, resp);
			
			break;
		case "artikelAnlegenAnzeigen":
			resp.addHeader("contentSite", "artikelAnlegenPanel");	
			break;
		case "artikelAnlegen":		
			artikelAnlegen(req, resp);
			
			break;
		case "artikelBearbeitenAnzeigen":		
			artikelBearbeitenAnzeigen(req, resp);
			break;				
		case "artikelBearbeiten":			
			artikelBearbeiten(req, resp);
			break;	
		default:
			resp.addHeader("contentSite", "artikelstammdatenAnzeigen");
			break;		
		}

		rd.forward(req, resp);		
	}

	private void artikelBearbeiten(HttpServletRequest req, HttpServletResponse resp) {
		if(artikelSpeichern(req)){
			updateWarenkorbArtikel(req);
			
			String hinweistext = "Die &Auml;nderungen wurden erfolgreich gespeichert.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);		
		}else{
			String fehlermeldung = "ung&uuml;ltige &Auml;nderungen.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
		}
		
		if(req.getParameter("nummer") == null){
			artikel = new Artikel();
			artikel.init("", -1, "", -1, -1, "", "", -1);
		}else{
			artikel = queryManager.searchArtikelByNummer(Integer.valueOf(req.getParameter("nummer")));
		}

		req.setAttribute("bearbeitenArtikel", artikel);
		resp.addHeader("contentSite", "artikelBearbeitenPanel");
	}

	private void artikelBearbeitenAnzeigen(HttpServletRequest req, HttpServletResponse resp) {
		if(req.getParameter("artikelnummer") != null){	

			int artikelnummer = Integer.valueOf(req.getParameter("artikelnummer"));
			
			artikel = queryManager.searchArtikelByNummer(artikelnummer);								
		}else{
			artikel = new Artikel();
			artikel.init("", -1, "", -1, -1, "", "", -1);
		}
		
		req.setAttribute("bearbeitenArtikel", artikel);
		resp.addHeader("contentSite", "artikelBearbeitenPanel");
	}

	private void artikelstammdatenAnzeigen(HttpServletRequest req, HttpServletResponse resp) {
		List<Artikel> artikelliste = null;
		
		artikelliste = queryManager.selectAllArtikel(false);

		req.setAttribute("artikelListeMitarbeiter", artikelliste);
				
		resp.addHeader("contentSite", "artikelstammdatenPanel");
	}

	private void artikelAnlegen(HttpServletRequest req, HttpServletResponse resp) {
		boolean result = false;
		String fehlertext = null;
		Artikel anlegenArtikel = null;
		
		int nummer = NumberUtils.toInt(req.getParameter("nummer"), 0);
		String bezeichnung = req.getParameter("bezeichnung");
		String beschreibung = req.getParameter("beschreibung");
		double preis = NumberUtils.toDouble(req.getParameter("preis"), 0.00);
		int lagermenge = NumberUtils.toInt(req.getParameter("lagermenge"), 0);
		//int meldebestand = NumberUtils.toInt(req.getParameter("meldebestand"), 0);
		String kategorie_1 = req.getParameter("kategorie_1");
		String kategorie_2 = req.getParameter("kategorie_2");
		
		anlegenArtikel = new Artikel().init(bezeichnung, nummer, beschreibung, preis, lagermenge, //meldebestand,
				kategorie_1, kategorie_2, 1);
		
		if((fehlertext = validateAttributes(req, true)) == null){	
			result = QueryManager.getInstance().createArtikel(anlegenArtikel);
		}
				
		if(result){
			String hinweistext = "Der Artikel wurde erfolgreich angelegt.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);
		}else{
			String fehlermeldung = fehlertext.toString();	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
			req.setAttribute("anlegenArtikel", anlegenArtikel);
		}
				
		resp.addHeader("contentSite", "artikelAnlegenPanel");
	}
    
    private void updateWarenkorbArtikel(HttpServletRequest req){
    	String benutzerEmailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }
    
    private boolean artikelSpeichern(HttpServletRequest req){
    	if(validateAttributes(req, false) == null){
	    	String bezeichnung = req.getParameter("bezeichnung");
	    	int nummer = NumberUtils.toInt(req.getParameter("nummer"));
	    	String beschreibung = req.getParameter("beschreibung");
	    	double preis = NumberUtils.toDouble(req.getParameter("preis"));
	    	int lagermenge = NumberUtils.toInt(req.getParameter("lagermenge"));
	    	//int meldebestand = NumberUtils.toInt(req.getParameter("meldebestand"), 0);
			String kategorie_1 = req.getParameter("kategorie_1");
			String kategorie_2 = req.getParameter("kategorie_2");
	    	int aktiv = NumberUtils.toInt(req.getParameter("aktiv"));
	    	  	
	    	Artikel artikel_save = new Artikel().init(bezeichnung, nummer, beschreibung, preis, lagermenge, //meldebestand,
	    			kategorie_1, kategorie_2, aktiv);
	    	
	    	// pr�fen ob es den Artikel gibt
	    	if(queryManager.searchArtikelByNummer(nummer) != null){
	    		return queryManager.modifyArtikel(artikel_save);
	    	}
    	}
    	
    	return false;
    }
    
    private String validateAttributes(HttpServletRequest req, boolean initial){	
    	String fehlertext = null;
    	int nummer = NumberUtils.toInt(req.getParameter("nummer"), 0);
		String bezeichnung = req.getParameter("bezeichnung");
		String beschreibung = req.getParameter("beschreibung");
		double preis = NumberUtils.toDouble(req.getParameter("preis"), 0.00);
		int lagermenge = NumberUtils.toInt(req.getParameter("lagermenge"), 0);
		//int meldebestand = NumberUtils.toInt(req.getParameter("meldebestand"), 0);
		String kategorie_1 = req.getParameter("kategorie_1");
		String kategorie_2 = req.getParameter("kategorie_2");
		int aktiv = 1;
		
		if(!initial){
			aktiv = NumberUtils.toInt(req.getParameter("aktiv"));
		}
		
    	if(bezeichnung != null && nummer != 0 && beschreibung != null && preis != 0 && lagermenge >= 0 /* && meldebestand >= 0 */ && (aktiv == 0 || aktiv == 1)){
    		if(kategorie_1 != null && !kategorie_1.equals("artikelkategorie_1") && kategorie_2 != null && !kategorie_2.equals("artikelkategorie_2")){
    			if(nummer <= MAX_ARTIKELNUMMER){
    				if(initial){
    					if(queryManager.searchArtikelByNummer(nummer) == null){
            				return null;
                		}else{
                			fehlertext = "Die Artikelnummer wird schon verwendet.";
                		}
    				}
        		}else{
        			fehlertext = "Die Artikelnummer liegt au�erhalb des Nummernbereichs";
        		}			
    		}else{
    			fehlertext = "Es m&uuml;ssen Artikelkategorien f�r den Artikel ausgew&auml;hlt werden";
    		}
    	}else{
    		fehlertext = "Bitte &uuml;berpr&uuml;fe die Eingaben auf Vollst&auml;ndigkeit und G&uuml;ltigkeit.";
    	}
    	return fehlertext;
    }

}
