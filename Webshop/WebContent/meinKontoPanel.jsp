<%@page import="entity.Benutzer"%>
<%@page import="entity.Adresse"%>
<%
	Benutzer benutzer = (Benutzer)request.getAttribute("benutzer");
	Adresse adresse = benutzer.getLieferAdresse() ;

	if(adresse == null){
		adresse = new Adresse().init("", "", "", "", "");	
	}
%>

<script type="text/javascript">
function kontoLoeschen(){
	var result = confirm("Sind Sie sicher, dass Ihr Benutzerkonto gel�scht werden soll?");
	
	if(result){
		$(document).ready(function() {
           window.location.href = "meinKonto?method=loeschen";  
		});
	}
}
</script>

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
					<button class="btnBenutzerSpeichern" type="submit" name="method" value="benutzerSpeichern">Speichern</button>
				</form>
			</div>
        </li>
        <li>
            <input type="radio" name="tabreiter-0" id="tabreiter-0-1" /><label for="tabreiter-0-1">Adresse</label>
            <div>
            <form action="meinKonto" method="post">
	            <input title="Strasse" class="inputField" type="text" name="strasse" placeholder="Stra�e" id="Strasse" value="<% out.print(adresse.getStrasse()); %>">
	            <input title="Hausnummer" class="inputField" type="text" name="hausnummer" placeholder="Hausnummer" id="Hausnummer" value="<% out.print(adresse.getHausnummer()); %>">
				<input title="PLZ" class="inputField" type="text" name="postleitzahl" placeholder="PLZ" value="<% out.print(adresse.getPostleitzahl()); %>">
				<input title="Ort" class="inputField" type="text" name="ort" placeholder="Ort" value="<% out.print(adresse.getOrt()); %>">
				<button class="btnAdresseSpeichern" type="submit" name="method" value="adresseSpeichern">Speichern</button>
			</form>	
            </div>
        </li>
    </ul> 
</div>
	<button class="btnLoeschen" onclick="kontoLoeschen()">Konto L&oumlschen</button>

</div>