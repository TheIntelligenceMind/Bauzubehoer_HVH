package controller;

import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import db.QueryManager;
import entity.Benutzer;
import entity.Bestellung;
import entity.WarenkorbArtikel;
import enums.RESPONSE_STATUS;

@WebServlet("/anmelden")
public class LoginController extends HttpServlet{
	private static final long serialVersionUID = -6106934665383263630L;
	
	QueryManager queryManager = QueryManager.getInstance();
	HttpSession session = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher("/suchen");
		resp.setContentType("text/html");  		
		
		// Berechtigung für die Seite prüfen
    	if(req.getSession().getAttribute("benutzer") != null){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
				
		String emailadresse = req.getParameter("emailadresse");
		String passwort = req.getParameter("passwort");
		boolean anmeldeStatus = false;
		
		if(emailadresse != null && passwort != null && !emailadresse.isEmpty() && !passwort.isEmpty() ){		
			Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(emailadresse);		
			
			try {
				MessageDigest hasher = MessageDigest.getInstance("MD5");
				hasher.update(passwort.getBytes());
				byte[] str = hasher.digest();
				
				String hashPasswort = DatatypeConverter.printHexBinary(str).toUpperCase();		

				if(benutzer != null && benutzer.getBestaetigt() == 1 && benutzer.getPasswort().equals(hashPasswort)){
					anmeldeStatus = true;
					
					session = req.getSession();
					// Nach 10 Minuten wird die Session automatisch geloescht
					session.setMaxInactiveInterval(10*60);
					
					session.setAttribute("benutzer", benutzer);
					
					List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzer.getEmailadresse());	
					session.setAttribute("warenkorbartikelliste", warenkorbartikelListe);
					
					List<Bestellung> bestellungenListe = queryManager.selectAllBestellungenByBenutzeremailadresse(benutzer);	
					session.setAttribute("bestellungenliste", bestellungenListe);
				}
			
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		if(!anmeldeStatus){
			resp.addHeader("status", RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader("fehlermeldung", "E-Mail-Adresse oder Passwort ist falsch.");
		}
		
		rd.forward(req, resp);		
	}
	
}
