<div id="benutzerPanel" class="controllPanel">
	<h3>Hallo, <i id="name"><% out.print(session.getAttribute("vorname").toString() + " " + session.getAttribute("nachname").toString()); %></i></h3>
	<ul>
		<li><i class="fa fa-cart-plus" aria-hidden="true"></i><a href="warenkorb"> Warenkorb <% out.println("(" + session.getAttribute("warenkorbAnzahl").toString() + ")"); %></a></li>
		<li><i class="fa fa-bars" aria-hidden="true"></i><a href="meineBestellungen"> Meine Bestellungen</a></li>
		<li><i class="fa fa-user" aria-hidden="true"></i><a href="meinKonto"> Mein Konto</a></li>
	</ul>
	
	<a href="abmelden" id="abmelden"><i class="fa fa-sign-out" aria-hidden="true"></i> abmelden</a>
</div>