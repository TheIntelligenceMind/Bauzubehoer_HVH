package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;

import db.QueryManager;
import entity.Artikel;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/artikelAnzeigenMitarbeiter")
public class ArtikellistenController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final QueryManager queryManager = QueryManager.getInstance();

    public ArtikellistenController() {
        super();

    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		
		resp.addHeader("contentSite", "artikelAnzeigenMitarbeiter");
		
		rd.forward(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rq = req.getRequestDispatcher("index.jsp");
		
		List<Artikel> artikelliste = null;
		
		artikelliste = queryManager.selectAllArtikel(false);
		
		req.setAttribute("artikelliste", artikelliste);
				
		resp.addHeader("contentSite", "artikelAnzeigenMitarbeiter");	
		rq.forward(req, resp);		
	}
}
