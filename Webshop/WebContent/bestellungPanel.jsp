<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="entity.WarenkorbArtikel"%>
<%@page import="entity.Benutzer"%>
<%@page import="entity.Adresse"%>

<!-- 
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Diese Datei behandelt das Panel zur Zahlungsartauswahl. Hier können verschieden Arten der Bezahlung ausgewählt werden.
 -->
 
<%
	final DecimalFormat formater = new DecimalFormat("#0.00");

	Benutzer benutzer = (Benutzer)request.getAttribute("benutzer");
	Adresse adresse = benutzer.getLieferAdresse() ;

	if(adresse == null){
		adresse = new Adresse().init("", "", "", "", "");	
	}
%>
 
 
<script type="text/javascript">
 function warenkorbAnzeigen(){
	$(document).ready(function() {	      	               
          window.location.href = "warenkorb?method=warenkorbAnzeigen";  
	}); 
}
</script>
 
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
	
 </div>
 
  <div class="showing" id="bestellungZahlungsartPanel">
	 <h1>Zahlungsart ausw&auml;hlen</h1>
	 
	 <form id="zahlungsartBestimmenForm">
		 <fieldset>
		 	<input type="radio" id="creditcard" name="Zahlungsmethode" value="Kreditkarte">
		 	<label for="creditcard">Kreditkarte</label>
		 	<img alt="mastercard" src="../images/mastercard.png">
		 	
		 	<input type="radio" id="lastschrift" name="Zahlungsmethode" value="Lastschrift">
		 	<label for="lastschrift">Lastschrift</label>
			<img alt="Lastschrift" src="../images/text-lastschrift.png">
		 	
		 	<input type="radio" id="rechnung" name="Zahlungsmethode" value="Rechnung">
		 	<label for="rechnung">Rechnung</label>
		 	<img alt="Rechnung" src="../images/text-rechnung.png">
		 	
		 	<input type="radio" id="ueberweisung" name="Zahlungsmethode" value="Ueberweisung">
		 	<label for="ueberweisung">&Uuml;berweisung</label>
		 	<img alt="'&Uuml;berweisung'" src="../images/payment-in-advance.png">
		 	
		 	<input type="radio" id="paypal" name="Zahlungsmethode" value="PayPal" checked="checked">
		 	<label for="paypal">PayPal</label>
		 	<img alt="PayPal" src="../images/paypal-alternative.png">
		 	
		 </fieldset>
	 </form>
 </div>
 
 
 <div class="showing"  id="bestellungArtikelPanel">
 	<table id="artikelTabelle">
	        <colgroup>
		       <col span="1" style="width: 8%;">
		       <col span="1" style="width: 16%;">
		       <col span="1" style="width: 57%;">
		       <col span="1" style="width: 14%;">
		       <col span="1" style="width: 5%;">
		    </colgroup>
	        
	        <thead>
	          <tr>
	            <th>Menge</th>
	            <th>Artikelnummer</th>
	            <th>Produkt</th>
	            <th>Summe</th>
	          </tr>
	        </thead>
	        <tbody>
	        	<%
		        	List<?> warenkorbartikelListe = (List<?>)session.getAttribute("warenkorbartikelliste");
		        	double gesamt = 0;
		        	int versandkosten = 20;
		        	double mwst = 0;
	
		        	if(warenkorbartikelListe != null){
			        	for(Object o : warenkorbartikelListe){
			        		WarenkorbArtikel warenkorbartikel = (WarenkorbArtikel)o;
			        		
			        		out.println("<tr>" +
			        		"<td class='tablecell'><input style='font-size:14px; width:20px; border:none;' type='text' onchange='updateQuantity(this," + warenkorbartikel.getArtikel().getNummer() + ")' name='menge' value='" + String.valueOf(warenkorbartikel.getMenge()) + "'></td>" +
			        		"<td class='tablecell'>" + String.format("%04d", warenkorbartikel.getArtikel().getNummer()) + "</td>" +
			        		"<td class='tablecell'>" + warenkorbartikel.getArtikel().getBezeichnung() + "</td>" +
			        		"<td class='tablecell'>" + formater.format(warenkorbartikel.getArtikel().getPreis()*warenkorbartikel.getMenge()) +" &euro;</td>" +
			        		"</tr>");	
		        		}	
		        	}
	        	%>
	        </tbody>
	      </table>
   	<button class="btnWeiter" type="submit" name="method" onclick="XXXXXXXXXXXXXX">Jetzt Kaufen</button>
 	<button class="btnZurueck" type="submit" name="method" onclick="warenkorbAnzeigen()"><i class="fa fa-arrow-left"></i> Zur&uuml;ck</button>
 </div> 