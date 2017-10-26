<%@page import="entity.Benutzer"%>

<%
	Benutzer benutzer = (Benutzer)request.getAttribute("benutzer");
%>
<div class="showing" id="meinKontoPanel">
	<h1>Konto Übersicht</h1>

<div class="tabreiter">
    <ul>
        <li>
            <input type="radio" name="tabreiter-0" checked="checked" id="tabreiter-0-0" /><label for="tabreiter-0-0">Benutzerinformationen</label>
            <div>
			<input title="Vorname" class="inputField" type="text" name="vorname" placeholder="Vorname" value="<% out.print(benutzer.getVorname()); %>" readonly>
			<input title="Nachname" class="inputField" type="text" name="nachname" placeholder="Nachname" value="<% out.print(benutzer.getNachname()); %>" readonly>
			<input title="E-Mail" class="inputField" type="text" name="email" placeholder="eMail" value="<% out.print(benutzer.getEmailadresse()); %>" readonly>
			
			<input title="kontoBearbeiten" type="submit" value="Bearbeiten">
			</div>
        </li>
        <li>
            <input type="radio" name="tabreiter-0" id="tabreiter-0-1" /><label for="tabreiter-0-1">Adressinformationen</label>
            <div>
            <input title="Strasse und Hausnummer" class="inputField" type="text" name="strasseHausnummer" placeholder="Straße und Hausnummer" readonly>
			<input title="PLZ" class="inputField" type="text" name="plz" placeholder="PLZ" readonly>
			<input title="Ort" class="inputField" type="text" name="ort" placeholder="Ort" readonly>
			<input title="kontoBearbeiten" type="submit" value="Bearbeiten">   
            </div>
        </li>
    </ul> 
</div>
</div>