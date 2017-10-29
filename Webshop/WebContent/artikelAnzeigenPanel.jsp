
<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat formaterArtikel = new DecimalFormat("#0.00");
%>
<div class="showing" id="artikelListe">

	<div id="artikelTabellen">
		      <table id="artikelTabelle">

		        <tbody>
		        	<%
		        	List<Artikel> artikelListe = (List<Artikel>)request.getAttribute("artikelliste");
	
		        	if(artikelListe != null)
		        	{
			        	for(int i = 0; i <artikelListe.size();i++)
				        {
			        		Artikel artikel = artikelListe.get(i);
			        		if(artikel != null)
			        		{
				        		out.println("<tr>" + "<div id='Bild'>" + "</div>" +
				        		"<div id='Bezeichnung'>" + "<td>" + artikel.getBezeichnung() + "</td>" + "</div>" +
				        	    "<div id='Beschreibung'>" + "<td>" + artikel.getBeschreibung() + "</td>" + "</div>" +
				        	    "<div id='Preis'>" + "<td>" + formaterArtikel.format(artikel.getPreis()) +  "</td>" + "</div>" +
				        	    "<div id='WarenkorbButton'>" + "<td class='rightRow'>" + "<a href='warenkorbArtikelEinfuegen' class='PickSymbol'><i class='fa fa-cart-arrow-down'></i></a>" + "</td>" + "</div>" +
				        		"</tr>");
				        	}
		        		}
		        	}
		        	%>
		        </tbody>
		      </table>
	</div>
</div>