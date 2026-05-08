<%@ page import="java.util.List" %>
<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 05/05/2026
  Time: 17:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Panel Admin</title>
</head>
<body>
<%
    List<Fanfaron> f = (List<Fanfaron>) request.getAttribute("fanfaronList");
    String error = (String) request.getAttribute("error");
%>
<h1>Panel Admin</h1>
<% if (error != null) { %>
    <p style="color: red;"><%= error %></p>
<% } %>
<table border="1">
    <thead>
        <tr>
            <th>Id</th>
            <th>Nom d'utilisateur</th>
            <th>Prénom</th>
            <th>Nom</th>
            <th>Email</th>
            <th>Genre</th>
            <th>Contrainte alimentaire</th>
            <th>Admin</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
<%
    for (Fanfaron fanfaron : f) {
%>
        <tr>
            <td><%= fanfaron.getId() %></td>
            <td><%= fanfaron.getUsername() %></td>
            <td><%= fanfaron.getName() %></td>
            <td><%= fanfaron.getSurname() %></td>
            <td><%= fanfaron.getEmail() %></td>
            <td><%= fanfaron.getGender() %></td>
            <td><%= fanfaron.getAlimentaryConstraint() %></td>
            <td><%= fanfaron.getIsAdmin() ? "Oui" : "Non" %></td>
            <td>
                <a href="./?action=edit&id=<%= fanfaron.getId() %>">Modifier</a>
                <a href="./?action=delete&id=<%= fanfaron.getId() %>">Supprimer</a>
            </td>
        </tr>
<%
    }
%>
    </tbody>
</table>
<a href="./?action=signup">Ajouter un nouvel utilisateur</a><br><br>
<a href="./?action=logout">Se déconnecter</a>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
