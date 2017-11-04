<%@page import="entity.Artikel"%>

<div class="showing" id="artikelBearbeitenPanel">

	<h1>Artikel bearbeiten</h1>
	
	<%
		Artikel artikel = (Artikel)request.getAttribute("artikel");
	%>
	
	<form action="artikel" method="POST">
		<div id="artikelBearbeitenPanelBeschreibung">
			<input title="Hinweis: Nur eine vierstellige Artikelnummer eingeben." class="inputField" type="text" name="nummer" value="<% out.print(artikel.getNummer()); %>" placeholder="Artikelnummer">
			<input title="Bezeichnung" class="inputField" type="text" name="bezeichnung" value="<% out.print(artikel.getBezeichnung()); %>" placeholder="Bezeichnung">
			<input title="Beschreibung" class="inputArea" name="beschreibung" value="<% out.print(artikel.getBeschreibung()); %>" placeholder="Beschreibung">
		</div>
		<div id="artikelBearbeitenPanelZahlen">
			<input title="Hinweis: Der Preis muss mit einem Punkt getrennt sein!" class="inputField" type="text" name="preis" value="<% out.print(artikel.getPreis()); %>" placeholder="Preis">
			<input title="Lagermenge" class="inputField" type="text" name="lagermenge" value="<% out.print(artikel.getLagermenge()); %>" placeholder="Lagermenge">
			<input title="Aktiv" class="inputField" type="text" name="aktiv" value="<% out.print(artikel.getAktiv()); %>" placeholder="aktiv">
		</div>
			<input type="hidden" name="method" value="artikelBearbeitenAnzeigen">	
			<input id="btnArtikelSpeichern" type="submit" value="Speichern">	
	</form>
	
</div>