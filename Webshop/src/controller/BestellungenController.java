package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/meineBestellungen")
public class BestellungenController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public BestellungenController() {
        super();

    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rq = req.getRequestDispatcher("index.jsp");
		
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
		
	
	

		rq.forward(req, resp);	
	}
}
