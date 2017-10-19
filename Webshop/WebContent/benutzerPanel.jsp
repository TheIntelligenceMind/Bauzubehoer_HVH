<div id="benutzerPanel" class="controllPanel">
	<h3>Hallo, <i id="name"><% out.print(session.getAttribute("vorname").toString() + " " + session.getAttribute("nachname").toString()); %></i></h3>
	<ul>
		<li><i class="fa fa-cart-plus"></i><a href="warenkorb"> Warenkorb <% out.println("(" + session.getAttribute("warenkorbAnzahl").toString() + ")"); %></a></li>
		<li><i class="fa fa-bars"></i><a href="meineBestellungen"> Meine Bestellungen</a></li>
		<li><i class="fa fa-user"></i><a href="meinKonto"> Mein Konto</a></li>
	</ul>
	
	<a href="abmelden" id="abmelden"><i class="fa fa-sign-out"></i> abmelden</a>
</div>