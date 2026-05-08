<%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 06/05/2026
  Time: 14:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Erreur</title>
</head>
<body>
<%
    String error = (String) request.getAttribute("error");
%>
<h1>Erreur 400 - Bad Request</h1>
<p><%= error != null ? error : "Une erreur est survenue." %></p>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
