<!-- 
IMPORT STATEMENTS HIER EINFÜGEN DIE FÜR DIE KATEGORIEN BENÖTIGT WERDEN!
 -->

<div class="showing" id="artikelAnlegenPanel">

	<h1>Artikel anlegen</h1>
	
	<form action="artikel" method="POST">
		<div id=artikelAnlegenPanelBeschreibung>
			<input title="Hinweis: Die Artikelnummer muss vierstellig sein." class="inputField" type="text" name="nummer" placeholder="Artikelnummer">
			<input title="Bezeichnung" class="inputField" type="text" name="bezeichnung" placeholder="Bezeichnung">
			<textarea title="Beschreibung" class="inputArea" wrap="soft" name="beschreibung" placeholder="Beschreibung"></textarea>
		</div>
		<div id="artikelAnlegenPanelKategorie">
			<select title="Artikelkategorie" name="artikelKategorie">
			<option>Artikelkategorie</option>
			<%
				/*
				for:each kategorie in artikel ...
				*/
			%>
			</select>
		</div>
		<div id=artikelAnlegenPanelZahlen>
			<input title="Hinweis: Der Preis muss mit einem Punkt getrennt sein!" class="inputField" type="text" name="preis" placeholder="Preis">
			<input title="Lagermenge" class="inputField" type="text" name="lagermenge" placeholder="Lagermenge">
		</div>	
			<input type="hidden" name="method" value="artikelAnlegen">
			<input id="btnArtikelAnlegen" type="submit" value="Artikel anlegen">	
	</form>
	<form action="artikel">
		<button class="btnZurueck" type="submit" name="method" value="artikellisteAnzeigen"><i class="fa fa-arrow-left"></i> Zur&uumlck</button>
	</form>
</div>