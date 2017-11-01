<%@page import="entity.WarenkorbArtikel"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.List"%>

<%
	final DecimalFormat formater = new DecimalFormat("#0.00");
%>

<script type="text/javascript">

function deleteRow(element){
	var result = confirm("Sind Sie sicher, dass der Artikel entfernt werden soll?");
	
	if(result){
		$(document).ready(function() {
			var row = element.closest('tr').rowIndex;
	      	               
           window.location.href = "warenkorb?method=artikelAusWarenkorbLoeschen&row="+row;
           element.closest('tr').remove();    
		});
	}
}

</script>

<div class="showing" id="warenkorbPanel">
	<h1>Warenkorb</h1>
	<div id="warenkorbTabellen">
	      <table id="artikelTabelle">
	        <colgroup>
		       <col span="1" style="width: 10%;">
		       <col span="1" style="width: 20%;">
		       <col span="1" style="width: 30%;">
		       <col span="1" style="width: 15%;">
		       <col span="1" style="width: 5%;">
		    </colgroup>
	        
	        <thead>
	          <tr>
	            <th>Menge</th>
	            <th>Artikelnummer</th>
	            <th>Produkt</th>
	            <th>Summe</th>
	            <th></th>
	          </tr>
	        </thead>
	        <tbody>
	        	<%
	        	List<WarenkorbArtikel> warenkorbartikelListe = (List<WarenkorbArtikel>)session.getAttribute("warenkorbartikelliste");
	        	double gesamt = 0;
	        	int versandkosten = 20;
	        	double mwst = 0;
	        	if(warenkorbartikelListe != null)
	        	{
		        	for(int i = 0; i < warenkorbartikelListe.size();i++)
			        {
		        		WarenkorbArtikel warenkorbartikel = warenkorbartikelListe.get(i);
		        		if(warenkorbartikel != null)
		        		{
			        		out.println("<tr>" +
			        		"<td><input style='width: 20px;border:none;' type='text' name='menge' value='" + String.valueOf(warenkorbartikel.getMenge()) + "' ></td>" +
			        		"<td>" + String.format("%04d", warenkorbartikel.getArtikel().getNummer()) + "</td>" +
			        		"<td>" + warenkorbartikel.getArtikel().getBezeichnung() + "</td>" +
			        		"<td>" + formater.format(warenkorbartikel.getArtikel().getPreis()*warenkorbartikel.getMenge()) +  "</td>" +
			        		"<td>" + "<a class='trashSymbol' onclick='deleteRow(this)' id='"+ warenkorbartikel.getPosition() +"'><i class='fa fa-trash-o'></i></a>" + "</td>" +
			        		"</tr>");
			        		gesamt = gesamt + (warenkorbartikel.getArtikel().getPreis() * warenkorbartikel.getMenge());
			        		mwst = mwst + (warenkorbartikel.getArtikel().getPreis() * warenkorbartikel.getMenge())*0.19;
			        	}
	        		}
	        	}
	        	%>
	        </tbody>
	      </table>
      
	      <table id="zusatzTabelle">
	      	<colgroup>
		       <col span="1" style="width: 10%;">
		       <col span="1" style="width: 20%;">
		       <col span="1" style="width: 30%;">
		       <col span="1" style="width: 15%;">
		       <col span="1" style="width: 5%;">
		    </colgroup>
	      	<tbody>
	        	<tr>
	          		<td class="textLeftBold" colspan="2">Versandkosten:</td>
	          		<td colspan="1"></td>
			        <td><% out.println(formater.format(versandkosten)); %></td>
		       	</tr>
				<tr>
				 	<td class="textLeftBold" colspan="2">MwSt:</td>
					<td colspan="1"></td>
					<td><% out.println(formater.format(mwst)); %></td>
				</tr>
				<tr class="totalprice">
					<td class="textLeftBold" colspan="2">Gesamt:</td>
				 	<td colspan="1"></td>
				 	<td><% out.println(formater.format(gesamt + mwst + versandkosten)); %><td>
				</tr>
	        </tbody>  
		</table>
  	</div>
	
  	<button id="btnBestellen">Bestellen</button>
  	<button id="btnwarenkorbSpeichern" type="submit">Speichern</button>	  
</div>