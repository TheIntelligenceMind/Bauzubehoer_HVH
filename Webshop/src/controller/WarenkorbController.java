package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.QueryManager;
import entity.WarenkorbArtikel;
import enums.RESPONSE_STATUS;

/**
 * Servlet implementation class WarenkorbController
 */
@WebServlet("/warenkorb")
public class WarenkorbController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final QueryManager queryManager = QueryManager.getInstance();
	
    public WarenkorbController() {
        super();

    }

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req, resp);
    }

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {	
    	RequestDispatcher rq = req.getRequestDispatcher("index.jsp");
    	resp.setContentType("text/html");
    	
    	boolean hasDeleted = false;
    	int row = 0;
		
		if(req.getParameter("row") != null){
			row = Integer.valueOf(req.getParameter("row") );
			
			hasDeleted = queryManager.deleteArtikelFromWarenkorb(row, req.getSession().getAttribute("emailadresse").toString());
			
			
			if(hasDeleted){
				updateWarenkorb(req);
				
				resp.addHeader("status", RESPONSE_STATUS.HINWEIS.toString());
				resp.addHeader("hinweismeldung", "Der Artikel wurde aus dem Warenkorb entfernt.");
			}else{
				resp.addHeader("status", RESPONSE_STATUS.FEHLER.toString());
				resp.addHeader("fehlermeldung", "Es ist ein Problem beim Löschen aufgetreten.");	
			}
		}
		
		resp.addHeader("contentSite", "warenkorbPanel");
		
		rq.forward(req, resp);
	}
    
    private void updateWarenkorb(HttpServletRequest req){
    	String benutzerEmailadresse = req.getSession().getAttribute("emailadresse").toString();
    	
    	List<WarenkorbArtikel> warenkorbartikelListe = queryManager.selectAllWarenkorbartikelByBenutzeremailadresse(benutzerEmailadresse);	

    	req.getSession().setAttribute("warenkorbartikelliste", warenkorbartikelListe);   	
    }

}
