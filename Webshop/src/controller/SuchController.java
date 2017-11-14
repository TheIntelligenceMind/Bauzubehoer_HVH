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
	private static final QueryManager queryManager = QueryManager.getInstance();
	
    /**
     * <pre>
     * <h3>Beschreibung:</h3>
     * Die Methode verarbeitet alle GET-Schnittstellenaufrufe und gibt 
     * diese an die doPost() Methode weiter
     * </pre>
     * 
     * @param req
     * @param resp
     */
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    /**
     * <pre>
     * <h3>Beschreibung:</h3>
     * Die Methode verarbeitet alle POST-Schnittstellenaufrufe und gibt 
     * entsprechende Werte an die View zur�ck
     * </pre>
     * 
     * @param req
     * @param resp
     */
    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");	
		resp.setContentType("text/html");
		
		String method = req.getParameter("method");
		
		// sicherstellen das bei null default eintritt
		if(method == null){
			method = "";
		}
		
		switch(method){
			case "suchKategorie":
				nachKategorieSuchen(req, resp);
				break;
			default:
				nachBegriffSuchen(req, resp);
				break;
		}	
		
		rd.forward(req, resp);	
	}
    
    /**
     * <pre>
     * <h3>Beschreibung:</h3>
     * Die Methode holt sich das Suchargument aus dem Request-Objekt und 
     * sucht �ber den QM alle dazugeh�rigen Artikel und gibt diese als List 
     * innerhalb des Request-Objekts an die View weiter
     * </pre>
     * 
     * @param req
     * @param resp
     */
    private void nachBegriffSuchen(HttpServletRequest req, HttpServletResponse resp){
    	List<Artikel> artikelliste = new ArrayList<Artikel>();
    	String suchargument = req.getParameter("suchargument");
    	
		artikelliste = queryManager.searchArtikelByBezeichnung(suchargument);
		
		req.setAttribute("artikelliste", artikelliste);
		
		if(artikelliste.size() == 0){
			resp.addHeader("status", "hinweis");
			resp.addHeader("hinweismeldung", "Es wurden keine Artikel gefunden.");
		} 	
    }
    
    /**
     * <pre>
     * <h3>Beschreibung:</h3>
     * Die Methode holt sich die Artikelkategorie aus dem Request-Objekt und 
     * sucht �ber den QM alle dazugeh�rigen Artikel und gibt diese als List 
     * innerhalb des Request-Objekts an die View weiter
     * </pre>
     * 
     * @param req
     * @param resp
     */
    private void nachKategorieSuchen(HttpServletRequest req, HttpServletResponse resp){
    	List<Artikel> artikelliste = new ArrayList<Artikel>();
    	String strKategorie = req.getParameter("kategorie");
    	
    	if(strKategorie != null){
    		ENUM_ARTIKELKATEGORIE kategorie = ENUM_ARTIKELKATEGORIE.valueOf(strKategorie);
    		artikelliste = queryManager.searchArtikelByKategorie(kategorie);
    		
    		req.setAttribute("artikelliste", artikelliste);
    	}
		
		if(artikelliste.size() == 0){
			resp.addHeader("status", "hinweis");
			resp.addHeader("hinweismeldung", "Es wurden keine Artikel zu der ausgew&auml;hlten Kategorie gefunden.");
		} 	
    }

}
