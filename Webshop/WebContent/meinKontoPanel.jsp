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
			<input title="Vorname" class="inputField" type="text" name="vorname" placeholder="Vorname"  value="<% out.print(benutzer.getVorname()); %>" disabled>
			<input title="Nachname" class="inputField" type="text" name="nachname" placeholder="Nachname" value="<% out.print(benutzer.getNachname()); %>" disabled>
			<input title="E-Mail" class="inputField" type="text" name="email" placeholder="eMail" value="<% out.print(benutzer.getEmailadresse()); %>" disabled>
			
			<button title="kontoBearbeiten" type="submit" name="function" value="f_bearbeiten">Bearbeiten</button>
			</div>
        </li>
        <li>
            <input type="radio" name="tabreiter-0" id="tabreiter-0-1" /><label for="tabreiter-0-1">Adresse</label>
            <div>
            <input title="Strasse und Hausnummer" class="inputField" type="text" name="strasseHausnummer" placeholder="Stra�e und Hausnummer" disabled>
			<input title="PLZ" class="inputField" type="text" name="plz" placeholder="PLZ" disabled>
			<input title="Ort" class="inputField" type="text" name="ort" placeholder="Ort" disabled>
			
			<button title="kontoBearbeiten" type="submit" name="function" value="f_bearbeiten">Bearbeiten</button> 
            </div>
        </li>
    </ul> 
</div>
</div>