<%@page import="entity.WarenkorbArtikel"%>
<%@page import="entity.Benutzer"%>
<%@page import="enums.SICHTEN"%>
<%@page import="java.util.List"%>

<script type="text/javascript">
function artikelListeMaAnzeigen(){
	$(document).ready(function() {	      	               
          window.location.href = "artikel?method=artikellisteAnzeigen";  
	}); 
}
function meinKontoAnzeigen(){
	$(document).ready(function() {	      	               
          window.location.href = "meinKonto?method=anzeigen";  
	}); 
}
function meineBestellungenAnzeigen(){
	$(document).ready(function() {	      	               
          window.location.href = "meineBestellungen?method=bestellungenAnzeigen";  
	}); 
}
function warenkorbAnzeigen(){
	$(document).ready(function() {	      	               
          window.location.href = "warenkorb?method=warenkorbAnzeigen";  
	}); 
}
</script>

<%
	Benutzer benutzer = (Benutzer)session.getAttribute("benutzer");
%>

<div id="benutzerPanel" class="controllPanel">
	<h3>Hallo, <i id="name"><% out.print(benutzer.getVorname() + " " + benutzer.getNachname()); %></i></h3>
	<ul>			
	<%
		List<SICHTEN> sichten = benutzer.getRolle().getSichten();
	
		if(sichten != null){
			for(SICHTEN sicht : sichten){
				switch(sicht){
					case WARENKORB:
						%>
						<li>
							<i class="fa fa-cart-plus"></i>
							<a onclick="warenkorbAnzeigen()"> 
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
						<%
						break;
					case BESTELLUNGEN:
						%>
							<li><i class="fa fa-bars"></i><a onclick="meineBestellungenAnzeigen()"> Meine Bestellungen</a></li>
						<%
						break;
					case KONTO:
						%>
							<li><i class="fa fa-user"></i><a onclick="meinKontoAnzeigen()"> Mein Konto</a></li>
						<%
						break;
					case ARTIKELSTAMMDATEN:
						%>
							<li><i class="fa fa-database"></i><a onclick="artikelListeMaAnzeigen()"> Artikelstammdaten</a>
						<%
						break;
					default:
						break;
				
				}
			}			
		
		}		
	%>	
	</ul>	
	<a href="abmelden" id="abmelden"><i class="fa fa-sign-out"></i> abmelden</a>
</div>