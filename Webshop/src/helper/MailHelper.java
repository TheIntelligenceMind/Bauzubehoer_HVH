package helper;

import entity.Artikel;
import entity.Benutzer;
import entity.Bestellung;
import entity.WarenkorbArtikel;

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
	
	private Session getSession(){
		Session session = Session.getDefaultInstance(getProperties(), new javax.mail.Authenticator() {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication("bauzubehoer.hvh@gmail.com","WIPWebshop");
	        }
	    });
		
		return session;
	}
	
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
	        
	        // Mail Inhalt setzen
	        message.setContent(getRechnungsmailContent(benutzer, bestellung, artikelliste), "text/html");
	        
	        // Mail verschicken
	        Transport.send(message);
	    }catch (MessagingException e) {
	        throw new RuntimeException(e);         
	    }
		
		
	}
	
	public void sendBestaetigungsmail(Benutzer benutzer){ 
	    Session session = getSession();

	    try {
	        SMTPMessage message = new SMTPMessage(session);
	        // Absender setzen
	        message.setFrom(new InternetAddress("bauzubehoer.hvh@gmail.com"));
	       
	        // Empfänger setzen
	        message.setRecipients(Message.RecipientType.TO,
	                                 InternetAddress.parse(benutzer.getEmailadresse()));

	        // Betreffzeile setzen
	        message.setSubject("Bauzubehoer HVH - Registrierung abschliessen!");
	        
	        // Mail Inhalt setzen
	        message.setText(getBestaetigungsmailContent(benutzer));
	        
	        // Benachrichtigungsoptionen setzen
	        message.setNotifyOptions(SMTPMessage.NOTIFY_SUCCESS);
	        
	        // Mail verschicken
	        Transport.send(message);
	    }catch (MessagingException e) {
	        throw new RuntimeException(e);         
	    }
	}

	
	private String getRechnungsmailContent(Benutzer benutzer, Bestellung bestellung, List<WarenkorbArtikel> artikelliste){	
		String content =  "<html>"
						+ "<body>"
						+ "<style> p{margin:0; padding:0;}"
						+ "table thead th, table tbody td{margin-right: 10px;}"
						+ "table{border-collapse: collapse;}"
						+ "table, td, th{border: 1px solid black;}"
						+ "</style>"
						+ "<p>Sehr geehrte(r) " + benutzer.getVorname() + " " + benutzer.getNachname() + ",</p>" 
						+ "</br>"
						+ "<p>Bauzubeh&ouml;r HVH dankt Ihnen für Ihre Bestellung, deren Eingang wir hiermit best&auml;tigen.</p>"
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
			content = content.concat("<td>" + String.valueOf(artikelliste.get(i).getArtikel().getPreis()).replace(".", ",") + "</td>");
			content = content.concat("<td>" + String.valueOf((artikelliste.get(i).getArtikel().getPreis() * artikelliste.get(i).getMenge())).replace(".", ",") + "</td>");
			content = content.concat("</tr>");
		}
						
		content = content.concat( "</tbody></table>"
						+ "</br>"
						+ "<p>Rechnungssumme ( inkl. 20&euro; Standard-Versandkosten): <i style='font-size: 16px; font-weight:bold;'>" + String.valueOf(bestellung.getBestellwert()).replace(".", ",") + "&euro;</i></p>"
						+ "</br>"
						+ "<p>Bitte überweisen Sie die Rechnungssumme unter Angabe der Rechnungsnummer im Verwendungszweck innerhalb der n&auml;chsten 14 Tage auf folgendes Konto:</p>"
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

	private String getBestaetigungsmailContent(Benutzer benutzer) {
		String content = "Guten Tag, " + benutzer.getVorname() + " " + benutzer.getNachname() 
				        + "\n"
				        + "\n"
				        + "Um die Registrierung Ihres Benutzerkontos abzuschliessen, klicken Sie bitte innerhalb der nächsten 24 Stunden auf den nachfolgenden Link: http://localhost:8080/Webshop/registrieren?method=confirm&ea="+benutzer.getEmailadresse()
				        + "\n"
				        + "\n"
				        + "Wichtig: Mit Ablauf der Zeit wird der Link ungültig und Sie müssen sich erneut registrieren."
				        + "\n"
				        + "\n"
				        + "Haben Sie noch Fragen? Schreiben Sie eine Mail an bauzubehoer.hvh@gmail.com oder rufen Sie unsere kostenlose Service-Hotline 0800 8228 100 an."
				        + "\n"
				        + "\n"
				        + "Wir wünschen Ihnen viel Vergnügen beim Stöbern in unserem Sortiment!"
				        + "\n"
				        + "\n"
				        + "Mit den besten Grüßen"
				        + "\n"
				        + "Bauzubehör-HVH, Kundenbetreuung";
		
		return content;				
	}
	
}
