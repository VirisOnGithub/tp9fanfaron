<%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 05/05/2026
  Time: 16:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inscription</title>
</head>
<body>
<%
    String errorMessage = (String) request.getAttribute("error");
    String username = (String) request.getAttribute("username");
    String email = (String) request.getAttribute("email");
    String password = (String) request.getAttribute("password");
    String name = (String) request.getAttribute("name");
    String surname = (String) request.getAttribute("surname");
    String gender = (String) request.getAttribute("gender");
    String dietaryConstraints = (String) request.getAttribute("dietaryConstraints");
%>
<% if (errorMessage != null) { %>
<p style="color: red;"><%= errorMessage %></p>
<% } %>
    <form method="post" action="signup">
        <label for="username">Nom d'utilisateur:</label>
        <input type="text" id="username" name="username" required value="<%= username != null ? username : "" %>"><br><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required value="<%= email != null ? email : "" %>"><br><br>

        <label for="password">Mot de passe:</label>
        <input type="password" id="password" name="password" required value="<%= password != null ? password : "" %>"><br><br>

        <label for="name">Prénom</label>
        <input type="text" id="name" name="name" required value="<%= name != null ? name : "" %>"><br><br>

        <label for="surname">Nom de famille</label>
        <input type="text" id="surname" name="surname" required value="<%= surname != null ? surname : "" %>"><br><br>

        <label for="gender">Genre</label>
        <select id="gender" name="gender" required>
            <option value="M" <%= "M".equals(gender) ? "selected" : "" %>>Homme</option>
            <option value="F" <%= "F".equals(gender) ? "selected" : "" %>>Femme</option>
            <option value="O" <%= "O".equals(gender) ? "selected" : "" %>>Autre</option>
        </select><br><br>

        <label for="dietaryConstraints">Contraintes alimentaires</label>
        <input type="text" id="dietaryConstraints" name="dietaryConstraints" value="<%= dietaryConstraints != null ? dietaryConstraints : "" %>"><br><br>

        <input type="submit" value="S'inscrire">
    </form>
</body>
</html>
