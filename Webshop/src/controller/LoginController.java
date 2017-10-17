package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import db.QueryManager;
import entity.Benutzer;

@WebServlet("/anmelden")
public class LoginController extends HttpServlet{
	private static final long serialVersionUID = 1L;

	QueryManager queryManager = QueryManager.getInstance();
	HttpSession session = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		resp.setContentType("text/html");  
		
		PrintWriter out = resp.getWriter();
		RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
		
		String benutzername = req.getParameter("benutzername");
		String passwort = req.getParameter("passwort");
		
		if(benutzername != null && passwort != null && !benutzername.isEmpty() && !passwort.isEmpty() ){		
			Benutzer benutzer = queryManager.getBenutzerByEMailAdresse(benutzername);		
			
			try {
				MessageDigest hasher = MessageDigest.getInstance("MD5");
				hasher.update(passwort.getBytes());
				byte[] str = hasher.digest();
				
				String hashPasswort = DatatypeConverter.printHexBinary(str).toUpperCase();		
					
				System.out.println(benutzer.getPasswort());
				System.out.println(hashPasswort);
				
				if(benutzer != null && benutzer.getPasswort().equals(hashPasswort)){
					
					session = req.getSession();
					// Nach 5 Minuten wird die Session automatisch geloescht
					session.setMaxInactiveInterval(10*60);
					
					session.setAttribute("benutzername", req.getParameter("benutzername"));
					resp.addHeader("benutzername", req.getParameter("benutzername"));
					
				}
			
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		rd.forward(req, resp);
		
		
	}
	
	
}
