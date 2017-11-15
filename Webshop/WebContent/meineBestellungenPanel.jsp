<%@page import="entity.Bestellung" %>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>

<!-- 
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Diese Datei behandelt die Darstellung der gesamten Bestellungen des eingeloggten Accounts.
 -->


<%
	final DecimalFormat formaterBestellungen = new DecimalFormat("#0.00");
%>
<div class="showing" id="meineBestellungenPanel">
	<h1>Meine Bestellungen</h1>
	
	<!-- Bestellungen Tabelle hier start-->
	<table id="meineBestellungenTabelle">
        <thead>
          <tr>
            <th>BestellNr.</th>
            <th>Bestelldatum</th>
            <th>Status</th>
            <th>Zahlungsart</th>
            <th>Bestellwert</th>
            <th>Lieferdatum</th>
          </tr>
        </thead>
	  	<tbody>
		<%
			List<?> bestellungenListe = (List<?>)session.getAttribute("bestellungenliste");
	
			if(bestellungenListe != null)
			{
		    	for(Object o : bestellungenListe)
		    	{
		    		Bestellung bestellung = (Bestellung)o;
		            
	        		out.println("<tr>" +
	        		"<td class='tablecell'><div>" + String.format("%04d", bestellung.getBestellnummer()) + "</div></td>" +
	        		"<td class='tablecell'><div>" + bestellung.getBestelldatum() + "</div></td>" +
	        		"<td class='tablecell'><div>" + bestellung.getStatus() + "</div></td>" +
	        		"<td class='tablecell'><div>" + bestellung.getZahlungsart() + "</div></td>" +
	        		"<td class='tablecell'><div>" + formaterBestellungen.format(bestellung.getBestellwert()) +  " &euro;</div></td>" +
	        		"<td class='tablecell'><div>" + bestellung.getVoraussichtlichesLieferdatum() + "</div></td>" +
	        		"</tr>");
		    	}	
	    	}
		%>
		</tbody>	
	</table>
</div>
	
