<%@ page import="com.example.tp9fanfaron.model.Event" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 08/05/2026
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Event event = (Event) request.getAttribute("event");
%>
<head>
    <title>Modification de l'évènement "<%= event.getName() %>"</title>
</head>
<body>
<h1>Modification de l'évènement "<%= event.getName() %>"</h1>
<%@ include file="includes/status.jsp" %>
<form action="./?action=editEvent" method="post">
    <input type="hidden" name="id" value="<%= event.getId() %>">

    <label for="type">Type de l'évènement:</label><br>
    <select id="type" name="type">
        <option value="Atelier" <%= "Atelier".equals(event.getType()) ? "selected" : "" %>>Atelier</option>
        <option value="Répétition" <%= "Répétition".equals(event.getType()) ? "selected" : "" %>>Répétition</option>
        <option value="Prestation" <%= "Prestation".equals(event.getType()) ? "selected" : "" %>>Prestation</option>
    </select><br><br>

    <label for="name">Nom de l'évènement:</label><br>
    <input type="text" id="name" name="name" value="<%= event.getName() %>"><br><br>

    <label for="date">Date de l'évènement:</label><br>
    <input type="datetime-local" id="date" name="date" value="<%= event.getDateTime().toString().replace(" ", "T") %>"><br><br>

    <label for="duration">Durée de l'évènement (en minutes):</label><br>
    <input type="number" id="duration" name="duration" min="1" value="<%= event.getLengthInMinutes() %>"><br><br>

    <label for="location">Lieu de l'évènement:</label><br>
    <input type="text" id="location" name="location" value="<%= event.getPlace() %>"><br><br>

    <label for="description">Description de l'évènement:</label><br>
    <textarea id="description" name="description" rows="4" cols="50"><%= event.getDescription() %></textarea><br><br>

    <input type="submit" value="Modifier">
</form>
</body>
</html>
