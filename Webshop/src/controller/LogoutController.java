package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/abmelden")
public class LogoutController extends HttpServlet{
	private static final long serialVersionUID = 7132205047322448035L;
	HttpSession session = null;	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("/suchen");

		// prüfen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
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
