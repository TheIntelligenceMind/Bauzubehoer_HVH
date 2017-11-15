package helper;

import entity.Benutzer;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import com.sun.mail.smtp.SMTPMessage;


public class MailHelper {

	private static final MailHelper instance = new MailHelper();
	
	private MailHelper(){
		
	}
	
	
	public static MailHelper getInstance(){
		return instance;
	}
	
	public void sendBestaetigungsmail(Benutzer benutzer){
		Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class",
	            "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "805");

	    Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication("bauzubehoer.hvh@gmail.com","WIPWebshop");
	        }
	    });

	    try {
	        SMTPMessage message = new SMTPMessage(session);
	        message.setFrom(new InternetAddress("bauzubehoer.hvh@gmail.com"));
	        message.setRecipients(Message.RecipientType.TO,
	                                 InternetAddress.parse(benutzer.getEmailadresse()));

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
	        
	        
	        message.setSubject("Bauzubehoer HVH - Registrierung abschliessen!");
	        message.setText(content);
	        
	        message.setNotifyOptions(SMTPMessage.NOTIFY_SUCCESS);
	        Transport.send(message);
	    }
	     catch (MessagingException e) {
	        throw new RuntimeException(e);         
	    }
	}
	
}
