package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.QueryManager;
import entity.Artikel;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/meineBestellungen")
public class ArtikelController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ArtikelController() {
        super();

    }

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher rq = request.getRequestDispatcher("index.jsp");
		boolean result = false;
		
		String bezeichnung = request.getParameter("bezeichnung");
		int id = Integer.valueOf(request.getParameter("id"));
		String beschreibung = request.getParameter("beschreibung");
		String preis = request.getParameter("preis");
		int lagermenge = Integer.valueOf(request.getParameter("lagermenge"));
		
		if(validateAttributes(bezeichnung, id, beschreibung, preis, lagermenge)){
			
			Artikel newArtikel = new Artikel().init(bezeichnung, id, beschreibung, preis, lagermenge);
			
			result = QueryManager.getInstance().createArtikel(newArtikel);
		}
				
		if(result){
			String hinweistext = "Der Artikel wurde erfolgreich angelegt.";
			response.addHeader("hinweis", hinweistext);
		}else{
			String fehlermeldung = "Bitte überprüfe die eingegebenen Daten auf Vollständigkeit und Gültigkeit.";	
			response.addHeader("fehlermeldung", fehlermeldung);;
		}
		
		
		response.addHeader("contentSite", "artikelanlegen");
	
		rq.forward(request, response);		
	}
    
    private boolean validateAttributes(String piBezeichnung, int piID, String piBeschreibung, String piPreis, int piLagermenge){
    	
    	
    	return false;
    }

}
