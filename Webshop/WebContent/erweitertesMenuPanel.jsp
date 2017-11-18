<%@page import="enums.ENUM_ARTIKELKATEGORIE"%>
<%@page import="java.util.List"%>
<%@page import="entity.Artikel"%>
<!-- 
Autor dieser Datei: Tim Hermbecker

Diese Datei behandelt die linke Sitebar auf der Website (Desktop-Version) oder die oben liegende Leiste (Smartphone-Version).
Hier wurde das Suchfeld und das Logo implementiert.
 -->

<%
	Artikel suchenArtikel = (Artikel)request.getAttribute("anlegenArtikel");
	if(suchenArtikel == null){
		suchenArtikel = new Artikel().init("", -1, "", -1, -1, "", "", 0);
	}
%>

<script type="text/javascript">
function artikelSuchenNachKategorie(kategorie){
	$(document).ready(function() {	      	               
          window.location.href = "suchen";  
	}); 	
}

function artikelSuchenNachBegriff(){
	$(document).ready(function() {	      	               
          window.location.href = "suchen";  
	}); 	
}
</script>
<div id="sitebar">
	<div onclick="artikelSuchenNachBegriff()" id="header">
		<h1>Bauzubehör HVH</h1>
		<h3>Online Shop</h3>
	</div>
	<div id="searchPanel">
	
		<ul>
			<li id="suchenHeader"><i class="fa fa-search"></i> Suchen</li> 
			
			<li>
				<form action="suchen" method="POST">
					<input id="sucheEingabe" name="suchargument" type="text" placeholder="Artikelbezeichnung ...">
					<input type="submit" value="suchen" style="display:none;">
				</form>
			<li>
		</ul>	
	</div>
	
	<!-- Hier kommen die Artikelkategorien -->
<!-- 
	<div id="artikelKategorie">
		<ul>
			<li><a class="active">Kategorien</a>
				<ul>
					<%
					/*
						List<ENUM_ARTIKELKATEGORIE> kategorien_a_1 = ENUM_ARTIKELKATEGORIE.getArtikelkategorien1List();
						
						for(ENUM_ARTIKELKATEGORIE e: kategorien_a_1){
							out.print("<li><a href='#'>" + e.toString());
								List<ENUM_ARTIKELKATEGORIE> kategorien_a_2 = ENUM_ARTIKELKATEGORIE.getArtikelkategorien2List();
								out.print("<ul>");
									for(ENUM_ARTIKELKATEGORIE f: kategorien_a_2){
										out.print("<li><button class='btnSuchenNachKategorie' type='submit' name='method' onlick='artikelSuchenNachKategorie(" + f.toString() + ")>" + f.toString() + "</button></li>");
									}
								out.print("</ul>");
							out.print("</li>");
						}
						*/
					%>
				</ul>	
			</li>
		</ul>						
	</div>
 -->
			<!-- am besten als onclickEvent implementieren und die Funktion artikelSuchenNachKategorie 
		aufrufen mit dem ausgewählten Kategoriewert(auf ENUM_ARTIKELKATEGORIE beziehen) 
		-->	
</div>