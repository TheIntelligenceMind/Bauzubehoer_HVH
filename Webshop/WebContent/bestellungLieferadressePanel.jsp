<%@page import="entity.Benutzer"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="entity.Adresse"%>


<!-- 
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Diese Datei behandelt die Bestimmung der Lieferadresse.
 -->


<%
	final DecimalFormat formater = new DecimalFormat("#0.00");

	Benutzer benutzer = (Benutzer)request.getAttribute("benutzer");
	Adresse adresse = benutzer.getLieferAdresse() ;

	if(adresse == null){
		adresse = new Adresse().init("", "", "", "", "");	
	}
%>

 <div class="showing"  id="LieferadresseBestimmenPanel">
 	<h3>Eine Versandadresse ausw&auml;hlen</h3>
 	
 	<!-- BENUTZERINFORMATIONEN !!!NICHT BEARBEITBAR!!! -->
 	<input title="Vorname" class="inputField" type="text" name="vorname" placeholder="Vorname"  value="<% out.print(benutzer.getVorname()); %>"disabled>
 	<input title="Nachname" class="inputField" type="text" name="nachname" placeholder="Nachname"  value="<% out.print(benutzer.getNachname()); %>" disabled>
 	 
 	 <!-- ADRESSINFORMATIONEN  !!!BEARBEITBAR!!! -->
 	<input title="Strasse" class="inputField" type="text" name="strasse" placeholder="Straße" id="Strasse" value="<% out.print(adresse.getStrasse()); %>">
	<input title="Hausnummer" class="inputField" type="text" name="hausnummer" placeholder="Hausnummer" id="Hausnummer" value="<% out.print(adresse.getHausnummer()); %>">
	<input title="Postleitzahl" class="inputField" type="text" name="postleitzahl" placeholder="PLZ" value="<% out.print(adresse.getPostleitzahl()); %>">
	<input title="Ort" class="inputField" type="text" name="ort" placeholder="Ort" value="<% out.print(adresse.getOrt()); %>">
	
	<button class="btnWeiter" type="submit" name="method" onclick="zusammenfassungAnzeigen()">Jetzt Kaufen</button>
	<button class="btnZurueck" type="submit" name="method" onclick="zahlungsartenAnzeigen()"><i class="fa fa-arrow-left"></i> Zur&uuml;ck</button>
 </div>