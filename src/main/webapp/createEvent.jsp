<%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 08/05/2026
  Time: 15:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Création d'un évènement</title>
</head>
<body>
<h1>Création d'un évènement</h1>
<%@ include file="includes/status.jsp" %>
<form method="post" action="./controller?action=createEvent">
    <label for="type">Type de l'évènement:</label><br>
    <select id="type" name="type">
        <option value="Atelier">Atelier</option>
        <option value="Répétition">Répétition</option>
        <option value="Prestation">Prestation</option>
    </select><br><br>

    <label for="name">Nom de l'évènement:</label><br>
    <input type="text" id="name" name="name"><br><br>

    <label for="date">Date de l'évènement:</label><br>
    <input type="datetime-local" id="date" name="date"><br><br>

    <label for="duration">Durée de l'évènement (en minutes):</label><br>
    <input type="number" id="duration" name="duration" min="1"><br><br>

    <label for="location">Lieu de l'évènement:</label><br>
    <input type="text" id="location" name="location"><br><br>

    <label for="description">Description de l'évènement:</label><br>
    <textarea id="description" name="description" rows="4" cols="50"></textarea><br><br>

    <input type="submit" value="Créer">
</form>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
