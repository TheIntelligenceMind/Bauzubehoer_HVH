package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.Benutzer;

@WebServlet("/MainController")
public class MainController extends HttpServlet{
	private static final long serialVersionUID = 235370755123768723L;
	HttpSession session = null;
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		session = req.getSession(false);
		
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		
		if(session != null){
			
			String emailadresse = ((Benutzer)req.getSession().getAttribute("benutzer")).getEmailadresse();
			
			req.setAttribute("emailadresse", emailadresse);
			
		}
		
		
		rd.forward(req, resp);
		
		
	}
}
