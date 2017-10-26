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
				<button class="btnSpeichern" type="submit" name="function" value="f_speichern_benutzer">Speichern</button>
			</form>
			</div>
        </li>
        <li>
            <input type="radio" name="tabreiter-0" id="tabreiter-0-1" /><label for="tabreiter-0-1">Adresse</label>
            <div>
            <input title="Strasse und Hausnummer" class="inputField" type="text" name="strasseHausnummer" placeholder="Straße und Hausnummer" disabled>
			<input title="PLZ" class="inputField" type="text" name="plz" placeholder="PLZ" disabled>
			<input title="Ort" class="inputField" type="text" name="ort" placeholder="Ort" disabled>
			
			<form action="meinKonto" method="post">
				<button class="btnSpeichern" type="submit" name="function" value="f_speichern_adresse">Speichern</button>
			</form>
            </div>
        </li>
    </ul> 
</div>
</div>