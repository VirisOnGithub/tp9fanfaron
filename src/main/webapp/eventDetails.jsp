<%@ page import="com.example.tp9fanfaron.model.Event" %>
<%@ page import="com.example.tp9fanfaron.model.Inscription" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 08/05/2026
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Event event = (Event) request.getAttribute("event");
    List<Inscription> inscriptions = (List<Inscription>) request.getAttribute("inscriptions");
    Fanfaron currentUser = (Fanfaron) session.getAttribute("fanfaron");
%>
<head>
    <title>Inscription pour l'évènement "<%= event == null ? "" : event.getName() %>"</title>
    <style>
        .confirmé {
            color: green;
            font-weight: bold;
        }

        .enattente {
            color: orange;
            font-weight: bold;
        }

        .annulé {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<%
    if (event == null) {
%>
<p>Aucun évènement trouvé.</p>
<% } else {
%>
<h1><%= event.getName() %>
</h1>
<p><strong>Type:</strong> <%= event.getType() %>
</p>
<p><strong>Description:</strong> <%= event.getDescription() %>
</p>
<p><strong>Date:</strong> <%= event.getDateTime() %>
</p>
<p><strong>Durée:</strong> <%= event.getLengthInMinutes() %> minutes</p>
<p><strong>Lieu:</strong> <%= event.getPlace() %>
</p>
<h2>Inscriptions</h2>
<table border="1">
    <tr>
        <th>Nom</th>
        <th>Instrument</th>
        <th>Statut</th>
    </tr>
        <% for (Inscription inscription : inscriptions) { %>
    <tr>
        <td><%= inscription.getName() %>
        </td>
        <td><%= inscription.getInstrument() %>
        </td>
        <!-- usage of status as CSS class -->
        <td class="<%= inscription.getStatus().toLowerCase().replace(" ", "") %>"><%= inscription.getStatus() %>
        </td>
    </tr>
        <% } %>
        <%
    }
%>
        <%
        // Check if the current user is already registered for this event
        if (inscriptions != null && inscriptions.stream().map(Inscription::getName).anyMatch(name -> name.equals(currentUser.getName() + " " + currentUser.getSurname())) && event != null) {
        %>
        <a href="./?action=editRegistration&id=<%= event.getId() %>">Modifier mon inscription</a>
        <%
        }
        %>
</body>
</html>
