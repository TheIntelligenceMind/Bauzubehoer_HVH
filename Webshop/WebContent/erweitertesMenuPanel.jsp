<script type="text/javascript">
function artikelAnzeigen(){
	$(document).ready(function() {	      	               
          window.location.href = "suchen";  
	}); 	
}
</script>
<div id="sitebar">
	<div onclick="artikelAnzeigen()" id="header">
		<h1>Bauzubehör HVH</h1>
		<h3>Online Shop</h3>
	</div>
	<div id="searchPanel">
		<ul>
			<li id="suchenHeader"><i class="fa fa-search"></i> Suchen</li> 
			<li>
				<form action="suchen" method="POST">
					<input id="sucheEingabe" name="suchargument" type="text" placeholder="Artikelbezeichnung ...">
					<input type="submit" value="suchen" style="display:none;">
				</form>
			<li>
		</ul>	
	</div>
</div>