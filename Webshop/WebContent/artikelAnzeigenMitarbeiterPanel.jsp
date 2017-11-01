
<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat formaterArtikelMitarbeiter = new DecimalFormat("#0.00");

%>
<div class="showing" id="artikelListeMitarbeiter">
<h1>Artikelliste</h1>

	<div id="artikelListeMitarbeiterTabellen">
		      <table id="artikelListeMitarbeiterTabelle">
		        <colgroup>
			       <col span="1" style="width: 10%;">
			       <col span="1" style="width: 15%;">
			       <col span="1" style="width: 20%;">
			       <col span="1" style="width: 36%;">
			       <col span="1" style="width: 12%;">
			       <col span="1" style="width: 7%;">
			       <col span="1" style="width: 5%;">
			    </colgroup>
		        
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
		        	List<Artikel> artikelListeMitarbeiter  = (List<Artikel>)request.getAttribute("artikelListeMitarbeiter");
	
		        	if(artikelListeMitarbeiter != null)
		        	{
			        	for(int i = 0; i <artikelListeMitarbeiter.size();i++)
				        {
			        		Artikel artikel = artikelListeMitarbeiter.get(i);
			        		if(artikel != null)
			        		{
				        		out.println("<tr>" +
				        		"<td class='tablerow'><div>" + artikel.getLagermenge() + "</div></td>" +
				        		"<td class='tablerow'><div>" + String.format("%04d", artikel.getNummer()) + "</div></td>" +
				        		"<td class='tablerow'><div>" + artikel.getBezeichnung() + "</div></td>" +
				        		"<td class='tablerow'><div>" + artikel.getBeschreibung() + "</div></td>" +
				        		"<td class='tablerow'><div>" + formaterArtikelMitarbeiter.format(artikel.getPreis()) +  "</div></td>" +
				        		"<td class='tablerow'><div>" + artikel.getAktiv() + "</div></td>" +
				        		"<td class='tablerow rightRow'>" + "<a href='artikelBearbeiten' class='PickSymbol'><i class='fa fa-pencil-square-o'></i></a>" + "</div></td>" +
				        		"</tr>");
				        	}
		        		}
		        	}
		        	%>
		        </tbody>
		    </table>
	</div>
	<div>
		<form action="artikelAnlegen">
			<button id="btnArtikelAnlegen" type="submit"><i class="fa fa-plus"></i> Neuen Artikel anlegen</button>
		</form>
	</div>
</div>