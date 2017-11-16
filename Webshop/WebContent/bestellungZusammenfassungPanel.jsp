<%@page import="java.util.List"%>
<%@page import="entity.Benutzer"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="entity.WarenkorbArtikel"%>

<!-- 
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Diese Datei behandelt die Zusammenfassung der Bestellung.
 -->
 
<script type="text/javascript"> 
	function meineBestellungenAnzeigen(){
		$(document).ready(function() {	      	               
	          window.location.href = "meineBestellungen?method=bestellungenAnzeigen";  
		}); 
	}
</script>
 
 <%
	final DecimalFormat formater = new DecimalFormat("#0.00");
 %>
 
 <div class="showing"  id="zusammenfassungBestellung">
 
 <div id="divZahlungsart">
 
 </div>
 <div id="divLieferadresse">
 
 </div>
 <div id="divArtikelTabelle">
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
	 </div>
   	<button class="btnWeiter" type="submit" name="method" onclick="meineBestellungenAnzeigen">Jetzt Kaufen</button>
   	<button class="btnZurueck" type="submit" name="method" onclick="lieferadresseAnzeigen()"><i class="fa fa-arrow-left"></i> Zur&uuml;ck</button>
 </div> 