<%@page import="entity.Bestellung" %>
<%@page import="java.util.List"%>
<div class="showing" id="meineBestellungenPanel">
	<h1>Meine Bestellungen</h1>
	
	
	
	<!-- Bestellungen Tabelle hier start-->
	<table id="meineBestellungenTabelle">
	
	        <thead>
	          <tr>
	            <th>Lagermenge</th>
	            <th>Artikelnummer</th>
	            <th>Produkt</th>
	            <th>Produktbeschreibung</th>
	            <th>Preis</th>
	            <th>Aktiv</th>
	            <th></th>
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
		    	/*
		            
			        		out.println("<tr>" +
			        		"<td class='tablecell'><div>" + bestellungenListe.get + "</div></td>" +
			        		"<td class='tablecell'><div>" + String.format("%04d", art.getNummer()) + "</div></td>" +
			        		"<td class='tablecell'><div>" + art.getBezeichnung() + "</div></td>" +
			        		"<td class='tablecell'><div>" + art.getBeschreibung() + "</div></td>" +
			        		"<td class='tablecell'><div>" + formaterArtikelMitarbeiter.format(art.getPreis()) +  "</div></td>" +
			        		"<td class='tablecell'><div>" + art.getAktiv() + "</div></td>" +
			        		"<td class='tablecellright'>" + "<a onclick='artikelBearbeitenAnzeigen(" + art.getNummer() + ")' class='PickSymbol'><i class='fa fa-pencil-square-o'></i></a>" + "</div></td>" +
			        		"</tr>");
		    	
		    	*/
				        }	
	    		}
		%>
		</tbody>	
	</table>
</div>
	
