<%@page import="entity.WarenkorbArtikel"%>
<%@page import="java.util.List"%>

<div class="showing" id="warenkorbPanel">
	<h1>Warenkorb</h1>
      <table id="cart">
        <thead>
          <tr>
            <th class="first">Anzahl</th>
            <th class="second">Artikelnummer</th>
            <th class="third">Produkt</th>
            <th class="fourth">Summe</th>
          </tr>
        </thead>
        <tbody>
        	<%
        	List<WarenkorbArtikel> warenkorbartikelListe = (List<WarenkorbArtikel>)request.getAttribute("warenkorbartikelliste");
        	
        	if(warenkorbartikelListe != null)
        	{
	        	for(int i = 0; i < warenkorbartikelListe.size();i++)
		        {
	        		WarenkorbArtikel warenkorbartikel = warenkorbartikelListe.get(i);
	        		if(warenkorbartikel != null)
	        		{
		        		out.println("<tr>" +
		        		"<td>" + warenkorbartikel.getMenge() + "</td>" +
		        		"<td>" + warenkorbartikel.getArtikel().getNummer() + "</td>" +
		        		"<td>" + warenkorbartikel.getArtikel().getBezeichnung() + "</td>" +
		        		"<td>" + warenkorbartikel.getArtikel().getPreis() + "</td>" +
		        		"</tr>");
		        	}
        		}
        	}
        	%>
          <tr class="totalprice">
            <td class="light">Gesamt:</td>
            <td colspan="2">&nbsp;</td>
            <td colspan="2"><span class="thick"></span></td>
          </tr>
          
          <tr class="checkoutrow">
            <td colspan="4" class="checkout"><button id="submitbtn">Bestellen.</button></td>
          </tr>
        </tbody>
      </table>
  
</div>