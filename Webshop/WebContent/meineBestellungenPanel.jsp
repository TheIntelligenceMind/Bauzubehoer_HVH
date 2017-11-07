<%@page import="entity.Bestellung" %>
<%@page import="java.util.List"%>
<div class="showing" id="meineBestellungenPanel">
	<h1>Meine Bestellungen</h1>
	
	
	
	<!-- Bestellungen Tabelle hier start-->

	<%
		List<?> bestellungenListe = (List<?>)session.getAttribute("bestellungenliste");

		if(bestellungenListe != null){
	    	for(Object o : bestellungenListe){
	    		Bestellung bestellung = (Bestellung)o;
	    		
	    		
	    		/**
	    		
	    			Hier kommt der tbody Teil der Tabelle für die Bestellungen rein
	    		
	    		
	    		*/
    		
		
    		
    		}
    	}
	%>
	
</div>
	
