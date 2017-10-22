<%@page import="enums.RESPONSE_STATUS"%>
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
		
		<%@ include file="sitebar.jsp" %>	
		
		
		<%	  
			if(session.getAttribute("vorname") == null || session.getAttribute("nachname") == null){ 	
		%>
				<%@ include file="anmeldungPanel.jsp" %>
		<%
			}else{ 
		%>
				<%@ include file="benutzerPanel.jsp" %>
		<%
			}
		%>
		
		
		<div id="fehlermeldungPanel">
			<div id="meldungPanelClose">
				<a onclick="FehlermeldungSchließen()"><i class="fa fa-times"></i></a>
			</div>
			<h4>Fehler !</h4>
			<p>
				<% out.println(response.getHeader("fehlermeldung")); %>
			</p>
		</div>	
		
		<div id="hinweismeldungPanel">
			<div id="meldungPanelClose">
				<a onclick="HinweismeldungSchließen()"><i class="fa fa-times"></i></a>
			</div>
			<h4>Hinweis !</h4>
			<p>
				<% out.println(response.getHeader("hinweismeldung")); %>
			</p>
		</div>	

		<script type="text/javascript">
			function FehlermeldungOeffnen(){
				document.getElementById('fehlermeldungPanel').style.display ='inline';
				document.getElementById('fehlermeldungPanel').className ='opening';
			}
			function FehlermeldungSchließen(){
				document.getElementById('fehlermeldungPanel').className ='closing';
				setTimeout(function(){
					document.getElementById('fehlermeldungPanel').style.display ='none';
				}, 1000);
			}
			function HinweismeldungOeffnen(){
				document.getElementById('hinweismeldungPanel').style.display ='inline';
				document.getElementById('hinweismeldungPanel').className ='opening';
			}
			function HinweismeldungSchließen(){
				document.getElementById('hinweismeldungPanel').className ='closing';
				setTimeout(function(){
					document.getElementById('hinweismeldungPanel').style.display ='none';
				}, 1000);
			}
		</script>
				
		<%
			if(response.getHeader("status") != null && response.getHeader("status").equals(RESPONSE_STATUS.FEHLER.toString())){
				out.println("<script type='text/javascript'>FehlermeldungOeffnen()</script>");
			}else if(response.getHeader("status") != null && response.getHeader("status").equals(RESPONSE_STATUS.HINWEIS.toString())){
				out.println("<script type='text/javascript'>HinweismeldungOeffnen()</script>");
			}
		%>

		<div class="contentWrapper">
			<% 
				if(response.getHeader("contentSite") != null ){ 	
					
					if(session.getAttribute("vorname") == null || session.getAttribute("nachname") == null){
						switch(response.getHeader("contentSite")){
						
						case "registrierung":
							%>
								<%@ include file="registrierung.jsp" %>
							<%
							break;
						default:
							%>
								<%@ include file="artikel.jsp" %>
							<%
							break;
						}
					}else{
						switch(response.getHeader("contentSite")){
						
						case "registrierung":
							%>
								<%@ include file="registrierung.jsp" %>
							<%
							break;
						case "warenkorb":
							%>
								<%@ include file="warenkorb.jsp" %>
							<%
							break;
						case "meineBestellungen":
							%>
								<%@ include file="meineBestellungen.jsp" %>
							<%
							break;
						case "meinKonto":
							%>
								<%@ include file="meinKonto.jsp" %>
							<%
							break;
						case "artikelAnlegen":
							%>
								<%@ include file="artikelPanel.jsp" %>
							<%
							break;
						default:
							%>
								<%@ include file="artikel.jsp" %>
							<%
							break;
						}
					}
				}else{
					%>
						<%@ include file="artikel.jsp" %>
					<%
				}
			%>
			
		</div>

	
	</body>
</html>