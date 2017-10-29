
<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat formaterArtikelMitarbeiter = new DecimalFormat("#0.00");
%>
<div class="showing" id="artikelListeMitarbeiter">
<h1>Artikelliste</h1>

	<div id="artikellisteTabellen">
		      <table id="artikellisteTabelle">
		        <colgroup>
			       <col span="1" style="width: 10%;">
			       <col span="1" style="width: 20%;">
			       <col span="1" style="width: 15%;">
			       <col span="1" style="width: 30%;">
			       <col span="1" style="width: 15%;">
			       <col span="1" style="width: 5%;">
			    </colgroup>
		        
		        <thead>
		          <tr>
		            <th>Lagermenge</th>
		            <th>Artikelnummer</th>
		            <th>Produkt</th>
		            <th>Produktbeschreibung</th>
		            <th>Preis</th>
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
				        		"<td><input style='width: 20px;border:none;' type='text' name='menge' value='" + String.valueOf(artikel.getLagermenge()) + "' ></td>" +
				        		"<td>" + artikel.getNummer() + "</td>" +
				        		"<td>" + artikel.getBezeichnung() + "</td>" +
				        		"<td>" + artikel.getBeschreibung() + "</td>" +
				        		"<td>" + formaterArtikelMitarbeiter.format(artikel.getPreis()) +  "</td>" +
				        		"<td class='rightRow'>" + "<a href='artikelBearbeiten' class='PickSymbol'><i class='fa fa-database'></i></a>" + "</td>" +
				        		"</tr>");
				        	}
		        		}
		        	}
		        	%>
		        </tbody>
		      </table>
	</div>
</div>