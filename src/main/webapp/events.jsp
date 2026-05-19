<%@ page import="com.example.tp9fanfaron.model.Event" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %>
<%@ page import="com.example.tp9fanfaron.dao.DAOFactory" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 08/05/2026
  Time: 15:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Évènements</title>
</head>
<body>
<h1>Évènements</h1>
<%@ include file="includes/status.jsp" %>
<%
    List<Event> events = (List<Event>) request.getAttribute("events");
    Fanfaron currentUser = (Fanfaron) session.getAttribute("fanfaron");
    boolean belongsToPrestation = currentUser != null && DAOFactory.getGroupDAO().belongsToPrestation(currentUser.getId());
    if (events == null || events.isEmpty()) {
%>
<p>Aucun évènement n'est disponible pour le moment.</p>
<% } else {
    for (Event event : events) {
%>
<div>
    <h2><%= event.getName() %>
    </h2>
    <p><strong>Type:</strong> <%= event.getType() %>
    </p>
    <p><strong>Description:</strong> <%= event.getDescription() %>
    </p>
    <p><strong>Date:</strong> <%= event.getDateTime() %>
    </p>
    <p><strong>Durée:</strong> <%= event.getLengthInMinutes() %> minutes
    </p>
    <p><strong>Lieu:</strong> <%= event.getPlace() %>
    </p>
    <a href="./controller?action=eventDetails&id=<%= event.getId() %>">Voir les inscriptions</a>
    <a href="./controller?action=registerEvent&id=<%= event.getId() %>">S'inscrire</a>
    <% if (belongsToPrestation) { %>
    <a href="./controller?action=editEvent&id=<%= event.getId() %>">Modifier</a>
    <a href="./controller?action=deleteEvent&id=<%= event.getId() %>">Supprimer</a>
    <% } %>
</div>
<% }
} %>
<%@ include file="includes/footer.jsp" %>
</body>
</html>