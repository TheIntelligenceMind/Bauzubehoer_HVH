package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import entity.Benutzer;
import entity.Nachricht;
import enums.ENUM_MELDUNG_ART;
import enums.ENUM_RESPONSE_STATUS;
import helper.MailHelper;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Kontaktformular zuständig.
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 *  @author Tim Hermbecker
 */
@WebServlet("/kontakt")
public class KontaktformularController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final static MailHelper mailHelper = MailHelper.getInstance();
	
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
    		
		String method = req.getParameter("method");
		
		if(method == null){
			method = "";
		}
		
		switch(method){
		case "kontaktAnzeigen":
			resp.addHeader("contentSite", "kontaktPanel");
			break;
		case "nachrichtAbschicken":
			kontaktformularAbschicken(req, resp);
			break;
		default:
			resp.addHeader("contentSite", "kontaktPanel");
			break;		
		}

		rd.forward(req, resp);		
	}

    /**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode überprüft die eingegebenen Formulardaten und 
	 * schickt die Nachricht als E-Mail an die Support E-Mail-Adresse.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
	private void kontaktformularAbschicken(HttpServletRequest req, HttpServletResponse resp){
		Benutzer benutzer = (Benutzer)req.getSession().getAttribute("benutzer");
			
		String absenderAdresse = req.getParameter("absenderAdresse");
		String betreff = req.getParameter("betreff");
		String inhalt = req.getParameter("inhalt");
		boolean registrierterBenutzer = false;
		
		Nachricht nachricht = new Nachricht().init(absenderAdresse, betreff, inhalt);
		
		if(absenderAdresse != null && !absenderAdresse.isEmpty() && betreff != null && !betreff.isEmpty() && inhalt != null && !inhalt.isEmpty()){
			
			if(benutzer != null){
				registrierterBenutzer = true;
			}		
			
			mailHelper.sendKontaktMail(nachricht, registrierterBenutzer);
			
			String hinweismeldung = "Die Nachricht wurde erfolgreich versandt.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.HINWEIS.toString());
			resp.addHeader(ENUM_MELDUNG_ART.HINWEISMELDUNG.toString(), hinweismeldung);		
		}else{
			String fehlermeldung = "Bitte alle Felder ausf&uuml;llen.";
			resp.addHeader("Status", ENUM_RESPONSE_STATUS.FEHLER.toString());
			resp.addHeader(ENUM_MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
			
			req.setAttribute("nachricht", nachricht);
		}
		
		resp.addHeader("contentSite", "kontaktPanel");
	}

}
