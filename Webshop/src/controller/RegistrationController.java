package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import db.QueryManager;
import entity.Benutzer;

@WebServlet("/registrieren")
public class RegistrationController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private boolean wurdeErstellt = false;
	private String email = null;
	private String passwort = null;
	private String passwortBestaetigt = null;
	private String fehlertext = "";
	QueryManager queryManager = QueryManager.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");  			
		RequestDispatcher rd = req.getRequestDispatcher("RegisterResult.jsp");
	
		email = req.getParameter("emailAdresse");
		passwort = req.getParameter("passwort");
		passwortBestaetigt = req.getParameter("passwortBestaetigt");
		
		if(isEmailValid(email) && passwoerterSindIdentisch(passwort, passwortBestaetigt) && passwortIstGueltig(passwort)){	
			
			QueryManager queryManager = QueryManager.getInstance();
			
			// neues Benutzerobjekt anlegen
			Benutzer newBenutzer = new Benutzer();		
			newBenutzer.init(email, passwort);
			
			// Benutzerobjekt in der Datenbank anlegen
			boolean result = queryManager.createBenutzer(newBenutzer);	
			
			if(result){
				wurdeErstellt = true;
			}
			
		}
		
		if(!wurdeErstellt){
			fehlertext = "Eine oder mehrere Eingaben sind nicht gültig." 
					+ "</br>" 
					+ "Bitte überprüfen ob sie die Email-Adresse "
					+ "richtig geschrieben haben und ob sie die Passwortrichtlinien einghalten haben.";
		
			resp.addHeader("fehlertext", fehlertext);
		}
		
		resp.addHeader("result", String.valueOf(wurdeErstellt));

		
		rd.forward(req, resp);
	}	
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft ob das Passwort alle Passwortrichtlinien 
	 * einhält und gibt je nach dem true oder false zurück
	 * </pre>
	 * 
	 * @param piPasswort
	 * @return true or false
	 */
	private boolean passwortIstGueltig(String piPasswort) {
		String passwort = piPasswort;
		
		if(passwort != null && passwort.length() >= 6){
			return true;
		}
		
		return false;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft ob beide übergebenen Passwörter 
	 * identisch sind und gibt je nach dem true oder false zurück
	 * </pre>
	 * 
	 * @param piPasswort
	 * @param piPasswortBestaetigt
	 * @return true or false
	 */
	private boolean passwoerterSindIdentisch(String piPasswort, String piPasswortBestaetigt) {
		String passwort = piPasswort;
		String passwortBestaetigt = piPasswortBestaetigt;
		
		if(passwort != null && !passwort.isEmpty() && passwortBestaetigt != null && !passwortBestaetigt.isEmpty()){
			if(passwort.equals(passwortBestaetigt)){
				return true;
			}	
		}
		
		return false;
	}

	/**
	 *  <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode überprüft:
	 * 	1. ob Email-Adresse null ist,
	 * 	2. ob Email-Adresse bereits vorhanden ist,
	 *	3. ob Email-Adresse das Email-Pattern erfüllt
	 * </pre>
	 * 
	 * @param piEmail
	 * @return true or false
	 */
	private boolean isEmailValid(String piEmail){
		String email = piEmail;
		String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		
		if(email != null){
			Matcher matcher = pattern.matcher(email);
			
			if(matcher.find()){
				if(queryManager.getBenutzerByEMailAdresse(email) == null){
					return true;
				}
			}
		}
		
		return false;
	}
}