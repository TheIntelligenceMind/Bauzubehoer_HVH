<%@page import="entity.Artikel"%>

<!-- 
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Diese Datei behandelt das Bearbeiten von vorhandenen Artikeln.
 -->


<%
	Artikel bearbeitenArtikel = (Artikel)request.getAttribute("bearbeitenArtikel");
%>

<script type="text/javascript">
function updateToggleButtonAktiv(){
	if($('#tButton').is(":checked")){
		$('#tButton').val("1");
	}else{
		$('#tButton').val("0");
	}
}
</script>

<div class="showing" id="artikelBearbeitenPanel">
	<h1>Artikel bearbeiten</h1>	
	<form action="artikel" method="POST">
	
	
		<div id="artikelBearbeitenPanelBeschreibung">
			<h3>Artikelnummer</h3>
			<input title="Artikelnummer" class="inputField" type="text" name="nummer" value="<% out.print(bearbeitenArtikel.getNummer()); %>" placeholder="Artikelnummer" readonly>
			<h3>Bezeichnung</h3>
			<input title="Bezeichnung" class="inputField" type="text" name="bezeichnung" value="<% out.print(bearbeitenArtikel.getBezeichnung()); %>" placeholder="Bezeichnung">
			<h3>Beschreibung</h3>
			<textarea title="Beschreibung" class="inputArea" wrap="soft" name="beschreibung" placeholder="Beschreibung"><% out.print(bearbeitenArtikel.getBeschreibung()); %></textarea>
		</div>
		<div id="artikelBearbeitenPanelZahlen">
			<h3>Preis</h3>
			<input title="Hinweis: Der Preis muss mit einem Punkt getrennt sein!" class="inputField" type="text" name="preis" value="<% out.print(bearbeitenArtikel.getPreis()); %>" placeholder="Preis">
			<h3>Lagermenge</h3>
			<input title="Lagermenge" class="inputField" type="text" name="lagermenge" value="<% out.print(bearbeitenArtikel.getLagermenge()); %>" placeholder="Lagermenge">
			<h3>Aktiv</h3>
			
			<div id="togglebutton">
  				<label class="switch">
				  <input id="tButton" onchange="updateToggleButtonAktiv()" type="checkbox" name="aktiv"
					<% 
						if(bearbeitenArtikel.getAktiv() == 1){	
							out.print("value='1' checked");
						}else{
							out.print("value='0' unchecked");
						}
					%>
				  >
				  <span class="slider round"></span>
				</label>
			</div>
		</div>
			<input type="hidden" name="method" value="artikelBearbeiten">	
			<input id="btnArtikelSpeichern" type="submit" value="Speichern">	
	</form>
	<form action="artikel">
		<button class="btnZurueck" type="submit" name="method" value="artikellisteAnzeigen"><i class="fa fa-arrow-left"></i> Zur&uuml;ck</button>
	</form>
</div>