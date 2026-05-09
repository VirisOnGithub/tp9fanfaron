<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 06/05/2026
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Fanfaron fanfaron = (Fanfaron) request.getSession().getAttribute("fanfaron");
    Boolean isMemberOfPrestationGroup = (Boolean) request.getAttribute("isMemberOfPrestationGroup");
%>
<head>
    <title>Profil de <%= fanfaron.getUsername() %></title>
</head>
<body>
<h1>Profil de <%= fanfaron.getUsername() %></h1>
<%@ include file="includes/status.jsp" %>
<p><strong>Prénom:</strong> <%= fanfaron.getName() %></p>
<p><strong>Nom:</strong> <%= fanfaron.getSurname() %></p>
<p><strong>Email:</strong> <%= fanfaron.getEmail() %></p>
<p><strong>Genre:</strong> <%= fanfaron.getGender() %></p>
<p><strong>Contrainte alimentaire:</strong> <%= fanfaron.getAlimentaryConstraint() == null ? "Aucune" : fanfaron.getAlimentaryConstraint() %></p>
<p><strong>Admin:</strong> <%= fanfaron.getIsAdmin() ? "Oui" : "Non" %></p>
<a href="./?action=editGroups">Gérer mes groupes et mes pupitres</a><br>
<a href="./?action=logout">Se déconnecter</a><br>
<a href="./?action=events">Voir les évènements</a><br>
<% if (isMemberOfPrestationGroup) { %>
    <a href="./?action=createEvent">Proposer un évènement</a>
<% } %>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
