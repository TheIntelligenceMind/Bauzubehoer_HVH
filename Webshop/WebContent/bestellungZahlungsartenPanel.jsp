<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="entity.WarenkorbArtikel"%>

<!-- 
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Diese Datei behandelt das Panel zur Zahlungsartauswahl. Hier können verschieden Arten der Bezahlung ausgewählt werden.
 -->

 
<script type="text/javascript">
 function warenkorbAnzeigen(){
	$(document).ready(function() {	      	               
          window.location.href = "warenkorb?method=warenkorbAnzeigen";  
	}); 
}
</script>

 
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
	 <button class="btnWeiter" type="submit" name="method" onclick="lieferadresseAnzeigen()">Jetzt Kaufen</button>
	 <button class="btnZurueck" type="submit" name="method" onclick="warenkorbAnzeigen()"><i class="fa fa-arrow-left"></i> Zur&uuml;ck</button>
 </div>