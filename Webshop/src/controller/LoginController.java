package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/anmelden")
public class LoginController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	HttpSession session = null;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");  
		
		PrintWriter out = resp.getWriter();
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
	
		if(req.getParameter("benutzername").equals("Admin") && req.getParameter("passwort").equals("1234")){
			
			session = req.getSession();
			// Nach 5 Minuten wird die Session automatisch geloescht
			session.setMaxInactiveInterval(10*60);
			
			session.setAttribute("benutzername", req.getParameter("benutzername"));
			resp.addHeader("benutzername", req.getParameter("benutzername"));
			
		}
		
		rd.forward(req, resp);
		
		
	}
	
	
}
