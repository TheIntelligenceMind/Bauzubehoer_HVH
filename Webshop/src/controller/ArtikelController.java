package controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.lang3.math.NumberUtils;

import db.QueryManager;
import entity.Artikel;
import entity.Benutzer;
import entity.WarenkorbArtikel;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;
import helper.UploadHelper;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Artikel zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 *  @author Tim Hermbecker
 */
@WebServlet("/artikel")
@MultipartConfig
public class ArtikelController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final int MAX_ARTIKELNUMMER = 9999;
	private static final QueryManager queryManager = QueryManager.getInstance();
	private static final UploadHelper uploadHelper = UploadHelper.getInstance();
	
	private String dispatchSite = "index.jsp";
	private Artikel artikel = null;
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode erhält alle GET-Aufrufe 
	 * und leitet diese an die doPost() Methode weiter
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode erhält alle POST-Aufrufe und die weitergeleiteten Aufrufe der doGet() Methode.
	 * Hier werden die verschiedenen Aufrufe verarbeitet. Durch den "method"-Parameter wird bestimmt, 
	 * welche Funktionen durch den Controller ausgeführt werden sollen.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
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
		case "artikelBildHochladenAnlegen":
			bildHochladenAnlegen(req, resp);
			break;
		case "artikelBildHochladenBearbeiten":
			bildHochladenBearbeiten(req, resp);
			break;
		default:
			resp.addHeader("contentSite", "artikelstammdatenAnzeigen");
			break;		
		}

		rd = req.getRequestDispatcher(dispatchSite);
		rd.forward(req, resp);		
	}
    
    
    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode speichert die Artikeldaten zu einem veränderten Artikel in der DB.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
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
			artikel.init("", -1, "", -1, -1, -1, null, "", "", -1);
		}else{
			artikel = queryManager.searchArtikelByNummer(Integer.valueOf(req.getParameter("nummer")));
		}

		req.setAttribute("bearbeitenArtikel", artikel);
		resp.addHeader("contentSite", "artikelBearbeitenPanel");
	}

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode leitet an die "Artikel Bearbeiten" Ansicht weiter.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
	private void artikelBearbeitenAnzeigen(HttpServletRequest req, HttpServletResponse resp) {
		if(req.getParameter("artikelnummer") != null){	

			int artikelnummer = Integer.valueOf(req.getParameter("artikelnummer"));
			
			artikel = queryManager.searchArtikelByNummer(artikelnummer);								
		}else{
			artikel = new Artikel();
			artikel.init("", -1, "", -1, -1, -1, null, "", "", -1);
		}
		
		req.setAttribute("bearbeitenArtikel", artikel);
		resp.addHeader("contentSite", "artikelBearbeitenPanel");
	}

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode leitet zu der Artikelstammdatenansicht mit den 
	 * dazugehörigen Daten weiter
	 * </pre>
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse
	 */
	private void artikelstammdatenAnzeigen(HttpServletRequest req, HttpServletResponse resp) {
		List<Artikel> artikelliste = null;
		
		artikelliste = queryManager.selectAllArtikel(false);

		req.setAttribute("artikelListeMitarbeiter", artikelliste);
				
		resp.addHeader("contentSite", "artikelstammdatenPanel");
	}

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode legt einen Artikel mit den übergebenen Artikeldaten in der DB an. 
	 * Danach wird an die Artikelstammdaten Ansicht weitergeleitet.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
	private void artikelAnlegen(HttpServletRequest req, HttpServletResponse resp) {
		boolean result = false;
		String fehlertext = null;
		Artikel anlegenArtikel = null;
		
		int nummer = NumberUtils.toInt(req.getParameter("nummer"), 0);
		String bezeichnung = req.getParameter("bezeichnung");
		String beschreibung = req.getParameter("beschreibung");
		double preis = NumberUtils.toDouble(req.getParameter("preis"), 0.00);
		int lagermenge = NumberUtils.toInt(req.getParameter("lagermenge"), 0);
		int meldebestand = NumberUtils.toInt(req.getParameter("meldebestand"), 0);
		String kategorie_1 = req.getParameter("kategorie_1");
		String kategorie_2 = req.getParameter("kategorie_2");
		String bildString = req.getParameter("bildString");
		byte[] bildBytes = null;
		
		if(bildString != null && !bildString.isEmpty()){
			bildBytes = org.apache.commons.codec.binary.Base64.decodeBase64(bildString);
		}

		anlegenArtikel = new Artikel().init(bezeichnung, nummer, beschreibung, preis, lagermenge, meldebestand, bildBytes,
				kategorie_1, kategorie_2, 1);
		
		if((fehlertext = validateAttributes(req, true)) == null){	
			result = QueryManager.getInstance().createArtikel(anlegenArtikel);
		}
				
		if(result){
			String hinweistext = "Der Artikel wurde erfolgreich angelegt.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);
			
			// Zur Artikelstammdaten Ansicht weiterleiten
			artikelstammdatenAnzeigen(req, resp);
		}else{
			String fehlermeldung = fehlertext.toString();	
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
			req.setAttribute("anlegenArtikel", anlegenArtikel);
			
			resp.addHeader("contentSite", "artikelAnlegenPanel");
		}
			
	}
    
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode aktualisiert die Warenkorbartikel Liste in der Session.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
    private void updateWarenkorbArtikel(HttpServletRequest req){
    	String benutzerEmailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }
    
    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode lädt ein ausgewähltes Bild für die "Artikel Anlegen" Ansicht hoch.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
    private void bildHochladenAnlegen(HttpServletRequest req, HttpServletResponse resp){ 
    	Part filePart = null;
    	
		try {
			filePart = req.getPart("bild");
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}

		Artikel anlegenArtikel = new Artikel().init("", -1, "", -1, -1, -1, null, "", "", 0);
		
		anlegenArtikel.setBild(uploadHelper.bildHochladen(filePart));
		
		req.setAttribute("anlegenArtikel", anlegenArtikel);
		resp.addHeader("contentSite", "artikelAnlegenPanel");	    
    }
    
    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode lädt ein ausgewähltes Bild für die "Artikel Bearbeiten" Ansicht hoch.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
    private void bildHochladenBearbeiten(HttpServletRequest req, HttpServletResponse resp){ 
    	Part filePart = null;
    	int artikelnummer = NumberUtils.toInt(req.getParameter("nummer"));
    	
		try {
			filePart = req.getPart("bild");
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	    	  
		Artikel artikel = queryManager.searchArtikelByNummer(artikelnummer);
		artikel.setBild(uploadHelper.bildHochladen(filePart));
	    
	    queryManager.modifyArtikel(artikel);
	    
		req.setAttribute("bearbeitenArtikel", artikel);
		resp.addHeader("contentSite", "artikelBearbeitenPanel");  
    }
   
    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode speichert die 
	 * übergebenen Artikeldaten zu einem Artikel in der Datenbank.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
    private boolean artikelSpeichern(HttpServletRequest req){
    	if(validateAttributes(req, false) == null){
    		Artikel artikel_save = null;
    		
	    	String bezeichnung = req.getParameter("bezeichnung");
	    	int nummer = NumberUtils.toInt(req.getParameter("nummer"));
	    	String beschreibung = req.getParameter("beschreibung");
	    	double preis = NumberUtils.toDouble(req.getParameter("preis"));
	    	int lagermenge = NumberUtils.toInt(req.getParameter("lagermenge"));
	    	int meldebestand = NumberUtils.toInt(req.getParameter("meldebestand"), 0);
			String kategorie_1 = req.getParameter("kategorie_1");
			String kategorie_2 = req.getParameter("kategorie_2");
	    	int aktiv = NumberUtils.toInt(req.getParameter("aktiv"));
	    	String bildString = req.getParameter("bildString");
	    	byte[] bildBytes = null;
	    	
	    	if(bildString != null && !bildString.isEmpty()){
	    		bildBytes = org.apache.commons.codec.binary.Base64.decodeBase64(bildString);
	    	}
	    	
    		artikel_save = new Artikel().init(bezeichnung, nummer, beschreibung, preis, lagermenge, meldebestand, bildBytes,
	    			kategorie_1, kategorie_2, aktiv);
    		
    		// prüfen ob es den Artikel gibt
	    	if(queryManager.searchArtikelByNummer(nummer) != null){
	    		return queryManager.modifyArtikel(artikel_save);
	    	} 	
	    	    	
    	}
    	
    	return false;
    }
    
    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode validiert alle übergebenen Artikelattribute.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
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
        			fehlertext = "Die Artikelnummer liegt außerhalb des Nummernbereichs";
        		}			
    		}else{
    			fehlertext = "Es m&uuml;ssen Artikelkategorien für den Artikel ausgew&auml;hlt werden";
    		}
    	}else{
    		fehlertext = "Bitte &uuml;berpr&uuml;fe die Eingaben auf Vollst&auml;ndigkeit und G&uuml;ltigkeit.";
    	}
    	return fehlertext;
    }

}
