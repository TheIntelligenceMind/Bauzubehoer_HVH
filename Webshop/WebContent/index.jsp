<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Bauzubehör HVH</title>
		<link rel="stylesheet" href="./css/main.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	</head>
	<body>
		<div id="header">
		
		</div>
		<div id="helpbar">
			<ul>
				<li><a href="#"><i class="fa fa-search" aria-hidden="true"></i> Suchen</a></li>
				<li>
					<form>
						<input id="searchInput" type="text" placeholder="Artikelbezeichnung ...">
					</form>
				<li>
			</ul>	
		</div>
		
		<% 
			if(session.getAttribute("benutzername") == null){ 	
		%>
				<%@ include file="anmeldungPanel.jsp" %>
		<%
			}else{ 
		%>
				<%@ include file="benutzerPanel.jsp" %>
		<%
			}
		%>

		<div id="content">	
			<div class="artikel"></div>
			<div class="artikel"></div>
			<div class="artikel"></div>
			<div class="artikel"></div>
			<div class="lastArtikel"></div>
		</div>
		<div id="footer">
		
		</div>
	
	
	</body>
</html>