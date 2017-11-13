<%@page import="enums.RESPONSE_STATUS"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!--
Autor dieser Datei: Tim Hermbecker, Lukas Vechtel

Die Index-Datei wird für die Zusammenfuehrung der Haupt-Files benötigt.
-->

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Bauzubehör HVH</title>
	
	
	
	<link rel="stylesheet" type="text/css" media="screen and (min-device-width: 1200px)" href="./css/desktop.css">	 


<!--  		  < in die "smartphone css link wieder einbinden.
-->
		
		<link rel="stylesheet" type="text/css" media="screen and (max-device width: 1200px)" href="./css/smartphone.css">
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
		<script src="js/jquery-1.11.1.js" type="text/javascript"></script>
	</head>
	<body>
		
		<%@ include file="erweitertesMenuPanel.jsp" %>	
		
		
		<%	  
			if(session.getAttribute("benutzer") == null){ 	
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
				<%
					String fehlermeldung = response.getHeader("fehlermeldung") != null ? response.getHeader("fehlermeldung") : request.getHeader("fehlermeldung");
				 	out.println(fehlermeldung); 
				 %>
			</p>
		</div>	
		
		<div id="hinweismeldungPanel">
			<div id="meldungPanelClose">
				<a onclick="HinweismeldungSchließen()"><i class="fa fa-times"></i></a>
			</div>
			<h4>Hinweis !</h4>
			<p>
				<% 
					String hinweismeldung = response.getHeader("hinweismeldung") != null ? response.getHeader("hinweismeldung") : request.getHeader("hinweismeldung");
					out.println(hinweismeldung); 
				%>
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
				
		

		<div class="contentWrapper">
			<%
				String siteContent = response.getHeader("contentSite") != null ? response.getHeader("contentSite") : request.getHeader("contentSite");	
				
				// Hier wird der Content dynamisch über das Attribut "ContentSite" geladen
				if(siteContent != null ){ 	
					if(session.getAttribute("benutzer") == null){
						if(siteContent.equals("registrierungPanel")){
							%>
								<%@ include file="registrierungPanel.jsp" %>
							<%		
						}else{
							%>
								<%@ include file="artikelAnzeigenPanel.jsp" %>
							<%	
						}
							
					}else{
						switch(siteContent){
						case "warenkorbPanel":
							%>
								<%@ include file="warenkorbPanel.jsp" %>
							<%
							break;
						case "meineBestellungenPanel":
							%>
								<%@ include file="meineBestellungenPanel.jsp" %>
							<%
							break;
						case "meinKontoPanel":
							%>
								<%@ include file="meinKontoPanel.jsp" %>
							<%
							break;
						case "artikelAnlegenPanel":
							%>
								<%@ include file="artikelAnlegenPanel.jsp" %>
							<%
							break;
						case "artikelAnzeigenMitarbeiterPanel":
							%>
								<%@ include file="artikelAnzeigenMitarbeiterPanel.jsp" %>
							<%
							break;
						case "artikelBearbeitenPanel":
							%>
								<%@ include file="artikelBearbeitenPanel.jsp" %>
							<%
							break;
						default:
							%>
								<%@ include file="artikelAnzeigenPanel.jsp" %>
							<%
							break;
						}
					}
				}else{
					%>
						<%@ include file="artikelAnzeigenPanel.jsp" %>
					<%
				}
			%>

			<!-- Hier wird der der Hinweis/Fehlerdialog via Javascript aufgerufen -->		
			<%
				if(response.getHeader("status") != null && response.getHeader("status").equals(RESPONSE_STATUS.FEHLER.toString())){
					out.println("<script type='text/javascript'>FehlermeldungOeffnen()</script>");
				}else if(response.getHeader("status") != null && response.getHeader("status").equals(RESPONSE_STATUS.HINWEIS.toString())){
					out.println("<script type='text/javascript'>HinweismeldungOeffnen()</script>");
				}
			
				if(request.getHeader("status") != null && request.getHeader("status").equals(RESPONSE_STATUS.FEHLER.toString())){
					out.println("<script type='text/javascript'>FehlermeldungOeffnen()</script>");
				}else if(request.getHeader("status") != null && request.getHeader("status").equals(RESPONSE_STATUS.HINWEIS.toString())){
					out.println("<script type='text/javascript'>HinweismeldungOeffnen()</script>");
				}
			%>
			
		</div>

	</body>
</html>