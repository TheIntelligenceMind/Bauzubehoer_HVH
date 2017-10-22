<%@page import="entity.Artikel"%>
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
        	List<entity.Artikel> artikelliste2 = (List<Artikel>)request.getAttribute("artikelliste");
        	
        	if(artikelliste2 != null)
        	{
        		
	        	for(int i = 0; i < artikelliste2.size();i++)
		        {
	        		Artikel artikel = artikelliste2.get(i);
	        		if(artikel != null)
	        		{
		        		out.println("<tr>" +
		        		"<td>" + artikel.getNummer() + "</td>" +
		        		"<td>" + artikel.getLagermenge() + "</td>" +
		        		"<td>" + artikel.getBezeichnung() + "</td>" +
		        		"<td>" + artikel.getPreis() + "</td>" +
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