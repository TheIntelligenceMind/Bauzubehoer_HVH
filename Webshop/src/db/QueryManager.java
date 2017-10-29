package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Adresse;
import entity.Artikel;
import entity.Benutzer;
import entity.Bestellung;
import entity.WarenkorbArtikel;
import enums.DB_TABELLE;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * <h3>Beschreibung:</h3>
 * <pre>
 * Die Klasse stellt Methoden zum Anlegen/Ver�ndern/L�schen von Daten in der Datenbank zur Verf�gung.
 * </pre>
 * 
 * @author Tim Hermbecker
 */
public class QueryManager {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
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
			
			String sql = "SELECT * FROM " + DB_TABELLE.BENUTZER.toString() + " left join " +  
					DB_TABELLE.ADRESSE.toString() + " on "+ DB_TABELLE.ADRESSE.toString()
					+".Benutzer_ID = " + DB_TABELLE.BENUTZER.toString() + ".ID  WHERE emailadresse=?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, piEMailAdresse);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				Adresse adresse = new Adresse();
				adresse.init(result.getString("stra�e"), result.getString("hausnummer"), result.getString("postleitzahl"), result.getString("ort"), result.getString("zusatz"));
				
				return benutzer.init(result.getString("emailadresse"), result.getString("passwort"), result.getString("vorname"), result.getString("nachname"), adresse);
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
	
	public boolean createAdresse(String piEmailAdresse, Adresse piAdresse){
		String emailadresse = piEmailAdresse;
		Adresse adresse = piAdresse;
		int benutzerID;
		ResultSet first_result = null;
		int result;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
			String pre_sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			// sicherstellen, dass es ein BenutzerObjekt
			if(!first_result.next()){
				return false;
			}
			
			benutzerID = first_result.getInt("ID");
			
			//=============================================================================
			
			String sql = "INSERT INTO " + DB_TABELLE.ADRESSE.toString() + " (stra�e, hausnummer, postleitzahl, ort, zusatz, benutzer_id, erstellt_Benutzer) " 
					+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
					
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, adresse.getStra�e());
			stmt.setString(2, adresse.getHausnummer());
			stmt.setString(3, adresse.getPostleitzahl());
			stmt.setString(4, adresse.getOrt());
			stmt.setString(5, adresse.getZusatz());
			stmt.setInt(6, benutzerID);
			stmt.setString(7, "db_user");
			
			result = stmt.executeUpdate();
			
			if(result == 1){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
			
		return false;	
	}
	
	public boolean modifyAdresse(String piEmailAdresse, Adresse piAdresse){
		String emailadresse = piEmailAdresse;
		Adresse adresse = piAdresse;
		int benutzerID;
		ResultSet first_result = null;
		int result;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
			String pre_sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			// sicherstellen, dass es ein BenutzerObjekt
			if(first_result == null){
				return false;
			}
			
			benutzerID = first_result.getInt("id");
			
			//=============================================================================
			String sql = "UPDATE " + DB_TABELLE.ADRESSE.toString() + "SET stra�e = ?, hausnummer = ?, postleitzahl = ?, ort = ?,"
					+ " zusatz = ?, geaendert_Benutzer = ?, geandert_Uhrzeit = ? WHERE Benutzer_ID = ?";
					
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, adresse.getStra�e());
			stmt.setString(2, adresse.getHausnummer());		
			stmt.setString(3, adresse.getPostleitzahl());
			stmt.setString(4, adresse.getOrt());
			stmt.setString(5, adresse.getZusatz());
			stmt.setString(6, "db_user");
			stmt.setString(7, sdf.format(timestamp));
			stmt.setInt(8, benutzerID);
			
			result = stmt.executeUpdate();
			
			if(result == 1){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		
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
		Benutzer benutzer = piBenutzer;
		int result = 0;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");		
			
			String sql = "UPDATE "+ DB_TABELLE.BENUTZER.toString() +" SET vorname = ?, nachname = ? WHERE emailadresse = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, benutzer.getVorname());
			stmt.setString(2, benutzer.getNachname());
			stmt.setString(3, benutzer.getEmailadresse());
			
			result = stmt.executeUpdate();
			
			if(result != 0){
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
	/**
	 * <h3>Beschreibung: </h3>
	 * <pre>
	 * Die Methode liefert alle in der DB vorhandenen Artikel zur�ck
	 * </pre>
	 * 
	 * @return artikelliste
	 */
	public List<Artikel> selectAllArtikel(Boolean active){
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		ResultSet result = null;
		String sql = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			if (active){			
				sql = "SELECT * FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE aktiv <> 0";
			}
			else if(!active){
				sql = "SELECT * FROM " + DB_TABELLE.ARTIKEL.toString();
			}
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
				result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), result.getInt("aktiv"));
				artikelliste.add(artikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}		
		
		return artikelliste;
	}
	
	public List<Artikel> searchArtikelByBezeichnung(String piBezeichnung){
		if (piBezeichnung == null || piBezeichnung.isEmpty()){
			return selectAllArtikel(true);
		}
		
		String bezeichnung = piBezeichnung;
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		ResultSet result = null;	
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
						
			String sql = "SELECT * FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE bezeichnung like ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, "%" + bezeichnung + "%");

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), result.getInt("aktiv"));
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
				artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), result.getInt("aktiv"));
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
						
			String sql = "SELECT w.position, w.menge, a.nummer, a.bezeichnung, a.beschreibung, a.preis, a.lagermenge, a.aktiv FROM " + 
			DB_TABELLE.WARENKORB.toString() + " w INNER JOIN "+ DB_TABELLE.BENUTZER.toString() + " b ON w.Benutzer_ID = b.ID LEFT JOIN " + 
			DB_TABELLE.ARTIKEL.toString() + " a ON a.ID = w.Artikel_ID WHERE b.emailadresse = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, piEmailadresse);

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), result.getInt("aktiv"));
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
	
	
	public boolean deleteArtikelFromWarenkorb(WarenkorbArtikel piWarenkorbArtikel, String piEmailAdresse){
		WarenkorbArtikel warenkorbartikel = piWarenkorbArtikel;
		String emailadresse = piEmailAdresse;
		ResultSet result = null;
		int benutzerID;
		int artikelID;
		ResultSet first_result = null;
		ResultSet second_result = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		//////////////////////
			String pre_sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			if(first_result == null){
				return false;
			}
			
			benutzerID = first_result.getInt("id");
		///////////////////////
			String pre_sql_2 = "SELECT ID FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE nummer = ?";
			
			PreparedStatement pre_stmt_2 = getConnection().prepareStatement(pre_sql_2);
			pre_stmt_2.setInt(1, warenkorbartikel.artikel.nummer);
			
			second_result = pre_stmt_2.executeQuery();
			
			if(second_result == null){
				return false;
			}
			
			artikelID = second_result.getInt("id");
		///////////////////////
			String sql = "DELETE FROM " + DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ? AND Artikel_ID = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, benutzerID);
			stmt.setInt(2, artikelID);

			result = stmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		
		
		return true;
	}
	
	public boolean modifyArtikelInWarenkorb(WarenkorbArtikel piWarenkorbArtikel, String piEmailAdresse, int piMenge){
		WarenkorbArtikel warenkorbartikel = piWarenkorbArtikel;
		String emailadresse = piEmailAdresse;
		int menge = piMenge;
		ResultSet result = null;
		int benutzerID;
		int artikelID;
		ResultSet first_result = null;
		ResultSet second_result = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		//////////////////////
			String pre_sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			if(first_result == null){
				return false;
			}
			
			benutzerID = first_result.getInt("id");
		///////////////////////
			String pre_sql_2 = "SELECT ID FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE nummer = ?";
			
			PreparedStatement pre_stmt_2 = getConnection().prepareStatement(pre_sql_2);
			pre_stmt_2.setInt(1, warenkorbartikel.artikel.nummer);
			
			second_result = pre_stmt_2.executeQuery();
			
			if(second_result == null){
				return false;
			}
			
			artikelID = second_result.getInt("id");
		///////////////////////
			String sql = "UPDATE " + DB_TABELLE.WARENKORB.toString() + "SET MENGE = ? WHERE Benutzer_ID = ? AND Artikel_ID = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, menge);
			stmt.setInt(2, benutzerID);
			stmt.setInt(3, artikelID);

			result = stmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		
		
		return true;
	}
	
}
