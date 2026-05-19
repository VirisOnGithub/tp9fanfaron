<%@ page import="java.util.List" %>
<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %>
<%@ page import="com.example.tp9fanfaron.model.Section" %>
<%@ page import="com.example.tp9fanfaron.model.Group" %><%--
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
    List<Section> sections = (List<Section>) request.getAttribute("sections");
    List<Group> groups = (List<Group>) request.getAttribute("groups");
%>
<h1>Panel Admin</h1>
<%@ include file="includes/status.jsp" %>
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
                <form action="./controller" method="get" style="display:inline;">
                    <input type="hidden" name="action" value="edit">
                    <input type="hidden" name="id" value="<%= fanfaron.getId() %>">
                    <button type="submit">Modifier</button>
                </form>
                <form action="./controller" method="get" style="display:inline;">
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="<%= fanfaron.getId() %>">
                    <button type="submit">Supprimer</button>
                </form>
            </td>
        </tr>
<%
    }
%>
    </tbody>
</table>

<h2>Ajouter une section</h2>
<form action="./controller?action=createSection" method="post">
    <label for="sectionName">Nom de la section:</label>
    <input type="text" id="sectionName" name="sectionName" required>
    <button type="submit">Ajouter</button>
</form>

<h2>Sections existantes</h2>
<table border="1">
    <thead>
        <tr>
            <th>Id</th>
            <th>Nom</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
<%
    if (sections != null) {
        for (Section section : sections) {
%>
        <tr>
            <td><%= section.getId() %></td>
            <td><%= section.getName() %></td>
            <td>
                <form action="./controller?action=deleteSection" method="post" style="display:inline;">
                    <input type="hidden" name="sectionId" value="<%= section.getId() %>">
                    <button type="submit">Supprimer</button>
                </form>
            </td>
        </tr>
<%
        }
    }
%>
    </tbody>
</table>

<h2>Ajouter un groupe</h2>
<form action="./controller?action=createGroup" method="post">
    <label for="groupName">Nom du groupe:</label>
    <input type="text" id="groupName" name="groupName" required>
    <button type="submit">Ajouter</button>
</form>

<h2>Groupes existants</h2>
<table border="1">
    <thead>
        <tr>
            <th>Id</th>
            <th>Nom</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
<%
    if (groups != null) {
        for (Group group : groups) {
%>
        <tr>
            <td><%= group.getId() %></td>
            <td><%= group.getName() %></td>
            <td>
                <form action="./controller?action=deleteGroup" method="post" style="display:inline;">
                    <input type="hidden" name="groupId" value="<%= group.getId() %>">
                    <button type="submit">Supprimer</button>
                </form>
            </td>
        </tr>
<%
        }
    }
%>
    </tbody>
</table>

<form action="./controller" method="get" style="display:inline;">
    <input type="hidden" name="action" value="signup">
    <button type="submit">Ajouter un nouvel utilisateur</button>
</form>
<form action="./controller" method="get" style="display:inline;">
    <input type="hidden" name="action" value="logout">
    <button type="submit">Se déconnecter</button>
</form>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
