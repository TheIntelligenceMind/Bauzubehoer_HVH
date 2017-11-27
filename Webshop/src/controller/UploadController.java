package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import db.QueryManager;
import entity.Artikel;
import entity.Benutzer;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Bestellungen zuständig. 
 * Hier werden alle GET- und POST-Schnittstellenaufrufe verarbeitet und an die View weitergeleitet
 * </pre>
 * @author Tim Hermbecker
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static QueryManager queryManager = QueryManager.getInstance();

	private String dispatchSite = "index.jsp";

    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

    @Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		RequestDispatcher rd = null;
		resp.setContentType("text/html"); 
		
		// prüfen ob es eine Session gibt, wenn nicht an die Startseite weiterleiten
		if(req.getSession().getAttribute("benutzer") == null){
			rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
		}
		
		
		// Berechtigung für die Seite prüfen
    	if(((Benutzer)req.getSession().getAttribute("benutzer")).getRolle().getSichtArtikelstammdaten() != 1){
    		rd = req.getRequestDispatcher("/suchen");	
    		rd.forward(req, resp);
    		return;
    	}
	
    	bildHochladen(req, resp);
    	
		
		rd = req.getRequestDispatcher(dispatchSite);
		rd.forward(req, resp);	
	}

    private void bildHochladen(HttpServletRequest req, HttpServletResponse resp){ 
	    try {
	    	Part filePart = req.getPart("bild");
		    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
			InputStream fileContent = filePart.getInputStream();
			
			Artikel artikel = queryManager.searchArtikelByNummer(3333);
		    
			 byte[] bytes = new byte[fileContent.available()];
			 fileContent.read(bytes);
			
		    artikel.setBild(bytes);
		    
		    queryManager.modifyArtikel(artikel);
		    

		    req.setAttribute("bild", org.apache.commons.codec.binary.Base64.encodeBase64String(bytes));
		    resp.addHeader("contentSite", "artikelAnlegenPanel");
		    
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	    
//
//    	
//    	boolean isMultipart = ServletFileUpload.isMultipartContent(req);
//    	
//    	if(isMultipart){
//    		FileItemFactory itemFactory = new DiskFileItemFactory();
//    		ServletFileUpload upload = new ServletFileUpload(itemFactory);
//    		
//    		try{
//    			List<FileItem> items = upload.parseRequest(req);
//    			for(FileItem item : items){
//    				String contentType = item.getContentType();
//    				if(!contentType.equals("image/png")){
//    					
//    				}
//    				String path = req.getServletContext().getRealPath("/images/articles"); 
//    				
//    				System.out.println(path);
//    				File uploadDir = new File(path);
//    				File file = File.createTempFile("img", ".png", uploadDir);
//    				item.write(file);
//    				
//    				System.out.println("erfolgreich hochgeladen.");
//    			}
//    		}catch(FileUploadException e){
//    			System.out.println("Upload fehlgeschlagen.");
//    			e.printStackTrace();
//    		}catch(Exception ex){
//    			System.out.println("konnte nicht gespeicher werden.");
//    			ex.printStackTrace();
//    		}
//    	}

    }
   
}
