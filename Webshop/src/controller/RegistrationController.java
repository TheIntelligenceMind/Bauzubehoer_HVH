package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

@WebServlet("/registrieren")
public class RegistrationController extends HttpServlet{
	private static final long serialVersionUID = 8289722651997847704L;
	
	private boolean wurdeErstellt = false;
	private String email = null;
	private String vorname = null;
	private String nachname = null;
	private String passwort = null;
	private String passwortBestaetigt = null;
	private String fehlertext = "";
	QueryManager queryManager = QueryManager.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		
		resp.addHeader("contentSite", "registrierung");
		
		rd.forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");  			
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");;
	
		email = req.getParameter("emailadresse");
		vorname = req.getParameter("vorname");
		nachname = req.getParameter("nachname");
		passwort = req.getParameter("passwort");
		passwortBestaetigt = req.getParameter("passwortBestaetigt");
	

		if(alleFelderGefuellt(email, vorname, nachname, passwort, passwortBestaetigt)){
			if(isEmailValid(email)){
				if(passwortIstGueltig(passwort)){
					if(passwoerterSindIdentisch(passwort, passwortBestaetigt)){
						QueryManager queryManager = QueryManager.getInstance();
						
						// neues Benutzerobjekt anlegen
						Benutzer newBenutzer = new Benutzer();			
						MessageDigest hasher = null;
						String hashPasswort = null;
						
						try {
							hasher = MessageDigest.getInstance("MD5");
							hasher.update(passwort.getBytes());
							byte[] str = hasher.digest();
							hashPasswort = DatatypeConverter.printHexBinary(str).toUpperCase();
							
						} catch (NoSuchAlgorithmException e) {
							e.printStackTrace();
						}
						
						newBenutzer.init(email, hashPasswort, vorname, nachname);
						
						// Benutzerobjekt in der Datenbank anlegen
						boolean result = queryManager.createBenutzer(newBenutzer);	
						
						if(result){
							wurdeErstellt = true;
						}
						
					}else{
						fehlertext = "Die Passw�rter sind nicht identisch.";
					}
				}else{
					fehlertext = "Das Passwort entspricht nicht den Richtlinien.";
				}
			}else{
				fehlertext = "Die E-Mail-Adresse ist bereits registriert.";
			}
		}else{
			fehlertext = "Es wurden nicht alle Felder ausgef�llt.";
		}
		
		if(!wurdeErstellt){
			resp.addHeader("status", "fehler");
			resp.addHeader("fehlermeldung", fehlertext);
			resp.addHeader("contentSite", "registrierung");
		}
		
		resp.addHeader("result", String.valueOf(wurdeErstellt));
		
		rd.forward(req, resp);
	}	
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode �berpr�ft ob das Passwort alle Passwortrichtlinien 
	 * einh�lt und gibt je nach dem true oder false zur�ck
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
	 * Die Methode �berpr�ft ob alle �bergebenen Attribute mit Werten gef�llt wurden.
	 * </pre>
	 * 
	 * @param piEmail
	 * @param piVorname
	 * @param piNachname
	 * @param piPasswort
	 * @param piPasswortBestaetigt
	 * @return true or false
	 */
	private boolean alleFelderGefuellt(String piEmail, String piVorname, String piNachname, String piPasswort, String piPasswortBestaetigt){
		String email = piEmail;
		String vorname = piVorname;
		String nachname = piNachname;
		String passwort = piPasswort;
		String passwortBestaetigt = piPasswortBestaetigt;
		
		if(!email.isEmpty() && !vorname.isEmpty() && !nachname.isEmpty() && !passwort.isEmpty() && !passwortBestaetigt.isEmpty()){
			return true;
		}
		
		return false;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode �berpr�ft ob beide �bergebenen Passw�rter 
	 * identisch sind und gibt je nach dem true oder false zur�ck
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
	 * Die Methode �berpr�ft:
	 * 	1. ob Email-Adresse null ist,
	 * 	2. ob Email-Adresse bereits vorhanden ist,
	 *	3. ob Email-Adresse das Email-Pattern erf�llt
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