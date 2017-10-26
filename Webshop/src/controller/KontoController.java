package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.QueryManager;
import entity.Benutzer;

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
		case "f_bearbeiten":
			
			break;
		
		
		}
		
		
		benutzer = queryManager.getBenutzerByEMailAdresse(req.getSession().getAttribute("emailadresse").toString());
		
		
		
		
		
		resp.addHeader("contentSite", "meinKontoPanel");
		
		rq.forward(req, resp);
		
		
	}

}
