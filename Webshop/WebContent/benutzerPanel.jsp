<div id="benutzerPanel" class="controllPanel">
	<h3>Hallo, <i id="name"><% out.print(session.getAttribute("benutzername")); %></i></h3>
	<ul>
		<li><i class="fa fa-cart-plus" aria-hidden="true"></i><a href="MainController"> Warenkorb (3)</a></li>
		<li><i class="fa fa-bars" aria-hidden="true"></i><a href="#"> Meine Bestellungen</a></li>
		<li><i class="fa fa-user" aria-hidden="true"></i><a href="#"> Mein Konto</a></li>
	</ul>
	
	<a href="abmelden" id="abmelden"><i class="fa fa-sign-out" aria-hidden="true"></i> abmelden</a>
</div>