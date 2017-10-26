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
import entity.WarenkorbArtikel;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		RequestDispatcher rq = request.getRequestDispatcher("index.jsp");

		
		response.addHeader("contentSite", "warenkorbPanel");
		
		rq.forward(request, response);
	}

}
