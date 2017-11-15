<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<!-- 
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Diese Datei behandelt das Anzeigen eines bestimmten Artikels.
 -->


<%
	final DecimalFormat preisFormatter2 = new DecimalFormat("#0.00");	
	Artikel detailansichtArtikel = (Artikel)request.getAttribute("detailansichtArtikel");
%>

<div class="showing" id="ArtikelDetailansichtPanel">

    <div id="artikelDetailansichtPanelBeschreibung">
			<h3>Artikelnummer</h3>
			<input title="Hinweis: Nur eine vierstellige Artikelnummer eingeben." class="inputField" type="text" name="nummer" value="<% out.print(detailansichtArtikel.getNummer()); %>" placeholder="Artikelnummer" readonly>
			<h3>Bezeichnung</h3>
			<input title="Bezeichnung" class="inputField" type="text" name="bezeichnung" value="<% out.print(detailansichtArtikel.getBezeichnung()); %>" placeholder="Bezeichnung" readonly>
			<h3>Beschreibung</h3>
			<textarea title="Beschreibung" class="inputArea" wrap="soft" name="beschreibung" placeholder="Beschreibung" readonly><% out.print(detailansichtArtikel.getBeschreibung()); %></textarea>
		</div>
		<div id="artikelDetailansichtPanelZahlen">
			<h3>Preis</h3>
			<input title="Hinweis: Der Preis muss mit einem Punkt getrennt sein!" class="inputField" type="text" name="preis" value="<% out.print(preisFormatter2.format(detailansichtArtikel.getPreis())); %>" placeholder="Preis" readonly>
			<h3>Lagermenge</h3>
			<input title="Lagermenge" class="inputField" type="text" name="lagermenge" value="<% out.print(detailansichtArtikel.getLagermenge()); %>" placeholder="Lagermenge" readonly>
		</div>	
		<div id="btnArtikelDetailansichtWarenkorb">
			<form action='warenkorb'><input type='hidden' name='method' value='artikelInDenWarenkorb'><input type='hidden' name='artikelnummer' value='" + a.getNummer() + "'><button id='artikelListeKundenWarenkorbButton'>In den Warenkorb</button></form>
		</div>


</div>