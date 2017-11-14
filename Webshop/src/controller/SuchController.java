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
import enums.ENUM_ARTIKELKATEGORIE;

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
		
		String method = req.getParameter("method");
			
		if(method != null){
			switch(method){
			case "suchKategorie":
				nachKategorieSuchen(req, resp);
				break;
			default:
				nachBegriffSuchen(req, resp);
				break;
			}
		}else{
			nachBegriffSuchen(req, resp);
		}
		
		rd.forward(req, resp);	
	}
    
    private void nachBegriffSuchen(HttpServletRequest req, HttpServletResponse resp){
    	List<Artikel> artikelliste = new ArrayList<Artikel>();
    	String suchargument = req.getParameter("suchargument");
    	
		artikelliste = QueryManager.getInstance().searchArtikelByBezeichnung(suchargument);
		
		req.setAttribute("artikelliste", artikelliste);
		
		if(artikelliste.size() == 0){
			resp.addHeader("status", "hinweis");
			resp.addHeader("hinweismeldung", "Es wurden keine Artikel gefunden.");
		} 	
    }
    
    private void nachKategorieSuchen(HttpServletRequest req, HttpServletResponse resp){
    	List<Artikel> artikelliste = new ArrayList<Artikel>();
    	String strKategorie = req.getParameter("kategorie");
    	
    	if(strKategorie != null){
    		ENUM_ARTIKELKATEGORIE kategorie = ENUM_ARTIKELKATEGORIE.valueOf(strKategorie);
    		artikelliste = QueryManager.getInstance().searchArtikelByKategorie(kategorie);
    		
    		req.setAttribute("artikelliste", artikelliste);
    	}
		
		if(artikelliste.size() == 0){
			resp.addHeader("status", "hinweis");
			resp.addHeader("hinweismeldung", "Es wurden keine Artikel zu der ausgew&auml;hlten Kategorie gefunden.");
		} 	
    }

}
