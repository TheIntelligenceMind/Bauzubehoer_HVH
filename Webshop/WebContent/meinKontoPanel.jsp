<%@page import="entity.Benutzer"%>

<%
	Benutzer benutzer = (Benutzer)request.getAttribute("benutzer");
%>

<div class="showing" id="meinKontoPanel">
	<h1>Konto</h1>

<div class="tabreiter">
    <ul>
        <li>
            <input type="radio" name="tabreiter-0" checked="checked" id="tabreiter-0-0" /><label for="tabreiter-0-0">Benutzer</label>
            <div>
	        <form action="meinKonto" method="post">
				<input title="Vorname" class="inputField" type="text" name="vorname" placeholder="Vorname"  value="<% out.print(benutzer.getVorname()); %>">
				<input title="Nachname" class="inputField" type="text" name="nachname" placeholder="Nachname" value="<% out.print(benutzer.getNachname()); %>">
				<input title="E-Mail" class="inputField" type="text" name="emailadresse" placeholder="eMail" value="<% out.print(benutzer.getEmailadresse()); %>" disabled>				
				<button class="btnSpeichern" type="submit" name="method" value="benutzerSpeichern">Speichern</button>
			</form>
			</div>
        </li>
        <li>
            <input type="radio" name="tabreiter-0" id="tabreiter-0-1" /><label for="tabreiter-0-1">Adresse</label>
            <div>
            <form action="meinKonto" method="post">
	            <input title="Strasse" class="inputField" type="text" name="strasse" placeholder="Straße" id="Strasse" value="<% out.print(benutzer.getLieferAdresse().getStrasse()); %>">
	            <input title="Hausnummer" class="inputField" type="text" name="hausnummer" placeholder="Hausnummer" id="Hausnummer" value="<% out.print(benutzer.getLieferAdresse().getHausnummer()); %>">
				<input title="PLZ" class="inputField" type="text" name="postleitzahl" placeholder="PLZ" value="<% out.print(benutzer.getLieferAdresse().getPostleitzahl()); %>">
				<input title="Ort" class="inputField" type="text" name="ort" placeholder="Ort" value="<% out.print(benutzer.getLieferAdresse().getOrt()); %>">
				<button class="btnSpeichern" type="submit" name="method" value="adresseSpeichern">Speichern</button>
			</form>	
            </div>
        </li>
    </ul> 
</div>
	<!-- Löschen Button zu testzwecken eingebaut jedoch ohne CSS muss beim richten einbauen noch gemacht werden
	
	<form action="meinKonto" method="POST">
		<button type="submit" name="method" value="loeschen">Löschen</button>
	</form>
	
	  -->
</div>