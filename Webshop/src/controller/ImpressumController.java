package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Kontaktformular zuständig.
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 *  @author Tim Hermbecker
 */
@WebServlet("/impressum")
public class ImpressumController extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		case "impressumAnzeigen":
			resp.addHeader("contentSite", "impressumPanel");
			break;
		default:
			resp.addHeader("contentSite", "impressumPanel");
			break;		
		}

		rd.forward(req, resp);		
	}
}
