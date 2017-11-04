<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat preisFormatter = new DecimalFormat("#0.00");	
%>
<div class="showing" id="artikelListeKunden">
	<div id="artikelKundenTabellen">
       	<%
		     	List<?> artikelListe = null;
		     	
		     	artikelListe = ((List<?>)request.getAttribute("artikelliste"));
		     
		     	for(Object o : artikelListe){
		     		Artikel a = (Artikel)o;
		     		
		     		out.println("<div id='artikelListeKundenDesign'>" + 
		    		"<div id='artikelListeKundenBild'>" + "BILD" + "</div>" +
		     		"<div id='artikelListeKundenBezeichnung'>" + a.getBezeichnung() + "</div>" +
		       		"<div id='artikelListeKundenPreis'>" + "&euro; " +  preisFormatter.format(a.getPreis()) + "</div>" +
		     	    "<div id='artikelListeKundenBeschreibung'>" + a.getBeschreibung() + "</div>" +
		     	    "<form action='warenkorb'><input type='hidden' name='method' value='artikelInDenWarenkorb'><input type='hidden' name='artikelnummer' value='" + a.getNummer() + "'><button id='artikelListeKundenWarenkorbButton'>In den Warenkorb</button></form>" +
		     		"</div>");
		     	}
       	%>
	</div>
</div>