<%@page import="entity.Artikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat preisFormatter = new DecimalFormat("#0.00");	
	Artikel artikel = (Artikel)request.getAttribute("artikel");
%>

<div class="showing" id="ArtikelDetailansichtPanel">

    <div id="artikelDetailansichtPanelBeschreibung">
			<h3>Artikelnummer</h3>
			<input title="Hinweis: Nur eine vierstellige Artikelnummer eingeben." class="inputField" type="text" name="nummer" value="<% out.print(artikel.getNummer()); %>" placeholder="Artikelnummer" readonly>
			<h3>Bezeichnung</h3>
			<input title="Bezeichnung" class="inputField" type="text" name="bezeichnung" value="<% out.print(artikel.getBezeichnung()); %>" placeholder="Bezeichnung" readonly>
			<h3>Beschreibung</h3>
			<textarea title="Beschreibung" class="inputArea" wrap="soft" name="beschreibung" placeholder="Beschreibung" readonly><% out.print(artikel.getBeschreibung()); %></textarea>
		</div>
		<div id="artikelDetailansichtPanelZahlen">
			<h3>Preis</h3>
			<input title="Hinweis: Der Preis muss mit einem Punkt getrennt sein!" class="inputField" type="text" name="preis" value="<% out.print(artikel.getPreis()); %>" placeholder="Preis" readonly>
			<h3>Lagermenge</h3>
			<input title="Lagermenge" class="inputField" type="text" name="lagermenge" value="<% out.print(artikel.getLagermenge()); %>" placeholder="Lagermenge" readonly>
		</div>	
		<div id="btnArtikelDetailansichtWarenkorb">
			<form action='warenkorb'><input type='hidden' name='method' value='artikelInDenWarenkorb'><input type='hidden' name='artikelnummer' value='" + a.getNummer() + "'><button id='artikelListeKundenWarenkorbButton'>In den Warenkorb</button></form>
		</div>


</div>