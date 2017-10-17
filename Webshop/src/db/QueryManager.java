package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Adresse;
import entity.Benutzer;
import entity.Bestellung;

/**
 * <h3>Beschreibung:</h3>
 * <pre>
 * Die Klasse stellt Methoden zum Anlegen/Ver�ndern/L�schen von Daten in der Datenbank zur Verf�gung.
 * </pre>
 * 
 * @author Tim Hermbecker
 */
public class QueryManager {
	private final static QueryManager instance = new QueryManager();
	
	Connection connection = null;
	
	QueryManager(){
		
	}

	
	
	
	public static QueryManager getInstance(){
		return instance;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert zu einer EmailAdresse einen Benutzer
	 * falls kein Benutzer gefunden wird ist der Returnwert null
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @return Benutzer
	 */
	public Benutzer getBenutzerByEMailAdresse(String piEMailAdresse){
		Benutzer benutzer = new Benutzer();
		ResultSet result = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webshop","admin","test");

			
			Statement stmt = connection.createStatement();
			
			String sql = "SELECT * FROM Users WHERE username='"+piEMailAdresse+"'";
			
			result = stmt.executeQuery(sql);
			
			if(result.next()){
				benutzer.init(result.getString("username"), result.getString("password"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return benutzer;
	}	
	
	public boolean createAdresse(Adresse piAdresse){
		
		
		
		return true;
	}
	
	public boolean modifyAdresse(Adresse piAdresse){
		
		
		
		return true;
	}
	
	public boolean deleteAdresse(int id){
		
		
		return true;
	}
	
	
	
	public boolean createBenutzer(Benutzer piBenutzer){
		
		
		
		return true;
	}
	
	public boolean modifyBenutzer(Benutzer piBenutzer){
		
		
		
		return true;
	}
	
	public boolean deleteBenutzer(int id){
		
		
		return true;
	}
	
	public boolean createBestellung(Bestellung piBestellung){
		
		
		return true;
	}
	
	
}
