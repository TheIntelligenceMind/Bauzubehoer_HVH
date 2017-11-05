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
 * Die Klasse stellt Methoden zum Anlegen/Verï¿½ï½¿ï½½ndern/Lï¿½ï½¿ï½½schen von Daten in der Datenbank zur Verfï¿½ï½¿ï½½gung.
 * </pre>
 * 
 * @author Tim Hermbecker
 */
public class QueryManager {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	
    private final static String DBUSER = "db_user";
	private final static QueryManager instance = new QueryManager();	
	private Connection connection = null;

	private Connection getConnection(){
		if(connection == null){
			try {
				Class.forName("com.mysql.jdbc.Driver");	
				//Windows
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webshop","root","");
				//Mac
				//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webshop","admin","test");
				
			} catch (SQLException | ClassNotFoundException e) {
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
			String sql = "SELECT * FROM " + DB_TABELLE.BENUTZER.toString() + " left join " +  
					DB_TABELLE.ADRESSE.toString() + " on "+ DB_TABELLE.ADRESSE.toString()
					+".Benutzer_ID = " + DB_TABELLE.BENUTZER.toString() + ".ID  WHERE emailadresse=?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, piEMailAdresse);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				Adresse adresse = new Adresse().init(result.getString("strasse"), result.getString("hausnummer"), result.getString("postleitzahl"), result.getString("ort"), result.getString("zusatz"));
				
				return benutzer.init(result.getString("emailadresse"), result.getString("passwort"), result.getString("vorname"), result.getString("nachname"), adresse);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	private int getBenutzerIDbyEmailadresse(String piEmailadresse){
		String emailadresse = piEmailadresse;
		int benutzer_ID = -1;
		ResultSet result = null;
		
		try {
			String sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, emailadresse);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				benutzer_ID = result.getInt("ID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return benutzer_ID;	
	}
	
	
	
	public boolean createAdresse(String piEmailAdresse, Adresse piAdresse){
		String emailadresse = piEmailAdresse;
		Adresse adresse = piAdresse;
		int benutzerID;
		ResultSet first_result = null;
		int result;
		
		try {
			String pre_sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			// sicherstellen, dass es ein BenutzerObjekt gibt
			if(!first_result.next()){
				return false;
			}
			
			benutzerID = first_result.getInt("ID");
			
			//=============================================================================
			
			String sql = "INSERT INTO " + DB_TABELLE.ADRESSE.toString() + " (strasse, hausnummer, postleitzahl, ort, zusatz, benutzer_id, erstellt_Benutzer) " 
					+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
					
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, adresse.getStrasse());
			stmt.setString(2, adresse.getHausnummer());
			stmt.setString(3, adresse.getPostleitzahl());
			stmt.setString(4, adresse.getOrt());
			stmt.setString(5, adresse.getZusatz());
			stmt.setInt(6, benutzerID);
			stmt.setString(7, DBUSER);
			
			result = stmt.executeUpdate();
			
			if(result == 1){
				return true;
			}
			
		} catch (SQLException e) {
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
			String pre_sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			// sicherstellen, dass es ein BenutzerObjekt
			if(!first_result.next()){
				return false;
			}
			
			benutzerID = first_result.getInt("id");
			
			//=============================================================================
			String sql = "UPDATE " + DB_TABELLE.ADRESSE.toString() + " SET strasse = ?, hausnummer = ?, postleitzahl = ?, ort = ?,"
					+ " zusatz = ?, geaendert_Benutzer = ?, geaendert_Datum = ? WHERE Benutzer_ID = ?";
					
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, adresse.getStrasse());
			stmt.setString(2, adresse.getHausnummer());		
			stmt.setString(3, adresse.getPostleitzahl());
			stmt.setString(4, adresse.getOrt());
			stmt.setString(5, adresse.getZusatz());
			stmt.setString(6, DBUSER);
			stmt.setString(7, sdf.format(timestamp));
			stmt.setInt(8, benutzerID);
			
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return false;
	}

	
	public boolean deleteAdresse(int id){
		
		
		return true;
	}
	
	
	
	public boolean createBenutzer(Benutzer piBenutzer){
		Benutzer benutzer = piBenutzer;
		int result;
		
		try {				
			String sql = "INSERT INTO " + DB_TABELLE.BENUTZER.toString() + " (emailadresse, passwort, vorname, nachname, erstellt_Benutzer) "
						+ "VALUES( ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, benutzer.getEmailadresse());
			stmt.setString(2, benutzer.getPasswort());
			stmt.setString(3, benutzer.getVorname());
			stmt.setString(4, benutzer.getNachname());
			stmt.setString(5, DBUSER);
			
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return false;
	}
	
	public boolean modifyBenutzer(Benutzer piBenutzer){	
		Benutzer benutzer = piBenutzer;
		int result = 0;
		
		try {	
			String sql = "UPDATE "+ DB_TABELLE.BENUTZER.toString() +" SET vorname = ?, nachname = ? WHERE emailadresse = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, benutzer.getVorname());
			stmt.setString(2, benutzer.getNachname());
			stmt.setString(3, benutzer.getEmailadresse());
			
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean deleteBenutzer(String piEmailadresse){		
		String emailadresse = piEmailadresse;
		int benutzerID;
		int result = 0;
		
		try {
			benutzerID = getBenutzerIDbyEmailadresse(emailadresse);
			
			if(benutzerID == -1){
				return false;
			}
			
			String sql = "DELETE FROM "+ DB_TABELLE.BENUTZER.toString() +" WHERE ID = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, benutzerID);
			
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean createBestellung(Bestellung piBestellung){
		
		
		return true;
	}
	
	private int getArtikelIDbyNummer(int piNummer){
		int artikelnummer = piNummer;
		int Artikel_ID = -1;
		ResultSet result = null;
		
		try {
			String sql = "SELECT ID FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE Nummer = ?";
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, artikelnummer);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				Artikel_ID = result.getInt("ID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return Artikel_ID;	
	}
	
	
	public boolean createArtikel(Artikel piArtikel){
		Artikel artikel = piArtikel;
		int result;
		
		try {	
			String sql = "INSERT INTO " + DB_TABELLE.ARTIKEL.toString() + " (nummer, bezeichnung, beschreibung, preis, lagermenge, erstellt_Benutzer) " 
			+ " VALUES(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setLong(1, artikel.getNummer());
			stmt.setString(2, artikel.getBezeichnung());		
			stmt.setString(3, artikel.getBeschreibung());
			stmt.setDouble(4, artikel.getPreis());
			stmt.setLong(5, artikel.getLagermenge());
			stmt.setString(6, DBUSER);
			
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return false;	
	}
	
	public boolean modifyArtikel(Artikel piArtikel){	
		Artikel artikel = piArtikel;
		int artikelID;
		int result = 0;
		
		try {
			artikelID = getArtikelIDbyNummer(artikel.getNummer());
			
			if(artikelID == -1){
				return false;
			}
			
			String sql = "UPDATE "+ DB_TABELLE.ARTIKEL.toString() +" SET nummer = ?, bezeichnung = ? , beschreibung = ?"
					+ ", preis = ?, lagermenge = ?, aktiv = ?, geaendert_Benutzer = ?, geaendert_Datum = ? WHERE ID = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, artikel.getNummer());
			stmt.setString(2, artikel.getBezeichnung());
			stmt.setString(3, artikel.getBeschreibung());
			stmt.setDouble(4, artikel.getPreis());
			stmt.setInt(5, artikel.getLagermenge());
			stmt.setInt(6, artikel.getAktiv());
			stmt.setString(7, DBUSER);
			stmt.setString(8, sdf.format(timestamp));
			stmt.setInt(9, artikelID);
			
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean deleteArtikel(int piNummer){
		int nummer = piNummer;
		int artikelID;
		int result = 0;
		
		try {
			artikelID = getArtikelIDbyNummer(nummer);
			
			if(artikelID == -1){
				return false;
			}
			
			String sql = "DELETE FROM "+ DB_TABELLE.ARTIKEL.toString() +" WHERE ID = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, artikelID);
			
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	/**
	 * <h3>Beschreibung: </h3>
	 * <pre>
	 * Die Methode liefert alle in der DB vorhandenen Artikel zurï¿½ck
	 * </pre>
	 * 
	 * @return artikelliste
	 */
	public List<Artikel> selectAllArtikel(Boolean active){
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		ResultSet result = null;
		String sql = "";
		
		try {
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
		}	
		
		return artikelliste;
	}
	
	public Artikel searchArtikelByNummer(int piNummer){
		int nummer = piNummer;
		Artikel artikel = null;
		ResultSet result = null;	
		
		try {				
			String sql = "SELECT * FROM " + DB_TABELLE.ARTIKEL.toString() + " WHERE nummer = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setLong(1, nummer);

			result = stmt.executeQuery();
			
			if(result.next()){
				artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), result.getInt("aktiv"));
			}
			
		} catch (SQLException e) {
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
		}
		return artikelliste;	
	}
	
	
	public boolean deleteArtikelFromWarenkorb(int piPosition, String piEmailAdresse){
		int position = piPosition;
		String emailadresse = piEmailAdresse;
		int benutzerID;
		int result;
		ResultSet first_result = null;
		
		try {	
			String pre_sql = "SELECT ID FROM " + DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			if(!first_result.next()){
				return false;
			}
			
			benutzerID = first_result.getInt("id");

		//====================================================
			
			String sql = "DELETE FROM " + DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ? AND Position = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, benutzerID);
			stmt.setInt(2, position);

			result = stmt.executeUpdate();

			if(result != 0){
				return updateWarenkorbPositions(position, benutzerID);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean updateWarenkorbPositions(int piPosition, int piBenutzerID){
		int position = piPosition;
		int benutzerID = piBenutzerID;
		int result;

		try {
			String sql = "UPDATE " + DB_TABELLE.WARENKORB.toString() + " SET Position = (Position-1) WHERE Benutzer_ID = ? AND Position > ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, benutzerID);
			stmt.setInt(2, position);
	
			result = stmt.executeUpdate();
			
			if(result != 0){
				return true;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;		
	}
	
	public boolean addArtikelToWarenkorb(String piEmailadresse, int piArtikelnummer){
		String emailadresse = piEmailadresse;
		int artikelnummer = piArtikelnummer;	
		int result;
		
		try {
			int benutzer_ID = getBenutzerIDbyEmailadresse(emailadresse);
			int artikel_ID = getArtikelIDbyNummer(artikelnummer);
			
			if(benutzer_ID == -1 || artikel_ID == -1){
				return false;
			}
					
			//==================================================
			// prï¿½fen, ob sich der Artikel schon im Warenkorb befindet, wenn ja wird die Menge um 1 erhî’�t
			//==================================================			
			if(isArtikelImWarenkorbVorhanden(benutzer_ID, artikel_ID)){	
				
				return warenkorbArtikelMengeErhoehen(-1, artikelnummer, emailadresse);
				
			}else{
				ResultSet preResult = null;
				String pre_sql = "SELECT MAX(Position) AS highestPos FROM " + DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ?";
				
				PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
				pre_stmt.setInt(1, benutzer_ID);
				
				preResult = pre_stmt.executeQuery();
				
				if(!preResult.next()){
					return false;
				}		
				int highestPos = preResult.getInt("highestPos");
					
				
				//=====================================================
				String sql = "INSERT INTO " + DB_TABELLE.WARENKORB.toString() + " (Position, Menge, Artikel_ID, Benutzer_ID, aktiv, erstellt_Benutzer) VALUES(?, ?, ?, ?, ?, ?)";
				
				PreparedStatement stmt = getConnection().prepareStatement(sql);
				stmt.setInt(1, highestPos + 1);
				stmt.setInt(2, 1);
				stmt.setInt(3, artikel_ID);
				stmt.setInt(4, benutzer_ID);
				stmt.setInt(5, 1);
				stmt.setString(6, DBUSER);
				
				result = stmt.executeUpdate();
				
				if(result != 0){
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return false;
	}
	
	private boolean isArtikelImWarenkorbVorhanden(int piBenutzerID, int piArtikelID){
		int benutzerID = piBenutzerID;
		int artikel_ID = piArtikelID;
		ResultSet result = null;
		
		try {
			String sql = "SELECT * FROM " + DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ? AND Artikel_ID = ?";
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, benutzerID);
			stmt.setInt(2, artikel_ID);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				return true;
			}else{
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return false;
	}
	

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3>
	 * Die Menge des Artikels im Warenkorb wird verèˆ…dert.
	 * 
	 * </pre>
	 * @param piMenge
	 * @param piArtikelnummer
	 * @param piEmailAdresse
	 * @return true or false
	 */
	public boolean modifyWarenkorbArtikelMenge(int piMenge, int piArtikelnummer, String piEmailAdresse){
		String emailadresse = piEmailAdresse;
		int artikelnummer = piArtikelnummer;
		int benutzer_ID;
		int artikel_ID;
		int menge = piMenge;
		int result = 0;
		
		try {
			benutzer_ID = getBenutzerIDbyEmailadresse(emailadresse);
			artikel_ID = getArtikelIDbyNummer(artikelnummer);
			
			if(benutzer_ID == -1 || artikel_ID == -1){
				return false;
			}
			
			String sql = "UPDATE " + DB_TABELLE.WARENKORB.toString() + " SET Menge = ? WHERE Artikel_ID = ? AND Benutzer_ID = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, menge);
			stmt.setInt(2, artikel_ID);
			stmt.setInt(3, benutzer_ID);
			
			result = stmt.executeUpdate();
		
			if(result == 0){
				return false;
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3>
	 * Die Menge des Artikels im Warenkorb wird erhî’�t.
	 * 
	 * Wenn piMenge gleich -1 ist wird die Menge automatisch um 1 erhî’�t.
	 * </pre>
	 * @param piMenge
	 * @param piArtikelnummer
	 * @param piEmailAdresse
	 * @return true or false
	 */
	private boolean warenkorbArtikelMengeErhoehen(int piMenge, int piArtikelnummer, String piEmailAdresse){
		String emailadresse = piEmailAdresse;
		int artikelnummer = piArtikelnummer;
		int benutzer_ID;
		int artikel_ID;
		int menge = piMenge;
		int result = 0;
		
		try {
			benutzer_ID = getBenutzerIDbyEmailadresse(emailadresse);
			artikel_ID = getArtikelIDbyNummer(artikelnummer);
			
			if(benutzer_ID == -1 || artikel_ID == -1){
				return false;
			}
			
			//=====================================
			PreparedStatement stmt = null;
			
			if(menge == -1){
				String sql = "UPDATE " + DB_TABELLE.WARENKORB.toString() + " SET Menge = Menge + 1 WHERE Artikel_ID = ? AND Benutzer_ID = ?";
				
				stmt = getConnection().prepareStatement(sql);
				stmt.setInt(1, artikel_ID);
				stmt.setInt(2, benutzer_ID);
			}else{
				String sql = "UPDATE " + DB_TABELLE.WARENKORB.toString() + " SET Menge = ? WHERE Artikel_ID = ? AND Benutzer_ID = ?";

				stmt = getConnection().prepareStatement(sql);
				stmt.setInt(1, menge);
				stmt.setInt(2, artikel_ID);
				stmt.setInt(3, benutzer_ID);	
			}

			result = stmt.executeUpdate();
			
			if(result == 0){
				return false;
			}else{
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return true;
	}
	
	
	
}
