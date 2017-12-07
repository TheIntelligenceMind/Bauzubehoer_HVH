package helper;

import entity.Benutzer;
import entity.Bestellung;
import entity.Nachricht;
import entity.WarenkorbArtikel;
import enums.ENUM_ZAHLUNGSART;

import java.text.DecimalFormat;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.smtp.SMTPMessage;

/**
 * <pre>
 * <h3>Beschreibung:</h3> Die Klasse ist für den Themenbereich Mails zuständig.
 * Hier werden Mails generiert und versandt.
 * </pre>
 *  @author Tim Hermbecker
 */
public class MailHelper {
	private static final MailHelper instance = new MailHelper();
	
	private MailHelper(){
		
	}
	
	public static MailHelper getInstance(){
		return instance;
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode liefert die nötigen Mailserver Credentials zurück. 
	 * </pre>
	 * @return props
	 */
	private Properties getProperties(){
		Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class",
	            "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "805");

		return props;
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode liefert eine Session zurück.
	 * </pre>
	 * @return session
	 */
	private Session getSession(){
		Session session = Session.getDefaultInstance(getProperties(), new javax.mail.Authenticator() {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication() {
	        	return new PasswordAuthentication("bauzubehoer.hvh@gmail.com","WIPWebshop");
	        }
	    });
		
		return session;
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode verschickt eine Zurücksetzungs E-Mail an den Kunden.
	 * </pre>
	 * @param emailadresse
	 * @param resetCode
	 * @return boolean
	 */
	public boolean sendResetEmail(String emailadresse, String resetCode){
		Session session = getSession();

	    try {
	        MimeMessage message = new SMTPMessage(session);
	        // Absender setzen
	        message.setFrom(new InternetAddress("bauzubehoer.hvh@gmail.com"));
	       
	        // Empfänger setzen
	        message.setRecipients(Message.RecipientType.TO,
	                                 InternetAddress.parse(emailadresse));

	        // Betreffzeile setzen
	        message.setSubject("Bauzubehoer HVH - Passwort zurücksetzen");
	        
	        // Mail Inhalt setzen
	        message.setContent(getResetMailContent(emailadresse, resetCode), "text/html");
	        
	        // Mail verschicken
	        Transport.send(message);
	    }catch (MessagingException e) {
	    	return false;
	    }
	    return true;
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode verschickt eine Kontakt E-Mail an die HVH E-Mail-Adresse.
	 * </pre>
	 * @param nachricht
	 * @param registrierterBenutzer
	 */
	public void sendKontaktMail(Nachricht nachricht, boolean registrierterBenutzer){
		Session session = getSession();

	    try {
	        MimeMessage message = new SMTPMessage(session);
	        // Absender setzen
	        message.setFrom(new InternetAddress("bauzubehoer.hvh@gmail.com"));
	       
	        // Empfänger setzen
	        message.setRecipients(Message.RecipientType.TO,
	                                 InternetAddress.parse("bauzubehoer.hvh@gmail.com"));

	        // Betreffzeile setzen
	        message.setSubject("Kontakt E-Mail");
	        
	        // Mail Inhalt setzen
	        message.setContent(getKontaktMailContent(nachricht, registrierterBenutzer), "text/html");
	        
	        // Mail verschicken
	        Transport.send(message);
	    }catch (MessagingException e) {
	        throw new RuntimeException(e);         
	    }
		
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode verschickt eine Rechnungsmail an die E-Mail des übergebenen Kunden.
	 * </pre>
	 * @param benutzer
	 * @param bestellung
	 * @param artikelliste
	 */
	public void sendRechnungsmail(Benutzer benutzer, Bestellung bestellung, List<WarenkorbArtikel> artikelliste){
		Session session = getSession();

	    try {
	        MimeMessage message = new SMTPMessage(session);
	        // Absender setzen
	        message.setFrom(new InternetAddress("bauzubehoer.hvh@gmail.com"));
	       
	        // Empfänger setzen
	        message.setRecipients(Message.RecipientType.TO,
	                                 InternetAddress.parse(benutzer.getEmailadresse()));

	        // Betreffzeile setzen
	        message.setSubject("Bauzubehoer HVH - Bestellbestätigung");
	        
	        // Mail Inhalt je nach Zahlungsart setzen
	        String mailContent = null;
	        if(bestellung.getZahlungsart().equals(ENUM_ZAHLUNGSART.RECHNUNG.toString())){
	        	mailContent = getRechnungsmailContentRechnung(benutzer, bestellung, artikelliste);
	        }else if(bestellung.getZahlungsart().equals(ENUM_ZAHLUNGSART.VORKASSE.toString())){
	        	mailContent = getRechnungsmailContentVorkasse(benutzer, bestellung, artikelliste);
	        }

	        // Wenn keine Zahlungsart gefunden werden kann wird ein Fehler geworfen
	        if(mailContent == null){
	        	throw new RuntimeException(); 
	        }

	        message.setContent(mailContent, "text/html");
	        
	        // Mail verschicken
	        Transport.send(message);
	    }catch (MessagingException e) {
	        throw new RuntimeException(e);         
	    }
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode verschickt eine Bestätigungsmail zur Registrierung an die E-Mail des übergebenen Kunden.
	 * </pre>
	 * @param benutzer
	 */
	public void sendBestaetigungsmail(Benutzer benutzer){ 
	    Session session = getSession();

	    try {
	    	MimeMessage message = new SMTPMessage(session);
	        // Absender setzen
	        message.setFrom(new InternetAddress("bauzubehoer.hvh@gmail.com"));
	       
	        // Empfänger setzen
	        message.setRecipients(Message.RecipientType.TO,
	                                 InternetAddress.parse(benutzer.getEmailadresse()));

	        // Betreffzeile setzen
	        message.setSubject("Bauzubehoer HVH - Registrierung abschliessen!");
	        
	        // Mail Inhalt setzen
	        message.setContent(getBestaetigungsmailContent(benutzer), "text/html");
	        
	        // Mail verschicken
	        Transport.send(message);
	    }catch (MessagingException e) {
	        throw new RuntimeException(e);         
	    }
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode liefert den Inhalt der ResetMail zurück.
	 * </pre>
	 * @param emailadresse
	 * @param resetCode
	 * @return content
	 */
	private String getResetMailContent(String emailadresse, String resetCode){
		String content =  "<html>"
				+ "<body>"
				+ "<style>"
				+ "p{margin:0; padding:0;}"
				+ "</style>"
				+ "<p>Guten Tag,</p>"
				+ "<br>"
				+ "<p>um Ihr Passwort zur&uuml;ckzusetzen, klicken Sie bitte auf den nachfolgenden Link und befolgen Sie die weiteren Anweisungen:</p>"
				+ "<p>http://localhost:8080/Webshop/registrieren?method=pwResetAnzeigen&bMail=" + emailadresse + "&resetCode=" + resetCode + "</p>"
				+ "<br>"
				+ "<p>Wenn Sie nicht der richtige Adressat sind oder diese E-Mail irrt&uuml;mlich erhalten haben, ignorieren und l&ouml;schen Sie diese Mail.</p>"
				+ "<br>"
				+ "<p>Haben Sie noch Fragen? Schreiben Sie eine Mail an bauzubehoer.hvh@gmail.com oder rufen Sie unsere kostenlose Service-Hotline 0800 8228 100 an.</p>"
				+ "<br>"
				+ "<p>Mit den besten Gr&uuml;ßen</p>"
				+ "<p>Bauzubeh&ouml;r-HVH, Kundenbetreuung</p>"
				+ "</body>"
				+ "</html>";

		return content;
	}

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode liefert den Inhalt der KontaktMail zurück.
	 * </pre>
	 * @param nachricht
	 * @param registrierterBenutzer
	 * @return content
	 */
	private String getKontaktMailContent(Nachricht nachricht, boolean registrierterBenutzer){
		
		String content =  "<html>"
						+ "<body>"
						+ "<style>"
						+ "p{margin:0; padding:0;}"
						+ "</style>"
						+ "<p>Absender: " + nachricht.getAbsenderAdresse() + "</p>"
						+ "<p>registriert: " + (registrierterBenutzer ? "Ja" : "Nein") + "</p>"
						+ "<br>"
						+ "<br>"
						+ "<p>====================== Start =========================</p>"
						+ "<br>"
						+ "<p>Betreff: " + nachricht.getBetreff() + "</p>"
						+ "<br>"
						+ "<p>Nachricht: " + nachricht.getInhalt() + "</p>"			
						+ "<br>"
						+ "<p>====================== Ende =========================</p>"
						+ "</body>"
						+ "</html>";
		
		return content;
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode liefert den Inhalt der Rechnungsmail (Vorkasse) zurück.
	 * </pre>
	 * @param benutzer
	 * @param bestellung
	 * @param artikelliste
	 * @return content
	 */
	private String getRechnungsmailContentVorkasse(Benutzer benutzer, Bestellung bestellung, List<WarenkorbArtikel> artikelliste){	
		DecimalFormat formater = new DecimalFormat("#0.00");
		
		String content =  "<html>"
						+ "<body>"
						+ "<style> p{margin:0; padding:0;}"
						+ "table thead th, table tbody td{margin-right: 10px;}"
						+ "table{border-collapse: collapse;}"
						+ "table, td, th{border: 1px solid black;}"
						+ "</style>"
						+ "<p>Sehr geehrte(r) " + benutzer.getVorname() + " " + benutzer.getNachname() + ",</p>" 
						+ "</br>"
						+ "<p>Bauzubeh&ouml;r HVH dankt Ihnen f&uuml;r Ihre Bestellung, deren Eingang wir hiermit best&auml;tigen.</p>"
						+ "</br>"
						+ "<p>Sie haben die Zahlungsart \"Vorkasse\" gew&auml;hlt. Der Versand erfolgt, sobald die Rechnungssumme (s.u.) auf unserem Konto eingegangen ist.</p>"
						+ "</br>"
						+ "<p>Die gew&ouml;hnliche Lieferzeit betr&auml;gt zwei bis vier Werktage.</p>"
						+ "</br>"
						+ "<p>Nachstehend finden Sie die einzelnen Positionen Ihrer Bestellung:</p>"
						+ "<p style='text-decoration:underline; margin-bottom: 5px;'>Rechnungsnummer: " + bestellung.getBestellnummer() + "</p>"
						+ "<table><thead><tr><th>Position</th><th>Menge</th><th>Artikelnummer</th><th>Artikelbezeichnung</th><th>Einzelpreis</th><th>Gesamtpreis</th></tr></thead>"
						+ "<tbody>";
						
		for(int i = 0; i < artikelliste.size(); i++){
			content = content.concat("<tr>");
			content = content.concat("<td>" + String.valueOf(i+1) + "</td>");
			content = content.concat("<td>" + artikelliste.get(i).getMenge() + "</td>");
			content = content.concat("<td>" + artikelliste.get(i).getArtikel().getNummer() + "</td>");
			content = content.concat("<td>" + artikelliste.get(i).getArtikel().getBezeichnung() + "</td>");
			content = content.concat("<td>" + formater.format(artikelliste.get(i).getArtikel().getPreis()).replace(".", ",") + "&euro;</td>");
			content = content.concat("<td>" + formater.format(artikelliste.get(i).getArtikel().getPreis() * artikelliste.get(i).getMenge()).replace(".", ",") + "&euro;</td>");
			content = content.concat("</tr>");
		}
		
		double gesamtBestellwert = bestellung.getBestellwert() + 20.00; // 20€Versandkosten pauschal
							
		content = content.concat( "</tbody></table>"
						+ "</br>"
						+ "<p>Rechnungssumme (inkl. 20&euro; Standard-Versandkosten): <i style='font-size: 16px; font-weight:bold;'>" + formater.format(gesamtBestellwert).replace(".", ",") + "&euro;</i></p>"
						+ "</br>"
						+ "<p>Bitte &uuml;berweisen Sie die Rechnungssumme unter Angabe der Rechnungsnummer im Verwendungszweck auf folgendes Konto:</p>"
						+ "<p style='margin-top: 5px;'>IBAN: DE82 1234 5678 9000 1005 28</p>"
						+ "<p style='margin-top: 5px;'>BIC: HVHLABADE082280</p>"
						+ "<p style='margin-top: 5px;'>HVH-Landesbank Deutschland AG</p>"
						+ "</br>"
						+ "</br>"
						+ "<p>Die Widerrufsbelehrung und ein entsprechendes Formular befinden sich im Anhang.</p>"
						+ "</br>"
						+ "<p>Haben Sie noch Fragen? Schreiben Sie eine Mail bauzubehoer.hvh@gmail.com oder rufen Sie unsere kostenlose Service-Hotline 0800 8228 100 an.</p>"
						+ "</br>"
						+ "<p>Wir w&uuml;nschen Ihnen viel Vergn&uuml;gen mit Ihrer Bestellung!</p>"
						+ "</br>"
						+ "<p>Mit den besten Gr&uuml;ßen</p>"
						+ "<p>Bauzubeh&ouml;r-HVH, Kundenbetreuung</p>"
						+ "</body>"
						+ "</html>");
					
		return content;		
	}
	
	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode liefert den Inhalt der Rechnungsmail (Rechnung) zurück.
	 * </pre>
	 * @param benutzer
	 * @param bestellung
	 * @param artikelliste
	 * @return content
	 */
	private String getRechnungsmailContentRechnung(Benutzer benutzer, Bestellung bestellung, List<WarenkorbArtikel> artikelliste){	
		DecimalFormat formater = new DecimalFormat("#0.00");
		
		String content =  "<html>"
						+ "<body>"
						+ "<style> p{margin:0; padding:0;}"
						+ "table thead th, table tbody td{margin-right: 10px;}"
						+ "table{border-collapse: collapse;}"
						+ "table, td, th{border: 1px solid black;}"
						+ "</style>"
						+ "<p>Sehr geehrte(r) " + benutzer.getVorname() + " " + benutzer.getNachname() + ",</p>" 
						+ "</br>"
						+ "<p>Bauzubeh&ouml;r HVH dankt Ihnen f&uuml;r Ihre Bestellung, deren Eingang wir hiermit best&auml;tigen.</p>"
						+ "</br>"
						+ "<p>Ihre Bestellung wird so schnell es geht von uns in die Bearbeitung genommen. Die gew&ouml;hnliche Lieferzeit betr&auml;gt zwei bis vier Werktage.</p>"
						+ "</br>"
						+ "<p>Nachstehend finden Sie die einzelnen Positionen Ihrer Bestellung:</p>"
						+ "<p style='text-decoration:underline; margin-bottom: 5px;'>Rechnungsnummer: " + bestellung.getBestellnummer() + "</p>"
						+ "<table><thead><tr><th>Position</th><th>Menge</th><th>Artikelnummer</th><th>Artikelbezeichnung</th><th>Einzelpreis</th><th>Gesamtpreis</th></tr></thead>"
						+ "<tbody>";
						
		for(int i = 0; i < artikelliste.size(); i++){
			content = content.concat("<tr>");
			content = content.concat("<td>" + String.valueOf(i+1) + "</td>");
			content = content.concat("<td>" + artikelliste.get(i).getMenge() + "</td>");
			content = content.concat("<td>" + artikelliste.get(i).getArtikel().getNummer() + "</td>");
			content = content.concat("<td>" + artikelliste.get(i).getArtikel().getBezeichnung() + "</td>");
			content = content.concat("<td>" + formater.format(artikelliste.get(i).getArtikel().getPreis()).replace(".", ",") + "&euro;</td>");
			content = content.concat("<td>" + formater.format(artikelliste.get(i).getArtikel().getPreis() * artikelliste.get(i).getMenge()).replace(".", ",") + "&euro;</td>");
			content = content.concat("</tr>");
		}
		
		double gesamtBestellwert = bestellung.getBestellwert() + 20.00; // 20€Versandkosten pauschal
							
		content = content.concat( "</tbody></table>"
						+ "</br>"
						+ "<p>Rechnungssumme (inkl. 20&euro; Standard-Versandkosten): <i style='font-size: 16px; font-weight:bold;'>" + formater.format(gesamtBestellwert).replace(".", ",") + "&euro;</i></p>"
						+ "</br>"
						+ "<p>Bitte &uuml;berweisen Sie die Rechnungssumme unter Angabe der Rechnungsnummer im Verwendungszweck innerhalb der n&auml;chsten 14 Tage auf folgendes Konto:</p>"
						+ "<p style='margin-top: 5px;'>IBAN: DE82 1234 5678 9000 1005 28</p>"
						+ "<p style='margin-top: 5px;'>BIC: HVHLABADE082280</p>"
						+ "<p style='margin-top: 5px;'>HVH-Landesbank Deutschland AG</p>"
						+ "</br>"
						+ "</br>"
						+ "<p>Die Widerrufsbelehrung und ein entsprechendes Formular befinden sich im Anhang.</p>"
						+ "</br>"
						+ "<p>Haben Sie noch Fragen? Schreiben Sie eine Mail bauzubehoer.hvh@gmail.com oder rufen Sie unsere kostenlose Service-Hotline 0800 8228 100 an.</p>"
						+ "</br>"
						+ "<p>Wir w&uuml;nschen Ihnen viel Vergn&uuml;gen mit Ihrer Bestellung!</p>"
						+ "</br>"
						+ "<p>Mit den besten Gr&uuml;ßen</p>"
						+ "<p>Bauzubeh&ouml;r-HVH, Kundenbetreuung</p>"
						+ "</body>"
						+ "</html>");
					
		return content;		
	}

	/**
	 * <pre>
	 * <h3>Beschreibung:</h3> Die Methode liefert den Inhalt der Bestätigungsmail zurück.
	 * </pre>
	 * @param benutzer
	 * @return content
	 */
	private String getBestaetigungsmailContent(Benutzer benutzer) {
		String content = "<html><body>"
						+ "<style>"
						+ "p{margin:0; padding:0;}"
						+ "</style>"
						+ "<p>Guten Tag " + benutzer.getVorname() + " " + benutzer.getNachname() + ",</p>"
						+ "</br>"
				        + "<p>um die Registrierung Ihres Benutzerkontos abzuschliessen klicken Sie bitte auf den nachfolgenden Link: http://localhost:8080/Webshop/registrieren?method=confirm&ea=" + benutzer.getEmailadresse() + "</p>"
				        + "</br>"
				        + "</br>"
				        + "<p>Haben Sie noch Fragen? Schreiben Sie eine Mail an bauzubehoer.hvh@gmail.com oder rufen Sie unsere kostenlose Service-Hotline 0800 8228 100 an.</p>"
				        + "</br>"
				        + "<p>Wir w&uuml;nschen Ihnen viel Vergn&uuml;gen beim St&ouml;bern in unserem Sortiment!</p>"
				        + "</br>"
				        + "</br>"
				        + "<p>Mit den besten Gr&uuml;&szlig;en</p>"
				        + "</br>"
				        + "<p>Bauzubeh&ouml;r-HVH, Kundenbetreuung</p>"
						+ "</body></html>";
		
		return content;				
	}
	
	
	
}
