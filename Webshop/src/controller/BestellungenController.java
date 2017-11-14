package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Benutzer;
/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/meineBestellungen")
public class BestellungenController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		resp.setContentType("text/html"); 
		
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
			case "bestellungenAnzeigen":				
				resp.addHeader("contentSite", "meineBestellungenPanel");
				break;
			default:
				resp.addHeader("contentSite", "meineBestellungenPanel");
				break;
		}	

		rd.forward(req, resp);	
	}
}
