package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import db.QueryManager;
import entity.Adresse;
import entity.Artikel;
import entity.Benutzer;
import entity.BestellArtikel;
import entity.Bestellung;
import entity.WarenkorbArtikel;
import enums.ENUM_BESTELLSTATUS;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;
import helper.AdressenHelper;
import helper.MailHelper;
/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Bestellungen zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 * @author Tim Hermbecker
 */
@WebServlet("/meineBestellungen")
public class BestellungenController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static QueryManager queryManager = QueryManager.getInstance();
	private final static MailHelper mailHelper = MailHelper.getInstance();
	private String dispatchSite = "index.jsp";

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = null;
		resp.setContentType("text/html"); 
		
		// prüfen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){
			rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
		}
		
		
		// Berechtigung für die Seite prüfen
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
			case "bestellungenstammdatenAnzeigen":
				bestellungstammdatenAnzeigen(req, resp);
				break;
			case "bestellungBearbeitenAnzeigen":
				bestellungBearbeitenAnzeigen(req, resp);
				break;
			case "bestellungenAnzeigen":				
				resp.addHeader("contentSite", "meineBestellungenPanel");
				break;
			case "bestellungErfassenS1Anzeigen":
				bestellungErfassenS1Anzeigen(req, resp);
				break;
			case "bestellungErfassenS2Anzeigen":
				dispatchSite = "index.jsp";
				resp.addHeader("contentSite", "bestellungZahlungsartenPanel");
				break;
			case "bestellungErfassenS3Anzeigen":
				dispatchSite = "index.jsp";
				resp.addHeader("contentSite", "bestellungZusammenfassungPanel");	
				break;
			case "bestellungErfassenS4Anzeigen":
				dispatchSite = "index.jsp";
				resp.addHeader("contentSite", "bestellungAbschlussPanel");	
				break;
			case "bestellungErfassenS1Bestaetigt":
				bestellungS1Validieren(req, resp);
				break;
			case "bestellungErfassenS2Bestaetigt":
				bestellungS2Validieren(req, resp);
				break;
			case "bestellungErfassenS3Bestaetigt":
				bestellungAbschliessen(req, resp);			
				break;
			default:
				resp.addHeader("contentSite", "meineBestellungenPanel");
				break;
		}	
		
		rd = req.getRequestDispatcher(dispatchSite);
		rd.forward(req, resp);	
	}

    private void bestellungBearbeitenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
    	int bestellnummer = NumberUtils.toInt(req.getParameter("bestellnummer"), -1);
    	Bestellung bestellung = queryManager.getBestellungByBestellungID(bestellnummer);
    	List<BestellArtikel> bestellartikellliste = queryManager.getAllArtikelByBestellungID(bestellnummer);
    	
    	if(bestellung == null){
    		resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), "Daten zur Bestellung konnten nicht abgerufen werden.");
			bestellungstammdatenAnzeigen(req, resp);
    	}else{
    		req.setAttribute("bearbeitenBestellung", bestellung);
        	req.setAttribute("bearbeitenBestellungArtikelliste", bestellartikellliste);
        	resp.addHeader("contentSite", "bestellungBearbeitenPanel");
    	}
	
    }
    
    /**
	 * <pre><h3>Beschreibung:</h3>
	 * Die Methode leitet zu der Bestellungstammdaten Ansicht mit allen
	 * dazugehörigen Daten weiter
	 * </pre> 
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse 
	 */	
    private void bestellungstammdatenAnzeigen(HttpServletRequest req, HttpServletResponse resp){
		req.setAttribute("bestellungstammdatenListe", getBestellungstammdatenListe(req, resp));		
		resp.addHeader("contentSite", "bestellungstammdatenPanel");   	
    }
    
    /**
	 * <pre><h3>Beschreibung:</h3>
	 * Die Methode liefert eine Liste mit allen vorhandenen Bestellungen zurück
	 * </pre> 
	 * @param req HttpServletRequest
	 * @param resp HttpServletResponse 
	 * @return bestellungenliste
	 */	
    private List<Bestellung> getBestellungstammdatenListe(HttpServletRequest req, HttpServletResponse resp){
    	List<Bestellung> bestellungenliste = null;
		
    	bestellungenliste = queryManager.selectAllBestellungen();

    	return bestellungenliste;
    }
    
	private void bestellungErfassenS1Anzeigen(HttpServletRequest req, HttpServletResponse resp) {
		String emailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
		Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);			
		req.setAttribute("benutzer", benutzer);
		dispatchSite = "index.jsp";
		resp.addHeader("contentSite", "bestellungLieferadressePanel");
	}
    
    private void bestellungS1Validieren(HttpServletRequest req, HttpServletResponse resp){
    	Adresse lieferAdresse = new Adresse().init(req.getParameter("strasse"), req.getParameter("hausnummer"), req.getParameter("postleitzahl"), req.getParameter("ort"), "");
    	
    	req.getSession().setAttribute("bestellvorgang_lieferadresse", lieferAdresse);
    	
    	if(!lieferAdresse.getStrasse().isEmpty() && !lieferAdresse.getHausnummer().isEmpty() && !lieferAdresse.getPostleitzahl().isEmpty() && !lieferAdresse.getOrt().isEmpty()){

    		if(AdressenHelper.getInstance().validateAdresse(lieferAdresse)){
    			dispatchSite = "/meineBestellungen?method=bestellungErfassenS2Anzeigen";	
    		}else{
    			Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());			
    			req.setAttribute("benutzer", benutzer);
    			
    			dispatchSite = "index.jsp";
    			resp.addHeader("contentSite", "bestellungLieferadressePanel");
    			
    			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
    			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), "Die Lieferadresse ist nicht gültig.");
    		}		
    	}else{
			Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse());			
			req.setAttribute("benutzer", benutzer);
			
			dispatchSite = "index.jsp";
			resp.addHeader("contentSite", "bestellungLieferadressePanel");
			
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), "Die Lieferadresse ist nicht vollständig.");
    	}
    }
    
	private void bestellungS2Validieren(HttpServletRequest req, HttpServletResponse resp){
		String zahlungsart = req.getParameter("zahlungsart");
		
		if(zahlungsart != null && !zahlungsart.isEmpty()){
			req.getSession().setAttribute("bestellvorgang_zahlungsart", zahlungsart);
			dispatchSite = "/meineBestellungen?method=bestellungErfassenS3Anzeigen";
		}else{
			dispatchSite = "index.jsp";
			resp.addHeader("contentSite", "bestellungZahlungsartenPanel");
			
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), "Es wurde keine Zahlungsart ausgew&auml;hlt");
		}
	}
	
	private void bestellungAbschliessen(HttpServletRequest req, HttpServletResponse resp){
		double bestellwert = 0.0;
		double versandkosten = 20.00; // pauschal 20€
		String zahlungsart = (String) req.getSession().getAttribute("bestellvorgang_zahlungsart");
		
		List<WarenkorbArtikel> bestellartikelliste = (List<WarenkorbArtikel>)req.getSession().getAttribute("warenkorbartikelliste");
		
		for(WarenkorbArtikel artikel : bestellartikelliste){
			Artikel db_Artikel = queryManager.searchArtikelByNummer(artikel.getArtikel().getNummer());
			db_Artikel.setLagermenge(db_Artikel.getLagermenge() - artikel.getMenge());
			
			if(db_Artikel.getLagermenge() < 0){
				db_Artikel.setLagermenge(0);
			}
			// Lagermenge mit der bestellten Menge dekrementieren, falls der Lagerbestand unter 0 sinken sollte trotzdem auf 0 updaten
			queryManager.modifyArtikel(db_Artikel);
			
			bestellwert += (artikel.getArtikel().getPreis() * artikel.getMenge());
		}
		
		Benutzer benutzer = (Benutzer)req.getSession().getAttribute("benutzer");
		Bestellung bestellung = new Bestellung().init("", new Date(), ENUM_BESTELLSTATUS.NEU.toString(), zahlungsart, null, bestellwert, versandkosten, benutzer);
		
		bestellung = queryManager.createBestellung(bestellung, benutzer);
		
		if(bestellung != null){
			mailHelper.sendRechnungsmail(benutzer, bestellung, bestellartikelliste);
	 		
			// Warenkorbartikel Anzeige im Benutzerpanel aktualisieren
			updateWarenkorbArtikel(req);
			// Bestellungen aktualisieren
			req.getSession().setAttribute("bestellungenliste" , queryManager.selectAllBestellungenByBenutzer(benutzer));
			
			dispatchSite = "/meineBestellungen?method=bestellungErfassenS4Anzeigen";
		}else{
			req.setAttribute("benutzer", benutzer);
			
			dispatchSite = "index.jsp";
			resp.addHeader("contentSite", "bestellungZusammenfassungPanel");
			
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), "Es ist ein unerwarteter Fehler beim Abschliessen der Bestellung aufgetreten.");
		}
			
	}
	
	private void updateWarenkorbArtikel(HttpServletRequest req){
    	String benutzerEmailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert den aktuellen Zeitstempel
	 * </pre>
	 * 
	 * @return Timestamp
	 */	
	public Timestamp getCurrentTimestamp(){
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	return timestamp;
	}
}
