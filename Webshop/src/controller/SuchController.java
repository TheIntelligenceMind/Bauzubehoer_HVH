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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher rq = request.getRequestDispatcher("index.jsp");	
		String suchargument = request.getParameter("suchargument");
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		
		artikelliste = QueryManager.getInstance().searchArtikelByBezeichnung(suchargument);
		
		request.setAttribute("artikelliste", artikelliste);
		
		rq.forward(request, response);	
	}

}
