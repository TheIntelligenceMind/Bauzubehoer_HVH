package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.QueryManager;
import entity.Artikel;
import entity.Benutzer;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/suchen")
public class SuchController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SuchController() {
        super();

    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");	
		resp.setContentType("text/html");
    			
		String suchargument = req.getParameter("suchargument");
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		
		artikelliste = QueryManager.getInstance().searchArtikelByBezeichnung(suchargument);
		
		req.setAttribute("artikelliste", artikelliste);
		
		if(artikelliste.size() == 0){
			resp.addHeader("status", "hinweis");
			resp.addHeader("hinweismeldung", "Es wurden keine Artikel gefunden.");
		}
		
		rd.forward(req, resp);	
	}

}
