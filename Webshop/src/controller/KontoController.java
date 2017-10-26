package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.QueryManager;
import entity.Benutzer;
import enums.MELDUNG_ART;
import enums.RESPONSE_STATUS;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/meinKonto")
public class KontoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final QueryManager queryManager = QueryManager.getInstance();
	private Benutzer benutzer = null;

    public KontoController() {
        super();

    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	RequestDispatcher rq = req.getRequestDispatcher("index.jsp");
		
		benutzer = queryManager.getBenutzerByEMailAdresse(req.getSession().getAttribute("emailadresse").toString());
		
		if(benutzer != null){
			req.setAttribute("benutzer", benutzer);
		}else{
			benutzer = new Benutzer().init("", "", "", "", null);
		}	
		
		resp.addHeader("contentSite", "meinKontoPanel");
		
		rq.forward(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestDispatcher rq = req.getRequestDispatcher("index.jsp");
		
		String function = req.getParameter("function");
			
		switch(function){
		case "f_speichern_benutzer":
			if(speicherBenutzer(req)){
				benutzer = queryManager.getBenutzerByEMailAdresse(req.getSession().getAttribute("emailadresse").toString());

				updateSessionDetails(req.getSession(), benutzer);

				String hinweistext = "Die Benutzerdaten wurden erfolgreich gespeichert.";
				resp.addHeader("Status", RESPONSE_STATUS.HINWEIS.toString());
				resp.addHeader(MELDUNG_ART.HINWEISMELDUNG.toString(), hinweistext);
					
			}else{
				String fehlermeldung = "ung�ltige �nderungen";	
				resp.addHeader("Status", RESPONSE_STATUS.FEHLER.toString());
				resp.addHeader(MELDUNG_ART.FEHLERMELDUNG.toString(), fehlermeldung);
			}				
			break;
		case "f_speichern_adresse":
			speicherAdresse(req);
			break;
		default:
			break;
		}

		req.setAttribute("benutzer", benutzer);
		resp.addHeader("contentSite", "meinKontoPanel");
		
		rq.forward(req, resp);	
	}
    
    private boolean speicherAdresse(HttpServletRequest req){
    	
    	
    	return false;
    }
    
    
    private boolean speicherBenutzer(HttpServletRequest req){
    	boolean result = false;
    	String vorname = req.getParameter("vorname");
    	String nachname = req.getParameter("nachname");
    	
    	if(vorname != null && !vorname.isEmpty() && nachname != null && !nachname.isEmpty()){
    		Benutzer update_benutzer = queryManager.getBenutzerByEMailAdresse(req.getSession().getAttribute("emailadresse").toString());
    		
    		if(update_benutzer != null){
    			update_benutzer.setVorname(vorname);
    			update_benutzer.setNachname(nachname);

        		result = queryManager.modifyBenutzer(update_benutzer);
    		}
    	}
    	
    	if(result){
    		return true;
    	}else{
    		return false;
    	}	
    }
    
    private void updateSessionDetails(HttpSession session, Benutzer benutzer){
    	session.setAttribute("vorname", benutzer.getVorname());
    	session.setAttribute("nachname", benutzer.getNachname());
    }

}
