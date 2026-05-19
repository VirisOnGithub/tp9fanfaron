<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 06/05/2026
  Time: 14:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modification de l'utilisateur</title>
</head>
<body>
<%
    Fanfaron fanfaron = (Fanfaron) request.getAttribute("fanfaron");
%>
<h1>Modification de l'utilisateur</h1>
<%@ include file="includes/status.jsp" %>
<form action="./controller?action=edit&id=<%= fanfaron.getId() %>" method="post">
    <input type="hidden" name="id" value="<%= fanfaron.getId() %>">
    <label for="username">Nom d'utilisateur:</label><br>
    <input type="text" id="username" name="username" value="<%= fanfaron.getUsername() %>" required><br><br>

    <label for="name">Prénom:</label><br>
    <input type="text" id="name" name="name" value="<%= fanfaron.getName() %>" required><br><br>
    <label for="surname">Nom:</label><br>
    <input type="text" id="surname" name="surname" value="<%= fanfaron.getSurname() %>" required><br><br>
    <label for="email">Email:</label><br>
    <input type="email" id="email" name="email" value="<%= fanfaron.getEmail() %>" required><br><br>
    <label for="password">Mot de passe (laisser vide pour ne pas changer):</label><br>
    <input type="password" id="password" name="password"><br><br>
    <label for="gender">Genre:</label><br>
    <select id="gender" name="gender" required>
        <option value="Homme" <%= "Homme".equals(fanfaron.getGender()) ? "selected" : "" %>>Homme</option>
        <option value="Femme" <%= "Femme".equals(fanfaron.getGender()) ? "selected" : "" %>>Femme</option>
        <option value="Autre" <%= "Autre".equals(fanfaron.getGender()) ? "selected" : "" %>>Autre</option>
    </select><br><br>
    <label for="alimentaryConstraint">Contrainte alimentaire:</label><br>
    <input type="text" id="alimentaryConstraint" name="alimentaryConstraint" value="<%= fanfaron.getAlimentaryConstraint() %>"><br><br>
    <label for="isAdmin">Admin:</label><br>
    <input type="checkbox" id="isAdmin" name="isAdmin" <%= fanfaron.getIsAdmin() ? "checked" : "" %>><br><br>
    <input type="submit" value="Enregistrer les modifications">
</form>

<%@ include file="includes/footer.jsp" %>
</body>
</html>
