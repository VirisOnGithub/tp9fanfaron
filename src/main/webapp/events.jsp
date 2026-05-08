<%@ page import="com.example.tp9fanfaron.model.Event" %>
<%@ page import="java.util.List" %><%--
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
<%
    List<Event> events = (List<Event>) request.getAttribute("events");
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
</div>
<% }
} %>
<%@ include file="includes/footer.jsp" %>
</body>
</html>