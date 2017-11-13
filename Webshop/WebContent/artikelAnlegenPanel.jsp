<!-- 
IMPORT STATEMENTS HIER EINFÜGEN DIE FÜR DIE KATEGORIEN BENÖTIGT WERDEN!
 -->

<div class="showing" id="artikelAnlegenPanel">

	<h1>Artikel anlegen</h1>
	
	<form action="artikel" method="POST">
		<div id=artikelAnlegenPanelBeschreibung>
			<input title="Hinweis: Nur eine vierstellige Artikelnummer eingeben." class="inputField" type="text" name="nummer" placeholder="Artikelnummer">
			<input title="Bezeichnung" class="inputField" type="text" name="bezeichnung" placeholder="Bezeichnung">
			<textarea title="Beschreibung" class="inputArea" wrap="soft" name="beschreibung" placeholder="Beschreibung"></textarea>
		</div>
		<div id="artikelAnlegenPanelKategorie">
			<text id="artikelAnlegenKategorieText">Artikelkategorie</text>
			<select name="artikelKategorie">
			<%
			/*
			for:each kategorie in artikel ...
			*/
			out.print("<option value='test'>TESTHFHFHFHFHFHFHFHFHFHFHFHFHFHHFHFHFHFHF</option>");
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
		<a id="btnArtikelAnlegenZurueck"  onclick="artikelListeMaAnzeigen()">Zur&uumlck</a>
	
</div>