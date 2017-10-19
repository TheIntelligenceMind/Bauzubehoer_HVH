<%@page import="entity.Artikel"%>
<%@page import="java.util.List"%>

<%
	List<entity.Artikel> artikelliste = (List<Artikel>)request.getAttribute("artikelliste");

	if(artikelliste != null){
		for(int i = 0; i < artikelliste.size(); i++){
			Artikel artikel = artikelliste.get(i);
			
			if(artikel != null){			
				out.println("<div class='artikel'><h3>"
							+ artikel.getBezeichnung() 
							+ " (" 
							+ artikel.getPreis() 
							+ "&euro;)" 
							+ "</h3><p>" 
							+ artikel.getBeschreibung() 
							+ "</p></div>");			
			}		
		}
	}
%>