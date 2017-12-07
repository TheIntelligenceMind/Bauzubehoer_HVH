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
import enums.ENUM_MELDUNG_ART;
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
    	RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
    	resp.setContentType("text/html");
    	
    	// prüfen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){		
			if("artikelInDenWarenkorb".equals(req.getParameter("method"))){
				resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
				resp.addHeader("fehlermeldung", "Sie m&uuml;ssen angemeldet sein, um Artikel zu Ihrem Warenkorb hinzuf&uuml;gen zu können.");
			}
			
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
	    		artikelMengeVeraendern(req, resp);

	    		resp.addHeader("contentSite", "warenkorbPanel");
	    		break;
	    	default:
	    		resp.addHeader("contentSite", "warenkorbPanel");	
	    		break;   	
    	}		
		
		rd.forward(req, resp);
	}

    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Löscht durch die übergebenen 
	 * Tabellenzeile den Eintrag aus der Warenkorbtabelle und der DB.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
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

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode fügt einen übergebenen Artikel in den 
	 * Warenkorb des Benutzers hinzu und updated die Session Attribute.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
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
    
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode updated das Sessionattribut "warenkorbartikelliste".
	 * </pre>
	 *  @param req
	 */
    private void updateWarenkorb(HttpServletRequest req){
    	String benutzerEmailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }

    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode überprüft die übergebenen Artikelnummer 
	 * und fügt danach den Artikel dem Warenkorb hinzu.
	 * </pre>
	 *  @param req
	 */
    private boolean artikelHinzufuegen(HttpServletRequest req){  	
    	int artikelnummer = 0;
    	int menge = NumberUtils.toInt(req.getParameter("artikelmenge"), 1);
    	boolean added = false;
    	String emailadresseBenutzer = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	 	
    	artikelnummer = NumberUtils.toInt(req.getParameter("artikelnummer"), -1);
    	
    	if(artikelnummer != -1){
    		added = queryManager.addArtikelToWarenkorb(emailadresseBenutzer, artikelnummer, menge);  	
    	}
    	
    	return added;
    }
    
    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode ändert die Menge 
	 * des übergebenen Artikels im Warenkorb.
	 * </pre>
	 *  @param req
	 */
    private void artikelMengeVeraendern(HttpServletRequest req, HttpServletResponse resp){
    	int menge = NumberUtils.toInt(req.getParameter("menge"), -1);
    	int artikelnummer = NumberUtils.toInt(req.getParameter("artikelnummer"), -1);
    	String emailadresseBenutzer = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();

    	if(menge > 0 && artikelnummer != -1){
    		boolean result = queryManager.modifyWarenkorbArtikelMenge(menge, artikelnummer, emailadresseBenutzer);
    		
    		if(result){
    			updateWarenkorb(req);
        		
        		resp.addHeader("status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
    			resp.addHeader("hinweismeldung", "Die Artikelmenge wurde ge&auml;ndert.");
    		}else{
    			resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
    			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), "Es ist ein unerwarteter Fehler beim &auml;ndern der Artikelmenge aufgetreten.");	
    		} 		
    	}else if(menge == -1 || menge == 0){
    		resp.addHeader("status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), "Die ausgew&auml;hlte Menge ist nicht g&uuml;ltig.");	
    	}
    }
}
