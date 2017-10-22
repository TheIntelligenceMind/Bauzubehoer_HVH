package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Adresse;
import entity.Artikel;
import entity.Benutzer;
import entity.Bestellung;
import entity.WarenkorbArtikel;
import enums.DB_TABELLE;

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
	private Connection connection = null;

	private Connection getConnection(){
		if(connection == null){
			try {
				//Windows
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webshop","root","");
				//Mac
				//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webshop","admin","test");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return connection;
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
			
			String sql = "SELECT * FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse=?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, piEMailAdresse);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				return benutzer.init(result.getString("emailadresse"), result.getString("passwort"), result.getString("vorname"), result.getString("nachname"));
			}else{
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
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
		Benutzer benutzer = piBenutzer;
		int result;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
						
			String sql = "INSERT INTO " + DB_TABELLE.BENUTZER.toString() + " (emailadresse, passwort, vorname, nachname, erstellt_Benutzer) "
						+ "VALUES( ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, benutzer.getEmailadresse());
			stmt.setString(2, benutzer.getPasswort());
			stmt.setString(3, benutzer.getVorname());
			stmt.setString(4, benutzer.getNachname());
			stmt.setString(5, "db_user");
			
			result = stmt.executeUpdate();
			
			if(result == 1){
				return true;
			}else{
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		return false;
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
	
	
	public boolean createArtikel(Artikel piArtikel){
		Artikel artikel = piArtikel;
		int result;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String sql = "INSERT INTO " + DB_TABELLE.ARTIKEL.toString() + " (nummer, bezeichnung, beschreibung, preis, lagermenge, erstellt_Benutzer) " 
			+ " VALUES(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setLong(1, artikel.getNummer());
			stmt.setString(2, artikel.getBezeichnung());		
			stmt.setString(3, artikel.getBeschreibung());
			stmt.setDouble(4, artikel.getPreis());
			stmt.setLong(5, artikel.getLagermenge());
			stmt.setString(6, "db_user");
			
			result = stmt.executeUpdate();
			
			if(result == 1){
				return true;
			}else{
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		return false;	
	}
	
	public List<Artikel> searchArtikelByBezeichnung(String piBezeichnung){
		String bezeichnung = piBezeichnung;
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		ResultSet result = null;	
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
						
			String sql = "SELECT * FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE bezeichnung like ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, bezeichnung);

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"));
				artikelliste.add(artikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		
		return artikelliste;
	}
	
	public Artikel searchArtikelByNummer(int piNummer){
		int nummer = piNummer;
		Artikel artikel = null;
		ResultSet result = null;	
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
						
			String sql = "SELECT * FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE nummer = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setLong(1, nummer);

			result = stmt.executeQuery();
			
			if(result.next()){
				artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		
		return artikel;
	}
	
	public List<WarenkorbArtikel> selectAllWarenkorbartikelByBenutzeremailadresse(String piEmailadresse){
		String emailadresse = piEmailadresse;
		List<WarenkorbArtikel> artikelliste = new ArrayList<WarenkorbArtikel>();
		ResultSet result = null;
		
		if(emailadresse == null || emailadresse.isEmpty()){
			return null;
		}
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
						
			String sql = "SELECT w.position, w.menge, a.nummer, a.bezeichnung, a.beschreibung, a.preis, a.lagermenge FROM " + 
			DB_TABELLE.WARENKORB.toString() + " w INNER JOIN "+ DB_TABELLE.BENUTZER.toString() + " b ON w.Benutzer_ID = b.ID LEFT JOIN " + 
			DB_TABELLE.ARTIKEL.toString() + " a ON a.ID = w.Artikel_ID WHERE b.emailadresse = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, piEmailadresse);

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"));
				WarenkorbArtikel warenkorbartikel = new WarenkorbArtikel().init(artikel, result.getInt("position"), result.getInt("menge"));
				artikelliste.add(warenkorbartikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		
		
		
		
		
		return artikelliste;
		
	}
	
}
