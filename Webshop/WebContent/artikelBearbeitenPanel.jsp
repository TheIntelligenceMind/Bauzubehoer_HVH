<%@page import="entity.Artikel"%>

<div class="showing" id="artikelBearbeitenPanel">

	<h1>Artikel bearbeiten</h1>
	
<%
	Artikel artikel= (Artikel)request.getAttribute("artikel");
%>
	
	<form action="artikelBearbeiten" method="POST">
		<div id=artikelBearbeitenPanelBeschreibung>
			<input title="Hinweis: Nur eine vierstellige Artikelnummer eingeben." class="inputField" type="text" name="nummer" value=<%artikel.getNummer();%>>
			<input title="Bezeichnung" class="inputField" type="text" name="bezeichnung" value=<%artikel.getBezeichnung();%>>
			<input title="Beschreibung" class="inputArea" name="beschreibung" value=<%artikel.getBeschreibung(); %>>
		</div>
		<div id=artikelBearbeitenPanelZahlen>
			<input title="Hinweis: Der Preis muss mit einem Punkt getrennt sein!" class="inputField" type="text" name="preis" value=<%artikel.getPreis(); %>>
			<input title="Lagermenge" class="inputField" type="text" name="lagermenge" value=<%artikel.getLagermenge(); %>>
			<input title="Aktiv" class="inputField" type="text" name="aktiv" value=<%artikel.getAktiv(); %>>
		</div>	
			<input id="btnArtikelSpeichern" type="submit" value="Speichern">	
	</form>
	
</div>