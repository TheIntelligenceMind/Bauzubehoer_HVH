
<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat preisFormatter = new DecimalFormat("#0.00");	
%>
<div class="showing" id="artikelListeKunden">
	<div id="artikelKundenTabellen">
       	<%
       	List<Artikel> artikelListe = (List<Artikel>)request.getAttribute("artikelliste");

       	if(artikelListe != null)
       	{
        	for(int i = 0; i <artikelListe.size();i++)
	        {
        		Artikel artikel = artikelListe.get(i);
        		if(artikel != null)
        		{
	        		out.println("<div id='artikelListeKundenDesign'>" + 
        			"<div id='artikelListeKundenBild'>" + "BILD" + "</div>" +
	        		"<div id='artikelListeKundenBezeichnung'>" + artikel.getBezeichnung() + "</div>" +
			        "<div id='artikelListeKundenPreis'>" + "&euro; " +  preisFormatter.format(artikel.getPreis()) + "</div>" +
	        	    "<div id='artikelListeKundenBeschreibung'>" + artikel.getBeschreibung() + "</div>" +
	        	    "<form action='warenkorb'><input type='hidden' name='method' value='artikelInDenWarenkorb'><input type='hidden' name='artikelnummer' value='" + artikel.getNummer() + "'><button id='artikelListeKundenWarenkorbButton'>In den Warenkorb</button></form>" +
	        		"</div>");
	        	}
       		}
       	}
       	%>
	</div>
</div>