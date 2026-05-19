<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Connexion</title>
</head>
<body>
    <h2>Login</h2>
    <%@ include file="includes/status.jsp" %>
    <form method="post" action="login">
        <label for="username">Nom d'utilisateur ou email:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Mot de passe:</label>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="Se connecter">
    </form>
    <p>Pas encore inscrit ? <a href="controller?action=signup">Créer un compte</a></p>
</body>
</html>
