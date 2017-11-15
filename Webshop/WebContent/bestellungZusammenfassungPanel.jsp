<%@page import="java.util.List"%>
<%@page import="entity.Benutzer"%>

<!-- 
Autor dieser Datei: Tim Hermbecker

Diese Datei behandelt die Zusammenfassung der getätigten Bestellung.
 -->
 
<script type="text/javascript"> 
	function meineBestellungenAnzeigen(){
		$(document).ready(function() {	      	               
	          window.location.href = "meineBestellungen?method=bestellungenAnzeigen";  
		}); 
	}
</script>
 
<div class="showing" id="bestellungZusammenfassungPanel">
 
 <h1>Vielen Dank f&uuml;r Ihre Bestellung.</h1>
 
 <p>Ihre Bestellnummer lautet: "Hier die Bestellnummer per Java einfuegen"</p>
 <button class="btnWeiter" type="submit" name="method" onclick="meineBestellungenAnzeigen()">Referenz zu "Meine Bestellungen" erstellen</button>
 
</div>