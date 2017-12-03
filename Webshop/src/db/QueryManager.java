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
import entity.Rolle;
import entity.WarenkorbArtikel;
import enums.ENUM_DB_TABELLE;
import enums.ENUM_ARTIKELKATEGORIE;
import enums.ENUM_ROLLE;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * <h3>Beschreibung:</h3>
 * <pre>
 * Die Klasse stellt Methoden zum Anlegen/Verändern/Löschen von Daten in der Datenbank zur Verfügung.
 * </pre>
 * 
 * @author Tim Hermbecker, Julian Hanfgarn
 */
public class QueryManager {
	
	//für Timestamp bei UPDATE-Statements
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	//für reine Datum-Angabe (Bestelldatum)
	private static final SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");
	
	//für Jahres-Angabe (Erzeugung der Bestellnummer)
	private static final SimpleDateFormat sdf_year = new SimpleDateFormat("yyyy");
	
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
	 * Die Methode liefert den aktuellen Zeitstempel für Eintragungen in der Datenbank
	 * </pre>
	 * 
	 * @return Timestamp
	 */	
	public Timestamp getCurrentTimestamp(){
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	return timestamp;
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
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.BENUTZER.toString() + " b "
					+ "left join " + ENUM_DB_TABELLE.ADRESSE.toString() + " a on(a.Benutzer_ID = b.ID) "
					+ "left join " + ENUM_DB_TABELLE.ROLLE.toString() + " r on(b.Rolle_ID = r.ID) WHERE b.emailadresse = ?";
			
			String post_sql = "SELECT * FROM " + ENUM_DB_TABELLE.ADRESSE.toString() + " WHERE Benutzer_ID = ?";					
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, piEMailAdresse);
			
			result = stmt.executeQuery();
				
			if(result.next()){	
				Adresse adresse = null;
				
				PreparedStatement post_stmt = getConnection().prepareStatement(post_sql);
				post_stmt.setInt(1, getBenutzerIDbyEmailadresse(piEMailAdresse));
					
				ResultSet post_result = post_stmt.executeQuery();
				
				// Es soll nur ein Adress-Objekt angelegt werden wenn es einen Adresse Datensatz in der DB zu dem Benutzer gibt
				if(post_result.next()){	
					adresse = new Adresse().init(result.getString("strasse"), result.getString("hausnummer"), result.getString("postleitzahl"), result.getString("ort"), result.getString("zusatz"));		
				}
				

				Rolle rolle = new Rolle().init(result.getString("bezeichnung"), result.getInt("Sicht_Warenkorb"), 
						result.getInt("Sicht_Bestellungen"), result.getInt("Sicht_Konto"), result.getInt("Sicht_Artikelstammdaten"),
						result.getInt("Sicht_Benutzerstammdaten"), result.getInt("Sicht_Bestellungstammdaten"));
	
				return benutzer.init(result.getString("emailadresse"), result.getString("passwort"), result.getString("vorname"), 
						result.getString("nachname"), adresse, rolle, result.getInt("bestaetigt"), result.getDate("registriert_Datum"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode setzt den Reset-Code für den Fall
	 * einer angeforderten Passwort-Zurücksetzung.
	 * </pre>
	 * 
	 * @param piBenutzer Benutzer
	 * @param piResetCode String
	 */
	public void setResetCode(Benutzer piBenutzer, String piResetCode){
		Benutzer benutzer = piBenutzer;
		String resetCode = piResetCode;
		try {
			String sql = "UPDATE " + ENUM_DB_TABELLE.BENUTZER.toString() + " SET reset_Code = ? WHERE emailadresse = ?";
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, resetCode);
			stmt.setString(2, benutzer.getEmailadresse());
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert den Reset-Code für einen
	 * Benutzer, der sein Passwort zurücksetzen will.
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @return resetCode String
	 */
	public String getResetCode(String piEmailadresse){
		String emailadresse = piEmailadresse;
		ResultSet result = null;
		
		try {
			String sql = "SELECT reset_Code FROM " + ENUM_DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, emailadresse);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				return result.getString("reset_Code");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert zu einer EmailAdresse die zugehörige
	 * BenutzerID aus der Datenbank.
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @return benutzer_ID
	 */
	private int getBenutzerIDbyEmailadresse(String piEmailadresse){
		String emailadresse = piEmailadresse;
		int benutzer_ID = -1;
		ResultSet result = null;
		
		try {
			String sql = "SELECT ID FROM " + ENUM_DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
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
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode löscht den Datensatz für einen Benutzer.
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @return true, wenn die Löschung erfolgreich war;
	 * andernfalls: false
	 */
	public boolean deleteBenutzer(String piEmailadresse){		
		String emailadresse = piEmailadresse;
		int benutzerID;
		int result = 0;
		
		try {
			benutzerID = getBenutzerIDbyEmailadresse(emailadresse);
			
			if(benutzerID == -1){
				return false;
			}
			
			String sql = "DELETE FROM "+ ENUM_DB_TABELLE.BENUTZER.toString() +" WHERE ID = ?";
		
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

	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode erstellt zu einem vorhandenen Benutzer
	 * den dazugehörigen Adress-Datensatz.
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @param piAdresse Adresse
	 * @return true, wenn die Adress-Anlage erfolgreich war;
	 * andernfalls: false
	 */
	public boolean createAdresse(String piEmailAdresse, Adresse piAdresse){
		String emailadresse = piEmailAdresse;
		Adresse adresse = piAdresse;
		int benutzerID;
		ResultSet first_result = null;
		int result;
		
		try {
			String pre_sql = "SELECT ID FROM " + ENUM_DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			// sicherstellen, dass es ein BenutzerObjekt gibt
			if(!first_result.next()){
				return false;
			}
			
			benutzerID = first_result.getInt("ID");
			
			//=============================================================================
			
			String sql = "INSERT INTO " + ENUM_DB_TABELLE.ADRESSE.toString() + " (strasse, hausnummer, postleitzahl, ort, zusatz, benutzer_id, erstellt_Benutzer) " 
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

	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode ändert die Adresse eines Benutzers.
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @param piEAdresse Adresse
	 * @return true, wenn die Änderung erfolgreich war;
	 * andernfalls: false
	 */
	public boolean modifyAdresse(String piEmailAdresse, Adresse piAdresse){
		String emailadresse = piEmailAdresse;
		Adresse adresse = piAdresse;
		int benutzerID;
		ResultSet first_result = null;
		int result;
		
		try {
			String pre_sql = "SELECT ID FROM " + ENUM_DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			// sicherstellen, dass es ein BenutzerObjekt
			if(!first_result.next()){
				return false;
			}
			
			benutzerID = first_result.getInt("id");
			
			//=============================================================================
			String sql = "UPDATE " + ENUM_DB_TABELLE.ADRESSE.toString() + " SET strasse = ?, hausnummer = ?, postleitzahl = ?, ort = ?,"
					+ " zusatz = ?, geaendert_Benutzer = ?, geaendert_Datum = ? WHERE Benutzer_ID = ?";
					
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, adresse.getStrasse());
			stmt.setString(2, adresse.getHausnummer());		
			stmt.setString(3, adresse.getPostleitzahl());
			stmt.setString(4, adresse.getOrt());
			stmt.setString(5, adresse.getZusatz());
			stmt.setString(6, DBUSER);
			stmt.setString(7, sdf.format(getCurrentTimestamp()));
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

	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert die ID der Rolle des
	 * jeweiligen Benutzers
	 * </pre>
	 * 
	 * @param bezeichnung ENUM_ROLLENBEZEICHNUNG
	 * @return id
	 */
	private int getRolleIDbyBezeichnung(ENUM_ROLLE bezeichnung){
		int id = -1;
		ResultSet result = null;
		
		if(bezeichnung == null){
			return id;
		}
		
		try {				
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.ROLLE.toString() + " WHERE bezeichnung = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, bezeichnung.toString());
			
			result = stmt.executeQuery();
			
			if(result.next()){
				return id = result.getInt("ID");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	
		return id;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode erstellt den Datensatz für einen Benutzer.
	 * </pre>
	 * 
	 * @param piBenutzer Benutzer
	 * @return true, wenn die Anlage erfolgreich war;
	 * andernfalls: false
	 */
	public boolean createBenutzer(Benutzer piBenutzer){
		Benutzer benutzer = piBenutzer;
		int result;
		
		try {				
			String sql = "INSERT INTO " + ENUM_DB_TABELLE.BENUTZER.toString() + " (emailadresse, passwort, vorname, nachname, Rolle_ID, bestaetigt, registriert_Datum, erstellt_Benutzer) "
						+ "VALUES( ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, benutzer.getEmailadresse());
			stmt.setString(2, benutzer.getPasswort());
			stmt.setString(3, benutzer.getVorname());
			stmt.setString(4, benutzer.getNachname());
			stmt.setInt(5, getRolleIDbyBezeichnung(ENUM_ROLLE.getRolleByName(benutzer.getRolle().getBezeichnung())));
			stmt.setInt(6, benutzer.getBestaetigt());
			stmt.setDate(7, benutzer.getRegistriertDatum());
			stmt.setString(8, DBUSER);
			
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
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode ändert den Datensatz für einen Benutzer.
	 * </pre>
	 * 
	 * @param piBenutzer Benutzer
	 * @return true, wenn die Änderung erfolgreich war;
	 * andernfalls: false
	 */
	public boolean modifyBenutzer(Benutzer piBenutzer){	
		Benutzer benutzer = piBenutzer;
		int result = 0;
		
		try {	
			String sql = "UPDATE "+ ENUM_DB_TABELLE.BENUTZER.toString() +" SET passwort = ?, vorname = ?, nachname = ?, Rolle_ID = ?, "
					+ "bestaetigt = ?, geaendert_Benutzer = ?, geaendert_Datum = ? WHERE emailadresse = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, benutzer.getPasswort());
			stmt.setString(2, benutzer.getVorname());
			stmt.setString(3, benutzer.getNachname());
			stmt.setInt(4, getRolleIDbyBezeichnung(ENUM_ROLLE.getRolleByName(benutzer.getRolle().getBezeichnung())));
			stmt.setInt(5, benutzer.getBestaetigt());
			stmt.setString(6, DBUSER);
			stmt.setString(7, sdf.format(getCurrentTimestamp()));
			stmt.setString(8, benutzer.getEmailadresse());
			
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
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode gibt alle Mitarbeiter-Datensätze zurück
	 * </pre>
	 * 
	 * @return mitarbeiterliste
	 */
	public List<Benutzer> selectAllMitarbeiter(){
		List<Benutzer> mitarbeiterliste = new ArrayList<Benutzer>();
		ResultSet result = null;
		int Rolle_Mitarbeiter = getRolleIDbyBezeichnung(ENUM_ROLLE.MITARBEITER);
		int Rolle_Admin = getRolleIDbyBezeichnung(ENUM_ROLLE.ADMINISTRATOR);
		
		try {
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.BENUTZER.toString() + " b Left Join " + 
					ENUM_DB_TABELLE.ROLLE.toString() + " r on(r.ID = b.Rolle_ID) Left Join " + ENUM_DB_TABELLE.ADRESSE.toString() +
					" a on(b.ID = a.Benutzer_ID) WHERE b.Rolle_ID IN (?, ?)";

			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, Rolle_Mitarbeiter);
			stmt.setInt(2, Rolle_Admin);
			
			result = stmt.executeQuery();
			
			while(result.next()){
				Rolle rolle = new Rolle().init(result.getString("bezeichnung"), result.getInt("Sicht_Warenkorb"), 
						result.getInt("Sicht_Bestellungen"), result.getInt("Sicht_Konto"), result.getInt("Sicht_Artikelstammdaten"),
						result.getInt("Sicht_Benutzerstammdaten"), result.getInt("Sicht_Bestellungstammdaten"));
				
				Adresse adresse = new Adresse().init(result.getString("strasse"), result.getString("hausnummer"), 
						result.getString("postleitzahl"), result.getString("ort"), result.getString("zusatz"));
				
				Benutzer mitarbeiter = new Benutzer().init(result.getString("emailadresse"), "", result.getString("vorname"), 
						result.getString("nachname"), adresse, rolle, result.getInt("bestaetigt"), result.getDate("registriert_Datum"));
				mitarbeiterliste.add(mitarbeiter);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return mitarbeiterliste;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode erstellt die Datensätze für eine Bestellung 
	 * in den entsprechenden Tabellen. Überdies werden die nicht
	 * benötigten Datensätze aus der Tabelle "Warenkorb gelöscht.
	 * </pre>
	 * 
	 * @param piBestellung Bestellung
	 * @param piBenutzer Benutzer
	 * @return bestellung
	 */
	public Bestellung createBestellung(Bestellung piBestellung, Benutzer piBenutzer){
		Bestellung bestellung = piBestellung;
		Benutzer benutzer = piBenutzer;
		int benutzer_ID;
		double bestellwert;
		int bestellung_ID;
		int result;
		int bestellartikel_result = 0;
		int warenkorb_result = 0;
		
		try {
			//Benutzer_ID holen und sichern
			benutzer_ID = getBenutzerIDbyEmailadresse(bestellung.getBenutzer().getEmailadresse());
			
			//Abbruch, wenn keine Benutzer_ID zu der E-Mail-Adresse gefunden wurde
			if(benutzer_ID == -1){
				return null;
			}
			
			//Bestellwert berechnen und sichern
			bestellwert = calculateBestellwertByBenutzerID(benutzer_ID);
			
			//Pflege der Tabelle "Bestellung"
			String sql = "INSERT INTO "+ ENUM_DB_TABELLE.BESTELLUNG.toString() +" (status, bestelldatum,"
					+ "zahlungsart, bestellwert, versandkosten, benutzer_id, erstellt_benutzer) VALUES(?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			
			stmt.setString(1, bestellung.getStatus());
			stmt.setString(2, sdf_date.format(getCurrentTimestamp()));
			stmt.setString(3, bestellung.getZahlungsart());
			stmt.setDouble(4, bestellwert);
			stmt.setDouble(5, bestellung.getVersandkosten());
			stmt.setInt(6, benutzer_ID);
			stmt.setString(7, DBUSER);
			
			result = stmt.executeUpdate();
			
			// sicherstellen, dass ein Ergebnis geliefert wird
			if(result == 0){
				return null;
			}
			
			//Bestellung_ID zur soeben generierten Bestellung holen und sichern
			bestellung_ID = getBestellungIDByBenutzerIDandBestellwert(benutzer_ID, bestellwert);
			
			//Eintragung der Bestellnummer in Abhängigkeit von der ID der Bestellung
			setBestellnummerByBestellungID(bestellung_ID);
			
			
			//Einfügen der relevanten Informationen in die Tabelle "Bestellartikel"
			String bestellartikel_sql = "INSERT INTO " + ENUM_DB_TABELLE.BESTELLARTIKEL.toString() + "(artikel_id, menge, position,"
					+ " bestellung_id, erstellt_benutzer) SELECT artikel_id, menge, position, ?, ? FROM " + 
					ENUM_DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ?";
			
			PreparedStatement bestellartikel_stmt = getConnection().prepareStatement(bestellartikel_sql);
			
			bestellartikel_stmt.setInt(1, bestellung_ID);
			bestellartikel_stmt.setString(2, DBUSER);
			bestellartikel_stmt.setInt(3, benutzer_ID);
			
			bestellartikel_result = bestellartikel_stmt.executeUpdate();
			
			// sicherstellen, dass ein Ergebnis geliefert wird
			if(bestellartikel_result == 0){
				
				//Löschen des zuvor angelegten Datensatzes in der Tabelle "Bestellung", falls es zu einem Problem gekommen ist
				String delete_bestellung_sql = "DELETE FROM " + ENUM_DB_TABELLE.BESTELLUNG.toString() + " WHERE ID = ?";
				
				PreparedStatement delete_bestellung_stmt = getConnection().prepareStatement(delete_bestellung_sql);
				delete_bestellung_stmt.setInt(1, bestellung_ID);
				
				delete_bestellung_stmt.executeUpdate();
				
				return null;
			}

			
			//Löschen des/der Datensatzes/Datensätze in der Tabelle "Warenkorb"
			String warenkorb_sql = "DELETE FROM " + ENUM_DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ?";
			
			PreparedStatement warenkorb_stmt = getConnection().prepareStatement(warenkorb_sql);
			
			warenkorb_stmt.setInt(1, benutzer_ID);
			
			warenkorb_result = warenkorb_stmt.executeUpdate();
		
			// sicherstellen, dass ein Ergebnis geliefert wird
			if(warenkorb_result == 0){
				
				//Löschen des zuvor angelegten Datensatzes in der Tabelle "Bestellung", falls es zu einem Problem gekommen ist
				String delete_bestellung_sql = "DELETE FROM " + ENUM_DB_TABELLE.BESTELLUNG.toString() + " WHERE ID = ?";
				
				PreparedStatement delete_bestellung_stmt = getConnection().prepareStatement(delete_bestellung_sql);
				
				delete_bestellung_stmt.setInt(1, bestellung_ID);
				
				delete_bestellung_stmt.executeUpdate();
				
				
				//Löschen des/der zuvor angelegten Datensatzes/Datensätze in der Tabelle "Bestellartikel", falls es zu einem Problem gekommen ist
				String delete_bestellartikel_sql = "DELETE FROM " + ENUM_DB_TABELLE.BESTELLARTIKEL.toString() + " WHERE Bestellung_ID = ?";
				
				PreparedStatement delete_bestellartikel_stmt = getConnection().prepareStatement(delete_bestellartikel_sql);
				delete_bestellartikel_stmt.setInt(1, bestellung_ID);
				
				delete_bestellartikel_stmt.executeUpdate();
				
				return null;
			}
						
			//Vorbereitung der Rückgabe der erstellten Bestellung
			bestellung = selectBestellungByBestellungID(bestellung_ID, benutzer);
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bestellung;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert den Bestellwert einer Bestellung
	 * eines Benutzers.
	 * </pre>
	 * 
	 * @param Benutzer_ID int
	 * @return bestellwert double
	 */
	private double calculateBestellwertByBenutzerID(int piBenutzerID){
		double bestellwert = -1;
		int benutzer_ID = piBenutzerID;
		ResultSet result = null;
		
		try{
			String sql = "SELECT sum(w.menge * a.preis) as 'bestellwert' FROM " + 
					ENUM_DB_TABELLE.WARENKORB.toString() + " w LEFT JOIN "+ ENUM_DB_TABELLE.ARTIKEL.toString() + " a on "
					+ "w.Artikel_ID = a.ID WHERE w.Benutzer_ID = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			
			stmt.setInt(1, benutzer_ID);
			
			result = stmt.executeQuery();
			
			if(result.next()){
				bestellwert = result.getDouble("bestellwert");
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bestellwert;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert die ID einer Bestellung der Benutzer-ID
	 * und dem Bestellwert nach.
	 * </pre>
	 * 
	 * @param Benutzer_ID int
	 * @param bestellwert double
	 * @return bestellung_ID int
	 */
	private int getBestellungIDByBenutzerIDandBestellwert(int piBenutzerID, double piBestellwert){
		int benutzer_ID = piBenutzerID;
		double bestellwert = piBestellwert;
		int bestellung_ID = -1;
		ResultSet result = null;
		
		try{
			String sql = "SELECT ID FROM " + ENUM_DB_TABELLE.BESTELLUNG.toString() + " WHERE Benutzer_ID = ? AND "
					+ "bestellwert = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			
			stmt.setInt(1, benutzer_ID);
			stmt.setDouble(2, bestellwert);
			
			result = stmt.executeQuery();
			
			// sicherstellen, dass ein Ergebnis geliefert wird und Zuweisung
			if(result.next()){
				bestellung_ID = result.getInt("id");
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bestellung_ID;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode setzt die Bestellnummer anhand der
	 * ID einer Bestellung. Das aktuelle Jahr wird angefügt.
	 * </pre>
	 * 
	 * @param piBestellungID int
	 * @return bestellnummer String
	 */	
	private Boolean setBestellnummerByBestellungID(int piBestellungID){
		int bestellung_ID = piBestellungID;
		int result = 0;
		
		try{
			String sql = "UPDATE " + ENUM_DB_TABELLE.BESTELLUNG.toString() + " SET bestellnummer = ?"
					+ " WHERE ID = ? AND Bestellnummer = 'keine_Bestellnummer_definiert'";
					
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, String.valueOf(bestellung_ID) + '-' + sdf_year.format(getCurrentTimestamp()));
			stmt.setInt(2, bestellung_ID);
			
			result = stmt.executeUpdate();
			 
			// sicherstellen, dass ein Ergebnis geliefert wird
			if(result == 0){
				return false;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die liefert alle Informationen einer Bestellung anhand
	 * ihrer ID
	 * </pre>
	 * 
	 * @param piBestellungID int
	 * @param piBenutzer Benutzer
	 * @return bestellung Bestellung
	 */	
	private Bestellung selectBestellungByBestellungID(int piBestellungID, Benutzer piBenutzer){
		int bestellung_ID = piBestellungID;
		Benutzer benutzer = piBenutzer;
		Bestellung bestellung = null;
		ResultSet result = null;
		
		try{
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.BESTELLUNG.toString() + " WHERE ID = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			
			stmt.setInt(1, bestellung_ID);

			result = stmt.executeQuery();
			
			while(result.next()){
				
				bestellung = new Bestellung().init(result.getString("bestellnummer"), result.getDate("bestelldatum"),
					result.getString("status"), result.getString("zahlungsart"), result.getDate("voraussichtliches_Lieferdatum"), 
					result.getDouble("bestellwert"), result.getDouble("versandkosten"), benutzer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bestellung;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert die Artikel-ID eines Artikels 
	 * aufgrund seiner Artikelnummer.
	 * </pre>
	 * 
	 * @param piNummer int
	 * @return Artikel_ID
	 */
	private int getArtikelIDbyNummer(int piNummer){
		int artikelnummer = piNummer;
		int Artikel_ID = -1;
		ResultSet result = null;
		
		try {
			String sql = "SELECT ID FROM " + ENUM_DB_TABELLE.ARTIKEL.toString() + " WHERE Nummer = ?";
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

	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode erstellt den Datensatz für einen Artikel.
	 * </pre>
	 * 
	 * @param piArtikel Artikel
	 * @return true, wenn die Anlage erfolgreich war;
	 * andernfalls: false
	 */
	public boolean createArtikel(Artikel piArtikel){
		Artikel artikel = piArtikel;
		int result;
		
		try {	
			String sql = "INSERT INTO " + ENUM_DB_TABELLE.ARTIKEL.toString() + " (nummer, bezeichnung, beschreibung, preis, lagermenge, meldebestand, bild, kategorie_1, kategorie_2, erstellt_Benutzer) " 
			+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setLong(1, artikel.getNummer());
			stmt.setString(2, artikel.getBezeichnung());		
			stmt.setString(3, artikel.getBeschreibung());
			stmt.setDouble(4, artikel.getPreis());
			stmt.setLong(5, artikel.getLagermenge());
			stmt.setLong(6, artikel.getMeldebestand());
			stmt.setBytes(7, artikel.getBild());
			stmt.setString(8, artikel.getKategorie_1());
			stmt.setString(9, artikel.getKategorie_2());
			stmt.setString(10, DBUSER);
			
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
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode ändert den Datensatz für einen Artikel.
	 * </pre>
	 * 
	 * @param piArtikel Artikel
	 * @return true, wenn die Änderung erfolgreich war;
	 * andernfalls: false
	 */
	public boolean modifyArtikel(Artikel piArtikel){	
		Artikel artikel = piArtikel;
		int artikelID;
		int result = 0;
		
		try {
			artikelID = getArtikelIDbyNummer(artikel.getNummer());
			
			if(artikelID == -1){
				return false;
			}
			
			String sql = "UPDATE "+ ENUM_DB_TABELLE.ARTIKEL.toString() +" SET nummer = ?, bezeichnung = ? , beschreibung = ?"
					+ ", preis = ?, lagermenge = ?, meldebestand = ?, bild = ?, kategorie_1 = ?, kategorie_2 = ?, aktiv = ?, geaendert_Benutzer = ?, "
					+ "geaendert_Datum = ? WHERE ID = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, artikel.getNummer());
			stmt.setString(2, artikel.getBezeichnung());
			stmt.setString(3, artikel.getBeschreibung());
			stmt.setDouble(4, artikel.getPreis());
			stmt.setInt(5, artikel.getLagermenge());
			stmt.setInt(6, artikel.getMeldebestand());
			stmt.setBytes(7, artikel.getBild());
			stmt.setString(8, artikel.getKategorie_1());
			stmt.setString(9, artikel.getKategorie_2());
			stmt.setInt(10, artikel.getAktiv());
			stmt.setString(11, DBUSER);
			stmt.setString(12, sdf.format(getCurrentTimestamp()));
			stmt.setInt(13, artikelID);
			
			result = stmt.executeUpdate();
			
			//übergebe Status (aktiv/deaktiv) an private Methode zur Übernahme in Warenkorb-Tabelle
			activateDeaktivateWarenkorbArtikel(artikel.getNummer(), artikel.getAktiv());
			
			if(result != 0){
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode ändert Datensätze in der Warenkorb-Tabelle 
	 * abhängig davon, ob der entsprechende Artikel aktiv oder
	 * deaktiv ist.
	 * </pre>
	 * 
	 * @param piNummer int
	 * @param piAktiv int
	 * @return true, wenn die Änderung(en) erfolgreich war(en);
	 * andernfalls: false
	 */
	private boolean activateDeaktivateWarenkorbArtikel(int piNummer, int piAktiv){
		int nummer = piNummer;
		int aktiv = piAktiv;
		int artikelID;
		int result = 0;
		
		try {
			artikelID = getArtikelIDbyNummer(nummer);
			
			if(artikelID == -1){
				return false;
			}
			
			String sql = "UPDATE "+ ENUM_DB_TABELLE.WARENKORB.toString() + " SET aktiv = ?, geaendert_Benutzer = ?, "
					+ "geaendert_Datum = ? WHERE Artikel_ID = ?";
		
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, aktiv);
			stmt.setString(2, DBUSER);
			stmt.setString(3, sdf.format(getCurrentTimestamp()));
			stmt.setInt(4, artikelID);
			
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
	 * Die Methode liefert alle in der DB vorhandenen Artikel zurück
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
				sql = "SELECT * FROM " + ENUM_DB_TABELLE.ARTIKEL.toString() + " WHERE aktiv <> 0";
			}
			else if(!active){
				sql = "SELECT * FROM " + ENUM_DB_TABELLE.ARTIKEL.toString();
			}
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
				result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), 
						result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), 
						result.getInt("meldebestand"), result.getBytes("bild"), result.getString("kategorie_1"), result.getString("kategorie_2"), 
						result.getInt("aktiv"));
				artikelliste.add(artikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return artikelliste;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert alle Artikel einer Kategorie.
	 * </pre>
	 * 
	 * @param kategorie ENUM_ARTIKELKATEGORIE
	 * @return artikelliste
	 */
	public List<Artikel> searchArtikelByKategorie1(ENUM_ARTIKELKATEGORIE kategorie){
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		
		ResultSet result = null;	
		
		try {			
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.ARTIKEL.toString() + " WHERE kategorie_1 like ? AND aktiv = 1";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, kategorie.toString());

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), 
						result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), 
						result.getInt("meldebestand"), result.getBytes("bild"), result.getString("kategorie_1"), result.getString("kategorie_2"),
						result.getInt("aktiv"));
				artikelliste.add(artikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return artikelliste;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert alle Artikel einer Kategorie.
	 * </pre>
	 * 
	 * @param kategorie ENUM_ARTIKELKATEGORIE
	 * @return artikelliste
	 */
	public List<Artikel> searchArtikelByKategorie2(ENUM_ARTIKELKATEGORIE kategorie1, ENUM_ARTIKELKATEGORIE kategorie2){
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		
		ResultSet result = null;	
		
		try {			
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.ARTIKEL.toString() + " WHERE kategorie_1 like ? AND kategorie_2 like ? AND aktiv = 1";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, kategorie1.toString());
			stmt.setString(2, kategorie2.toString());

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), 
						result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), 
						result.getInt("meldebestand"), result.getBytes("bild"), result.getString("kategorie_1"), result.getString("kategorie_2"), 
						result.getInt("aktiv"));
				artikelliste.add(artikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return artikelliste;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert alle Artikel, die der 
	 * gesuchten Bezeichnung entsprechen.
	 * </pre>
	 * 
	 * @param piBezeichung String
	 * @return artikelliste
	 */
	public List<Artikel> searchArtikelByBezeichnung(String piBezeichnung){
		if (piBezeichnung == null || piBezeichnung.isEmpty()){
			return selectAllArtikel(true);
		}
		
		String bezeichnung = piBezeichnung;
		List<Artikel> artikelliste = new ArrayList<Artikel>();
		ResultSet result = null;	
		
		try {			
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.ARTIKEL.toString() + " WHERE bezeichnung like ? AND aktiv = 1";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, "%" + bezeichnung + "%");

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"),
						result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"),
						result.getInt("meldebestand"), result.getBytes("bild"), result.getString("kategorie_1"), result.getString("kategorie_2"),
						result.getInt("aktiv"));
				artikelliste.add(artikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return artikelliste;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert alle Artikel, die der 
	 * gesuchten Bezeichnung entsprechen.
	 * </pre>
	 * 
	 * @param piBezeichung String
	 * @return artikelliste
	 */
	public Artikel selectArtikelByBezeichnung(String piBezeichnung){	
		String bezeichnung = piBezeichnung;
		ResultSet result = null;
		
		try {			
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.ARTIKEL.toString() + " WHERE bezeichnung = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, bezeichnung);

			result = stmt.executeQuery();
			
			if(result.next()){
				return new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"),
						result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"),
						result.getInt("meldebestand"), result.getBytes("bild"), result.getString("kategorie_1"), result.getString("kategorie_2"),
						result.getInt("aktiv"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return null;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert einen Artikel mit 
	 * der gesuchten Artikelnummer.
	 * </pre>
	 * 
	 * @param piNummer int
	 * @return artikel
	 */
	public Artikel searchArtikelByNummer(int piNummer){
		int nummer = piNummer;
		Artikel artikel = null;
		ResultSet result = null;	
		
		try {				
			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.ARTIKEL.toString() + " WHERE nummer = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setLong(1, nummer);

			result = stmt.executeQuery();
			
			if(result.next()){
				artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), 
						result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), 
						result.getInt("meldebestand"), result.getBytes("bild"), result.getString("kategorie_1"), result.getString("kategorie_2"), 
						result.getInt("aktiv"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		return artikel;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode liefert alle Artikel, die der gesuchte 
	 * Benutzer in seinen Warenkorb gelegt hat.
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @return artikelliste
	 */
	public List<WarenkorbArtikel> selectAllWarenkorbartikelByBenutzeremailadresse(String piEmailadresse){
		String emailadresse = piEmailadresse;
		List<WarenkorbArtikel> artikelliste = new ArrayList<WarenkorbArtikel>();
		ResultSet result = null;
		
		if(emailadresse == null || emailadresse.isEmpty()){
			return null;
		}
		
		try {					
			String sql = "SELECT w.position, w.menge, a.nummer, a.bezeichnung, a.beschreibung, a.preis, a.lagermenge, "
					+ "a.meldebestand, a.bild, a.kategorie_1, a.kategorie_2, a.aktiv FROM " + ENUM_DB_TABELLE.WARENKORB.toString() + 
					" w INNER JOIN "+ ENUM_DB_TABELLE.BENUTZER.toString() + " b ON w.Benutzer_ID = b.ID LEFT JOIN " + 
					ENUM_DB_TABELLE.ARTIKEL.toString() + " a ON a.ID = w.Artikel_ID WHERE b.emailadresse = ? AND w.aktiv = 1";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, piEmailadresse);

			result = stmt.executeQuery();
			
			while(result.next()){
				Artikel artikel = new Artikel().init(result.getString("bezeichnung"), result.getInt("nummer"), 
						result.getString("beschreibung"), result.getDouble("preis"), result.getInt("lagermenge"), 
						result.getInt("meldebestand"), result.getBytes("bild"), result.getString("kategorie_1"), result.getString("kategorie_2"), 
						result.getInt("aktiv"));
				WarenkorbArtikel warenkorbartikel = new WarenkorbArtikel().init(artikel, result.getInt("position"), 
						result.getInt("menge"));
				artikelliste.add(warenkorbartikel);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return artikelliste;	
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode löscht Artikel aus dem Warenkorb eines Benutzers.
	 * </pre>
	 * 
	 * @param piPosition int
	 * @param piEMailAdresse String
	 * @return true, wenn die Löschung erfolgreich war;
	 * andernfalls: false
	 */
	public boolean deleteArtikelFromWarenkorb(int piPosition, String piEmailAdresse){
		int position = piPosition;
		String emailadresse = piEmailAdresse;
		int benutzerID;
		int result;
		ResultSet first_result = null;
		
		try {	
			String pre_sql = "SELECT ID FROM " + ENUM_DB_TABELLE.BENUTZER.toString() + " WHERE emailadresse = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setString(1, emailadresse);
			
			first_result = pre_stmt.executeQuery();
			
			if(!first_result.next()){
				return false;
			}
			
			benutzerID = first_result.getInt("id");

		//====================================================
			
			String sql = "DELETE FROM " + ENUM_DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ? AND Position = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, benutzerID);
			stmt.setInt(2, position);

			result = stmt.executeUpdate();

			if(result != 0){
				updateWarenkorbPositions(position, benutzerID);
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode ändert die Position eines Artikels 
	 * im Warenkorb eines Benutzers.
	 * </pre>
	 * 
	 * @param piPosition int
	 * @param piBenutzerID int
	 */
	public void updateWarenkorbPositions(int piPosition, int piBenutzerID){
		int position = piPosition;
		int benutzerID = piBenutzerID;

		try {
			String sql = "UPDATE " + ENUM_DB_TABELLE.WARENKORB.toString() + " SET Position = (Position-1), "
					+ "geaendert_Benutzer = ?, geaendert_Datum = ? WHERE Benutzer_ID = ? AND Position > ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setString(1, DBUSER);
			stmt.setString(2, sdf.format(getCurrentTimestamp()));
			stmt.setInt(3, benutzerID);
			stmt.setInt(4, position);
	
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * <h3>Beschreibung:</h3>
	 * <pre>
	 * Die Methode fügt dem Warenkorb eines Benutzers einen 
	 * Artikel hinzu.
	 * </pre>
	 * 
	 * @param piEMailAdresse String
	 * @param piArtikelnummer int
	 * @return true, wenn die Anlage erfolgreich war;
	 * andernfalls: false
	 */
	public boolean addArtikelToWarenkorb(String piEmailadresse, int piArtikelnummer, int piMenge){
		String emailadresse = piEmailadresse;
		int artikelnummer = piArtikelnummer;	
		int menge = piMenge;
		int result;
		
		try {
			int benutzer_ID = getBenutzerIDbyEmailadresse(emailadresse);
			int artikel_ID = getArtikelIDbyNummer(artikelnummer);
			
			if(benutzer_ID == -1 || artikel_ID == -1){
				return false;
			}
					
			ResultSet preResult = null;
			String pre_sql = "SELECT MAX(Position) AS highestPos FROM " + ENUM_DB_TABELLE.WARENKORB.toString() + " WHERE Benutzer_ID = ?";
			
			PreparedStatement pre_stmt = getConnection().prepareStatement(pre_sql);
			pre_stmt.setInt(1, benutzer_ID);
			
			preResult = pre_stmt.executeQuery();
			
			if(!preResult.next()){
				return false;
			}		
			int highestPos = preResult.getInt("highestPos");
				
			
			//=====================================================
			String sql = "INSERT INTO " + ENUM_DB_TABELLE.WARENKORB.toString() + " "
					+ "(Position, Menge, Artikel_ID, Benutzer_ID, aktiv, erstellt_Benutzer) VALUES(?, ?, ?, ?, ?, ?)";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, highestPos + 1);
			stmt.setInt(2, menge);
			stmt.setInt(3, artikel_ID);
			stmt.setInt(4, benutzer_ID);
			stmt.setInt(5, 1);
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
	
	

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3>
	 * Die Menge des Artikels im Warenkorb wird verändert.
	 * </pre>
	 * 
	 * @param piMenge
	 * @param piArtikelnummer
	 * @param piEmailAdresse
	 * @return true, wenn die Änderung erfolgreich war;
	 * andernfalls: false
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
			
			String sql = "UPDATE " + ENUM_DB_TABELLE.WARENKORB.toString() + " SET Menge = ?, geaendert_Benutzer = ?, "
					+ "geaendert_Datum = ? WHERE Artikel_ID = ? AND Benutzer_ID = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, menge);
			stmt.setString(2, DBUSER);
			stmt.setString(3, sdf.format(getCurrentTimestamp()));
			stmt.setInt(4, artikel_ID);
			stmt.setInt(5, benutzer_ID);
			
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
	 * Die Methode liefert alle Bestellungen eines Benutzers
	 * </pre>
	 * 
	 * @param piBenutzer Benutzer
	 * @return bestellungListe
	 */
	public List<Bestellung> selectAllBestellungenByBenutzer(Benutzer piBenutzer){
		Benutzer benutzer = piBenutzer;
		List<Bestellung> bestellungListe = new ArrayList<Bestellung>();
		ResultSet result = null;
		
		int benutzerID = getBenutzerIDbyEmailadresse(benutzer.getEmailadresse());
		
		try{

			String sql = "SELECT * FROM " + ENUM_DB_TABELLE.BESTELLUNG.toString() + " WHERE Benutzer_ID = ?";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			stmt.setInt(1, benutzerID);

			result = stmt.executeQuery();
			
			while(result.next()){
				
				Bestellung bestellung = new Bestellung().init(result.getString("bestellnummer"), result.getDate("bestelldatum")
					, result.getString("status"), result.getString("zahlungsart"), result.getDate("voraussichtliches_Lieferdatum"), 
					result.getDouble("bestellwert"), result.getDouble("versandkosten"), benutzer);
				
				bestellungListe.add(bestellung);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		return bestellungListe;
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3>
	 * Die Methode liefert alle Bestellungen 
	 * </pre>
	 * 
	 * @return bestellungListe
	 */
	public List<Bestellung> selectAllBestellungen(){
		List<Bestellung> bestellungListe = new ArrayList<Bestellung>();
		ResultSet result = null;
		
		try{

			String sql = "select bes.*, ben.*, r.*, a.* from " + ENUM_DB_TABELLE.BESTELLUNG.toString() + " bes left join " + 
					ENUM_DB_TABELLE.BENUTZER.toString() + " ben on bes.Benutzer_ID = ben.ID left join " + 
					ENUM_DB_TABELLE.ROLLE.toString() + " r on ben.Rolle_ID = r.ID left join " + ENUM_DB_TABELLE.ADRESSE.toString() 
					+ " a on a.Benutzer_ID = ben.ID";
			
			PreparedStatement stmt = getConnection().prepareStatement(sql);

			result = stmt.executeQuery();
			
			while(result.next()){
				Rolle rolle = new Rolle().init(result.getString("bezeichnung"), result.getInt("Sicht_Warenkorb"), 
						result.getInt("Sicht_Bestellungen"), result.getInt("Sicht_Konto"), 
						result.getInt("Sicht_Artikelstammdaten"), result.getInt("Sicht_Benutzerstammdaten"), 
						result.getInt("Sicht_Bestellungstammdaten"));
				
				Adresse adresse = new Adresse().init(result.getString("strasse"), result.getString("hausnummer"), 
						result.getString("postleitzahl"), result.getString("ort"), result.getString("zusatz"));
				
				Benutzer benutzer = new Benutzer().init(result.getString("emailadresse"), "", result.getString("vorname"), 
						result.getString("nachname"), adresse, rolle, result.getInt("bestaetigt"), result.getDate("registriert_Datum"));
				
				Bestellung bestellung = new Bestellung().init(result.getString("bestellnummer"), result.getDate("bestelldatum")
					, result.getString("status"), result.getString("zahlungsart"), result.getDate("voraussichtliches_Lieferdatum"), 
					result.getDouble("bestellwert"), result.getDouble("versandkosten"), benutzer);
				
				bestellungListe.add(bestellung);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
				
		return bestellungListe;
	}
	
	
}
