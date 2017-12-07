package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist f�r den Themenbereich Logout zust�ndig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 * @author Tim Hermbecker
 */
@WebServlet("/abmelden")
public class LogoutController extends HttpServlet{
	private static final long serialVersionUID = 7132205047322448035L;
	private HttpSession session = null;	
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode erh�lt alle GET-Aufrufe 
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
	 * <h3>Beschreibung:</h3> Die Methode erh�lt alle POST-Aufrufe und die weitergeleiteten Aufrufe der doGet() Methode.
	 * Hier werden die verschiedenen Aufrufe verarbeitet. Durch den "method"-Parameter wird bestimmt, 
	 * welche Funktionen durch den Controller ausgef�hrt werden sollen.
	 * </pre>
	 *  @param req
	 *  @param resp
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/suchen");

		// pr�fen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
    	if(req.getSession().getAttribute("benutzer") == null){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
		
		session = req.getSession();
		session.invalidate();
		
		rd.forward(req, resp);
	}
	
	
}
