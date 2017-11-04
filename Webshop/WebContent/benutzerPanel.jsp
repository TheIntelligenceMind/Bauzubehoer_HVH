<%@page import="entity.WarenkorbArtikel"%>
<%@page import="java.util.List"%>

<script type="text/javascript">
function showArtikelListeMa(){
	$(document).ready(function() {	      	               
          window.location.href = "artikel?method=artikellisteAnzeigen";  
	}); 
}
</script>

<div id="benutzerPanel" class="controllPanel">
	<h3>Hallo, <i id="name"><% out.print(session.getAttribute("vorname").toString() + " " + session.getAttribute("nachname").toString()); %></i></h3>
	<ul>
		<li>
			<i class="fa fa-cart-plus"></i>
				<a href="warenkorb"> 
					Warenkorb 
					<% 
						List<?> warenkorbartikelListe = (List<?>)session.getAttribute("warenkorbartikelliste");
						int warenkorbArtikelAnzahl = 0;
									
						if(warenkorbartikelListe != null){
							warenkorbArtikelAnzahl = warenkorbartikelListe.size();
			        	}
						out.println("(" + warenkorbArtikelAnzahl + ")"); 
					%>
				</a>
		</li>
		<li><i class="fa fa-bars"></i><a href="meineBestellungen"> Meine Bestellungen</a></li>
		<li><i class="fa fa-user"></i><a href="meinKonto"> Mein Konto</a></li>
		<li><i class="fa fa-database"></i><a onclick="showArtikelListeMa()"> Artikelstammdaten</a>
	</ul>
	
	<a href="abmelden" id="abmelden"><i class="fa fa-sign-out"></i> abmelden</a>
</div>