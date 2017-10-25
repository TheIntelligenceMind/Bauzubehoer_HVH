<%@page import="entity.WarenkorbArtikel"%>
<%@page import="java.util.List"%>

<div class="showing" id="warenkorbPanel">
	<h1>Warenkorb</h1>
      <table id="warenkorbTabelle">
        <thead>
          <tr>
            <th class="first">Anzahl</th>
            <th class="second">Artikelnummer</th>
            <th class="third">Produkt</th>
            <th class="fourth">Summe</th>
            <th class="fifth">L&oumlschen</th>	
          </tr>
        </thead>
        <tbody>
        	<%
        	List<WarenkorbArtikel> warenkorbartikelListe = (List<WarenkorbArtikel>)request.getAttribute("warenkorbartikelliste");
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
		        		"<td>" + warenkorbartikel.getMenge() + "</td>" +
		        		"<td>" + warenkorbartikel.getArtikel().getNummer() + "</td>" +
		        		"<td>" + warenkorbartikel.getArtikel().getBezeichnung() + "</td>" +
		        		"<td>" + (warenkorbartikel.getArtikel().getPreis()*warenkorbartikel.getMenge()) +  "</td>" +
		        		"<td>" + "<input type='checkbox' name=deleteCheckBox>" + "</td>" +
		        		"</tr>");
		        		gesamt = gesamt + (warenkorbartikel.getArtikel().getPreis() * warenkorbartikel.getMenge());
		        		mwst = mwst + (warenkorbartikel.getArtikel().getPreis() * warenkorbartikel.getMenge())*0.19;
		        	}
        		}
        	}
        	%>
        	
        <tr>
        <%
        for(int i = 0; i < 4; i++)
        {
        	out.println("<td>" + "</td>");
        }
        %>       
        </tr>	
          <tr>
          	<td>Versandkosten:</td>
          <td colspan="2"></td>
          <%
          		out.println("<td>" + versandkosten + "</td>");
          %>
          </tr>
          <tr>
          	<td>MwSt:</td>
          <td colspan="2"></td>
          <%
          		out.println("<td>" + mwst + "</td>");
          %>
          </tr>
          <tr class="totalprice">
          <td class="light">Gesamt:</td>
          <td colspan="2"></td>
          <%	
          		gesamt = gesamt + mwst + versandkosten;
	        	out.println(
	        			"<td>" + gesamt + "</td>"
	        			);
        	%>
          </tr>
          
          <tr class="checkoutrow">
            <td colspan="4" class="checkout"><button id="btnBestellen">Bestellen.</button></td>
            <td><button id=btnDelete>L&oumlschen</button></td>
          </tr>
        </tbody>
      </table>
  
</div>