<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat formaterArtikelMitarbeiter = new DecimalFormat("#0.00");
%>

<script type="text/javascript">
function artikelBearbeitenAnzeigen(piArtikelNummer){
	var artikelNummer = piArtikelNummer;

	if(artikelNummer != null){
		$(document).ready(function() {	      	               
	          window.location.href = "artikel?method=artikelBearbeitenAnzeigen&artikelnummer="+artikelNummer;  
		}); 
	}	
}
</script>

<div class="showing" id="artikelListeMitarbeiter">
<h1>Artikelliste</h1>

      <table id="artikelListeMitarbeiterTabelle">
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
	        	List<?> artikelListeMitarbeiter  = (List<?>)request.getAttribute("artikelListeMitarbeiter");

	        	for(Object o : artikelListeMitarbeiter){	
	        		Artikel art = (Artikel)o;

	        		out.println("<tr>" +
	        		"<td class='tablecell'><div>" + art.getLagermenge() + "</div></td>" +
	        		"<td class='tablecell'><div>" + String.format("%04d", art.getNummer()) + "</div></td>" +
	        		"<td class='tablecell'><div>" + art.getBezeichnung() + "</div></td>" +
	        		"<td class='tablecell'><div>" + art.getBeschreibung() + "</div></td>" +
	        		"<td class='tablecell'><div>" + formaterArtikelMitarbeiter.format(art.getPreis()) +  "</div></td>" +
	        		"<td class='tablecell'><div>" + art.getAktiv() + "</div></td>" +
	        		"<td class='tablecellright'>" + "<a onclick='artikelBearbeitenAnzeigen(" + art.getNummer() + ")' class='PickSymbol'><i class='fa fa-pencil-square-o'></i></a>" + "</div></td>" +
	        		"</tr>");
		        }
        	
        	%>
        </tbody>
    </table>
	<div>
		<form action="artikel">
			<input type="hidden" name="method" value="artikelAnlegenAnzeigen">
			<button id="btnArtikelAnlegen" type="submit"><i class="fa fa-plus"></i> Neuen Artikel anlegen</button>
		</form>
	</div>
</div>